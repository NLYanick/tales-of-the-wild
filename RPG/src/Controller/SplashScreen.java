package Controller;

import View.MainScene;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen extends Preloader {

    private final Pane parent = new Pane();

    private Stage preloaderStage;

    @Override
    public void init() throws Exception {

        Image image = new Image("Images/Background/Grass.png");

        parent.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));
    }

    @Override
    public void start(Stage stage) throws Exception {
        preloaderStage = stage;

        Scene scene = new Scene(parent, MainScene.SCENEWIDTH, MainScene.SCENEHEIGHT);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {

        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloaderStage.close();
        }
    }
}