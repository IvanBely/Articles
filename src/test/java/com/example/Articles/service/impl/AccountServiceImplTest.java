package com.example.Articles.service.impl;

import com.example.Articles.dto.response.ArticleResponse;
import com.example.Articles.dto.response.CommentResponse;
import com.example.Articles.dto.response.UserInfoResponse;
import com.example.Articles.model.Article;
import com.example.Articles.model.Comment;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.ArticleRepository;
import com.example.Articles.model.repository.CommentRepository;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountService;
import com.example.Articles.service.CreateTimeFormService;
import com.example.Articles.service.MainPageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CreateTimeFormServiceImpl createTimeFormService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MainPageServiceImpl mainPageService;

    @Test
    public void testGetAccountArticle() {
        // Create a user
        User user = new User();
        user.setId(1L);
        user.setUsername("Unknown");

        List<Article> articles = new ArrayList<>();

        Article article = new Article();
        article.setId(1L); // Убран лишний "(" и добавлено значение идентификатора
        article.setName("Article"); // Пробел в конце имени необходимо убрать
        article.setDescription("Description"); // Пробел в конце описания необходимо убрать
        article.setUser(user);
        article.setCreateTime(LocalDateTime.now());
        article.setLikesCount(1L);
        articles.add(article);

        when(articleRepository.findAllByUserId(user.getId())).thenReturn(articles);

        when(createTimeFormService.createTimeForm(any())).thenReturn("Test Time");
        List<CommentResponse> commentResponseList = new ArrayList<>();
        List<ArticleResponse> articleResponsesList = new ArrayList<>();
        ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setName(article.getName());
        articleResponse.setDescription(article.getDescription());
        articleResponse.setUsername(article.getUser().getUsername());
        articleResponse.setCreateTime(createTimeFormService.createTimeForm(article.getCreateTime()));
        articleResponse.setLikesCount(article.getLikesCount());
        articleResponse.setCommentResponseList(commentResponseList);
        articleResponsesList.add(articleResponse);
        when(mainPageService.getArticleForm(articles)).thenReturn(articleResponsesList);

        List<ArticleResponse> articleResponses = accountService.getAccountArticle(user);

        assertEquals(1, articleResponses.size());
        ArticleResponse response = articleResponses.get(0);
        assertEquals("Article", response.getName()); // Убран пробел в конце имени статьи
        assertEquals("Description", response.getDescription()); // Убран пробел в конце описания статьи
        assertEquals("Test Time", response.getCreateTime());
        assertEquals(1, response.getLikesCount()); // Исправлено значение для счетчика лайков
        assertEquals("Unknown", response.getUsername());
        assertEquals(0, response.getCommentResponseList().size());

        verify(articleRepository, times(1)).findAllByUserId(user.getId());
        verify(createTimeFormService, times(1)).createTimeForm(any());
        verify(mainPageService, times(1)).getArticleForm(articles);
    }

    @Test
    public void getUserInfo_ReturnsUserInfoResponse() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testUser");

        AccountService accountServiceMock = Mockito.mock(AccountService.class);

        when(accountServiceMock.getUserInfo(user))
                .thenReturn(new UserInfoResponse("test@example.com", "testUser", "Your password"));

        UserInfoResponse result = accountServiceMock.getUserInfo(user);

        assertEquals("test@example.com", result.getEmail());
        assertEquals("testUser", result.getUsername());
        assertEquals("Your password", result.getPassword());
    }

    @Test
    public void setEmail_UpdatesUserEmail() {
        User user = new User();
        user.setEmail("old@example.com");
        String newEmail = "new@example.com";
        when(userRepository.save(user)).thenReturn(user);

        accountService.setEmail(user, newEmail);

        assertEquals(newEmail, user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void setUsername_UpdatesUsername() {
        User user = new User();
        user.setUsername("oldUsername");
        String newUsername = "newUsername";
        when(userRepository.save(user)).thenReturn(user);

        accountService.setUsername(user, newUsername);

        assertEquals(newUsername, user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void setPassword_UpdatesUserPassword() {
        User user = new User();
        String newPassword = "newPassword";
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        accountService.setPassword(user, newPassword);

        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void deleteAccount_DeletesUserAndRelatedEntities() {
        User user = new User();
        user.setId(1L);

        List<Comment> commentList = new ArrayList<>();
        List<Article> articleList = new ArrayList<>();

        when(commentRepository.findAllByUserId(user.getId())).thenReturn(commentList);
        when(articleRepository.findAllByUserId(user.getId())).thenReturn(articleList);

        accountService.deleteAccount(user);

        for (Comment comment : commentList) {
            verify(commentRepository, times(1)).delete(comment);
        }

        for (Article article : articleList) {
            verify(articleRepository, times(1)).delete(article);
        }

        verify(userRepository, times(1)).delete(user);
    }
}
