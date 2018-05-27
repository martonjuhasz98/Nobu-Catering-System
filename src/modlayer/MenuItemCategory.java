package modlayer;

public class MenuItemCategory {

	private int id;
	private String name;
	
	public MenuItemCategory() {
		id = -1;
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
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof MenuItemCategory)) {
	      return false;
	    }
	    MenuItemCategory i = (MenuItemCategory)o;
	    return i.id == id;
	}
	@Override
	public int hashCode() {
	    return 0;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
