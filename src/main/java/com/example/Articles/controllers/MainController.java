package com.example.Articles.controllers;

import com.example.Articles.dto.response.ArticlesResponse;
import com.example.Articles.model.Articles;
import com.example.Articles.model.Users;
import com.example.Articles.model.repository.CommentsRepository;
import com.example.Articles.model.repository.UsersRepository;
import com.example.Articles.service.impl.AccountArticleServiceImpl;
import com.example.Articles.service.impl.CommentArticleServiceImpl;
import com.example.Articles.service.impl.MainArticleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
@AllArgsConstructor
public class MainController {
    private final MainArticleServiceImpl mainArticleService;
    private final AccountArticleServiceImpl accountArticleService;
    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;
    private final CommentArticleServiceImpl commentArticleService;

    @GetMapping("/")
    public ResponseEntity<List<ArticlesResponse>> getPublicList() {
        List<ArticlesResponse> articlesResponseList = mainArticleService.getFirstPublicArticles();
        return ResponseEntity.ok(articlesResponseList);
    }
    @GetMapping("/{hash}")
    public ResponseEntity<ArticlesResponse> getByHash(@PathVariable String hash) {
        Optional<ArticlesResponse> optionalArticlesResponse = mainArticleService.getFormByHash(hash);
        if (optionalArticlesResponse.isPresent()) {
            return ResponseEntity.ok(optionalArticlesResponse.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//    @PostMapping("/{hash}") // лайк
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ArticlesResponse> getByHash(@PathVariable String hash) {
//        Optional<ArticlesResponse> optionalArticlesResponse = mainArticleService.getByHash(hash);
//        if (optionalArticlesResponse.isPresent()) {
//            return ResponseEntity.ok(optionalArticlesResponse.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
    @PostMapping("/{hash}")
    public ResponseEntity<String> addCommentToArticle(@PathVariable String hash, @RequestBody String commentText, Principal principal) {
        String username = principal.getName();
        // Ищем статью по ее хешу
        Optional<Articles> optionalArticle = mainArticleService.findByHash(hash);
        Optional<Users> optionalUser = usersRepository.findByUsername(username);
        if (optionalArticle.isPresent() && optionalUser.isPresent()) {
            // Получаем статью
            Articles article = optionalArticle.get();
            // Получаем пользователя
            Users user = optionalUser.get();
            // Добавляем комментарий к статье
            commentArticleService.addCommentToArticle(user, article, commentText);
            return ResponseEntity.ok("Comment added successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{hash}")
    public ResponseEntity<String> updateCommentInArticle(@PathVariable String hash, @PathVariable Long commentId, @RequestBody ArticlesRequest updatedComment, Principal principal) {
        // Получаем имя пользователя из Principal
        String username = principal.getName();
        // Ищем статью по ее хешу
        Optional<Article> optionalArticle = mainArticleService.findByHash(hash);
        if (optionalArticle.isPresent()) {
            // Получаем статью
            Article article = optionalArticle.get();
            // Получаем пользователя
            Users user = AccountArticleService.findByUsername(username);
            // Обновляем комментарий к статье
            AccountArticleService.updateCommentInArticle(user, article, commentId, updatedComment);
            return ResponseEntity.ok("Comment updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{hash}")
    public ResponseEntity<String> deleteCommentFromArticle(@PathVariable String hash, @PathVariable Long commentId, Principal principal) {
        // Получаем имя пользователя из Principal
        String username = principal.getName();
        // Ищем статью по ее хешу
        Optional<Article> optionalArticle = mainArticleService.findByHash(hash);
        if (optionalArticle.isPresent()) {
            // Получаем статью
            Article article = optionalArticle.get();
            // Получаем пользователя
            Users user = AccountArticleService.findByUsername(username);
            // Удаляем комментарий из статьи
            AccountArticleService.deleteCommentFromArticle(user, article, commentId);
            return ResponseEntity.ok("Comment deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
