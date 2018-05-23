package guilayer.invoices;

import java.awt.Font;
import java.awt.Label;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ctrllayer.InvoiceController;
import ctrllayer.ItemController;
import guilayer.MainWindow;
import guilayer.interfaces.EditPanel;
import modlayer.Invoice;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Consumer;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.JScrollPane;

public class ShowInvoice extends EditPanel {

	private InvoiceController invCtrl;
	private Invoice invoice;
	
	private JTextField txt_supplier_cvrname;
	private JTextField txt_suppPhone;
	private JTextField txt_suppEmail;
	private JTextField txt_empPhone;
	private JTextField txt_empName;
	private JTextField txt_orderDate;
	private JTextField txt_deliverDate;
	private JButton btn_cancel;
	
	public ShowInvoice() {
		invCtrl = new InvoiceController();
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setVisible(false);
		setBounds(0, 0, MainWindow.contentWidth, MainWindow.totalHeight);
		
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
		txt_supplier_cvrname.setEnabled(false);
		txt_supplier_cvrname.setEditable(false);
		txt_supplier_cvrname.setColumns(10);
		txt_supplier_cvrname.setBounds(16, 44, 376, 20);
		add(txt_supplier_cvrname);
		
		txt_suppPhone = new JTextField();
		txt_suppPhone.setEnabled(false);
		txt_suppPhone.setEditable(false);
		txt_suppPhone.setColumns(10);
		txt_suppPhone.setBounds(16, 103, 170, 20);
		add(txt_suppPhone);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(654, 420, 122, 32);
		add(btn_cancel);
		
		txt_suppEmail = new JTextField();
		txt_suppEmail.setEditable(false);
		txt_suppEmail.setEnabled(false);
		txt_suppEmail.setText("");
		txt_suppEmail.setColumns(10);
		txt_suppEmail.setBounds(222, 103, 170, 20);
		add(txt_suppEmail);
		
		txt_empPhone = new JTextField();
		txt_empPhone.setEditable(false);
		txt_empPhone.setEnabled(false);
		txt_empPhone.setText("");
		txt_empPhone.setColumns(10);
		txt_empPhone.setBounds(222, 162, 170, 20);
		add(txt_empPhone);
		
		txt_empName = new JTextField();
		txt_empName.setEditable(false);
		txt_empName.setEnabled(false);
		txt_empName.setText("");
		txt_empName.setColumns(10);
		txt_empName.setBounds(16, 162, 170, 20);
		add(txt_empName);
		
		txt_orderDate = new JTextField();
		txt_orderDate.setEnabled(false);
		txt_orderDate.setEditable(false);
		txt_orderDate.setText("");
		txt_orderDate.setColumns(10);
		txt_orderDate.setBounds(16, 226, 170, 20);
		add(txt_orderDate);
		
		txt_deliverDate = new JTextField();
		txt_deliverDate.setEditable(false);
		txt_deliverDate.setEnabled(false);
		txt_deliverDate.setText("");
		txt_deliverDate.setColumns(10);
		txt_deliverDate.setBounds(222, 226, 170, 20);
		add(txt_deliverDate);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 257, 760, 152);
		add(scrollPane);
		
		reset();
	}
	
	
	
	private void fill(Invoice invoice) {
		this.invoice = invoice;
		
		txt_supplier_cvrname.setText(invoice.getSupplier().getCvr() +" - "+ invoice.getSupplier().getName());
		txt_suppPhone.setText(invoice.getSupplier().getPhone());
		txt_suppEmail.setText(invoice.getSupplier().getEmail());
		txt_empName.setText("");
		txt_empPhone.setText("");
		txt_orderDate.setText(invoice.getTimestamp().toString());
		txt_deliverDate.setText(invoice.getDateDelivered().toString());
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
}
