package back_ptitulo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ControllerEjecucion {

    private String azureFunctionUrl;

    @PostMapping("/api/ejecutar")
    public Map<String, String> ejecutarCodigo(@RequestBody String codigo) {
        System.out.println("URL: " + azureFunctionUrl);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(codigo, headers);
        Map<String, String> responseMap = new HashMap<>();

        try {
            System.out.println("Comenzando comunicación");

            ResponseEntity<String> response = restTemplate.postForEntity(azureFunctionUrl, request, String.class);

            String contentType = response.getHeaders().getContentType().toString();
            System.out.println("Content Type: " + contentType);
            System.out.println("Full Response: " + response.getBody());

            if (!contentType.contains("application/json")) {
                System.err.println("Error: Expected JSON, but got " + contentType);
                System.err.println("Response Body: " + response.getBody());
                throw new RuntimeException("Expected JSON, but got " + contentType);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> responseBody = objectMapper.readValue(response.getBody(), Map.class);

            if (responseBody != null) {
                responseMap.put("stdout", responseBody.getOrDefault("stdout", ""));
                responseMap.put("stderr", responseBody.getOrDefault("stderr", ""));
            } else {
                responseMap.put("stdout", "");
                responseMap.put("stderr", "No se recibió respuesta del intérprete. Disculpe las molestias.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al ejecutar código: " + e.getMessage());
            responseMap.put("stdout", "");
            responseMap.put("stderr", "Error al ejecutar código: " + e.getMessage());
        }
        return responseMap;
    }
}
