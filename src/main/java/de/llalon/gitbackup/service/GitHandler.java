package de.llalon.gitbackup.service;

import de.llalon.gitbackup.model.GitRepo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface GitHandler {

    List<GitRepo> getAllRepos() throws Exception;
}
