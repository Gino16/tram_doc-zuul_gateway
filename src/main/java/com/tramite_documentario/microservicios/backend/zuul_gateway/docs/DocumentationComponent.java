package com.tramite_documentario.microservicios.backend.zuul_gateway.docs;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@EnableAutoConfiguration
public class DocumentationComponent implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("microservicio-personas", "/api/personas/v2/api-docs", "2.0"));
        resources.add(swaggerResource("microservicio-archivos", "/api/archivos/v2/api-docs", "2.0"));
        resources.add(swaggerResource("microservicio-solicitudes", "/api/solicitudes/v2/api-docs", "2.0"));
        resources.add(swaggerResource("microservicio-usuarios", "/api/usuarios/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
