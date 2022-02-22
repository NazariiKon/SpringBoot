package program.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import program.dto.author.AuthorAddDto;
import program.dto.author.AuthorDto;
import program.dto.author.BookAddDto;
import program.dto.author.UploadImageDto;
import program.entities.Author;
import program.entities.Book;
import program.entities.ImageEntity;
import program.mapper.ApplicationMapper;
import program.repositories.AuthorRepository;
import program.repositories.BookRepository;
import program.repositories.ImageRepository;
import program.storage.StorageService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
//@RequiredArgsConstructor
public class HomeController {
    private final AuthorRepository authorRepository;
    private final ImageRepository imageRepository;
    private final BookRepository bookRepository;
    private final ApplicationMapper applicationMapper;
    private final StorageService storageService;


    @Autowired
    public HomeController(AuthorRepository authorRepository, ImageRepository imageRepository,
                          BookRepository bookRepository,
                          ApplicationMapper applicationMapper,
                          StorageService storageService) {
        this.authorRepository = authorRepository;
        this.applicationMapper = applicationMapper;
        this.storageService = storageService;
        this.imageRepository = imageRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public List<AuthorDto> index() { // взяти всіх авторів
       return  applicationMapper
               .ListAuthorByListAuthorDto(authorRepository.findAll());
    }

    @PostMapping("/")
    public String create(AuthorAddDto model) {
        Author author = applicationMapper.AuthorByAddAuthorDto(model); // мап автора
        String fileName=storageService.store(model.getImageBase64()); // зберегти фотку
        author.setImage(fileName);
        authorRepository.save(author); // зберегти автора
        return fileName;
    }

    @PostMapping("/upload")
    public String upload(@RequestBody UploadImageDto dto) {
        try {
            String imageName = storageService.store(dto.getBase64()); // зберегти фото
            ImageEntity image = new ImageEntity(imageName); // створити новий імейдж
            //image.setUrlImage(imageName);
            imageRepository.save(image);
        } catch(Exception ex)
        {
            System.out.println("Error "+ ex.getMessage());
        }

        return imageName;
    }

    //додати нову книжку в базу з множиною фото.
    @PostMapping( "/addbook")
    public ResponseEntity create(@RequestBody BookAddDto add) throws IOException {

        Book book =new Book();
        book.setName(add.getName());
        Author author =  authorRepository.getById(add.getAuthorId()); // взяти автора по айді
        book.setAuthor(author);
        //book.setDescription(bookItemDto.getDescription());
        bookRepository.save(book);

        for (String name:add.getImages()) { // цикл по зображенням з моделі
            List<ImageEntity> images = imageRepository.findByName(name);
            ImageEntity image = images.get(0);
            image.setBook(book);
            imageRepository.save(image);
        }

        return ResponseEntity.ok(HttpStatus.OK);
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


}
