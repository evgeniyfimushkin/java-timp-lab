package edu.evgen;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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

        rootStage.setWidth(800);
        rootStage.setHeight(600);
        //преобразование объектов если не null
        // map - это преобразование объекта далее и далее
        // ifPresent - void процедура сеттер, финализация цепочки преобразований
        // если в optional null, то он просто идёт в конец. То есть если null то ничего не выполнится но и NPE не возникнет

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainScene.fxml"));
        Optional.ofNullable(loader)
                .map(this::loadSafe)
                .map(Scene::new)
                .ifPresent(rootStage::setScene);

        String applicationCss = this.getResource("/application.css").toExternalForm();

        Optional.ofNullable(rootStage)
                .map(Stage::getScene)
                .map(Scene::getRoot)
                .map(Parent::getStylesheets)
                .ifPresent((ObservableList<String> sheets) -> sheets.add(applicationCss));

//        rootStage.getScene()
//                .getRoot()
//                .getStylesheets()
//                .add(this.getResource("/application.css").toExternalForm());
//



        log.info("loader inited");
        // корневой компонент пользовательского интерфейса, остальные вложены в него
//        Parent roortScene = loader.load();
//        roortScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        log.info("loader loaded");
//        rootStage.setScene(new Scene(roortScene));

        rootStage.show();
        //SceneController.class.cast(loader.getController()).doMoving();
        //Thread.sleep(5000);

        //((SceneController) loader.getController())


    }

    Parent load(FXMLLoader fxmlLoader) {
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    Parent loadSafe(FXMLLoader fxmlLoader){
            return fxmlLoader.load();
    }

    private URL getResource(String resource) {
        return this.getClass().getResource(resource);
    }
}