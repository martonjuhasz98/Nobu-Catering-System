package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBItem {

    public ArrayList<Item> getItems();
    public ArrayList<Item> searchItems(String keyword);
    public Item selectItem(String barcode);
    public String insertItem(Item item);
    public boolean updateItem(Item item);
    public boolean deleteItem(Item item);
}