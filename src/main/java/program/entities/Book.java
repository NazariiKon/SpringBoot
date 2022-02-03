package program.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="tbl_books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 200, nullable = false)
    private String name;
    @ManyToOne()
    @JoinColumn(name="author_id", nullable=false)
    private Author author;
}