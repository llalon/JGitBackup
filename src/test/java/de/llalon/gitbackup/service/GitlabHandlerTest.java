package de.llalon.gitbackup.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GitlabHandlerTest {

    @Autowired
    private GitLabHandler handler;

    @Test
    @SneakyThrows
    void getAllRepos() {
        var result = handler.getAllRepos();

        assertNotNull(result);
    }
}
