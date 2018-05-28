package guilayer.orders;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.NumberFormatter;

import ctrllayer.OrderController;
import ctrllayer.MenuItemController;
import guilayer.WaiterWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformPanel;
import modlayer.OrderMenuItem;
import modlayer.MenuItem;
import modlayer.Order;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JSeparator;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;

public class EditOrder extends PerformPanel implements ActionListener, CaretListener, TableModelListener, ChangeListener {

	private OrderController orderCtrl;
	private MenuItemController itemCtrl;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_menu;
	private InventoryTableModel mdl_menu;
	private JTable tbl_order;
	private OrderTableModel mdl_order;
	private JButton btn_create;
	private JButton btn_cancel;
	private boolean isSearching;
	private String lastKeyword;
	private JFormattedTextField txt_subtotal;
	private JFormattedTextField txt_tax;
	private JFormattedTextField txt_total;
	private JSpinner spnr_table;
	
	public EditOrder() {
		orderCtrl = new OrderController();
		itemCtrl = new MenuItemController();
		lastKeyword = "";
		isSearching = false;
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setVisible(false);
		setBounds(0, 0, WaiterWindow.contentWidth, WaiterWindow.totalHeight);
		
		mdl_menu = new InventoryTableModel();
		mdl_order = new OrderTableModel();
		
		Label lbl_table = new Label("Table *");
		lbl_table.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_table.setBounds(10, 10, 129, 22);
		add(lbl_table);
		
		spnr_table = new JSpinner();
		spnr_table.setModel(new SpinnerNumberModel(new Integer(1), new Integer(0), null, new Integer(1)));
		spnr_table.setBounds(10, 38, 179, 20);
		add(spnr_table);
		
		Label lbl_items = new Label("Menu items *");
		lbl_items.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_items.setBounds(10, 64, 129, 22);
		add(lbl_items);
		
		txt_search = new JTextField();
		txt_search.setBounds(10, 92, 179, 20);
		add(txt_search);
		txt_search.setColumns(10);
		
		btn_search = new JButton("Search");
		btn_search.setBounds(199, 92, 73, 20);
		add(btn_search);
		
		JScrollPane scrlPane_menu = new JScrollPane();
		scrlPane_menu.setBounds(10, 123, 361, 293);
		add(scrlPane_menu);
		
		tbl_menu = new JTable();
		tbl_menu.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_menu.getTableHeader().setReorderingAllowed(false);
		tbl_menu.setAutoCreateRowSorter(true);
		mdl_menu.setItems(itemCtrl.getMenuItems());
		tbl_menu.setModel(mdl_menu);
		scrlPane_menu.setViewportView(tbl_menu);
		
		NumberFormat format = NumberFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(0);

		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setMinimum(0.0);
		formatter.setMaximum(10000000.0);
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		
		Label lbl_subtotal = new Label("Subtotal");
		lbl_subtotal.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_subtotal.setBounds(381, 32, 97, 22);
		add(lbl_subtotal);
		
		txt_subtotal = new JFormattedTextField(formatter);
		txt_subtotal.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_subtotal.setEditable(false);
		txt_subtotal.setBounds(611, 32, 179, 22);
		add(txt_subtotal);
		
		Label lbl_tax = new Label("Tax");
		lbl_tax.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_tax.setBounds(381, 56, 97, 22);
		add(lbl_tax);
		
		txt_tax = new JFormattedTextField(formatter);
		txt_tax.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_tax.setEditable(false);
		txt_tax.setBounds(611, 56, 179, 22);
		add(txt_tax);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(381, 84, 409, 2);
		add(separator);
		
		Label lbl_total = new Label("Total");
		lbl_total.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_total.setBounds(381, 92, 97, 22);
		add(lbl_total);
		
		txt_total = new JFormattedTextField(formatter);
		txt_total.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_total.setEditable(false);
		txt_total.setBounds(611, 89, 179, 22);
		add(txt_total);
		
		JScrollPane scrlPane_order = new JScrollPane();
		scrlPane_order.setBounds(381, 123, 409, 293);
		add(scrlPane_order);
		
		tbl_order = new JTable();
		tbl_order.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_order.getTableHeader().setReorderingAllowed(false);
		tbl_order.setAutoCreateRowSorter(true);
		mdl_order.setItems(new ArrayList<OrderMenuItem>());
		tbl_order.setModel(mdl_order);
		scrlPane_order.setViewportView(tbl_order);
		
		btn_create = new JButton("Create");
		btn_create.setBounds(536, 427, 122, 32);
		add(btn_create);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 427, 122, 32);
		add(btn_cancel);
		
		reset();
		
		AbstractAction add = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				MenuItem item = mdl_menu.getItem(modelRowIndex);
				OrderMenuItem orderItem = new OrderMenuItem();
				orderItem.setMenuItem(item);
				orderItem.setOrder(new Order());
				orderItem.setQuantity(1);
				
				mdl_order.addItem(orderItem);
			}
		};
		AbstractAction remove = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				OrderMenuItem orderItem = mdl_order.getItem(modelRowIndex);
				
				mdl_order.removeItem(orderItem);
			}
		};

		ButtonColumn addColumn = new ButtonColumn(tbl_menu, add, mdl_menu.getColumnCount() - 1);
		addColumn.setMnemonic(KeyEvent.VK_ACCEPT);
		ButtonColumn removeColumn = new ButtonColumn(tbl_order, remove, mdl_order.getColumnCount() - 1);
		removeColumn.setMnemonic(KeyEvent.VK_CANCEL);
		
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		btn_cancel.addActionListener(this);
		mdl_order.addTableModelListener(this);
		spnr_table.addChangeListener(this);
	}
	private void reset() {
		mdl_menu.setItems(itemCtrl.getMenuItems());
		mdl_order.setItems(new ArrayList<OrderMenuItem>());
		spnr_table.setValue(new Integer(1));
		updatePrices();
		
		txt_search.setText("");
		btn_create.setEnabled(false);
	}
	public void create() {
		setVisible(true);
	}
	private void close() {
		setVisible(false);
		reset();
	}
	private void createOrder() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Creating order", JOptionPane.YES_NO_OPTION)
				!= JOptionPane.YES_OPTION) {
			return;
		}
		
		//Table number
		int tableNo = (Integer)spnr_table.getValue();
		
		//Items
		ArrayList<OrderMenuItem> items = mdl_order.getItems();
		
		if (!orderCtrl.createOrder(tableNo, items)) {
			JOptionPane.showMessageDialog(this,
				    "An error occured while creating the Order!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(this,
			    "The Order was successfully created!",
			    "Success!",
			    JOptionPane.INFORMATION_MESSAGE);
		

		close();
		triggerPerformListeners();
	}
	private void cancel() {
		close();
		triggerCancelListeners();
	}
	private boolean isFilled() {
		if (mdl_order.getItems().isEmpty())
			return false;
		
		return true;
	}
	private void search() {
		if (isSearching)
			return;
		isSearching = true;
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) {
			isSearching = false;
			return;
		}
		lastKeyword = keyword;
		
		new SearchWorker(keyword).execute();
	}
	private void updatePrices() {
		double subtotal = 0, tax = 0, total = 0;
		for (OrderMenuItem item : mdl_order.getItems()) {
			subtotal += item.getQuantity() * item.getMenuItem().getPrice();
		}
		total = subtotal * 1.25;
		tax = total - subtotal;
		
		txt_subtotal.setValue(subtotal);
		txt_tax.setValue(tax);
		txt_total.setValue(total);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			search();
		} else if (e.getSource() == btn_create) {
			createOrder();
		} else if (e.getSource() == btn_cancel) {
			cancel();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			search();
		}
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == spnr_table) {
			btn_create.setEnabled(isFilled());
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == mdl_order) {
			btn_create.setEnabled(isFilled());
			updatePrices();
		}
	}
	private class InventoryTableModel extends ItemTableModel<MenuItem> {

		public InventoryTableModel() {
			super();
			
			columns = new String[] { "Item No.", "Name", "Price", "Category", ""};
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItem item = getItem(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return item.getId();
				case 1:
					return item.getName();
				case 2:
					return item.getPrice();
				case 3:
					return item.getCategory().getName();
				case 4:
					return "Add";
			}
			
			return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == 4); 
		}
	}
	private class OrderTableModel extends ItemTableModel<OrderMenuItem> {

		public OrderTableModel() {
			super();
			
			columns = new String[] { "Item No.", "Name", "Price", "Quantity", "Total", ""};
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			OrderMenuItem orderItem = getItem(rowIndex);
			MenuItem item = orderItem.getMenuItem();
			
			switch(columnIndex) {
				case 0:
					return item.getId();
				case 1:
					return item.getName();
				case 2:
					return item.getPrice();
				case 3:
					return orderItem.getQuantity();
				case 4:
					return item.getPrice() * orderItem.getQuantity();
				case 5:
					return "Remove";
			}
			
			return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == 3 || columnIndex == 5);
		}
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 3) {
				int quantity = (int)value;
				if (quantity > 0) {
					getItem(rowIndex).setQuantity(quantity);
				}
				
				updatePrices();
			}
		}
	}
	
	public class SearchWorker extends SwingWorker<ArrayList<MenuItem>, Void> {
		private String keyword;

		public SearchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<MenuItem> doInBackground() throws Exception {
			// Start
			return itemCtrl.searchMenuItems(keyword);
		}

		@Override
		protected void done() {
			try {
				mdl_menu.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isSearching = false;
			search();
		}
	}
}
