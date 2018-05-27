package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBOrder {

    public ArrayList<Order> getOrders(boolean payed);
    public ArrayList<Order> searchOrders(String keyword, boolean payed);
    public Order selectOrder(int id);
    public int insertOrder(Order order);
    public boolean payOrder(Order order);
    public boolean cancelOrder(Order order);
}