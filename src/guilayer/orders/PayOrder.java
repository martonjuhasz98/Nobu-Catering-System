package guilayer.orders;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;

import ctrllayer.OrderController;
import guilayer.ManagerWindow;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.PerformPanel;
import modlayer.Order;
import modlayer.OrderMenuItem;
import modlayer.TransactionType;
import modlayer.MenuItem;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Label;
import javax.swing.JComboBox;

public class PayOrder extends PerformPanel implements ActionListener, ItemListener {

	private OrderController orderCtrl;
	private Order order;
	private JTable table;
	private OrderTableModel model;
	private JButton btn_pay;
	private JButton btn_cancel;
	private JFormattedTextField txt_subtotal;
	private JFormattedTextField txt_tax;
	private JFormattedTextField txt_total;
	private JComboBox<TransactionType> cmb_payment;
	
	public PayOrder() {
		orderCtrl = new OrderController();
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);
		
		model = new OrderTableModel();
		
		Label lbl_items = new Label("Items");
		lbl_items.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_items.setBounds(10, 11, 129, 22);
		add(lbl_items);
		

		NumberFormat format = NumberFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(0);

		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setMinimum(0.0);
		formatter.setMaximum(10000000.0);
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		
		Label lbl_subtotal = new Label("Subtotal");
		lbl_subtotal.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_subtotal.setBounds(10, 40, 97, 22);
		add(lbl_subtotal);
		
		txt_subtotal = new JFormattedTextField(formatter);
		txt_subtotal.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_subtotal.setEditable(false);
		txt_subtotal.setBounds(240, 40, 179, 22);
		add(txt_subtotal);
		
		Label lbl_tax = new Label("Tax");
		lbl_tax.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_tax.setBounds(10, 64, 97, 22);
		add(lbl_tax);
		
		txt_tax = new JFormattedTextField(formatter);
		txt_tax.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_tax.setEditable(false);
		txt_tax.setBounds(240, 64, 179, 22);
		add(txt_tax);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 92, 409, 2);
		add(separator);
		
		Label lbl_total = new Label("Total");
		lbl_total.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_total.setBounds(10, 100, 97, 22);
		add(lbl_total);
		
		txt_total = new JFormattedTextField(formatter);
		txt_total.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_total.setEditable(false);
		txt_total.setBounds(240, 97, 179, 22);
		add(txt_total);
		
		JScrollPane scrlPane = new JScrollPane();
		scrlPane.setBounds(10, 133, 409, 318);
		add(scrlPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrlPane.setViewportView(table);
		
		Label lbl_payment = new Label("Payment");
		lbl_payment.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_payment.setBounds(491, 11, 129, 22);
		add(lbl_payment);
		
		cmb_payment = new JComboBox<TransactionType>(TransactionType.values());
		cmb_payment.setBounds(491, 42, 217, 20);
		add(cmb_payment);
		
		btn_pay = new JButton("Pay");
		btn_pay.setBounds(635, 428, 73, 23);
		btn_pay.setEnabled(true);
		add(btn_pay);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setEnabled(true);
		btn_cancel.setBounds(717, 428, 73, 23);
		add(btn_cancel);
		
		cmb_payment.addItemListener(this);
		btn_pay.addActionListener(this);
		btn_cancel.addActionListener(this);
		
		reset();
	}
	private void reset() {
		model.setItems(new ArrayList<OrderMenuItem>());
		cmb_payment.setSelectedIndex(-1);
		txt_subtotal.setValue(new Double(0.0));
		txt_tax.setValue(new Double(0.0));
		txt_total.setValue(new Double(0.0));
		
		btn_pay.setEnabled(false);
	}
	public void pay(Order order) {
		reset();
		
		this.order = order;
		model.setItems(order.getItems());
		updatePrices();
		
		setVisible(true);
	}
	private void cancel() {
		triggerCancelListeners();
		close();
	}
	private void close() {
		setVisible(false);
	}
	private boolean isFilled() {
		if (cmb_payment.getSelectedIndex() < 0)
			return false;
		
		return true;
	}
	private void payOrder() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Paying order", JOptionPane.YES_NO_OPTION)
				!= JOptionPane.YES_OPTION) {
			return;
		}
		
		TransactionType payment = (TransactionType)cmb_payment.getSelectedItem();
		if (!orderCtrl.payOrder(payment, order)) {
			JOptionPane.showMessageDialog(this,
				    "An error occured while paying the Order!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(this,
				"The Order was successfully payed!",
			    "Success!",
			    JOptionPane.INFORMATION_MESSAGE);
		
		triggerPerformListeners();
		close();
	}
	private void updatePrices() {
		double subtotal = 0, tax = 0, total = 0;
		for (OrderMenuItem item : model.getItems()) {
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
		if (e.getSource() == btn_pay) {
			payOrder();
		} else if (e.getSource() == btn_cancel) {
			cancel();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cmb_payment) {
			btn_pay.setEnabled(isFilled());
		}
	}
	
	private class OrderTableModel extends ItemTableModel<OrderMenuItem> {

		public OrderTableModel() {
			super();
			
			columns = new String[] { "Item No.", "Name", "Price", "Quantity", "Total"};
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
			}
			
			return null;
		}
	}
}
