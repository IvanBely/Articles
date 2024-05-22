package com.example.Articles.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class UrlConfig {
    private String host;
    private int publicListSize;
}
