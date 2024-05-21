package com.example.Articles.service.impl;

import com.example.Articles.dto.request.ArticlesRequest;
import com.example.Articles.service.CreateArticlesHash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateArticlesHashImpl implements CreateArticlesHash {
    @Override
    public String createHashUrl(ArticlesRequest articlesRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        // Получаем текущее время в миллисекундах и преобразуем его в строку
        String currentTime = String.valueOf(Instant.now().toEpochMilli());

        // Хэшируем текущее время
        String hash = hashString(currentTime);

        // Используем первые 8 символов хэша
        String timeHash = hash.substring(0, 8);

        // Хэшируем текущее название статьи
        String name = hashString(articlesRequest.getName());

        // Используем первые 8 символов хэша
        String nameHash = hash.substring(0, 8);

        // Соединяем
        stringBuilder.append(timeHash).append(nameHash);

        return stringBuilder.toString();
    }

    public static String hashString(String input) {
        try {
            // Создаем экземпляр MessageDigest с алгоритмом SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Хэшируем входную строку
            byte[] hashBytes = digest.digest(input.getBytes());

            // Преобразуем байты хэша в шестнадцатеричную строку
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}


