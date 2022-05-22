package com.readile.readile.controllers;

import com.readile.readile.config.FxController;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@FxmlView("/fxml/GoogleSignIn.fxml")
public class GoogleSignInController implements Initializable, FxController {

    @Lazy
    @Autowired
    StageManager stageManager;

    @FXML
    private WebView googleWebView;

    private WebEngine webEngine;

    @FXML
    private HBox toolBar;
    private double xOffset = 0, yOffset = 0;

    private String clientId = "532707911915-5p40npcifslosr3f67t3gbhjru7ijlg4.apps.googleusercontent.com";
    private String clientSecret = "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";
    private String redirectUrl = "https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?response_type=code&client_id=532707911915-iutmfj62fe2bc5pc8ut8nvf5rdim0fok.apps.googleusercontent.com&scope=openid%20profile%20email&state=w5_Xmqgaf-dxP-EzBk4QJuskUL8_pSvG-vEXiLX-Q5w%3D&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin%2Foauth2%2Fcode%2Fgoogle&nonce=92DaLzjyqv2ewYxEniF7mXloaAABgRQNt2M58ZV7swM&flowName=GeneralOAuthFlow";
    private String state;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = googleWebView.getEngine();
        loadPage();
    }

    @FXML
    void loadPage() {
        webEngine.load(redirectUrl);
    }

    @FXML
    public void minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }

    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void move(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

        toolBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        toolBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
