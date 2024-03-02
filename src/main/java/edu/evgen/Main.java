package edu.evgen;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;
//import java.awt.Image;

@Slf4j
public class Main extends Application {

    public static void main(String[] args) {
        //Запуск апликейшн атакуейшн
        Application.launch();
    }

    //загрузка сцены
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/mainScene.fxml");
        log.info("mainScene.fxml: {}", url);
        loader.setLocation(url);

        // корневой компонент пользовательского интерфейса, остальные вложены в него
        Parent root = loader.load();
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setScene(new Scene(root));
        String css = this.getClass().getResource("/application.css").toExternalForm();
        root.getStylesheets().add(css);

        primaryStage.show();
        Controller controller = loader.getController();
    }
}