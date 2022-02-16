package program.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import program.dto.author.*;
import program.entities.Animal;
import program.entities.Author;
import program.mapper.ApplicationMapper;
import program.repositories.AnimalRepository;
import program.repositories.AuthorRepository;
import program.storage.StorageService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@RequiredArgsConstructor
public class HomeController {
    private final AuthorRepository authorRepository;
    private final AnimalRepository animalRepository;
    private final ApplicationMapper applicationMapper;
    private final StorageService storageService;

    @Autowired
    public HomeController(AuthorRepository authorRepository, ApplicationMapper applicationMapper, AnimalRepository animalRepository,
                          StorageService storageService)
    {
        this.authorRepository = authorRepository;
        this.applicationMapper = applicationMapper;
        this.animalRepository = animalRepository;
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public String upload(@RequestBody UploadImageDto dto) {
        String image = storageService.store(dto.getBase64());
        return image;
    }

    @GetMapping("/")
    public List<AuthorDto> index() {
       return  applicationMapper
               .ListAuthorByListAuthorDto(authorRepository.findAll());
    }

    @PostMapping("/")
    public String create(AuthorAddDto model) {
        Author author = applicationMapper.AuthorByAddAuthorDto(model);
        String fileName=storageService.store(model.getImageBase64());
        author.setImage(fileName);
        authorRepository.save(author);
        return fileName;
    }
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws Exception {

        Resource file = storageService.loadAsResource(filename);
        String urlFileName =  URLEncoder.encode("сало.jpg", StandardCharsets.UTF_8.toString());
        return ResponseEntity.ok()
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
                .contentType(MediaType.IMAGE_JPEG)

                .header(HttpHeaders.CONTENT_DISPOSITION,"filename=\""+urlFileName+"\"")
                .body(file);
    }

    @PostMapping("/create")
    public int create(AnimalAddDto model) {
        Animal animal = applicationMapper.AnimalByAddAnimalDto(model);
        animalRepository.save(animal);
        return animal.getId();
    }

    @GetMapping("/delete/{id}")
    public int delete(@PathVariable("id") int id) {
        animalRepository.deleteById(id);
        return id;
    }

    @GetMapping("/read")
    public List<AnimalDto> read() {
        return  applicationMapper
                .ListAnimalByListAnimalDto(animalRepository.findAll());
    }
}
