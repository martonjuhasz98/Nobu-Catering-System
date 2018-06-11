package guilayer.orders;


import ctrllayer.OrderController;
import guilayer.WaiterWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import modlayer.Order;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListOrders extends NavigationPanel
		implements ActionListener, MouseListener, PerformListener, CaretListener {

	private EditOrder editOrder;
	private PayOrder payOrder;
	private OrderController orderCtrl;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable table;
	private OrderTableModel model;
	private ButtonColumn btn_pay;
	private ButtonColumn btn_cancel;
	private JButton btn_create;
	private boolean fetchingData;
	private String lastKeyword;

	public ListOrders(PayOrder payOrder, EditOrder editOrder) {
		super();
		
		this.payOrder = payOrder;
		this.editOrder = editOrder;
		
		orderCtrl = new OrderController();
		
		payOrder.addPerformListener(this);
		editOrder.addPerformListener(this);

		initialize();
	}
	//Layout
	private void initialize() {

		setBounds(0, 0, WaiterWindow.contentWidth, WaiterWindow.totalHeight);

		model = new OrderTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 11, 142, 20);
		add(txt_search);

		btn_search = new JButton("Search");
		btn_search.setBounds(162, 10, 65, 23);
		add(btn_search);

		btn_create = new JButton("Create");
		btn_create.setBounds(725, 10, 65, 23);
		add(btn_create);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 780, 417);
		add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrollPane.setViewportView(table);

		btn_pay = new ButtonColumn(table, model.getColumnCount() - 2, this);
		btn_pay.setMnemonic(KeyEvent.VK_ACCEPT);
		btn_cancel = new ButtonColumn(table, model.getColumnCount() - 1, this);
		btn_cancel.setMnemonic(KeyEvent.VK_CANCEL);

		reset();
		
		payOrder.addPerformListener(this);
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);
	}
	@Override
	public void prepare() {
		new FetchWorker().execute();
	}
	@Override
	public void reset() {
		txt_search.setText("");
		fetchingData = false;
		lastKeyword = "";
	}
	@Override
	public void performed() {
		setVisible(true);
		prepare();
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
	//Functionalities
	private void createOrder() {
		editOrder.openToCreate();
		setVisible(false);
	}
	private void editOrder(Order order) {
		editOrder.openToUpdate(order);
		setVisible(false);
	}
	private void payOrder(Order order) {
		payOrder.openToPay(order);
		setVisible(false);
	}
	private void cancelOrder(Order order) {
		String message, title;
		int messageType;
		
		if (!orderCtrl.cancelOrder(order)) {
			message = "An error occured while cancelling the Order!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Order was successfully cancelled!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			prepare();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void searchOrders() {
		if (fetchingData) return;
		
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) return;
		
		fetchingData = true;
		lastKeyword = keyword;
		
		new FetchWorker(keyword).execute();
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_search) {
			searchOrders();
		} else if (source == btn_pay) {
			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			Order order = model.getItem(modelRowIndex);

			payOrder(order);
		} else if (source == btn_cancel) {
			if (JOptionPane.showConfirmDialog(ListOrders.this, "Are you sure?", "Cancelling order",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				return;
			}

			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			Order order = model.getItem(modelRowIndex);

			cancelOrder(order);
		} else if (source == btn_create) {
			createOrder();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchOrders();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Order order = model.getItem(modelRowIndex);

			editOrder(order);
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	//Classes
	private class OrderTableModel extends ItemTableModel<Order> {

		public OrderTableModel() {
			super();

			columns = new String[] { "Id", "Table No.", "Menu items", "", "" };
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Order order = items.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return order.getId();
			case 1:
				return order.getTableNo();
			case 2:
				return order.getItems().size();
			case 3:
				return "Pay";
			case 4:
				return "Cancel";
			}

			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex >= getColumnCount() - 2;
		}
	}
	private class FetchWorker extends SwingWorker<ArrayList<Order>, Void> {
		
		private String keyword;

		public FetchWorker() {
			this("");
		}
		public FetchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Order> doInBackground() throws Exception {
			return keyword.isEmpty()
					? orderCtrl.getUnpaidOrders()
					: orderCtrl.searchUnpaidOrders(keyword);
		}
		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchOrders();
		}
	}
}
