package guilayer.orders;

import java.awt.Label;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.NumberFormatter;

import ctrllayer.OrderController;
import ctrllayer.MenuItemController;
import guilayer.WaiterWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.PerformPanel;
import modlayer.OrderMenuItem;
import modlayer.MenuItem;
import modlayer.Order;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

public class EditOrder extends PerformPanel implements ActionListener, CaretListener, TableModelListener, ItemListener {

	private OrderController orderCtrl;
	private MenuItemController itemCtrl;
	private Order order;
	private JComboBox<String> cmb_table;
	private JTextField txt_search;
	private JButton btn_search;
	private boolean fetchingData;
	private String lastKeyword;
	private JTable tbl_menu;
	private InventoryTableModel mdl_menu;
	private JTable tbl_order;
	private OrderTableModel mdl_order;
	private JFormattedTextField txt_subtotal;
	private JFormattedTextField txt_tax;
	private JFormattedTextField txt_total;
	private JButton btn_submit;
	private JButton btn_cancel;
	private boolean isCreating;
	private ButtonColumn btn_add;
	private ButtonColumn btn_remove;
	
	public EditOrder() {
		super();
		
		itemCtrl = new MenuItemController();
		orderCtrl = new OrderController();
		
		order = null;
		isCreating = true;
		lastKeyword = "";
		fetchingData = false;
		
		initialize();
	}
	//Layout
	private void initialize() {
		
		setVisible(false);
		setBounds(0, 0, WaiterWindow.contentWidth, WaiterWindow.totalHeight);
		
		mdl_menu = new InventoryTableModel();
		mdl_order = new OrderTableModel();
		
		Label lbl_table = new Label("Table *");
		lbl_table.setBounds(10, 10, 129, 22);
		add(lbl_table);
		
		cmb_table = new JComboBox<String>();
		cmb_table.setBounds(10, 38, 179, 20);
		add(cmb_table);
		
		Label lbl_items = new Label("Menu items *");
		lbl_items.setBounds(10, 64, 129, 22);
		add(lbl_items);
		
		txt_search = new JTextField();
		txt_search.setMargin(new Insets(2, 5, 5, 5));
		txt_search.setBounds(10, 92, 179, 20);
		add(txt_search);
		
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
		lbl_subtotal.setBounds(381, 32, 97, 22);
		add(lbl_subtotal);
		
		txt_subtotal = new JFormattedTextField(formatter);
		txt_subtotal.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_subtotal.setEditable(false);
		txt_subtotal.setBounds(611, 32, 179, 22);
		add(txt_subtotal);
		
		Label lbl_tax = new Label("Tax");
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
		tbl_order.setModel(mdl_order);
		scrlPane_order.setViewportView(tbl_order);
		
		btn_submit = new JButton("Create");
		btn_submit.setBounds(536, 427, 122, 32);
		add(btn_submit);
		
		btn_cancel = new JButton("Back");
		btn_cancel.setBounds(668, 427, 122, 32);
		add(btn_cancel);

		btn_add = new ButtonColumn(tbl_menu, mdl_menu.getColumnCount() - 1, this);
		btn_add.setMnemonic(KeyEvent.VK_ADD);
		btn_remove = new ButtonColumn(tbl_order, mdl_order.getColumnCount() - 1, this);
		btn_remove.setMnemonic(KeyEvent.VK_DELETE);
		
		cmb_table.addItemListener(this);
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_submit.addActionListener(this);
		btn_cancel.addActionListener(this);
		mdl_order.addTableModelListener(this);
	}
	@Override
	public void prepare() {
		new FetchWorker().execute();
		String[] tables = new String[15];
		for (int i = 0; i < tables.length; i++) {
			tables[i] = String.format("Table No. %d", i+1); 
		}
		cmb_table.setModel(new DefaultComboBoxModel<String>(tables));
		cmb_table.setSelectedIndex(-1);
	}
	@Override
	public void reset() {
		order = null;
		isCreating = true;
		
		cmb_table.removeItemListener(this);
		cmb_table.setSelectedIndex(-1);
		cmb_table.addItemListener(this);
		tbl_menu.setEnabled(false);
		tbl_order.setEnabled(false);
		mdl_order.setItems(new ArrayList<OrderMenuItem>());
		updatePrices();
		
		txt_search.setText("");
		btn_submit.setText("Create");
		btn_submit.setEnabled(false);
		btn_cancel.setText("Cancel");
	}
	public void openToCreate() {
		open();
	}
	public void openToUpdate(Order order) {
		open();
		
		this.order = order;
		isCreating = false;
		
		cmb_table.setSelectedIndex(order.getTableNo()-1);
		tbl_menu.setEnabled(true);
		tbl_order.setEnabled(true);
		mdl_order.setItems(order.getItems());
		updatePrices();
		
		btn_submit.setText("Update");
		btn_cancel.setText("Back");
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
	//Functionalities
	private void createOrder(int tableNo) {
		order = new Order();
		order.setTableNo(tableNo);
		order.setId(orderCtrl.createOrder(tableNo));
	}
	private void updateOrder(Order order) {
		orderCtrl.updateOrder(order);
	}
	private void cancelOrder(Order order) {
		orderCtrl.cancelOrder(order);
	}
	private void addOrderMenuItem(OrderMenuItem orderItem) {
		if (orderCtrl.hasOrderMenuItem(orderItem)) return;
		
		boolean forced = false;
		if (!orderCtrl.canAddOrderMenuItem(orderItem)) {
			if (JOptionPane.showConfirmDialog(this, 
					"There are not enough ingredients for this Menu item in the inventory!\nDo you still want to add it to the Order?", 
					"Adding Menu Item", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			forced = true;
		}
		
		if (!orderCtrl.addOrderMenuItem(orderItem, forced)) {
			JOptionPane.showMessageDialog(this,
				    "An error occured while adding the Menu Item to the Order!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		mdl_order.addItem(orderItem);
	}
	private void editOrderMenuItem(OrderMenuItem orderItem, int newQuantity) {
		if (!orderCtrl.hasOrderMenuItem(orderItem)) return;
		int prevQuantity = orderItem.getQuantity();
		
		boolean forced = false;
		if (newQuantity > prevQuantity) {
			orderItem.setQuantity(newQuantity - prevQuantity);
			if (!orderCtrl.canAddOrderMenuItem(orderItem)) {
				if (JOptionPane.showConfirmDialog(EditOrder.this, 
						"There are not enough ingredients for this Menu orderItem in the inventory!\nDo you still want to update the newQuantity?", 
						"Update Menu orderItem", JOptionPane.YES_NO_OPTION)
						!= JOptionPane.YES_OPTION) {
					orderItem.setQuantity(prevQuantity);
					return;
				}
				forced = true;
			}
		}
		orderItem.setQuantity(newQuantity);
			
		if (!orderCtrl.editOrderMenuItem(orderItem, forced)) {
			JOptionPane.showMessageDialog(EditOrder.this,
				    "The Menu orderItem was not updated!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			orderItem.setQuantity(prevQuantity);
			return;
		}

		updatePrices();
	}
	private void removeOrderMenuItem(OrderMenuItem orderItem) {
		if (!orderCtrl.removeOrderMenuItem(orderItem)) {
			JOptionPane.showMessageDialog(this,
				    "The Menu item was not removed!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		mdl_order.removeItem(orderItem);
	}
	private void submit() {
		String message = (isCreating ? "Creating" : "Updating") + " an Order";
		String title = "Are you sure?";
		int messageType = JOptionPane.YES_NO_OPTION;

		if (JOptionPane.showConfirmDialog(this, message, title, messageType) != JOptionPane.YES_OPTION) {
			return;
		}
		
		message = "The Order was successfully " + (isCreating ? "creating" : "updating") + "!";
		title = "Success!";
		messageType = JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(this, message, title, messageType);

		close();
		triggerPerformListeners();
	}
	private void cancel() {
		if (isCreating && order != null) {
			cancelOrder(order);
		}
		
		close();
		triggerCancelListeners();
	}
	private void searchMenuItems() {
		if (fetchingData) return;
		
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) return;
		
		fetchingData = true;
		lastKeyword = keyword;
		
		new FetchWorker(keyword).execute();
	}
	private boolean isFilled() {
		if (cmb_table.getSelectedIndex() < 0)
			return false;
		if (mdl_order.getItems().isEmpty())
			return false;
		
		return true;
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_search) {
			searchMenuItems();
		} else if (source == btn_add) {
			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			MenuItem item = mdl_menu.getItem(modelRowIndex);
			OrderMenuItem orderItem = new OrderMenuItem();
			orderItem.setMenuItem(item);
			orderItem.setOrder(order);
			orderItem.setQuantity(1);
			
			addOrderMenuItem(orderItem);
		} else if (source == btn_remove) {
			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			OrderMenuItem orderItem = mdl_order.getItem(modelRowIndex);
			
			removeOrderMenuItem(orderItem);
		} else if (source == btn_submit) {
			submit();
		} else if (source == btn_cancel) {
			cancel();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cmb_table) {
			int tableNo = cmb_table.getSelectedIndex() + 1;
			if (tableNo < 1) return;
			
			if (isCreating) {
				if (order == null) {
					createOrder(tableNo);
					
					tbl_menu.setEnabled(true);
					tbl_order.setEnabled(true);
				}
			} else {
				order.setTableNo(tableNo);
				
				updateOrder(order);
			}
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchMenuItems();
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == mdl_order) {
			btn_submit.setEnabled(isFilled());
			updatePrices();
		}
	}
	//Classes
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
				if (quantity <= 0) return;
				OrderMenuItem item = getItem(rowIndex);
				
				editOrderMenuItem(item, quantity);
				
				update();
			}
		}
	}
	private class FetchWorker extends SwingWorker<ArrayList<MenuItem>, Void> {
		
		private String keyword;

		public FetchWorker() {
			this("");
		}
		public FetchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<MenuItem> doInBackground() throws Exception {
			return keyword.isEmpty()
					? itemCtrl.getMenuItems()
					: itemCtrl.searchMenuItems(keyword);
		}
		@Override
		protected void done() {
			try {
				mdl_menu.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchMenuItems();
		}
	}
}
