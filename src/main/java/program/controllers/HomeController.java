package program.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import program.dto.author.AnimalAddDto;
import program.dto.author.AuthorDto;
import program.entities.Animal;
import program.entities.Author;
import program.mapper.ApplicationMapper;
import program.repositories.AnimalRepository;
import program.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final AuthorRepository authorRepository;
    private final AnimalRepository animalRepository;
    private final ApplicationMapper applicationMapper;

    @GetMapping("/")
    public List<AuthorDto> index() {
       return  applicationMapper
               .ListAuthorByListAuthorDto(authorRepository.findAll());
    }

    @PostMapping("")
    public int create(AnimalAddDto model) {
        Animal animal = applicationMapper.AnimalByAddAnimalDto(model);
        animalRepository.save(animal);
        return animal.getId();
    }
}
