package com.example.scalablereadapis.dto;

import jakarta.validation.constraints.*;

public record PostRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 4000) String content,
        @NotBlank @Size(max = 80) String author,
        @NotBlank @Size(max = 80) String category,
        @NotNull @Min(0) Integer likes
) { }
