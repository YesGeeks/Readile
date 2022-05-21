package com.readile.readile.views;

import com.jfoenix.controls.JFXDialog;
import com.readile.readile.config.FxController;
import com.readile.readile.models.user.User;
import com.readile.readile.utils.ResultBook;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Intent {
    public static User activeUser;
    public static JFXDialog addNewBookDialog;
    public static Class<? extends FxController> currentSceneClass;
    public static Observer observer;

    private static final Stack<Class<? extends FxController>> backStack = new Stack<>();
    public static Circle innerCircle;

    public static void pushClosedScene(Class<? extends FxController> sceneController) {
        backStack.push(sceneController);
    }

    public static void clearBackStack() {
        backStack.clear();
    }

    public static Class<? extends FxController> popClosedScene() {
        return backStack.pop();
    }

    public static List<ResultBook> tempSearchResults = new ArrayList<>();

    public static void set(List<ResultBook> resultBookList) {
        tempSearchResults.clear();
        tempSearchResults.addAll(resultBookList);
    }

    public static void clearTempResults() {
        tempSearchResults.clear();
    }

    public static Long categoryId;

    public static int generatedSecurityCode;
    public static long sendingTime;
}