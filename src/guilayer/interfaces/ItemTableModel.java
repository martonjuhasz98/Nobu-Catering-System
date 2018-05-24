package guilayer.interfaces;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public abstract class ItemTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	protected String[] columns;
	protected ArrayList<T> items;
	
	public ItemTableModel() {
		this(new ArrayList<T>());
	}
	public ItemTableModel(ArrayList<T> items) {
		this.items = items;
		update();
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
	public T getItemAt(int rowIndex) {
		return items.get(rowIndex);
	}
	public ArrayList<T> getItems() {
		return items;
	}
	public void setItems(ArrayList<T> items) {
		this.items = items;
		update();
	}
}
