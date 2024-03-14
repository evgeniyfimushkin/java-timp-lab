package edu.evgen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;

@Slf4j
public class Main extends Application {

    public static void main(String[] args) {
        //Запуск апликейшн атакуейшн
        Application.launch();
    }
    @Override
    public void start(Stage rootStage) throws Exception {

        rootStage.setWidth(1200);
        rootStage.setHeight(860);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainScene.fxml"));
        rootStage.setScene(new Scene(loader.load()));

        String applicationCss = this.getResource("/application.css").toExternalForm();

        rootStage.getScene()
                .getRoot()
                .getStylesheets()
                .add(applicationCss);

        SceneController controller = loader.getController();
        rootStage.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent) -> {
                    switch (keyEvent.getCode()){
                        case B-> controller.startButton.fire();
                        case E-> controller.stopButton.fire();
                        case T-> controller.setSimulationTimeVisible();
                    }
                });

        rootStage.show();
    }
    private URL getResource(String resource) {
        return this.getClass().getResource(resource);
    }
}