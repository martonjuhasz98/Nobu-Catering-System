package guilayer.stocktaking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import ctrllayer.ItemController;
import guilayer.MainWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.EditListener;
import guilayer.inventory.ListInventory;
import modlayer.Employee;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class CheckInventory extends JPanel implements ActionListener, CaretListener, ListSelectionListener, TableModelListener {

	private ItemController itemCtrl;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_inventory;
	private InventoryTableModel mdl_inventory;
	private JButton btn_add;
	private JButton btn_remove;
	private JTable tbl_stocktaking;
	private StocktakingTableModel mdl_stocktaking;
	private JButton btn_confirm;
	
	public CheckInventory() {
		itemCtrl = new ItemController();
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setBounds(0, 0, 801, 500);
		
		mdl_inventory = new InventoryTableModel();
		mdl_stocktaking = new StocktakingTableModel();
		
		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		txt_search.setColumns(10);
		add(txt_search);
		
		btn_search = new JButton("Search");
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);
		
		btn_confirm = new JButton("Confirm");
		btn_confirm.setBounds(718, 3, 73, 23);
		add(btn_confirm);
		
		JScrollPane scrlPane_inventory = new JScrollPane();
		scrlPane_inventory.setBounds(10, 28, 300, 461);
		add(scrlPane_inventory);
		
		tbl_inventory = new JTable();
		tbl_inventory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_inventory.getTableHeader().setReorderingAllowed(false);
		tbl_inventory.setAutoCreateRowSorter(true);
		tbl_inventory.setModel(mdl_inventory);
		scrlPane_inventory.setViewportView(tbl_inventory);
		
		btn_add = new JButton("Add");
		btn_add.setBounds(320, 236, 73, 23);
		add(btn_add);
		
		btn_remove = new JButton("Remove");
		btn_remove.setBounds(320, 270, 73, 23);
		add(btn_remove);
		
		JScrollPane scrlPane_stocktaking = new JScrollPane();
		scrlPane_stocktaking.setBounds(400, 28, 391, 461);
		add(scrlPane_stocktaking);
		
		tbl_stocktaking = new JTable();
		tbl_stocktaking.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_stocktaking.getTableHeader().setReorderingAllowed(false);
		tbl_stocktaking.setAutoCreateRowSorter(true);
		tbl_stocktaking.setModel(mdl_stocktaking);
		scrlPane_stocktaking.setViewportView(tbl_stocktaking);
		
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_confirm.addActionListener(this);
		tbl_inventory.getSelectionModel().addListSelectionListener(this);
		tbl_stocktaking.getSelectionModel().addListSelectionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		mdl_stocktaking.addTableModelListener(this);
		
		resetForm();
	}
	private void resetForm() {
		mdl_inventory.setItems(itemCtrl.getItems());
		mdl_stocktaking.setItems(new ArrayList<Item>());
		
		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
		btn_confirm.setEnabled(false);
	}
	private void searchInventory() {
		String keyword = txt_search.getText().trim();
		mdl_inventory.setItems(itemCtrl.searchItems(keyword));
	}
	private void addToStocktaking() {
		int[] selection = tbl_inventory.getSelectedRows();
		ArrayList<Item> items = new ArrayList<Item>(selection.length);
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_inventory.convertRowIndexToModel(selection[i]);
			items.add(mdl_inventory.getItemAt(selection[i]));
		}
		
		mdl_stocktaking.addItems(items);
	}
	private void removeFromStocktaking() {
		int[] selection = tbl_stocktaking.getSelectedRows();
		ArrayList<Item> items = new ArrayList<Item>(selection.length);
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_stocktaking.convertRowIndexToModel(selection[i]);
			items.add(mdl_stocktaking.getItemAt(selection[i]));
		}
		
		mdl_stocktaking.removeItems(items);
	}
	private void createStocktaking() {
		Employee employee = new Employee();
		employee.setCpr("100298-0612");
		
		if (!itemCtrl.createStocktaking(employee, mdl_stocktaking.getItems())) {
			JOptionPane.showMessageDialog(this,
				    "An error occured while creating the Stock-taking!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(this,
				"The Stock-taking was successfully created!",
			    "Success!",
			    JOptionPane.INFORMATION_MESSAGE);
		resetForm();
	}
	
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchInventory();
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel source = (ListSelectionModel)e.getSource();
		boolean empty = source.isSelectionEmpty();
		
		if (source == tbl_inventory.getSelectionModel()) {
			btn_add.setEnabled(!empty);
		} else if (source == tbl_stocktaking.getSelectionModel()) {
			btn_remove.setEnabled(!empty);
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == mdl_stocktaking) {
			boolean empty = mdl_stocktaking.getItems().isEmpty();
			btn_confirm.setEnabled(!empty);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			searchInventory();
		} else if (e.getSource() == btn_add) {
			addToStocktaking();
		} else if (e.getSource() == btn_remove) {
			removeFromStocktaking();
		} else if (e.getSource() == btn_confirm) {
			createStocktaking();
		}
	}
	
	
	private class InventoryTableModel extends AbstractTableModel {
		
		private String[] columns = new String[] { "Name", "Quantity", "Unit", "Category" };
		protected ArrayList<Item> items;
		
		public InventoryTableModel() {
			this(new ArrayList<Item>());
		}
		public InventoryTableModel(ArrayList<Item> items) {
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
		public Object getValueAt(int rowIndex, int columnIndex) {
			
			Item item = items.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return item.getName();
				case 1:
					return item.getQuantity();
				case 2:
					return item.getUnit().getAbbr();
				case 3:
					return item.getCategory().getName();
			}
			
			return null;
		}
		@Override
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}
		@Override
		public Class getColumnClass(int columnIndex) {
			switch(columnIndex) {
				case 0:
					return String.class;
				case 1:
					return Double.class;
				case 2:
					return Unit.class;
				case 3:
					return ItemCategory.class;
		}
		
		return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		public void update() {
			fireTableDataChanged();
		}
		public Item getItemAt(int rowIndex) {
			return items.get(rowIndex);
		}
		public void setItems(ArrayList<Item> items) {
			this.items = items;
			update();
		}
		public void addItems(ArrayList<Item> items) {
			for (Item item : items) {
				if (this.items.indexOf(item) < 0)
					this.items.add(item);
			}
			update();
		}
		public void removeItems(ArrayList<Item> items) {
			for (Item item : items) {
				this.items.remove(item);
			}
			update();
		}
	}
	private class StocktakingTableModel extends InventoryTableModel {
		
		public StocktakingTableModel() {
			super();
		}
		public StocktakingTableModel(ArrayList<Item> items) {
			super(items);
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1;
		}
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			try {
				double quantity = (double)value;
				super.items.get(rowIndex).setQuantity(quantity);
			} catch(Exception e) {}
		}
		public ArrayList<Item> getItems() {
			return super.items;
		}
	}
}
