package modlayer;

public class ItemCategory {

	private int id;
	private String name;
	
	public ItemCategory() {
		id = -1;
	}
	public ItemCategory(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof ItemCategory)) {
	      return false;
	    }
	    ItemCategory i = (ItemCategory)o;
	    return i.id == id;
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
