package guilayer.invoices;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JTextField;

import ctrllayer.InvoiceController;
import guilayer.MainWindow;
import guilayer.interfaces.PerformPanel;
import modlayer.Invoice;
import modlayer.InvoiceItem;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;

public class ShowInvoice extends PerformPanel implements ActionListener{

	private InvoiceController invCtrl;
	private Invoice invoice;
	
	private JTextField txt_supplier_cvrname;
	private JTextField txt_suppPhone;
	private JTextField txt_suppEmail;
	private JTextField txt_empPhone;
	private JTextField txt_empName;
	private JTextField txt_orderDate;
	private JTextField txt_deliverDate;
	private JButton btn_ok;
	private JTable table;
	
	private InvoiceTable model;
	
	public ShowInvoice() {
		invCtrl = new InvoiceController();
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setVisible(false);
		setBounds(0, 0, MainWindow.contentWidth, MainWindow.totalHeight);
		model = new InvoiceTable();
		
		Label lbl_supplier = new Label("Supplier");
		lbl_supplier.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_supplier.setBounds(16, 16, 129, 22);
		add(lbl_supplier);
		
		Label lbl_orderDate = new Label("Ordered");
		lbl_orderDate.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_orderDate.setBounds(16, 198, 170, 22);
		add(lbl_orderDate);
		
		Label lbl_deliverDate = new Label("Delivered");
		lbl_deliverDate.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_deliverDate.setBounds(222, 198, 170, 22);
		add(lbl_deliverDate);
		
		Label lbl_empPlacedBy = new Label("Placed by");
		lbl_empPlacedBy.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_empPlacedBy.setBounds(16, 134, 170, 22);
		add(lbl_empPlacedBy);
		
		Label lbl_suppPhone = new Label("Phone");
		lbl_suppPhone.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_suppPhone.setBounds(16, 75, 170, 22);
		add(lbl_suppPhone);
		
		Label lbl_suppEmail = new Label("Email");
		lbl_suppEmail.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_suppEmail.setBounds(222, 75, 170, 22);
		add(lbl_suppEmail);
		
		Label lbl_empPhone = new Label("Phone");
		lbl_empPhone.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_empPhone.setBounds(222, 134, 170, 22);
		add(lbl_empPhone);
		
		txt_supplier_cvrname = new JTextField();
		txt_supplier_cvrname.setEditable(false);
		txt_supplier_cvrname.setColumns(10);
		txt_supplier_cvrname.setBounds(16, 44, 376, 20);
		add(txt_supplier_cvrname);
		
		txt_suppPhone = new JTextField();
		txt_suppPhone.setEditable(false);
		txt_suppPhone.setColumns(10);
		txt_suppPhone.setBounds(16, 103, 170, 20);
		add(txt_suppPhone);
		
		btn_ok = new JButton("Ok");
		btn_ok.setBounds(654, 420, 122, 32);
		add(btn_ok);
		
		txt_suppEmail = new JTextField();
		txt_suppEmail.setEditable(false);
		txt_suppEmail.setText("");
		txt_suppEmail.setColumns(10);
		txt_suppEmail.setBounds(222, 103, 170, 20);
		add(txt_suppEmail);
		
		txt_empPhone = new JTextField();
		txt_empPhone.setEditable(false);
		txt_empPhone.setText("");
		txt_empPhone.setColumns(10);
		txt_empPhone.setBounds(222, 162, 170, 20);
		add(txt_empPhone);
		
		txt_empName = new JTextField();
		txt_empName.setEditable(false);
		txt_empName.setText("");
		txt_empName.setColumns(10);
		txt_empName.setBounds(16, 162, 170, 20);
		add(txt_empName);
		
		txt_orderDate = new JTextField();
		txt_orderDate.setEditable(false);
		txt_orderDate.setText("");
		txt_orderDate.setColumns(10);
		txt_orderDate.setBounds(16, 226, 170, 20);
		add(txt_orderDate);
		
		txt_deliverDate = new JTextField();
		txt_deliverDate.setEditable(false);
		txt_deliverDate.setText("");
		txt_deliverDate.setColumns(10);
		txt_deliverDate.setBounds(222, 226, 170, 20);
		add(txt_deliverDate);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 257, 760, 152);
		add(scrollPane);
		
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
		
		reset();
		btn_ok.addActionListener(this);
	}
	
	
	
	private void fill(Invoice invoice) {
		this.invoice = invoice;
		Date delivered = invoice.getDateDelivered();
		
		txt_supplier_cvrname.setText(invoice.getSupplier().getCvr() +" - "+ invoice.getSupplier().getName());
		txt_suppPhone.setText(invoice.getSupplier().getPhone());
		txt_suppEmail.setText(invoice.getSupplier().getEmail());
		txt_empName.setText(invoice.getPlacedBy().getName());
		txt_empPhone.setText(invoice.getPlacedBy().getPhone());
		txt_orderDate.setText(invoice.getTimestamp().toString());
		txt_deliverDate.setText(delivered == null ? "" : delivered.toString());
		model.setInvoiceItems(invoice.getItems());
		setVisible(true);
	}
	
	
	
	private void reset() {
		invoice = null;
		
		txt_supplier_cvrname.setText("");
		txt_suppPhone.setText("");
		txt_suppEmail.setText("");
		txt_empName.setText("");
		txt_empPhone.setText("");
		txt_orderDate.setText("");
		txt_deliverDate.setText("");
	}
	
	
	
	
	public void createItem() {
		
	}
	public void details(Invoice invoice) {
		fill(invoice);
	}

	private void close() {
		setVisible(false);
		reset();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_ok) {
			triggerCancelListeners();
			close();
		}
	}
	
	private class InvoiceTable extends AbstractTableModel {

		private String[] columns = new String[] { "Barcode", "Name", "Quantity", "Unit", "Price" };
		private ArrayList<InvoiceItem> invItems;
		
		public InvoiceTable() {
			this(new ArrayList<InvoiceItem>());
		}

		public InvoiceTable(ArrayList<InvoiceItem> invItems) {
			this.invItems = invItems;
			update();
		}

		@Override
		public Class getColumnClass(int columnIndex) {
			try {
			return getValueAt(0, columnIndex).getClass();
			} catch (Exception ignored) {
				return new Object().getClass();
			}
		}

		@Override
		public int getRowCount() {
			return invItems.size();
		}
		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			InvoiceItem invItem = invItems.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return invItem.getItem().getBarcode();
				case 1:
					return invItem.getItem().getName();
				case 2:
					return invItem.getQuantity();
				case 3:
					return invItem.getItem().getUnit().getAbbr();
				case 4:
					return invItem.getUnitPrice() +" kr";
			}
		
			return null;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
		public InvoiceItem getInvoiceAt(int rowIndex) {
			return invItems.get(rowIndex);
		}
		
		public void setInvoiceItems(ArrayList<InvoiceItem> invItems) {
			this.invItems = invItems;
			update();
		}
		
		public void update() {
			fireTableDataChanged();
		}
	}
}
