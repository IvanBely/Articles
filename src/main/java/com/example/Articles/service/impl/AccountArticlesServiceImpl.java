package com.example.Articles.service.impl;

import com.example.Articles.Config.UrlConfig;
import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.dto.response.ArticlesUrlResponse;
import com.example.Articles.model.Articles;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticlesRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountArticlesService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountArticlesServiceImpl implements AccountArticlesService {
    private final CreateArticlesHashImpl сreateArticlesHashImpl;
    private final ArticlesRepository articlesRepository;
    private final UserRepository userRepository;
    private final UrlConfig urlConfig;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<ArticlesResponse> getAccountArticles(User user) {

        List<Articles> articlesList = articlesRepository.findAllByUserId(user.getId());

        return articlesList.stream().map(Articles ->
                        new ArticlesResponse(Articles.getName(), Articles.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public ArticlesUrlResponse addAndResponseUrl(User user, ArticlesRequest articlesRequest) {
        String hash = сreateArticlesHashImpl.createHashUrl();

        Articles Articles = new Articles();
        Articles.setUser(user);
        Articles.setHash(hash);
        Articles.setName(articlesRequest.getName());
        Articles.setDescription(articlesRequest.getDescription());
        Articles.setCreateTime(LocalDateTime.now()); // устанавливаем текущее время
        Articles.setLifeTime(articlesRequest.getLifeTime());
        Articles.setPublic(articlesRequest.isPublic());
        articlesRepository.save(Articles); // Сохраняем в базу данных

        return new ArticlesUrlResponse(urlConfig.getHost() + "/" + Articles.getHash());
    }
}
