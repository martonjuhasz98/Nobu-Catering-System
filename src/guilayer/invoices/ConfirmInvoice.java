package guilayer.invoices;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ctrllayer.InvoiceController;
import guilayer.interfaces.PerformPanel;
import modlayer.Invoice;
import modlayer.InvoiceItem;
import modlayer.Item;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class ConfirmInvoice extends PerformPanel implements ActionListener, CaretListener, ListSelectionListener, TableModelListener {

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
	
	public ConfirmInvoice() {
		invoiceCtrl = new InvoiceController();
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
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
		
		btn_confirm = new JButton("Confirm");
		btn_confirm.setBounds(718, 3, 73, 23);
		add(btn_confirm);
		
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
		
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_confirm.addActionListener(this);
		tbl_ordered.getSelectionModel().addListSelectionListener(this);
		tbl_delivered.getSelectionModel().addListSelectionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		mdl_delivered.addTableModelListener(this);
		
		resetForm();
	}
	private void resetForm() {
		mdl_ordered.setItems(new ArrayList<InvoiceItem>());
		mdl_delivered.setItems(new ArrayList<InvoiceItem>());
		
		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
		btn_confirm.setEnabled(false);
	}
	public void confirm(Invoice invoice) {
		resetForm();
		
		this.invoice = invoice;
		mdl_ordered.setItems(invoice.getItems());
		
		setVisible(true);
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
		ArrayList<InvoiceItem> items = new ArrayList<InvoiceItem>(selection.length);
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_ordered.convertRowIndexToModel(selection[i]);
			items.add(mdl_ordered.getItemAt(selection[i]));
		}
		
		mdl_delivered.addItems(items);
	}
	private void removeFromDelivered() {
		int[] selection = tbl_delivered.getSelectedRows();
		ArrayList<InvoiceItem> items = new ArrayList<InvoiceItem>(selection.length);
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_delivered.convertRowIndexToModel(selection[i]);
			items.add(mdl_delivered.getItemAt(selection[i]));
		}
		
		mdl_delivered.removeItems(items);
	}
	private void confirmInvoice() {
		invoice.setItems(mdl_delivered.getItems());
		
		if (!invoiceCtrl.confirmInvoice(invoice)) {
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
		triggerPerformListeners();
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
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == mdl_delivered) {
			boolean empty = mdl_delivered.getItems().isEmpty();
			btn_confirm.setEnabled(!empty);
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
		}
	}
	
	
	private class OrderedTableModel extends AbstractTableModel {
		
		private String[] columns = new String[] { "Barcode", "Name", "Quantity", "Unit", "Unit price", "Category" };
		protected ArrayList<InvoiceItem> items;
		
		public OrderedTableModel() {
			this(new ArrayList<InvoiceItem>());
		}
		public OrderedTableModel(ArrayList<InvoiceItem> items) {
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
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}
		@Override
		public Class getColumnClass(int columnIndex) {
			switch(columnIndex) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return int.class;
				case 3:
					return String.class;
				case 4:
					return double.class;
				case 5:
					return String.class;
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
		public InvoiceItem getItemAt(int rowIndex) {
			return items.get(rowIndex);
		}
		public void setItems(ArrayList<InvoiceItem> items) {
			this.items = items;
			update();
		}
		public void addItems(ArrayList<InvoiceItem> items) {
			for (InvoiceItem item : items) {
				if (this.items.indexOf(item) < 0)
					this.items.add(item);
			}
			update();
		}
		public void removeItems(ArrayList<InvoiceItem> items) {
			for (InvoiceItem item : items) {
				this.items.remove(item);
			}
			update();
		}
	}
	private class DeliveredTableModel extends OrderedTableModel {
		
		public DeliveredTableModel() {
			super();
		}
		public DeliveredTableModel(ArrayList<InvoiceItem> items) {
			super(items);
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1;
		}
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			try {
				int quantity = (int)value;
				super.items.get(rowIndex).setQuantity(quantity);
			} catch(Exception e) {}
		}
		public ArrayList<InvoiceItem> getItems() {
			return super.items;
		}
	}
}
