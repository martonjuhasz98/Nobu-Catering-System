package modlayer;

public class MenuItem {
	
	private int id;
	private String name;
	private double price;
	private MenuItemCategory category;
	
	public MenuItem() {
		this.id = -1;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public MenuItemCategory getCategory() {
		return category;
	}
	public void setCategory(MenuItemCategory category) {
		this.category = category;
	}
}
