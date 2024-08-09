package com.example.demoEntity.controller;
import com.example.demoEntity.model.Book;
import com.example.demoEntity.model.request.BookRequest;
import com.example.demoEntity.model.response.APIResponse;
import com.example.demoEntity.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/book")
public class BookController {
    //Injection repository
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //Post book
    @PostMapping
    @Operation(summary = "Insert book")
    public ResponseEntity<APIResponse<Book>> insertBook(@RequestBody BookRequest bookRequest){
        bookRepository.insertBook(bookRequest);
        APIResponse<Book> response = APIResponse.<Book> builder()
                .message("Insert book successfully")
                .status(HttpStatus.CREATED)
                .creationDate(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Get all books
    @GetMapping
    @Operation(summary = "Read all book")
    public ResponseEntity<APIResponse<List<Book>>> findAllBooks(@RequestParam(defaultValue = "1") Integer offset,@RequestParam(defaultValue = "5")  Integer limit){

        // Validate parameters
        if (offset < 0) {
            return ResponseEntity.badRequest().body(APIResponse.<List<Book>>builder()
                    .message("Offset must be greater than  0")
                    .status(HttpStatus.BAD_REQUEST)
                    .creationDate(new Date())
                    .build());
        }

        if (limit <= 0) {
            return ResponseEntity.badRequest().body(APIResponse.<List<Book>>builder()
                    .message("Limit must be greater than 0")
                    .status(HttpStatus.BAD_REQUEST)
                    .creationDate(new Date())
                    .build());
        }
        offset = (offset - 1) * limit;
        List<Book> books = bookRepository.findAllBooks(offset, limit);

        APIResponse<List<Book>> response = APIResponse.<List<Book>>builder()
                .message("Books have been successfully retrieved")
                .payload(books)
                .status(HttpStatus.OK)
                .creationDate(new Date())
                .build();

        return ResponseEntity.ok(response);
    }

    //Get book by id
    @GetMapping("/{id}")
    @Operation(summary = "Read book by id")
    public ResponseEntity<APIResponse<Book>> findBookById(@PathVariable UUID id){
        APIResponse<Book> response= APIResponse.<Book> builder()
                .message("Read book have been successfully founded")
                .payload(bookRepository.findBookById(id))
                .status(HttpStatus.OK)
                .creationDate(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Get book by title
    @GetMapping("/title/{title}")
    @Operation(summary = "Read books by title")
    public ResponseEntity<APIResponse<List<Book>>> findBooksByTitle(@PathVariable String title) {
        List<Book> books = bookRepository.findBooksByTitle(title);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    APIResponse.<List<Book>>builder()
                            .message("No books found with the title '" + title + "'")
                            .status(HttpStatus.NOT_FOUND)
                            .creationDate(new Date())
                            .build()
            );
        }

        APIResponse<List<Book>> response = APIResponse.<List<Book>>builder()
                .message("Books have been successfully found")
                .payload(books)
                .status(HttpStatus.OK)
                .creationDate(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Update book
    @PutMapping("/{id}")
    @Operation(summary = "Update book by id")
    public ResponseEntity<APIResponse<Book>> updateBookById(@PathVariable UUID id,@RequestBody BookRequest bookRequest){
        APIResponse<Book> response = APIResponse.<Book>builder()
                .message("Update  Book Success!")
                .payload(bookRepository.updateBookById(id,bookRequest))
                .status(HttpStatus.OK)
                .creationDate(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Delete book
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by id")
    public ResponseEntity<APIResponse<Book>> removeBook(@PathVariable UUID id){
        bookRepository.removeBook(id);
        APIResponse<Book> response = APIResponse.<Book> builder()
                .message("The Book has been successfully deleted.")
                .status(HttpStatus.OK)
                .creationDate(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
