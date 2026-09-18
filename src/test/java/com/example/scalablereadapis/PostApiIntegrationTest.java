package com.example.scalablereadapis;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scalablereadapis.repository.PostRepository;

@SpringBootTest
@AutoConfigureMockMvc
class PostApiIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired PostRepository repository;

    @Test void contextLoadsAndSeedsPosts() { org.junit.jupiter.api.Assertions.assertTrue(repository.count() >= 15); }

    @Test void paginationAndSortingWork() throws Exception {
        mockMvc.perform(get("/api/posts?page=0&size=5&sort=title,asc"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(5)))
            .andExpect(jsonPath("$.totalElements", is(15)))
                .andExpect(header().exists("X-Correlation-ID"));
    }

    @Test void getPostAndAuthorJpqlWork() throws Exception {
        mockMvc.perform(get("/api/posts/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
        mockMvc.perform(get("/api/posts/author/John")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test void optimizedJoinFetchAndNativeQueryWork() throws Exception {
        mockMvc.perform(get("/api/posts/with-comments")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$[0].commentCount", greaterThanOrEqualTo(1)));
        mockMvc.perform(get("/api/posts/top")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(5)));
    }

    @Test void analyticsAndValidationWork() throws Exception {
        mockMvc.perform(get("/api/posts/analytics")).andExpect(status().isOk()).andExpect(jsonPath("$.totalPosts", is(15)))
                .andExpect(jsonPath("$.totalComments", greaterThan(0)));
        mockMvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"\",\"content\":\"\",\"author\":\"\",\"category\":\"\",\"likes\":-1}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.validationErrors", aMapWithSize(5)));
    }
}
