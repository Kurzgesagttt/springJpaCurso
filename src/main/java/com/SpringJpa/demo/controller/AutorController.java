package com.SpringJpa.demo.controller;

import com.SpringJpa.demo.controller.dto.AutorDTO;
import com.SpringJpa.demo.controller.dto.ErroResposta;
import com.SpringJpa.demo.exceptions.OperacaoNaoPermitidaException;
import com.SpringJpa.demo.exceptions.RegistroDuplicadoException;
import com.SpringJpa.demo.model.Autor;
import com.SpringJpa.demo.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid AutorDTO autor) {
        try {
            Autor autorEntidade = autor.mapearAutor();
            service.salvarAutor(autorEntidade);


            //retorna o ID da entidade criada na url para o padrao rest
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(autorEntidade.getId())
                    .toUri();

            return ResponseEntity.created(location).build();

        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){
        var idAutor = UUID.fromString((id));
        Optional<Autor> autorOptional = service.obterPorId(idAutor);
        if(autorOptional.isPresent()){
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(
                    autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNacionalidade()
            );
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") String id){

        try{
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorId(idAutor);

            if(autorOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            service.deletar(autorOptional.get());
            return ResponseEntity.noContent().build();
        }catch (OperacaoNaoPermitidaException e){
            var erroResposta = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome",required = false) String nome
            ,@RequestParam(value = "nacionalidade",required = false) String nacionalidade) {
        List<Autor> resultado = service.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> lista = resultado.stream().map(autor -> new AutorDTO(autor.getId(),
                autor.getNome(), autor.getDataNascimento(),autor.getNacionalidade())).
                collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable String id,@RequestBody @Valid AutorDTO dto){
        try {
            var idAutor = UUID.fromString(id);

            Optional<Autor> encontrado = service.obterPorId(idAutor);
            if(encontrado.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            var autor = encontrado.get();

            autor.setNome(dto.nome());
            autor.setNacionalidade(dto.nacionalidade());
            autor.setDataNascimento(dto.dataNascimento());

            service.atualizarAutor(autor);
            return ResponseEntity.noContent().build();

        }catch(RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

}
