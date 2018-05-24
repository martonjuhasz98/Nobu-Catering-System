package guilayer.invoices;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import ctrllayer.InvoiceController;
import ctrllayer.ItemController;
import guilayer.MainWindow;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformPanel;
import modlayer.Invoice;
import modlayer.InvoiceItem;
import modlayer.Item;
import modlayer.Supplier;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class CreateInvoice extends PerformPanel implements ActionListener{

	private InvoiceController invCtrl;
	private ItemController itemCtrl;
	private JComboBox<Supplier> cmb_supplier;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_inventory;
	private ItemTableModel<Item> mdl_inventory;
	private JButton btn_add;
	private JButton btn_remove;
	private JTable tbl_invoiceItem;
	private InvoiceTableModel<InvoiceItem> mdl_invoiceItem;
	private JButton btn_create;
	private JButton btn_cancel;
	private boolean searching;
	
	public CreateInvoice() {
		invCtrl = new InvoiceController();
		itemCtrl = new ItemController();
		searching = false;
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setVisible(false);
		setBounds(0, 0, MainWindow.contentWidth, MainWindow.totalHeight);
		
		mdl_inventory = new InventoryTableModel();
		mdl_invoiceItem = new InvoiceTableModel();
		
		Label lbl_supplier = new Label("Supplier *");
		lbl_supplier.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_supplier.setBounds(10, 10, 129, 22);
		add(lbl_supplier);
		
		cmb_supplier = new JComboBox();
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 123, 580, 378);
		add(scrollPane);
		
		tbl_inventory = new JTable();
		tbl_inventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbl_inventory.getTableHeader().setReorderingAllowed(false);
		tbl_inventory.setAutoCreateRowSorter(true);
		mdl_inventory.setItems(itemCtrl.getItems());
		tbl_inventory.setModel(mdl_inventory);
		scrollPane.setViewportView(tbl_inventory);
		
		btn_create = new JButton("Create");
		btn_create.setBounds(536, 457, 122, 32);
		add(btn_create);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 457, 122, 32);
		add(btn_cancel);
		
		resetForm();
		
		btn_cancel.addActionListener(this);
	}
	private void resetForm() {
	}
	
	public void create() {
		setVisible(true);
	}

	private void close() {
		setVisible(false);
		resetForm();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_cancel) {
			//TODO createInvoice
			
			triggerPerformListeners();
			close();
		} else if (e.getSource() == btn_cancel) {
			triggerCancelListeners();
			close();
		}
	}
	
	private class InventoryTableModel<T extends Item> extends ItemTableModel<T> {

		public InventoryTableModel() {
			this(new ArrayList<Item>());
		}
		public InventoryTableModel(ArrayList<Item> items) {
			super((ArrayList<T>)items);
			
			this.columns = new String[] { "Name", "Quantity", "Unit", "Category"};
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Item item = getItemAt(rowIndex);
			
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
	private class InvoiceTableModel<T extends InvoiceItem> extends ItemTableModel<T> {

		public InvoiceTableModel() {
			this(new ArrayList<InvoiceItem>());
		}
		public InvoiceTableModel(ArrayList<InvoiceItem> items) {
			super((ArrayList<T>)items);
			
			this.columns = new String[] { "Name", "Quantity", "Unit", "Unit price", "Category"};
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			InvoiceItem invoiceItem = getItemAt(rowIndex);
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
		
		public void addItems(ArrayList<Item> items) {
			InvoiceItem invoiceItem;
			for (Item item : items) {
				invoiceItem = new InvoiceItem();
				invoiceItem.setInvoice(new Invoice());
				invoiceItem.setItem(item);
				invoiceItem.setQuantity(0);
				invoiceItem.setUnitPrice(0.0);
				
				if (this.items.indexOf((T)invoiceItem) < 0)
					this.items.add((T)invoiceItem);
			}
			update();
		}
		public void removeItems(ArrayList<Item> items) {
			InvoiceItem invoiceItem;
			for (Item item : items) {
				invoiceItem = new InvoiceItem();
				invoiceItem.setInvoice(new Invoice());
				invoiceItem.setItem(item);
				invoiceItem.setQuantity(0);
				invoiceItem.setUnitPrice(0.0);
				
				this.items.remove((T)invoiceItem);
			}
			update();
		}
	}
}
