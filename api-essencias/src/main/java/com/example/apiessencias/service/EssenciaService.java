package com.example.apiessencias.service;

import com.example.apiessencias.client_essencias.EssenceCache;
import com.example.apiessencias.client_essencias.EssenciaClient;
import com.example.apiessencias.client_essencias.LimiteBusca;
import com.example.apiessencias.dto.EssenciaResponseDto;
import com.example.apiessencias.dto.EssenciasResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class EssenciaService {

    private final EssenciaClient essenciaClient;
    private final LimiteBusca rateLimiter;
    private final EssenceCache essenceCache;

    public List<EssenciasResponseDto> getAllEssences() throws IOException {
        String clientId = getClientId();
        rateLimiter.checkLimit(clientId);

        List<EssenciasResponseDto> cachedEssences = essenceCache.getEssences(clientId);
        if (cachedEssences != null) {
            return cachedEssences;
        }

        List<EssenciasResponseDto> essences = essenciaClient.listarEssencias();
        essenceCache.putEssences(clientId, essences);
        return essences;
    }

    public EssenciaResponseDto getEssenceById(String essenceId) {
        String clientId = getClientId();
        rateLimiter.checkLimit(clientId);

        EssenciaResponseDto cachedEssence = essenceCache.getEssenceById(essenceId);
        if (cachedEssence != null) {
            return cachedEssence;
        }

        EssenciaResponseDto essence = essenciaClient.detalharEssencia(essenceId);
        essenceCache.putEssenceById(essenceId, essence);
        return essence;
    }

    private String getClientId() {
        // Implemente a lógica para obter o ID do cliente, se necessário
        return "client1";
    }
}