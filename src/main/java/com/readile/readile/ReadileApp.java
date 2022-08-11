package com.readile.readile;

import com.readile.readile.config.StageReadyEvent;
import com.readile.readile.controllers.SplashScreenController;
import com.readile.readile.views.StageManager;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ReadileApp implements ApplicationListener<StageReadyEvent> {
    private static StageManager STAGE_MANAGER;
    private final ConfigurableApplicationContext springAppContext;

    @Autowired // constructor injection
    public ReadileApp(ConfigurableApplicationContext springAppContext) {
        this.springAppContext = springAppContext;
    }

    //1. runs Java application. Neither SpringContext nor JavaFx context is initialized in this stage
    public static void main(String[] args) {
        //2. Start JavaFX application in another Thread (by calling com.sun.javafx.application.LauncherImpl.run())
        Application.launch(SpringBootJavaFx.class, args);
    }

    //6. callback method. Catching event produced by SpringBootJavaFxApplication.start() method, once the initialization of Spring context, JavaFx context and FxWeaver context is done
    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        STAGE_MANAGER = springAppContext.getBean(StageManager.class, event.stage);
        STAGE_MANAGER.rebuildStage(SplashScreenController.class);
    }

    public static StageManager getStageManager() {
        return STAGE_MANAGER;
    }
}