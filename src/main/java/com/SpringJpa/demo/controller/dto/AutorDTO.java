package com.SpringJpa.demo.controller.dto;

import com.SpringJpa.demo.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(UUID id,
                       @NotBlank(message = "campo obrigatorio")
                       String nome,
                       @NotNull(message = "campo obrigatorio")
                       LocalDate dataNascimento,
                       @NotBlank(message = "campo obrigatorio")
                       String nacionalidade) {

    public Autor mapearAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }
}
