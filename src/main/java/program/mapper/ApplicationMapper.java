package program.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import program.dto.author.AnimalAddDto;
import program.dto.author.AuthorDto;
import program.entities.Animal;
import program.entities.Author;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(source = "fullName", target = "name")
    AuthorDto AuthorByAuthorDto(Author author);
    Animal AnimalByAddAnimalDto(AnimalAddDto dto);
    List<AuthorDto> ListAuthorByListAuthorDto(List<Author> authors);
}
