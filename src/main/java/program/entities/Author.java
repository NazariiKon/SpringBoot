package program.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name="tbl_authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="fullname", length = 200, nullable = false)
    private String fullName;

    @OneToMany(mappedBy="author")
    private Set<Book> books;
}
