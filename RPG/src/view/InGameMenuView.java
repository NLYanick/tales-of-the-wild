package view;

import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InGameMenuView extends BorderPane {

	private GridPane menuPane;
	private BorderPane buttonsMenu;
	private HBox playerPane;
	private VBox buttonsBox;
	
	private MainScene scene;
	
	private Button[] buttons;
	private ImageView arrowView;
	
	private StringProperty coinsText;
	
	private final int arrowMargin = 45;
	private final int imageDifference = 14;
	
	public InGameMenuView(MainScene scene) {
		this.scene = scene;
		
		setUpLayout();
	}

	private void setUpLayout() {
		menuPane = new GridPane();
		
		buttonsMenu = getButtonsMenu();
		fillButtonsArray();
		
		menuPane.add(buttonsMenu, 1, 1);
		
		setCenter(menuPane);
		
	}
	
	public void fillGrid() {
		menuPane.getChildren().clear();
		for(int x = 0; x < 2; x++) {
			for(int y = 0; y < 3; y++) {
				if(x == 1 && y == 1) {
					menuPane.add(buttonsMenu, x, y);
				} else if(x == 1 && y == 2) {
					menuPane.add(playerPane, x, y);
				} else {
					menuPane.add(new Rectangle(scene.getWidth()/4.5, scene.getHeight()/5, Color.TRANSPARENT), x, y);
				}
			}
		}
	}
	
	private void fillButtonsArray() {
		buttons = new Button[buttonsBox.getChildren().size()];
		
		int i = 0;
		for(Node node : buttonsBox.getChildren()) {
			buttons[i] = (Button) node;
			i++;
		}
	}
		
	private HBox getPlayerPane() {
		int spacing = 40;
		
		HBox playerPane = new HBox(spacing);
		playerPane.setAlignment(Pos.CENTER);
		playerPane.getStyleClass().add("ingame-menu-playerpane");
		
		HBox playerText = getPlayerText();
		HBox coinsText = getPlayerCoins();
		
		playerPane.getChildren().addAll(playerText, coinsText);
		
		return playerPane;
	}
	
	private HBox getPlayerText() {
		
		HBox playerText = new HBox();
		playerText.setAlignment(Pos.CENTER);
		
		Label playerName = new Label(scene.getPlayer().getName());
		playerName.getStyleClass().add("ingame-menu-player-stats");
		
		playerText.getChildren().addAll(playerName);
		
		return playerText;
	}
	
	private HBox getPlayerCoins() {
		int spacing = 4;
		
		HBox coinsText = new HBox(spacing);
		coinsText.setAlignment(Pos.CENTER);
		
		Image coinImg = new Image("Images/Items/TalesCoin.png");
		ImageView coinView = new ImageView(coinImg);
		
		Label coinsLabel = new Label();
		this.coinsText = coinsLabel.textProperty();
		coinsLabel.getStyleClass().add("ingame-menu-player-stats");
		
		int coins = scene.getPlayer().getTalesCoins();
		this.coinsText.set("" + coins);
		
		coinsText.getChildren().addAll(coinView, coinsLabel);
		
		return coinsText;
	}
	
	public void setPlayerPane() {
		playerPane = getPlayerPane();
		menuPane.add(playerPane, 1, 3);
	}

	private BorderPane getButtonsMenu() {
		
		BorderPane buttonsMenu = new BorderPane();
		buttonsMenu.getStyleClass().add("ingame-menu-buttonspane");
		
		VBox buttonsBox = getButtonsBox();
		
		buttonsMenu.setCenter(buttonsBox);
		
		arrowView = new ImageView(new Image("Images/SelectArrow.png"));;
		buttonsMenu.getChildren().add(arrowView);
		
		return buttonsMenu;
	}
	
	private VBox getButtonsBox() {
		
		int spacing = 20;
		
		buttonsBox = new VBox(spacing);
		buttonsBox.setAlignment(Pos.CENTER);
		
		Button inventoryButton = getButton("Inventory");
		inventoryButton.setOnAction(e -> openInventory());
		Button tempButtonOne = getButton("Temp Text");
		tempButtonOne.setOnAction(e -> System.out.println("1 1 1 1"));
		Button tempButtonTwo = getButton("Temp Text");
		tempButtonTwo.setOnAction(e -> System.out.println("2 2 2 2"));
		Button tempButtonThree = getButton("Temp Text");
		tempButtonThree.setOnAction(e -> System.out.println("3 3 3 3"));
		
		buttonsBox.getChildren().addAll(inventoryButton, tempButtonOne, tempButtonTwo, tempButtonThree);
		
		return buttonsBox;
	}
	
	private Button getButton(String text) {
		Button button = new Button(text);
		button.getStyleClass().add("ingame-menu-button");
			
		button.setOnKeyPressed(e -> handleButtonKeyPressed(e, button));
		addListenersToButton(button);
		
		return button;
	}
	
	private void addListenersToButton(Button button) {		
		button.focusedProperty().addListener(((observableValue, isNotFocused, isFocused) -> {
		 	if(isFocused) {
		 		arrowView.layoutXProperty().unbind();
		        arrowView.layoutYProperty().unbind();
		        
		 		arrowView.layoutXProperty().bind(button.layoutXProperty().subtract(arrowMargin));
				arrowView.layoutYProperty().bind(button.layoutYProperty().add(imageDifference));
		 	} 
		}));
	}
	
	private void handleButtonKeyPressed(KeyEvent e, Button button) {
		if(e.getCode().equals(KeyCode.ENTER) && button.isFocused()) {
			button.fire();
		}
	}
	
	public void requestFocusForButtons() {
		buttons[0].requestFocus();
	}
	
	public void resetView() {
		buttonsMenu.setCenter(buttonsBox);
	}
	
	private void openInventory() {
		scene.openPlayerInventory();
	}
	
	public void setCoinsText(int coins) {
		coinsText.set("" + coins);
	}
	
}
