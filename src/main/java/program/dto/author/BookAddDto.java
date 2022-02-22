package program.dto.author;

import lombok.Data;

import java.util.List;
@Data
public class BookAddDto {
    private String name;
    private int authorId;
    private List<String> images;
}
