package controller;

import java.io.File;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import view.MainScene;

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
		stage.getIcons().add(new Image("Images/Background/Grass/Grass.png"));
		
		stage.setFullScreen(true);
		stage.setFullScreenExitHint("");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		stage.centerOnScreen();
		stage.setResizable(false);
		
		stage.setScene(scene);
		stage.show();
		
	}
	
	@Override
	public void stop() {
		controller.stopNPCThreads();
	}
	
	public void setFullScreen(boolean isFullScreen) {
		stage.setFullScreen(isFullScreen);
	}
	
	public boolean isFullScreen() {
		return stage.isFullScreen();
	}

    @Override
    public void init() throws Exception {
        super.init();
        
        fileIO = new FileIO();
    	fileIO.readText(new File(FileIO.BACKGROUNDFILEPATH));
    }

}
