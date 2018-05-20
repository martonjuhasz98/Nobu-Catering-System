package modlayer;

public class Ingredient {

	private Item item;
	private MenuItem menuItem;
	private double quantity;
	
	public Ingredient() {
		
	}

	public Item getItem() {
		return item;
	}
	public MenuItem getMenuItem() {
		return menuItem;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Ingredient)) {
	      return false;
	    }
	    Ingredient i = (Ingredient)o;
	    return i.item.equals(item) && i.menuItem.equals(menuItem);
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
