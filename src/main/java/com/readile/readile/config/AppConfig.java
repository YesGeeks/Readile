package com.readile.readile.config;

import com.readile.readile.views.StageManager;
import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AppConfig {
    @Bean
    @Lazy()
    public StageManager stageManager(Stage stage) {
        return new StageManager(stage);
    }
}