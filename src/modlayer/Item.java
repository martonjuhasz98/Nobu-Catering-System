package modlayer;

public class Item {
	
	private String barcode;
	private String name;
	private double quantity;
	private Unit unit;
	private ItemCategory category;
	
	public Item() {
		
	}
	public Item(String barcode, String name, double quantity, Unit unit, ItemCategory category) {
		super();
		this.barcode = barcode;
		this.name = name;
		this.quantity = quantity;
		this.unit = unit;
		this.category = category;
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
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Item)) {
	      return false;
	    }
	    Item i = (Item)o;
	    return i.barcode.equals(barcode);
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
