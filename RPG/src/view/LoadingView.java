package view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;

public class LoadingView extends BorderPane {

	public LoadingView() {
		setUpLayout();
	}

	private void setUpLayout() {		
		Image image = new Image("Images/Background/Grass/Grass.png");
        setBackground(new Background(new BackgroundImage(image, null, null, null, null)));
		
		Label loadingText = new Label("Loading");
		loadingText.getStyleClass().add("loading-view-text");
		
		BorderPane loadingTextPane = new BorderPane(loadingText);
		loadingTextPane.getStyleClass().add("loading-view-text-pane");
		
		setCenter(loadingTextPane);
		
		setAnimation(loadingText);
	}
	
	private void setAnimation(Label loadingText) {
		Thread animation = new Thread(() -> {
		    int i = 0;

		    while (true) {
		        try {
		            Thread.sleep(500);

		            int newI = i;
		            Platform.runLater(() -> {
		                StringBuilder dots = new StringBuilder();
		                for (int j = 0; j < newI; j++) {
		                    dots.append(".");
		                }
		                loadingText.setText("Loading" + dots);
		            });

		            i = (i + 1) % 4;

		        } catch (InterruptedException e) {
		            e.printStackTrace();
		            break;
		        }
		    }
		});
		animation.setDaemon(true);
		animation.start();
	}
	
}
