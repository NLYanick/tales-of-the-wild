package Model;

public class Item {

	private String imageUrl;
	private String name;
	
	public Item(String name, String imageUrl) {
		this.imageUrl = imageUrl;
		this.name = name;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public String getName() {
		return name;
	}
	
}
