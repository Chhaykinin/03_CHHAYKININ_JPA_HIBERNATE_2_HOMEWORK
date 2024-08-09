package com.example.demoEntity.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String author;
    @NotNull
    private LocalDate publicationYear;
}
