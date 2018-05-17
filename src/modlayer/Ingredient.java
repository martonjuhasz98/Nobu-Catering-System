package modlayer;

public class Ingredient {

	private Item item;
	private MenuItem menuItem;
	private int quantity;
	
	public Ingredient() {
		
	}

	public Item getItem() {
		return item;
	}
	public MenuItem getMenuItem() {
		return menuItem;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
