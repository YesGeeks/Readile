package com.readile.readile.controllers.authentication;

import com.readile.readile.config.FxController;
import com.readile.readile.controllers.HomeScreenController;
import com.readile.readile.models.user.LoginInfo;
import com.readile.readile.models.user.User;
import com.readile.readile.services.implementation.authentication.LoginInfoService;
import com.readile.readile.services.implementation.user.UserService;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
public class OAuth2Authentication implements FxController {
    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    @Autowired
    UserService userService;
    @Autowired
    LoginInfoService loginInfoService;
    // SERVICES --- >

    @GetMapping
    public String authenticate(Principal principal) {
        try {
            String response = principal.toString();
            String name = response.substring(response.indexOf(" name=")+6).split(",")[0];
            String email = response.substring(response.indexOf("email=")+6).split(",")[0].replace("}","").replace("]","");
            String profileImage = response.substring(response.indexOf("picture=")+8).split(",")[0];

            if (userService.findByEmail(email) == null) {
                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setProfileImage(profileImage);
                user.setTheme((byte) 1);
                user.setRegistration("GOOGLE");
                userService.save(user);

                LoginInfo entry = new LoginInfo(userService.findByEmail(email), "pass");
                loginInfoService.save(entry);

                Intent.pushClosedScene(GoogleSignInController.class);
                Intent.activeUser = userService.findByEmail(email);
            } else {
                if (userService.findByEmail(email).getRegistration().equals("EMAIL"))
                    throw new Exception();
                else Intent.activeUser = userService.findByEmail(email);
            }
        } catch (Exception e) {
            stageManager.rebuildStage(SignInScreenController.class);
        }
        Platform.runLater(() -> stageManager.rebuildStage(HomeScreenController.class));
        return "201";
    }
}