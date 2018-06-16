package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBMenuItem {

    public ArrayList<MenuItem> getMenuItems(boolean orderedByCategory);
    public ArrayList<MenuItem> searchMenuItems(String keyword, boolean orderedByCategory);
    public MenuItem selectMenuItem(int id);
    public int insertMenuItem(MenuItem menuItem);
    public boolean updateMenuItem(MenuItem menuItem);
    public boolean deleteMenuItem(MenuItem menuItem);
}