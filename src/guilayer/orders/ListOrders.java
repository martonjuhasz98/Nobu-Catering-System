package guilayer.orders;

import javax.swing.JPanel;

import ctrllayer.OrderController;
import guilayer.ManagerWindow;
import guilayer.WaiterWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformListener;
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

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListOrders extends JPanel
		implements ActionListener, MouseListener, PerformListener, CaretListener {

	private OrderController orderCtrl;
	private EditOrder editOrder;
	private PayOrder payOrder;
	private JTextField txt_search;
	private JButton btn_search;
	private JButton btn_create;
	private JTable table;
	private OrderTableModel model;
	private boolean isSearching;
	private String lastKeyword;

	public ListOrders(PayOrder payOrder, EditOrder editOrder) {
		this.payOrder = payOrder;
		this.editOrder = editOrder;
		orderCtrl = new OrderController();
		lastKeyword = "";
		isSearching = false;
		
		payOrder.addPerformListener(this);
		editOrder.addPerformListener(this);

		initialize();
	}

	private void initialize() {
		setLayout(null);
		setBounds(0, 0, WaiterWindow.contentWidth, WaiterWindow.totalHeight);

		model = new OrderTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 11, 142, 20);
		txt_search.setColumns(10);
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

		AbstractAction pay = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				Order order = model.getItem(modelRowIndex);

				setVisible(false);
				payOrder.pay(order);
			}
		};
		AbstractAction cancel = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(ListOrders.this, "Are you sure?", "Canceling order",
						JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					return;
				}

				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				Order order = model.getItem(modelRowIndex);

				if (!orderCtrl.cancelOrder(order)) {
					JOptionPane.showMessageDialog(ListOrders.this,
							"An error occured while canceled the Order!", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(ListOrders.this, "The Order was successfully canceled!",
						"Success!", JOptionPane.INFORMATION_MESSAGE);
				reset();
			}
		};

		ButtonColumn payColumn = new ButtonColumn(table, pay, model.getColumnCount() - 2);
		payColumn.setMnemonic(KeyEvent.VK_ACCEPT);
		ButtonColumn cancelColumn = new ButtonColumn(table, cancel, model.getColumnCount() - 1);
		cancelColumn.setMnemonic(KeyEvent.VK_CANCEL);

		payOrder.addPerformListener(this);
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);

		reset();
	}
	private void reset() {
		model.setItems(orderCtrl.getUnpaidOrders());
		txt_search.setText("");
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			search();
		}
		if (e.getSource() == btn_create) {
			editOrder.create();
			setVisible(false);
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			search();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Order order = model.getItem(modelRowIndex);

			editOrder.update(order);
			setVisible(false);
		}
	}
	@Override
	public void performed() {
		model.setItems(orderCtrl.getUnpaidOrders());
		setVisible(true);
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

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

	public class SearchWorker extends SwingWorker<ArrayList<Order>, Void> {
		private String keyword;

		public SearchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Order> doInBackground() throws Exception {
			// Start
			return orderCtrl.searchUnpaidOrders(keyword);
		}

		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isSearching = false;
			search();
		}
	}
}
