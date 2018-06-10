package guilayer.invoices;

import java.awt.Label;

import javax.swing.JTextField;

import guilayer.ManagerWindow;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.PerformPanel;
import modlayer.Invoice;
import modlayer.InvoiceItem;

import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class ShowInvoice extends PerformPanel implements ActionListener{

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
		super();
		
		initialize();
	}
	//Layout
	private void initialize() {
		
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);
		
		model = new InvoiceTable();
		
		Label lbl_supplier = new Label("Supplier");
		lbl_supplier.setBounds(16, 16, 129, 22);
		add(lbl_supplier);
		
		txt_supplier_cvrname = new JTextField();
		txt_supplier_cvrname.setEditable(false);
		txt_supplier_cvrname.setBounds(16, 44, 376, 20);
		add(txt_supplier_cvrname);
		
		Label lbl_orderDate = new Label("Ordered");
		lbl_orderDate.setBounds(16, 198, 170, 22);
		add(lbl_orderDate);
		
		txt_orderDate = new JTextField();
		txt_orderDate.setEditable(false);
		txt_orderDate.setBounds(16, 226, 170, 20);
		add(txt_orderDate);
		
		Label lbl_deliverDate = new Label("Delivered");
		lbl_deliverDate.setBounds(222, 198, 170, 22);
		add(lbl_deliverDate);
		
		txt_deliverDate = new JTextField();
		txt_deliverDate.setEditable(false);
		txt_deliverDate.setBounds(222, 226, 170, 20);
		add(txt_deliverDate);
		
		Label lbl_empPlacedBy = new Label("Placed by");
		lbl_empPlacedBy.setBounds(16, 134, 170, 22);
		add(lbl_empPlacedBy);
		
		txt_empName = new JTextField();
		txt_empName.setEditable(false);
		txt_empName.setBounds(16, 162, 170, 20);
		add(txt_empName);
		
		Label lbl_suppPhone = new Label("Phone");
		lbl_suppPhone.setBounds(16, 75, 170, 22);
		add(lbl_suppPhone);
		
		txt_suppPhone = new JTextField();
		txt_suppPhone.setEditable(false);
		txt_suppPhone.setBounds(16, 103, 170, 20);
		add(txt_suppPhone);
		
		Label lbl_suppEmail = new Label("Email");
		lbl_suppEmail.setBounds(222, 75, 170, 22);
		add(lbl_suppEmail);
		
		txt_suppEmail = new JTextField();
		txt_suppEmail.setEditable(false);
		txt_suppEmail.setBounds(222, 103, 170, 20);
		add(txt_suppEmail);
		
		Label lbl_empPhone = new Label("Phone");
		lbl_empPhone.setBounds(222, 134, 170, 22);
		add(lbl_empPhone);
		
		txt_empPhone = new JTextField();
		txt_empPhone.setEditable(false);
		txt_empPhone.setBounds(222, 162, 170, 20);
		add(txt_empPhone);
		
		btn_ok = new JButton("Ok");
		btn_ok.setBounds(654, 420, 122, 32);
		add(btn_ok);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 257, 760, 160);
		add(scrollPane);
		
		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		reset();
		
		btn_ok.addActionListener(this);
	}
	@Override
	public void prepare() {}
	@Override
	public void reset() {
		txt_supplier_cvrname.setText("");
		txt_suppPhone.setText("");
		txt_suppEmail.setText("");
		txt_empName.setText("");
		txt_empPhone.setText("");
		txt_orderDate.setText("");
		txt_deliverDate.setText("");
	}
	public void openToShow(Invoice invoice) {
		open();
		
		txt_supplier_cvrname.setText(invoice.getSupplier().getCvr() +" - "+ invoice.getSupplier().getName());
		txt_suppPhone.setText(invoice.getSupplier().getPhone());
		txt_suppEmail.setText(invoice.getSupplier().getEmail());
		txt_empName.setText(invoice.getPlacedBy().getName());
		txt_empPhone.setText(invoice.getPlacedBy().getPhone());
		txt_orderDate.setText(invoice.getTimestamp().toString());
		Date delivered = invoice.getDateDelivered();
		txt_deliverDate.setText(delivered == null ? "" : delivered.toString());
		model.setItems(invoice.getItems());
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_ok) {
			triggerCancelListeners();
			close();
		}
	}
	//Classes
	private class InvoiceTable extends ItemTableModel<InvoiceItem> {

		public InvoiceTable() {
			super();
			
			columns = new String[] { "Barcode", "Name", "Quantity", "Unit", "Price" };
		}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			InvoiceItem invItem = items.get(rowIndex);
			
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
					return invItem.getUnitPrice() + " kr";
			}
		
			return null;
		}
	}
}
