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
import javax.swing.event.ListDataListener;
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
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JSeparator;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;

public class EditOrder extends PerformPanel implements ActionListener, CaretListener, TableModelListener, ItemListener {

	private OrderController orderCtrl;
	private MenuItemController itemCtrl;
	private Order order;
	private boolean isCreating;
	private JComboBox<String> cmb_table;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_menu;
	private InventoryTableModel mdl_menu;
	private JTable tbl_order;
	private OrderTableModel mdl_order;
	private boolean isSearching;
	private String lastKeyword;
	private JFormattedTextField txt_subtotal;
	private JFormattedTextField txt_tax;
	private JFormattedTextField txt_total;
	private JButton btn_submit;
	private JButton btn_back;
	private boolean reseting;
	
	public EditOrder() {
		itemCtrl = new MenuItemController();
		orderCtrl = new OrderController();
		order = null;
		isCreating = true;
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
		
		String[] tables = new String[15];
		for (int i = 0; i < tables.length; i++) {
			tables[i] = String.format("Table No. %d", i+1); 
		}
		cmb_table = new JComboBox<String>(tables);
		cmb_table.setSelectedIndex(-1);
		cmb_table.setBounds(10, 38, 179, 20);
		add(cmb_table);
		
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
		tbl_order.setModel(mdl_order);
		scrlPane_order.setViewportView(tbl_order);
		
		btn_submit = new JButton("Create");
		btn_submit.setBounds(536, 427, 122, 32);
		add(btn_submit);
		
		btn_back = new JButton("Back");
		btn_back.setBounds(668, 427, 122, 32);
		add(btn_back);
		
		AbstractAction add = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				MenuItem item = mdl_menu.getItem(modelRowIndex);
				OrderMenuItem orderItem = new OrderMenuItem();
				orderItem.setMenuItem(item);
				orderItem.setOrder(order);
				orderItem.setQuantity(1);
				
				addOrderMenuItem(orderItem);
			}
		};
		AbstractAction remove = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				OrderMenuItem orderItem = mdl_order.getItem(modelRowIndex);
				
				removeOrderMenuItem(orderItem);
			}
		};

		ButtonColumn addColumn = new ButtonColumn(tbl_menu, mdl_menu.getColumnCount() - 1, this);
		addColumn.setMnemonic(KeyEvent.VK_ACCEPT);
		ButtonColumn removeColumn = new ButtonColumn(tbl_order, mdl_order.getColumnCount() - 1, this);
		removeColumn.setMnemonic(KeyEvent.VK_CANCEL);
		
		cmb_table.addItemListener(this);
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_submit.addActionListener(this);
		btn_back.addActionListener(this);
		mdl_order.addTableModelListener(this);
	}
	@Override
	public void prepare() {
		
	}
	@Override
	public void reset() {
		reseting = true;
		order = null;
		isCreating = true;
		
		cmb_table.removeItemListener(this);
		cmb_table.setSelectedIndex(-1);
		cmb_table.addItemListener(this);
		tbl_menu.setEnabled(false);
		tbl_order.setEnabled(false);
		mdl_menu.setItems(itemCtrl.getMenuItems());
		mdl_order.setItems(new ArrayList<OrderMenuItem>());
		updatePrices();
		
		txt_search.setText("");
		btn_submit.setText("Create");
		btn_submit.setEnabled(false);
		reseting = false;
	}
	private void fill(Order order) {
		this.order = order;
		isCreating = false;
		
		cmb_table.setSelectedIndex(order.getTableNo()-1);
		tbl_menu.setEnabled(true);
		tbl_order.setEnabled(true);
		mdl_order.setItems(order.getItems());
		updatePrices();
		
		btn_submit.setText("Update");
	}
	public void create() {
		open();
	}
	public void update(Order order) {
		open();
		fill(order);
	}
	private void createOrder() {
		order = new Order();
		order.setId(orderCtrl.createOrder(0));
	}
	private void updateOrder() {
		orderCtrl.updateOrder(order);
	}
	private void submitOrder() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure?", (isCreating ? "Creating" : "Updating") + " order", JOptionPane.YES_NO_OPTION)
				!= JOptionPane.YES_OPTION) {
			return;
		}
		
		JOptionPane.showMessageDialog(this,
			    "The Order was successfully " + (isCreating ? "creating" : "updating") + "!",
			    "Success!",
			    JOptionPane.INFORMATION_MESSAGE);

		close();
		triggerPerformListeners();
	}
	private void cancel() {
		if (order != null) {
			orderCtrl.cancelOrder(order);
		}
		
		close();
		triggerCancelListeners();
	}
	private boolean isFilled() {
		if (cmb_table.getSelectedIndex() < 0)
			return false;
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
	private void addOrderMenuItem(OrderMenuItem orderItem) {
		if (orderCtrl.hasOrderMenuItem(orderItem)) {
			return;
		}
		boolean forced = false;
		if (!orderCtrl.canAddOrderMenuItem(orderItem)) {
			if (JOptionPane.showConfirmDialog(this, 
					"There are not enough ingredients for this Menu item in the inventory!\nDo you still want to add it to the Order?", 
					"Add Menu item", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			forced = true;
		}
		
		if (!orderCtrl.addOrderMenuItem(orderItem, forced)) {
			JOptionPane.showMessageDialog(this,
				    "The Menu item was not added!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		mdl_order.addItem(orderItem);
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
		} else if (e.getSource() == btn_submit) {
			submitOrder();
		} else if (e.getSource() == btn_back) {
			cancel();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (reseting) return;
		if (e.getSource() == cmb_table) {
			if (isCreating && order == null) {
				createOrder();
			}
			order.setTableNo(cmb_table.getSelectedIndex()+1);
			updateOrder();

			tbl_menu.setEnabled(true);
			tbl_order.setEnabled(true);
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			search();
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		if (reseting) return;
		if (e.getSource() == mdl_order) {
			btn_submit.setEnabled(isFilled());
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
				OrderMenuItem item = getItem(rowIndex);
				int prevQuantity = item.getQuantity();
				int quantity = (int)value;
				if (quantity <= 0) return;
				
				boolean forced = false;
				if (quantity > prevQuantity) {
					item.setQuantity(quantity - prevQuantity);
					if (!orderCtrl.canAddOrderMenuItem(item)) {
						if (JOptionPane.showConfirmDialog(EditOrder.this, 
								"There are not enough ingredients for this Menu item in the inventory!\nDo you still want to update the quantity?", 
								"Update Menu item", JOptionPane.YES_NO_OPTION)
								!= JOptionPane.YES_OPTION) {
							item.setQuantity(prevQuantity);
							return;
						}
						forced = true;
					}
				}
				item.setQuantity(quantity);
					
				if (!orderCtrl.editOrderMenuItem(item, forced)) {
					JOptionPane.showMessageDialog(EditOrder.this,
						    "The Menu item was not updated!",
						    "Error!",
						    JOptionPane.ERROR_MESSAGE);
					item.setQuantity(prevQuantity);
					return;
				}
				
				update();
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
