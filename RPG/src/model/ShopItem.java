package model;

public class ShopItem {
    private Item item;
    private int price;

    public ShopItem(Item item, int price) {
        this.item = item;
        this.price = price;
    }

    public Item getItem() { return item; }
    public int getPrice() { return price; }
}