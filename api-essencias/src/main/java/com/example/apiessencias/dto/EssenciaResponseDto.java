package com.example.apiessencias.dto;


import lombok.Data;

import java.util.List;

@Data
public class EssenciaResponseDto {

    private String id;
    private String name;
    private List<String> values;

}
