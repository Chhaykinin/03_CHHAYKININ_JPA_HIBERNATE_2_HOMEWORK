package com.example.demoEntity.repository;
import com.example.demoEntity.model.Book;
import com.example.demoEntity.model.request.BookRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class BookRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public BookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Book insertBook(BookRequest bookRequest) {
        Book book =new Book();
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        book.setAuthor(bookRequest.getAuthor());
        book.setPublicationYear(bookRequest.getPublicationYear());
        entityManager.persist(book);
        return book;
    }

    public List<Book> findAllBooks(Integer offset, Integer limit) {
        // Create a JPQL query to fetch books with pagination
        String jpql = "SELECT b FROM Book b";
        TypedQuery<Book> query = entityManager.createQuery(jpql, Book.class);

        // Set the pagination parameters
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        // Execute the query and return the results
        return query.getResultList();
    }

    public Book findBookById(UUID id) {
        return entityManager.find(Book.class,id);
    }

    public Book updateBookById(UUID id, BookRequest bookRequest) {
        // Find the existing Person entity
        Book existingBook = entityManager.find(Book.class, id);
        if (existingBook != null) {
            // Update the existing entity with the values from the passed entity
            existingBook.setTitle(bookRequest.getTitle());
            existingBook.setDescription(bookRequest.getDescription());
            existingBook.setAuthor(bookRequest.getAuthor());
            existingBook.setPublicationYear(bookRequest.getPublicationYear());
            // Merge the updated entity
            entityManager.merge(existingBook);
        }
        return existingBook;
    }

    public void removeBook(UUID id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        }
    }

    public List<Book> findBooksByTitle(String title) {
        String jpql = "SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:title)";
        TypedQuery<Book> query = entityManager.createQuery(jpql, Book.class);
        query.setParameter("title","%"+ title +"%");
        return query.getResultList();
    }
}
