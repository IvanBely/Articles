package com.example.Articles.controllers;

import com.example.Articles.model.User;
import com.example.Articles.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void addUser_ReturnsCreatedResponse_WhenUserAddedSuccessfully() {
        User user = new User();

        ResponseEntity<String> responseEntity = authenticationController.addUser(user);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("User is saved", responseEntity.getBody());
        verify(userService, times(1)).addUser(user);
    }
}
