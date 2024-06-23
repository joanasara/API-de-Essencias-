package com.example.apiessencias.controller;

import com.example.apiessencias.dto.EssenciaResponseDto;
import com.example.apiessencias.dto.EssenciasResponseDto;
import com.example.apiessencias.service.EssenciaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/essences")
public class EssenciaController {

    private final EssenciaService essenciaService;

    @GetMapping
    public List<EssenciasResponseDto> getAllEssences() throws IOException {
        return essenciaService.getAllEssences();
    }

    @GetMapping("/{id}")
    public EssenciaResponseDto getEssenceById(@PathVariable("id") String essenceId) {
        return essenciaService.getEssenceById(essenceId);
    }
}