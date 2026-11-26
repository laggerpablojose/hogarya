package com.hogarya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "HogarYA API", version = "1.0",
        description = "Facturas, pagos, incidentes e informes de resolución del integrador DAOS"))
public class HogarYaApplication {
    public static void main(String[] args) {
        SpringApplication.run(HogarYaApplication.class, args);
    }
}
