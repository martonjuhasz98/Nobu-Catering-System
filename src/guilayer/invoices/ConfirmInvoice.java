package guilayer.invoices;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.InvoiceController;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformPanel;
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

public class ConfirmInvoice extends PerformPanel implements ActionListener, CaretListener, ListSelectionListener {

	private InvoiceController invoiceCtrl;
	private Invoice invoice;
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
		invoiceCtrl = new InvoiceController();
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setVisible(false);
		setBounds(0, 0, 801, 500);
		
		mdl_ordered = new OrderedTableModel();
		mdl_delivered = new DeliveredTableModel();
		
		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		txt_search.setColumns(10);
		add(txt_search);
		
		btn_search = new JButton("Search");
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);
		
		JScrollPane scrlPane_ordered = new JScrollPane();
		scrlPane_ordered.setBounds(10, 28, 300, 461);
		add(scrlPane_ordered);
		
		tbl_ordered = new JTable();
		tbl_ordered.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_ordered.getTableHeader().setReorderingAllowed(false);
		tbl_ordered.setAutoCreateRowSorter(true);
		tbl_ordered.setModel(mdl_ordered);
		scrlPane_ordered.setViewportView(tbl_ordered);
		
		btn_add = new JButton("Add");
		btn_add.setBounds(320, 236, 73, 23);
		add(btn_add);
		
		btn_remove = new JButton("Remove");
		btn_remove.setBounds(320, 270, 73, 23);
		add(btn_remove);
		
		JScrollPane scrlPane_delivered = new JScrollPane();
		scrlPane_delivered.setBounds(400, 28, 391, 461);
		add(scrlPane_delivered);
		
		tbl_delivered = new JTable();
		tbl_delivered.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_delivered.getTableHeader().setReorderingAllowed(false);
		tbl_delivered.setAutoCreateRowSorter(true);
		tbl_delivered.setModel(mdl_delivered);
		scrlPane_delivered.setViewportView(tbl_delivered);
		
		btn_confirm = new JButton("Confirm");
		btn_confirm.setBounds(636, 3, 73, 23);
		btn_confirm.setEnabled(true);
		add(btn_confirm);
		btn_confirm.addActionListener(this);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setEnabled(true);
		btn_cancel.setBounds(718, 3, 73, 23);
		add(btn_cancel);
		
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		tbl_ordered.getSelectionModel().addListSelectionListener(this);
		tbl_delivered.getSelectionModel().addListSelectionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		btn_confirm.addActionListener(this);
		btn_cancel.addActionListener(this);
		
		resetForm();
	}
	private void resetForm() {
		mdl_ordered.setItems(new ArrayList<InvoiceItem>());
		mdl_delivered.setItems(new ArrayList<InvoiceItem>());
		
		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
	}
	public void confirm(Invoice invoice) {
		resetForm();
		
		this.invoice = invoice;
		mdl_ordered.setItems(invoice.getItems());
		
		setVisible(true);
	}
	private void close() {
		setVisible(false);
	}
	
	private void searchOrdered() {
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
	private void addToDelivered() {
		int[] selection = tbl_ordered.getSelectedRows();
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_ordered.convertRowIndexToModel(selection[i]);
			mdl_delivered.addItem(mdl_ordered.getItem(selection[i]));
		}
	}
	private void removeFromDelivered() {
		int[] selection = tbl_delivered.getSelectedRows();
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_delivered.convertRowIndexToModel(selection[i]);
			mdl_delivered.removeItem(mdl_ordered.getItem(selection[i]));
		}
	}
	private void confirmInvoice() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure?") != JOptionPane.YES_OPTION) {
        	return;
        }
		
		invoice.setItems(mdl_delivered.getItems());
		
		if (!invoiceCtrl.confirmInvoice(invoice)) {
			JOptionPane.showMessageDialog(this,
				    "An error occured while confirming the Invoice!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(this,
				"The Invoice was successfully confirmed!",
			    "Success!",
			    JOptionPane.INFORMATION_MESSAGE);
		
		triggerPerformListeners();
		close();
	}
	private void cancel() {
		triggerCancelListeners();
		close();
	}
	
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchOrdered();
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel source = (ListSelectionModel)e.getSource();
		boolean empty = source.isSelectionEmpty();
		
		if (source == tbl_ordered.getSelectionModel()) {
			btn_add.setEnabled(!empty);
		} else if (source == tbl_delivered.getSelectionModel()) {
			btn_remove.setEnabled(!empty);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			searchOrdered();
		} else if (e.getSource() == btn_add) {
			addToDelivered();
		} else if (e.getSource() == btn_remove) {
			removeFromDelivered();
		} else if (e.getSource() == btn_confirm) {
			confirmInvoice();
		} else if (e.getSource() == btn_cancel) {
			cancel();
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
