package com.hogarya;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogarya.factura.FacturaRepository;
import com.hogarya.factura.Factura;
import com.hogarya.incidente.Incidente;
import jakarta.persistence.EntityManager;
import com.hogarya.incidente.IncidenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(ApiIntegrationTests.RelojDePrueba.class)
class ApiIntegrationTests {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired EntityManager entityManager;
    @SpyBean FacturaRepository facturas;
    @SpyBean IncidenteRepository incidentes;

    @TestConfiguration
    static class RelojDePrueba {
        @Bean
        @Primary
        Clock relojFijo() {
            return Clock.fixed(Instant.parse("2026-11-25T15:00:00Z"),
                    ZoneId.of("America/Argentina/Buenos_Aires"));
        }
    }

    @BeforeEach
    void limpiarSoloEsquemaDePruebas() {
        assertThat(jdbc.queryForObject("SELECT DATABASE()", String.class))
                .isEqualToIgnoringCase("02-BocchieriDiazLaggerDiaz_pruebas");
        for (String table : new String[]{"informes_resolucion", "historial_estados_incidente",
                "incidentes", "pagos", "facturas"}) {
            jdbc.update("DELETE FROM " + table);
        }
    }

    private JsonNode enviar(String method, String path, Object body, int expected) throws Exception {
        var builder = request(HttpMethod.valueOf(method), path).contentType("application/json");
        if (body != null) {
            builder.content(json.writeValueAsBytes(body));
        }
        MvcResult result = mvc.perform(builder).andReturn();
        assertThat(result.getResponse().getStatus())
                .as("%s %s devuelve %s", method, path, result.getResponse().getContentAsString())
                .isEqualTo(expected);
        return json.readTree(result.getResponse().getContentAsByteArray());
    }

    private long emitir() throws Exception {
        return enviar("POST", "/facturas",
                Map.of("contratoId", 1, "periodo", "2026-12"), 201).get("id").asLong();
    }

    private Map<String, Object> pago(long factura) {
        return Map.of("facturaId", factura, "medioPago", "EFECTIVO",
                "importePagado", 450000, "interes", 0);
    }

    private long registrar() throws Exception {
        return enviar("POST", "/incidentes", Map.of("contratoId", 1, "titulo", "Pérdida",
                "descripcion", "Goteo en la cocina", "categoria", "PLOMERIA",
                "prioridad", "MEDIA"), 201).get("id").asLong();
    }

    private void estado(long id, String estado) throws Exception {
        enviar("PATCH", "/incidentes/" + id + "/estado", Map.of("estado", estado), 200);
    }

    private long enProceso() throws Exception {
        long id = registrar();
        estado(id, "EN_PROCESO");
        return id;
    }

    private Map<String, Object> informe(long incidente) {
        return Map.of("incidenteId", incidente, "descripcionTrabajo", "Cambio de junta",
                "costo", 0, "responsable", "Técnico de prueba");
    }

    private int cantidad(String table) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
    }

    private void selfConFiltros(JsonNode lista, String query) {
        assertThat(lista.at("/_links/self/href").asText()).endsWith(query);
    }

    @Nested
    class S01Facturas {
        @Test void emisionExitosa() throws Exception {
            long id = emitir();
            JsonNode factura = enviar("GET", "/facturas/" + id, null, 200);
            assertThat(factura.get("importe").decimalValue()).isEqualByComparingTo("450000");
            assertThat(factura.get("estado").asText()).isEqualTo("PENDIENTE");
        }
        @Test void consultaPorIdentificador() throws Exception {
            long id = emitir();
            assertThat(enviar("GET", "/facturas/" + id, null, 200)
                    .at("/_links/contrato/href").asText()).endsWith("/contratos/1");
        }
        @Test void listadoFiltrado() throws Exception {
            emitir();
            JsonNode lista = enviar("GET", "/facturas?contratoId=1&estado=PENDIENTE", null, 200);
            selfConFiltros(lista, "?contratoId=1&estado=PENDIENTE");
            assertThat(lista.at("/_embedded/facturas").size()).isEqualTo(1);
        }
        @Test void datosInvalidos() throws Exception {
            enviar("POST", "/facturas", Map.of("contratoId", 1, "periodo", "2026-13"), 400);
            assertThat(cantidad("facturas")).isZero();
        }
        @Test void recursoInexistente() throws Exception {
            enviar("POST", "/facturas", Map.of("contratoId", 999999, "periodo", "2026-12"), 404);
        }
        @Test void mensualDuplicada() throws Exception {
            emitir();
            enviar("POST", "/facturas", Map.of("contratoId", 1, "periodo", "2026-12"), 409);
            assertThat(cantidad("facturas")).isEqualTo(1);
        }
    }

    @Nested
    class S02Pagos {
        @Test void pagoExitoso() throws Exception {
            long id = emitir();
            enviar("POST", "/pagos", pago(id), 201);
            assertThat(enviar("GET", "/facturas/" + id, null, 200).get("estado").asText())
                    .isEqualTo("PAGADA");
        }
        @Test void consultaPorIdentificador() throws Exception {
            long id = enviar("POST", "/pagos", pago(emitir()), 201).get("id").asLong();
            enviar("GET", "/pagos/" + id, null, 200);
        }
        @Test void listadoFiltrado() throws Exception {
            long factura = emitir();
            enviar("POST", "/pagos", pago(factura), 201);
            JsonNode lista = enviar("GET", "/pagos?facturaId=" + factura, null, 200);
            selfConFiltros(lista, "?facturaId=" + factura);
            assertThat(lista.at("/_embedded/pagos").size()).isEqualTo(1);
        }
        @Test void datosInvalidos() throws Exception {
            enviar("POST", "/pagos", Map.of("facturaId", emitir(), "medioPago", "EFECTIVO",
                    "importePagado", -1, "interes", 0), 400);
            assertThat(cantidad("pagos")).isZero();
        }
        @Test void recursoInexistente() throws Exception {
            enviar("POST", "/pagos", pago(999999), 404);
        }
        @Test void segundoPagoRechazado() throws Exception {
            long factura = emitir();
            enviar("POST", "/pagos", pago(factura), 201);
            enviar("POST", "/pagos", pago(factura), 409);
            assertThat(cantidad("pagos")).isEqualTo(1);
        }
    }

    @Nested
    class S03Incidentes {
        @Test void altaExitosaConHistorial() throws Exception {
            long id = registrar();
            assertThat(enviar("GET", "/incidentes/" + id, null, 200).get("estado").asText())
                    .isEqualTo("ABIERTO");
            assertThat(cantidad("historial_estados_incidente")).isEqualTo(1);
        }
        @Test void consultaPorIdentificador() throws Exception {
            JsonNode incidente = enviar("GET", "/incidentes/" + registrar(), null, 200);
            String informes = incidente.at("/_links/informes/href").asText();
            assertThat(informes).contains("/informes-resolucion?incidenteId=");
            enviar("GET", informes, null, 200);
        }
        @Test void listadoFiltrado() throws Exception {
            registrar();
            JsonNode lista = enviar("GET", "/incidentes?estado=ABIERTO&prioridad=MEDIA", null, 200);
            selfConFiltros(lista, "?estado=ABIERTO&prioridad=MEDIA");
            assertThat(lista.at("/_embedded/incidentes").size()).isEqualTo(1);
        }
        @Test void datosInvalidos() throws Exception {
            enviar("POST", "/incidentes", Map.of("contratoId", 1, "titulo", "",
                    "descripcion", "Goteo", "categoria", "GENERAL", "prioridad", "BAJA"), 400);
            assertThat(cantidad("incidentes")).isZero();
        }
        @Test void recursoInexistente() throws Exception {
            enviar("GET", "/incidentes/999999", null, 404);
        }
        @Test void transicionRechazadaSinHistorialParcial() throws Exception {
            long id = registrar();
            enviar("PATCH", "/incidentes/" + id + "/estado", Map.of("estado", "RESUELTO"), 409);
            assertThat(cantidad("historial_estados_incidente")).isEqualTo(1);
            assertThat(enviar("GET", "/incidentes/" + id, null, 200).get("estado").asText())
                    .isEqualTo("ABIERTO");
        }
    }

    @Nested
    class S04Informes {
        @Test void resolucionExitosa() throws Exception {
            long id = enProceso();
            enviar("POST", "/informes-resolucion", informe(id), 201);
            assertThat(enviar("GET", "/incidentes/" + id, null, 200).get("estado").asText())
                    .isEqualTo("RESUELTO");
        }
        @Test void consultaPorIdentificador() throws Exception {
            long id = enviar("POST", "/informes-resolucion", informe(enProceso()), 201).get("id").asLong();
            enviar("GET", "/informes-resolucion/" + id, null, 200);
        }
        @Test void listadoFiltrado() throws Exception {
            long id = enProceso();
            enviar("POST", "/informes-resolucion", informe(id), 201);
            JsonNode lista = enviar("GET", "/informes-resolucion?incidenteId=" + id, null, 200);
            selfConFiltros(lista, "?incidenteId=" + id);
            assertThat(lista.at("/_embedded/informes").size()).isEqualTo(1);
        }
        @Test void datosInvalidos() throws Exception {
            enviar("POST", "/informes-resolucion", Map.of("incidenteId", enProceso(),
                    "descripcionTrabajo", "", "costo", -1, "responsable", ""), 400);
            assertThat(cantidad("informes_resolucion")).isZero();
        }
        @Test void recursoInexistente() throws Exception {
            enviar("POST", "/informes-resolucion", informe(999999), 404);
        }
        @Test void resolverAbiertoRechazado() throws Exception {
            long id = registrar();
            enviar("POST", "/informes-resolucion", informe(id), 409);
            assertThat(cantidad("informes_resolucion")).isZero();
            assertThat(cantidad("historial_estados_incidente")).isEqualTo(1);
        }
    }

    @Test
    void recorridoFacturacionPagoYEnlace() throws Exception {
        long factura = emitir();
        enviar("POST", "/pagos", pago(factura), 201);
        JsonNode actual = enviar("GET", "/facturas/" + factura, null, 200);
        assertThat(actual.get("estado").asText()).isEqualTo("PAGADA");
        enviar("GET", actual.at("/_links/pago/href").asText(), null, 200);
        enviar("POST", "/pagos", pago(factura), 409);
        assertThat(cantidad("pagos")).isEqualTo(1);
    }

    @Test
    void recorridoMantenimientoConDosResoluciones() throws Exception {
        long id = enProceso();
        JsonNode primero = enviar("POST", "/informes-resolucion", informe(id), 201);
        estado(id, "REABIERTO");
        estado(id, "EN_PROCESO");
        JsonNode segundo = enviar("POST", "/informes-resolucion", informe(id), 201);
        enviar("GET", "/incidentes/" + id + "/historial", null, 200);
        assertThat(primero.get("id")).isNotEqualTo(segundo.get("id"));
        assertThat(cantidad("informes_resolucion")).isEqualTo(2);
        assertThat(cantidad("historial_estados_incidente")).isEqualTo(6);
        assertThat(jdbc.queryForObject("""
                SELECT COUNT(*) FROM informes_resolucion i
                JOIN historial_estados_incidente h ON h.id = i.historial_id
                WHERE i.fecha_resolucion = h.fecha_hora AND h.estado_nuevo = 'RESUELTO'
                """, Integer.class)).isEqualTo(2);
        enviar("POST", "/informes-resolucion", informe(id), 409);
        assertThat(cantidad("informes_resolucion")).isEqualTo(2);
        assertThat(cantidad("historial_estados_incidente")).isEqualTo(6);
    }

    @Test
    void falloTrasInsertarPagoRevierteTambienFactura() throws Exception {
        long id = emitir();
        doThrow(new DataIntegrityViolationException("Fallo inducido de prueba")).when(facturas).flush();
        enviar("POST", "/pagos", pago(id), 409);
        assertThat(cantidad("pagos")).isZero();
        assertThat(jdbc.queryForObject("SELECT estado FROM facturas WHERE id = ?",
                String.class, id)).isEqualTo("PENDIENTE");
    }

    @Test
    void falloTrasInsertarInformeRevierteEstadoEHistorial() throws Exception {
        long id = enProceso();
        doThrow(new DataIntegrityViolationException("Fallo inducido de prueba")).when(incidentes).flush();
        enviar("POST", "/informes-resolucion", informe(id), 409);
        assertThat(cantidad("informes_resolucion")).isZero();
        assertThat(cantidad("historial_estados_incidente")).isEqualTo(2);
        assertThat(jdbc.queryForObject("SELECT estado FROM incidentes WHERE id = ?",
                String.class, id)).isEqualTo("EN_PROCESO");
    }

    private void simultaneas(String path, Object body) throws Exception {
        var first = CompletableFuture.supplyAsync(() -> peticionConcurrente(path, body));
        var second = CompletableFuture.supplyAsync(() -> peticionConcurrente(path, body));
        CompletableFuture.allOf(first, second).get(15, TimeUnit.SECONDS);
        assertThat(java.util.List.of(first.get(), second.get())).containsExactlyInAnyOrder(201, 409);
    }

    private int peticionConcurrente(String path, Object body) {
        try {
            MvcResult result = mvc.perform(request(HttpMethod.POST, path).contentType("application/json")
                    .content(json.writeValueAsBytes(body))).andReturn();
            return result.getResponse().getStatus();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void emisionConcurrenteConservaUnaMensual() throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(2);
        doAnswer(call -> {
            String clave = call.getArgument(0);
            Object result = jdbc.queryForObject(
                    "SELECT EXISTS(SELECT 1 FROM facturas WHERE clave_mensual_activa = ?)",
                    Boolean.class, clave);
            barrier.await(10, TimeUnit.SECONDS);
            return result;
        }).when(facturas).existsByClaveMensualActiva(anyString());
        simultaneas("/facturas", Map.of("contratoId", 1, "periodo", "2026-12"));
        assertThat(cantidad("facturas")).isEqualTo(1);
    }

    @Test
    void pagosConcurrentesConservanUnPago() throws Exception {
        long id = emitir();
        CyclicBarrier barrier = new CyclicBarrier(2);
        doAnswer(call -> {
            Object result = java.util.Optional.ofNullable(entityManager.find(Factura.class, id));
            barrier.await(10, TimeUnit.SECONDS);
            return result;
        }).when(facturas).findById(id);
        simultaneas("/pagos", pago(id));
        assertThat(cantidad("pagos")).isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT estado FROM facturas WHERE id = ?",
                String.class, id)).isEqualTo("PAGADA");
    }

    @Test
    void resolucionesConcurrentesConservanUnInforme() throws Exception {
        long id = enProceso();
        CyclicBarrier barrier = new CyclicBarrier(2);
        doAnswer(call -> {
            Object result = java.util.Optional.ofNullable(entityManager.find(Incidente.class, id));
            barrier.await(10, TimeUnit.SECONDS);
            return result;
        }).when(incidentes).findById(id);
        simultaneas("/informes-resolucion", informe(id));
        assertThat(cantidad("informes_resolucion")).isEqualTo(1);
        assertThat(cantidad("historial_estados_incidente")).isEqualTo(3);
    }

    @Test
    void contratoOpenApiPublicaOperaciones() throws Exception {
        JsonNode spec = enviar("GET", "/v3/api-docs", null, 200);
        assertThat(spec.at("/info/title").asText()).isEqualTo("HogarYA API");
        assertThat(spec.at("/paths/~1incidentes/post/responses/201").isMissingNode()).isFalse();
        assertThat(spec.at("/paths/~1incidentes/post/responses/409").isMissingNode()).isFalse();
        assertThat(spec.at("/paths/~1informes-resolucion/post").isMissingNode()).isFalse();
        assertThat(mvc.perform(request(HttpMethod.GET, "/swagger-ui/index.html"))
                .andReturn().getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void mensualAnuladaPermiteReemitirConservandoLaAnterior() throws Exception {
        long primera = emitir();
        enviar("PATCH", "/facturas/" + primera + "/estado", Map.of("estado", "ANULADA"), 200);
        long segunda = emitir();
        assertThat(segunda).isNotEqualTo(primera);
        assertThat(cantidad("facturas")).isEqualTo(2);
    }

    @Test
    void vencimientoUsaUltimoDiaYLosFiltrosReflejanVencidas() throws Exception {
        JsonNode factura = enviar("POST", "/facturas", Map.of("contratoId", 2,
                "periodo", "2026-02", "fechaEmision", "2026-02-01"), 201);
        assertThat(factura.get("fechaVencimiento").asText()).isEqualTo("2026-02-28");
        assertThat(factura.get("estado").asText()).isEqualTo("VENCIDA");
        assertThat(enviar("GET", "/facturas?estado=VENCIDA", null, 200)
                .at("/_embedded/facturas").size()).isEqualTo(1);
        assertThat(enviar("GET", "/facturas?estado=PENDIENTE", null, 200)
                .path("_embedded").path("facturas").size()).isZero();
    }

    @Test
    void modificarContratoConservaImporteEmitido() throws Exception {
        long id = emitir();
        try {
            jdbc.update("UPDATE contratos SET importe_mensual = 500000 WHERE id = 1");
            assertThat(enviar("GET", "/facturas/" + id, null, 200)
                    .get("importe").decimalValue()).isEqualByComparingTo("450000");
        } finally {
            jdbc.update("UPDATE contratos SET importe_mensual = 450000 WHERE id = 1");
        }
    }

    @Test
    void contratosFueraDeVigenciaInactivosYEliminadosImpidenAlta() throws Exception {
        for (int contrato : new int[]{2, 3, 4}) {
            enviar("POST", "/incidentes", Map.of("contratoId", contrato, "titulo", "Prueba",
                    "descripcion", "Prueba", "categoria", "GENERAL", "prioridad", "BAJA"), 409);
        }
        assertThat(cantidad("incidentes")).isZero();
        assertThat(cantidad("historial_estados_incidente")).isZero();
    }

    @Test
    void seguimientoContinuaAlFinalizarElContrato() throws Exception {
        long id = registrar();
        try {
            jdbc.update("UPDATE contratos SET activo = 0 WHERE id = 1");
            estado(id, "EN_PROCESO");
            estado(id, "CANCELADO");
            enviar("PATCH", "/incidentes/" + id + "/estado", Map.of("estado", "EN_PROCESO"), 409);
            assertThat(cantidad("historial_estados_incidente")).isEqualTo(3);
        } finally {
            jdbc.update("UPDATE contratos SET activo = 1 WHERE id = 1");
        }
    }

    @Test
    void pagoParcialNoDejaPagoNiFacturaPagada() throws Exception {
        long id = emitir();
        enviar("POST", "/pagos", Map.of("facturaId", id, "medioPago", "EFECTIVO",
                "importePagado", 100, "interes", 0), 409);
        assertThat(cantidad("pagos")).isZero();
        assertThat(enviar("GET", "/facturas/" + id, null, 200)
                .get("estado").asText()).isEqualTo("PENDIENTE");
    }
}
