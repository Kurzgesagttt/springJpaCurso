package com.SpringJpa.demo.controller.dto;

import com.SpringJpa.demo.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(UUID id,
                       @NotBlank(message = "campo obrigatorio")
                               @Size(min = 2, max = 100,message = "Campo fora do tamanho padrao")
                       String nome,
                       @NotNull(message = "campo obrigatorio")
                               @Past(message = "Nao pode ser uma data futura")
                       LocalDate dataNascimento,
                       @NotBlank(message = "campo obrigatorio")
                       @Size(min = 2, max = 100,message = "Campo fora do tamanho padrao")
                       String nacionalidade) {

}
