package com.SpringJpa.demo.dto;

import com.SpringJpa.demo.model.Autor;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(UUID id,
                       String nome,
                       LocalDate dataNascimento,
                       String nacionalidade) {

    public Autor mapearAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }
}
