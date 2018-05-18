package modlayer;

public class Item {
	
	private String barcode;
	private String name;
	private double quantity;
	private Unit unit;
	private ItemCategory category;
	
	public Item() {
		
	}

	public String getBarcode() {
		return barcode;
	}
	public String getName() {
		return name;
	}
	public double getQuantity() {
		return quantity;
	}
	public Unit getUnit() {
		return unit;
	}
	public ItemCategory getCategory() {
		return category;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public void setCategory(ItemCategory category) {
		this.category = category;
	}
}
