package guilayer.interfaces;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public abstract class ItemTableModel<T> extends AbstractTableModel {

	protected String[] columns;
	protected ArrayList<T> items;
	
	public ItemTableModel() {
		super();
		
		columns = new String[0];
		items = new ArrayList<T>();
	}

	@Override
	public int getRowCount() {
		return items.size();
	}
	@Override
	public int getColumnCount() {
		return columns.length;
	}
	@Override
	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}
	@Override
	public Class getColumnClass(int columnIndex) {
		Class result;
		
		try {
			result = getValueAt(0, columnIndex).getClass();
		} catch (Exception e) {
			result = Object.class;
		}
		
		return result;
	}
	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);
	
	public void update() {
		fireTableDataChanged();
	}
	public T getItem(int index) {		
		return items.get(index);
	}
	public boolean addItem(T item) {
		boolean result = items.add(item);
		update();
		
		return result;
	}
	public T removeItem(int index) {
		T result = items.remove(index);
		update();
		
		return result;
	}
	public boolean removeItem(T item) {
		boolean result = items.remove(item);
		update();
		
		return result;
	}
	public ArrayList<T> getItems() {
		return items;
	}
	public void setItems(ArrayList<T> items) {
		this.items = items;
		update();
	}
}
