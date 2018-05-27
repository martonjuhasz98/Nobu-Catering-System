package modlayer;

import java.util.ArrayList;

public class MenuItem {
	
	private int id;
	private String name;
	private double price;
	private MenuItemCategory category;
	private ArrayList<Ingredient> ingredients;
	
	public MenuItem() {
		id = -1;
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
	public MenuItemCategory getCategory() {
		return category;
	}
	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
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
	public void setCategory(MenuItemCategory category) {
		this.category = category;
	}
	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof MenuItem)) {
	      return false;
	    }
	    MenuItem i = (MenuItem)o;
	    return i.id == id;
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
