package view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import model.Item;
import model.ShopItem;

public class ShopInventoryView extends InventoryView {
	
	protected StringProperty infoBoxItemPrice, playerCoinsText;
	
	public ShopInventoryView(MainScene scene) {
		super(scene);
		
		canDragAndDrop = false;
	}
	
	protected void setUpStyling() {
		styling.put("background", "Images/Background/Water/Sand.png");
		
		styling.put("buttons-pane", "shop-inventory-buttons-pane");
		styling.put("slots-box", "shop-inventory-slots-box");
		styling.put("info-box", "shop-inventory-info-box");
		styling.put("button", "shop-inventory-button");
		
		styling.put("modal", "inventory-modal");
		styling.put("modal-text", "inventory-modal-text");
		styling.put("modal-close", "inventory-modal-close");
		
		styling.put("title", "inventory-title");
		styling.put("player-coins", "inventory-player-coins");
		styling.put("info-box-title", "inventory-info-box-title");
		styling.put("info-box-separator-line", "inventory-info-box-separator-line");
		styling.put("info-box-description", "inventory-info-box-description");
	}
	
	protected void setUpTexts() {
		texts.put("title", "Shop");
		
		texts.put("button-1", "Close");
		texts.put("button-2", "Buy");
	}
	
	protected void setUpBindings() {
		infoBoxName = new SimpleStringProperty();
		infoBoxDescription = new SimpleStringProperty();
		modalMessage = new SimpleStringProperty();
		
		slotIsFocused = new SimpleBooleanProperty(false).not();
		slotIsFocused = infoBoxName.isNotEmpty().and(infoBoxDescription.isNotEmpty());
		
		infoBoxItemPrice = new SimpleStringProperty();
	}
	
	protected VBox getTitleBox() {
		int spacing = 60;
		
		Text title = new Text(texts.get("title"));
		title.getStyleClass().add(styling.get("title"));
		
		HBox playerCoins = getPlayerCoins();
		
		VBox titleBox = new VBox(spacing/2, title, playerCoins);
		titleBox.setPadding(new Insets(spacing, 0, 0, 0));
		titleBox.setAlignment(Pos.TOP_CENTER);
		
		return titleBox;
	}
	
	private HBox getPlayerCoins() {
		int spacing = 8;
		int size = 48;
		
		ImageView coinView = getCoinImage(size);
		
		Text coinsText = new Text("-");
		coinsText.getStyleClass().add(styling.get("player-coins"));
		
		playerCoinsText = coinsText.textProperty();
		
		HBox playerCoins = new HBox(spacing, coinView, coinsText);
		playerCoins.setAlignment(Pos.CENTER);
		
		return playerCoins;
	}
	
	protected VBox getButtonsPane() {
		int spacing = 30;
		
		close = getButton(texts.get("button-1"));
		close.setOnAction(e -> scene.removeInventoryView(this));
		
		Button buy = getButton(texts.get("button-2"));
		buy.setOnAction(e -> buyItem());
		
		VBox buttonsPane = new VBox(close, buy);
		buttonsPane.setSpacing(spacing);
		buttonsPane.setAlignment(Pos.CENTER);
		
		return buttonsPane;
	}
	
	protected VBox getInfoTexts() {
		int spacing = 10;
		int innerSpace = 434;

		HBox titles = getInfoTitlesBox(innerSpace * 0.6);
		
		Line separator = getSeparatorLine("info-box-separator-line", innerSpace, true);
		
		Text description = getText("info-box-description", innerSpace, infoBoxDescription);
		
		VBox texts = new VBox(titles, separator, description);
		texts.setAlignment(Pos.TOP_LEFT);
		texts.setSpacing(spacing);
		
		return texts;
	}
	
	private HBox getInfoTitlesBox(double innerSpace) {
		Text name = getText("info-box-title", innerSpace, infoBoxName);
		HBox priceBox = getPriceBox();
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		HBox titles = new HBox(name, spacer, priceBox);
		titles.setAlignment(Pos.CENTER);
		
		return titles;
	}
	
	private HBox getPriceBox() {
		Text price = getText("info-box-title", infoBoxItemPrice);
		
		int size = 40;
		int spacing = 5;
		
		ImageView coinView = getCoinImage(size);
		coinView.visibleProperty().bind(slotIsFocused);
		
		HBox priceBox = new HBox(spacing, price, coinView);
		priceBox.setAlignment(Pos.CENTER);
		
		return priceBox;
	}
	
	private ImageView getCoinImage(int size) {
		Image image = new Image("Images/Items/TalesCoin.png");
		ImageView imageView = new ImageView(image);
		
		if(size > 0) {			
			imageView.setFitWidth(size);
			imageView.setPreserveRatio(true);
		}
		
		return imageView;
	}
	
	protected void focusInventorySlot(InventorySlot slot, boolean focused) {
		if(focused) focusedSlot = slot;
		else focusedSlot = null;
		
		ItemView itemView = slot.getItemView();
		Item item = scene.getItemFromView(itemView);
		ShopItem shopItem = scene.getCurrentBuildingShopItem(item);
		
		infoBoxName.set(itemView != null ? item.getName() : "");
		infoBoxDescription.set(itemView != null ? item.getDescription() : "");
		infoBoxItemPrice.set(shopItem != null ? "Price: " + shopItem.getPrice() : "");
		
		selectSlot(slot);
	}
	
	
	public void setPlayerCoins(int coins) {
		playerCoinsText.set("" + coins);
	}
	
	public void buyItem() {
		ItemView itemView = selectedSlot.getItemView();
		if(itemView == null) return;
		
		scene.buyItem(itemView);
	}

}
