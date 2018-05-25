package guilayer.invoices;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ctrllayer.InvoiceController;
import ctrllayer.ItemController;
import ctrllayer.SupplierController;
import guilayer.ManagerWindow;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformPanel;
import modlayer.Employee;
import modlayer.Invoice;
import modlayer.InvoiceItem;
import modlayer.Item;
import modlayer.Supplier;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class CreateInvoice extends PerformPanel implements ActionListener, CaretListener, ItemListener, ListSelectionListener, TableModelListener {

	private InvoiceController invoiceCtrl;
	private SupplierController supplierCtrl;
	private ItemController itemCtrl;
	private JComboBox<Supplier> cmb_supplier;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_inventory;
	private InventoryTableModel mdl_inventory;
	private JButton btn_add;
	private JButton btn_remove;
	private JTable tbl_invoiceItem;
	private InvoiceTableModel mdl_invoice;
	private JButton btn_create;
	private JButton btn_cancel;
	private boolean searching;
	
	public CreateInvoice() {
		invoiceCtrl = new InvoiceController();
		supplierCtrl = new SupplierController();
		itemCtrl = new ItemController();
		searching = false;
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);
		
		mdl_inventory = new InventoryTableModel();
		mdl_invoice = new InvoiceTableModel();
		
		Label lbl_supplier = new Label("Supplier *");
		lbl_supplier.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_supplier.setBounds(10, 10, 129, 22);
		add(lbl_supplier);
		
		cmb_supplier = new JComboBox<Supplier>();
		cmb_supplier.setLightWeightPopupEnabled(false);
		cmb_supplier.setBounds(10, 38, 376, 20);
		add(cmb_supplier);
		
		Label lbl_items = new Label("Items *");
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
		
		JScrollPane scrlPane_inventory = new JScrollPane();
		scrlPane_inventory.setBounds(10, 123, 316, 366);
		add(scrlPane_inventory);
		
		tbl_inventory = new JTable();
		tbl_inventory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_inventory.getTableHeader().setReorderingAllowed(false);
		tbl_inventory.setAutoCreateRowSorter(true);
		mdl_inventory.setItems(itemCtrl.getItems());
		tbl_inventory.setModel(mdl_inventory);
		scrlPane_inventory.setViewportView(tbl_inventory);
		
		btn_add = new JButton("Add");
		btn_add.setBounds(336, 244, 73, 23);
		add(btn_add);
		
		btn_remove = new JButton("Remove");
		btn_remove.setBounds(336, 278, 73, 23);
		add(btn_remove);
		
		JScrollPane scrlPane_invoiceItem = new JScrollPane();
		scrlPane_invoiceItem.setBounds(419, 123, 371, 366);
		add(scrlPane_invoiceItem);
		
		tbl_invoiceItem = new JTable();
		tbl_invoiceItem.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_invoiceItem.getTableHeader().setReorderingAllowed(false);
		tbl_invoiceItem.setAutoCreateRowSorter(true);
		mdl_invoice.setItems(new ArrayList<InvoiceItem>());
		tbl_invoiceItem.setModel(mdl_invoice);
		scrlPane_invoiceItem.setViewportView(tbl_invoiceItem);
		
		btn_create = new JButton("Create");
		btn_create.setBounds(536, 10, 122, 32);
		add(btn_create);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 10, 122, 32);
		add(btn_cancel);
		
		reset();
		
		cmb_supplier.addItemListener(this);
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		btn_create.addActionListener(this);
		btn_cancel.addActionListener(this);
		tbl_inventory.getSelectionModel().addListSelectionListener(this);
		tbl_invoiceItem.getSelectionModel().addListSelectionListener(this);
		mdl_invoice.addTableModelListener(this);
	}
	private void reset() {
		cmb_supplier.setModel(new DefaultComboBoxModel(supplierCtrl.getSuppliers().toArray()));
		cmb_supplier.setSelectedIndex(-1);
		mdl_inventory.setItems(itemCtrl.getItems());
		mdl_invoice.setItems(new ArrayList<InvoiceItem>());
		
		txt_search.setText("");
		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
		btn_create.setEnabled(false);
	}
	public void create() {
		setVisible(true);
	}
	private void close() {
		setVisible(false);
		reset();
	}
	private void createInvoice() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure?") != JOptionPane.YES_OPTION) {
        	return;
        }
		
		//Supplier
		Supplier supplier = (Supplier)cmb_supplier.getSelectedItem();
		
		//Employee
		Employee employee = ((ManagerWindow)SwingUtilities.getWindowAncestor(this)).getUser();
		
		//Items
		ArrayList<InvoiceItem> items = mdl_invoice.getItems();
		
		if (!invoiceCtrl.createInvoice(supplier, employee, items)) {
			JOptionPane.showMessageDialog(this,
				    "An error occured while creating the Invoice!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(this,
			    "The Invoice was successfully created!",
			    "Success!",
			    JOptionPane.INFORMATION_MESSAGE);
		
		triggerPerformListeners();
		close();
	}
	private void cancel() {
		triggerCancelListeners();
		close();
	}
	private boolean isFilled() {
		if (cmb_supplier.getSelectedIndex() < 0)
			return false;
		if (mdl_invoice.getItems().isEmpty())
			return false;
		
		return true;
	}
	private void search() {
		if (searching) return;
		searching = true;
		
		String keyword = txt_search.getText().trim();
		mdl_inventory.setItems(itemCtrl.searchItems(keyword));
		
		searching = false;
	}
	private void addToInvoice() {
		int[] selection = tbl_inventory.getSelectedRows();
		InvoiceItem invoiceItem;
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_inventory.convertRowIndexToModel(selection[i]);
			invoiceItem = new InvoiceItem();
			invoiceItem.setInvoice(new Invoice());
			invoiceItem.setItem((Item)mdl_inventory.getItem(selection[i]));
			invoiceItem.setQuantity(1.0);
			invoiceItem.setUnitPrice(1.0);
			mdl_invoice.removeItem(invoiceItem);
		}
	}
	private void removeFromInvoice() {
		int[] selection = tbl_invoiceItem.getSelectedRows();
		
		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_invoiceItem.convertRowIndexToModel(selection[i]);
			mdl_invoice.removeItem(mdl_invoice.getItem(selection[i]));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			search();
		} else if (e.getSource() == btn_add) {
			addToInvoice();
		} else if (e.getSource() == btn_remove) {
			removeFromInvoice();
		} else if (e.getSource() == btn_create) {
			createInvoice();
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
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel source = (ListSelectionModel)e.getSource();
		boolean empty = source.isSelectionEmpty();
		
		if (source == tbl_inventory.getSelectionModel()) {
			btn_add.setEnabled(!empty);
		} else if (source == tbl_invoiceItem.getSelectionModel()) {
			btn_remove.setEnabled(!empty);
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cmb_supplier) {
			btn_create.setEnabled(isFilled());
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == mdl_invoice) {
			btn_create.setEnabled(isFilled());
		}
	}
	private class InventoryTableModel extends ItemTableModel<Item> {

		public InventoryTableModel() {
			super();
			
			columns = new String[] { "Name", "Quantity", "Unit", "Category"};
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Item item = getItem(rowIndex);
			
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
	}
	private class InvoiceTableModel extends ItemTableModel<InvoiceItem> {

		public InvoiceTableModel() {
			super();
			
			columns = new String[] { "Name", "Quantity", "Unit", "Unit price", "Category"};
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			InvoiceItem invoiceItem = getItem(rowIndex);
			Item item = invoiceItem.getItem();
			
			switch(columnIndex) {
				case 0:
					return item.getName();
				case 1:
					return invoiceItem.getQuantity();
				case 2:
					return item.getUnit().getAbbr();
				case 3:
					return invoiceItem.getUnitPrice();
				case 4:
					return item.getCategory().getName();
			}
			
			return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case 1:
				case 3:
					return true;
				default:
					return false;
			}
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
}
