package edu.evgen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

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
    public void start(Stage rootStage) throws Exception {
        rootStage.setHeight(500);
        rootStage.setWidth(500);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainScene.fxml"));

        log.info("loader inited");
        // корневой компонент пользовательского интерфейса, остальные вложены в него
        Parent scene = loader.load();
        log.info("loader loaded");
        rootStage.setScene(new Scene(scene));
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

        rootStage.show();
    }
}