package com.example.Articles.service.impl;

import com.example.Articles.service.CreateTimeFormService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
@Service
public class CreateTimeFormServiceImpl implements CreateTimeFormService {
    @Override
    public String createTimeForm(LocalDateTime createTime) {

        LocalDateTime currentTime = LocalDateTime.now();

        Duration duration = Duration.between(createTime, currentTime).abs();
        Instant instant = Instant.now().minus(duration);
        return new PrettyTime(new Locale("ru")).format(Date.from(instant));
    }
}
