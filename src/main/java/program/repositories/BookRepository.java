package program.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import program.entities.Author;
import program.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
