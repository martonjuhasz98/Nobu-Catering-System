package guilayer.invoices;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.InvoiceController;
import guilayer.ManagerWindow;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.PerformPanel;
import modlayer.Invoice;
import modlayer.InvoiceItem;
import modlayer.Item;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.Label;

public class ConfirmInvoice extends PerformPanel implements ActionListener, CaretListener, ListSelectionListener {

	private InvoiceController invoiceCtrl;
	private Invoice invoice;
	private JTextField txt_supplier;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_ordered;
	private OrderedTableModel mdl_ordered;
	private JButton btn_add;
	private JButton btn_remove;
	private JTable tbl_delivered;
	private DeliveredTableModel mdl_delivered;
	private JButton btn_confirm;
	private JButton btn_cancel;
	
	public ConfirmInvoice() {
		super();
		
		invoiceCtrl = new InvoiceController();
		
		initialize();
	}
	//Layout
	private void initialize() {
		
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);
		
		mdl_ordered = new OrderedTableModel();
		mdl_delivered = new DeliveredTableModel();
		
		Label lbl_supplier = new Label("Supplier");
		lbl_supplier.setBounds(10, 11, 129, 22);
		add(lbl_supplier);
		
		txt_supplier = new JTextField();
		txt_supplier.setEditable(false);
		txt_supplier.setBounds(10, 39, 341, 20);
		add(txt_supplier);
		
		Label lbl_items = new Label("Items");
		lbl_items.setBounds(10, 80, 129, 22);
		add(lbl_items);
		
		txt_search = new JTextField();
		txt_search.setBounds(10, 110, 217, 20);
		add(txt_search);
		
		btn_search = new JButton("Search");
		btn_search.setBounds(237, 110, 73, 20);
		add(btn_search);
		
		JScrollPane scrlPane_ordered = new JScrollPane();
		scrlPane_ordered.setBounds(10, 141, 300, 310);
		add(scrlPane_ordered);
		
		tbl_ordered = new JTable();
		tbl_ordered.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_ordered.getTableHeader().setReorderingAllowed(false);
		tbl_ordered.setAutoCreateRowSorter(true);
		tbl_ordered.setModel(mdl_ordered);
		scrlPane_ordered.setViewportView(tbl_ordered);
		
		btn_add = new JButton("Add");
		btn_add.setBounds(316, 272, 73, 23);
		add(btn_add);
		
		btn_remove = new JButton("Remove");
		btn_remove.setBounds(316, 306, 73, 23);
		add(btn_remove);
		
		JScrollPane scrlPane_delivered = new JScrollPane();
		scrlPane_delivered.setBounds(399, 141, 391, 310);
		add(scrlPane_delivered);
		
		tbl_delivered = new JTable();
		tbl_delivered.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_delivered.getTableHeader().setReorderingAllowed(false);
		tbl_delivered.setAutoCreateRowSorter(true);
		tbl_delivered.setModel(mdl_delivered);
		scrlPane_delivered.setViewportView(tbl_delivered);
		
		btn_confirm = new JButton("Confirm");
		btn_confirm.setBounds(635, 11, 73, 23);
		btn_confirm.setEnabled(true);
		add(btn_confirm);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setEnabled(true);
		btn_cancel.setBounds(717, 11, 73, 23);
		add(btn_cancel);
		
		reset();
		
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		tbl_ordered.getSelectionModel().addListSelectionListener(this);
		tbl_delivered.getSelectionModel().addListSelectionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		btn_confirm.addActionListener(this);
		btn_cancel.addActionListener(this);
	}
	@Override
	public void prepare() {}
	@Override
	public void reset() {
		txt_supplier.setText("");
		txt_search.setText("");

		mdl_delivered.setItems(new ArrayList<InvoiceItem>());
		
		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
	}
	public void openToConfirm(Invoice invoice) {
		open();
		
		this.invoice = invoice;
		txt_supplier.setText(invoice.getSupplier().toString());
		mdl_ordered.setItems(invoice.getItems());
	}
	//Functionalities
	private void confirmInvoice() {
		String message, title;
		int messageType;
		
		if (!invoiceCtrl.confirmInvoice(invoice)) {
			message = "An error occured while confirming the Invoice!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Invoice was successfully confirmed!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			triggerPerformListeners();
			close();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void cancel() {
		triggerCancelListeners();
		close();
	}
	private void addToDelivered() {
		int[] selection = tbl_ordered.getSelectedRows();
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_ordered.convertRowIndexToModel(selection[i]);
			mdl_delivered.addItem(mdl_ordered.getItem(selection[i]));
		}
	}
	private void removeFromDelivered() {
		int[] selection = tbl_delivered.getSelectedRows();
		
		for (int i = selection.length; i >= 0; i--) {
			selection[i] = tbl_delivered.convertRowIndexToModel(selection[i]);
			mdl_delivered.removeItem(mdl_ordered.getItem(selection[i]));
		}
	}
	private void searchOrderedItems() {
		String keyword = txt_search.getText().trim();
		ArrayList<InvoiceItem> results = new ArrayList<InvoiceItem>();
		for (InvoiceItem invoiceItem : invoice.getItems()) {
			Item item = invoiceItem.getItem(); 
			if (item.getBarcode().contains(keyword) ||
					item.getName().contains(keyword)) {
				results.add(invoiceItem);
			}
		}
		mdl_ordered.setItems(results);
	}
	//EventListener
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_search) {
			searchOrderedItems();
		} else if (source == btn_add) {
			addToDelivered();
		} else if (source == btn_remove) {
			removeFromDelivered();
		} else if (source == btn_confirm) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirming invoice", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			invoice.setItems(mdl_delivered.getItems());
			
			confirmInvoice();
		} else if (source == btn_cancel) {
			cancel();
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		final ListSelectionModel source = (ListSelectionModel)e.getSource();
		boolean empty = source.isSelectionEmpty();
		
		if (source == tbl_ordered.getSelectionModel()) {
			btn_add.setEnabled(!empty);
		} else if (source == tbl_delivered.getSelectionModel()) {
			btn_remove.setEnabled(!empty);
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchOrderedItems();
		}
	}
	
	private class OrderedTableModel extends ItemTableModel<InvoiceItem> {

		public OrderedTableModel() {
			super();
			
			columns = new String[] { "Barcode", "Name", "Quantity", "Unit", "Unit price", "Category" };
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			InvoiceItem invoiceItem = items.get(rowIndex);
			Item item = invoiceItem.getItem();
			
			switch(columnIndex) {
				case 0:
					return item.getBarcode();
				case 1:
					return item.getName();
				case 2:
					return invoiceItem.getQuantity();
				case 3:
					return item.getUnit().getAbbr();
				case 4:
					return invoiceItem.getUnitPrice();
				case 5:
					return item.getCategory().getName();
			}
			
			return null;
		}
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case 1:
					double quantity = (double)value;
					if (quantity > 0) {
						getItem(rowIndex).setQuantity(quantity);
					}
					break;
				case 3:
					double price = (double)value;
					if (price > 0) {
						getItem(rowIndex).setUnitPrice(price);
					}
					break;
			}
		}
	}
	private class DeliveredTableModel extends OrderedTableModel {
		
		public DeliveredTableModel() {
			super();
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 2;
		}
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			try {
				double quantity = (double)value;
				if (quantity >= 0) {
					super.items.get(rowIndex).setQuantity(quantity);
				}
			} catch(Exception e) {}
		}
	}
}
