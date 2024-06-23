package com.example.apiessencias.client_essencias;


import com.example.apiessencias.dto.EssenciaResponseDto;
import com.example.apiessencias.dto.EssenciasResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
public class EssenciaClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiUsername;
    private final String apiPassword;
    private final ObjectMapper objectMapper;

    @Autowired
    public EssenciaClient(RestTemplate restTemplate,
                          ObjectMapper objectMapper,
                          @Value("${api.url}/essences") String baseUrl,
                          @Value("${api.username}") String apiUsername,
                          @Value("${api.password}") String apiPassword) {
        this.restTemplate = restTemplate;
        this.restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(apiUsername, apiPassword));
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.apiUsername = apiUsername;
        this.apiPassword = apiPassword;
    }

    public List<EssenciasResponseDto> listarEssencias() throws IOException {
        HttpURLConnection conexao = prepararConexao(baseUrl, apiUsername, apiPassword);
        conexao.setRequestMethod("GET");

        int codigoResposta = conexao.getResponseCode();
        if (codigoResposta == HttpURLConnection.HTTP_OK) {
            BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }
            leitor.close();

            return parsearEssencias(resposta.toString());
        } else {
            throw new IOException("Erro HTTP: " + codigoResposta);
        }
    }

    public EssenciaResponseDto detalharEssencia(String idEssencia) {
        try {
            String url = String.format("%s/%s", baseUrl, idEssencia);
            HttpHeaders headers = obterHeaders();
            HttpEntity<String> entidade = new HttpEntity<>(headers);
            ResponseEntity<String> resposta = restTemplate.exchange(url, HttpMethod.GET, entidade, String.class);

            return objectMapper.readValue(resposta.getBody(), EssenciaResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao buscar detalhes da essência", e);
        }
    }

    private List<EssenciasResponseDto> parsearEssencias(String json) throws IOException {
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    private HttpHeaders obterHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", String.valueOf(MediaType.APPLICATION_JSON));
        return headers;
    }

    private HttpURLConnection prepararConexao(String url, String username, String password) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection conexao = (HttpURLConnection) apiUrl.openConnection();

        String auth = username + ":" + password;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        conexao.setRequestProperty("Authorization", authHeader);

        return conexao;
    }
}