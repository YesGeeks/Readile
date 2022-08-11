package com.readile.readile.controllers.authentication;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.readile.readile.config.FxController;
import com.readile.readile.controllers.ToolBar;
import com.readile.readile.views.Intent;
import com.readile.readile.views.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@FxmlView("/fxml/GoogleSignIn.fxml")
public class GoogleSignInController extends ToolBar implements Initializable, FxController {
    // VIEW VARIABLES --- <
    @FXML
    private WebView googleWebView;
    private WebEngine webEngine;
    @FXML
    private HBox toolBar;
    // VIEW VARIABLES --- >

    // SERVICES --- <
    @Lazy
    @Autowired
    StageManager stageManager;
    // SERVICES --- >

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Intent.toolBar = toolBar;

        webEngine = googleWebView.getEngine();

        if (Intent.isLogin) {
            try {
                setGoal(Intent.bookName, Intent.freq, Intent.count);
                back();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else loadPage();
    }

    @FXML
    void loadPage() {
        String redirectUrl = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=799625423213-kri1664qhnu9j8bk09le3cl9ig96pdq1.apps.googleusercontent.com&scope=profile%20email&state=w5_Xmqgaf-dxP-EzBk4QJuskUL8_pSvG-vEXiLX-Q5w%3D&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth2%2Fauthorization%2Fgoogle&nonce=92DaLzjyqv2ewYxEniF7mXloaAABgRQNt2M58ZV7swM&flowName=GeneralOAuthFlow";
        webEngine.load(redirectUrl);
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);

        // Load client secrets
        String CREDENTIALS_FILE_PATH = "/credentials.json";
        InputStream in = GoogleSignInController.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        String TOKENS_DIRECTORY_PATH = "tokens";
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver, browser -> {
            System.out.println(browser);
            webEngine.load(browser);
        }).authorize("user");
    }

    public void setGoal(String bookName, String freq, int count) throws GeneralSecurityException, IOException {
        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Readile")
                .build();

        Event event = new Event()
                .setSummary("Keep going with your goal to finish: " + bookName)
                .setDescription("Reading Goal Reminder | Readile");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        DateTime startDateTime = new DateTime(format.format(new Date()).replace(' ', 'T')+"-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(format.getTimeZone().getID());
        event.setStart(start);

        String[] recurrence = new String[] {"RRULE:FREQ="+freq+";COUNT="+count};
        event.setRecurrence(Arrays.asList(recurrence));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        service.events().insert(calendarId, event).execute();
    }

    public void back() {
        stageManager.rebuildStage(Intent.popClosedScene());
    }
}