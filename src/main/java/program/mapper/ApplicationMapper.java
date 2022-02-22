package program.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import program.dto.author.AuthorAddDto;
import program.dto.author.AuthorDto;
import program.entities.Author;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(source = "fullName", target = "name")
    AuthorDto AuthorByAuthorDto(Author author);
    List<AuthorDto> ListAuthorByListAuthorDto(List<Author> authors);
    Author AuthorByAddAuthorDto(AuthorAddDto dto);
}
