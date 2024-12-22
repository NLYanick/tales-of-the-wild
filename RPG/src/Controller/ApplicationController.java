package Controller;

import java.io.File;

import View.MainScene;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class ApplicationController extends Application {

	public final static String APPLICATIONNAME = "Tales of the Wild";
	
	private Stage stage;
	
	private FileIO fileIO;
	private MainController controller;
	private MainScene scene;
	
	public ApplicationController() {
		
	}
	
	public void launchApplication(String[] args) {
		System.setProperty("javafx.preloader", SplashScreen.class.getName());
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		this.stage = stage;
		
		controller = new MainController(this, fileIO);
		scene = controller.getMainScene();
		
		stage.setTitle(APPLICATIONNAME);
		stage.getIcons().add(new Image("Images/Background/Grass.png"));
		
		stage.setFullScreen(true);
		stage.setFullScreenExitHint("");
		stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("Esc"));
//		stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("Alt + F"));
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.show();
		
	}
	
	@Override
	public void stop() {
		
	}
	
	public void setFullScreen() {
		stage.setFullScreen(true);
	}

    @Override
    public void init() throws Exception {
        super.init();
        
        fileIO = new FileIO();
    	fileIO.readText(new File(FileIO.BACKGROUNDFILEPATH));
    }

}
