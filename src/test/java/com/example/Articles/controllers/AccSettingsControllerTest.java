package com.example.Articles.controllers;

import com.example.Articles.dto.response.UserInfoResponse;
import com.example.Articles.model.User;
import com.example.Articles.model.repository.UserRepository;
import com.example.Articles.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccSettingsControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccSettingsController accSettingsController;

    @Test
    public void getUserInfo_ReturnsUserInfo_WhenUserExists() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        UserInfoResponse expectedResponse = new UserInfoResponse();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(accountService.getUserInfo(user)).thenReturn(expectedResponse);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        ResponseEntity<UserInfoResponse> responseEntity = accSettingsController.getUserInfo(principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void getUserInfo_ReturnsNotFound_WhenUserDoesNotExist() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        ResponseEntity<UserInfoResponse> responseEntity = accSettingsController.getUserInfo(principal);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateAccountSettings_ReturnsBadRequest_WhenAllParametersAreEmpty() {
        String username = "testUser";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User())); // Возвращаем Optional с пустым пользователем

        ResponseEntity<String> responseEntity = accSettingsController.updateAccountSettings(null, null, null, principal);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("All parameters are empty", responseEntity.getBody());
    }

    @Test
    public void updateAccountSettings_CallsSetEmailUsernameAndPassword_WhenNewEmailUsernameAndPasswordProvided() {
        String newEmail = "newEmail";
        String newUsername = "newUsername";
        String newPassword = "newPassword";
        String username = "testUser";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<String> responseEntity = accSettingsController.updateAccountSettings(newEmail, newUsername, newPassword, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Account settings updated", responseEntity.getBody());
    }
    @Test
    public void deleteAccount_ReturnsOk_WhenUserExists() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        ResponseEntity<String> responseEntity = accSettingsController.deleteAccount(principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Account deleted", responseEntity.getBody());
    }

    @Test
    public void deleteAccount_ReturnsNotFound_WhenUserDoesNotExist() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        ResponseEntity<String> responseEntity = accSettingsController.deleteAccount(principal);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
