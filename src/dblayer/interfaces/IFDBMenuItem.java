package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBMenuItem {

    public ArrayList<MenuItem> getMenuItems();
    public ArrayList<MenuItem> searchMenuItems(String keyword);
    public MenuItem selectMenuItem(int id);
    public int insertMenuItem(MenuItem menuItem);
    public boolean updateMenuItem(MenuItem menuItem);
    public boolean deleteMenuItem(MenuItem menuItem);
}