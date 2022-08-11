package com.readile.readile;

import com.readile.readile.config.StageReadyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class SpringBootJavaFx extends Application {
    private ConfigurableApplicationContext context;

    // 3. JavaFx application thread. Started in MainApp.main method
    @Override
    public void init() {
        ApplicationContextInitializer<GenericApplicationContext> initializer =
                context -> {
                    context.registerBean(Application.class, () -> SpringBootJavaFx.this);
                    context.registerBean(Parameters.class, this::getParameters);
                };

        /* 4. initialize Spring application and run it. In the next step:
        all @SpringBootApplication and @Configuration classes are going to be initialized
        all @Bean classes are going to be initialized. Both inside our application and libraries (most importantly @Component SpringFxWeaver) */
        this.context = new SpringApplicationBuilder()
                .sources(ReadileApp.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) {
        /* 5. we are done with spring initialization in .init method. Now, throw Spring's ApplicationEvent which contains the primary stage.
        This stage is meant to be used by any registered event listener to build JavaFX UI */
        context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }
}