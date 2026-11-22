package tuti.daos.hogarya;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class InicioRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void inicioPermiteDescubrirLaDocumentacion() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre").value("HogarYA"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/"))
                .andExpect(jsonPath("$._links.documentacion.href")
                        .value("http://localhost/v3/api-docs"))
                .andExpect(jsonPath("$._links.swagger.href")
                        .value("http://localhost/swagger-ui/index.html"));
    }

    @Test
    void enlacesDeDocumentacionSonAccesibles() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("HogarYA API"))
                .andExpect(jsonPath("$.paths['/'].get").exists());

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"));
    }
}
