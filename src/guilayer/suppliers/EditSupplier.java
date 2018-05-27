package guilayer.suppliers;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ctrllayer.SupplierController;
import guilayer.ManagerWindow;
import guilayer.interfaces.PerformPanel;
import modlayer.City;
import modlayer.Supplier;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.Color;

public class EditSupplier extends PerformPanel implements ActionListener, CaretListener {

	private SupplierController supplierCtrl;
	private Supplier supplier;
	private boolean creatingSupplier = false;
	private JTextField txtCvr;
	private JTextField txtName;
	private JTextField txtZipCode;
	private JTextField txtCity;
	private JTextField txtAddress;
	private JTextField txtPhone;
	private JTextField txtEmail;
	private JButton btn_submit;
	private JButton btn_cancel;

	public EditSupplier() {
		supplierCtrl = new SupplierController();

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);
		
		Label lbl_cvr = new Label("CVR *");
		lbl_cvr.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_cvr.setBounds(10, 10, 181, 22);
		add(lbl_cvr);
		
		txtCvr = new JTextField();
		txtCvr.setText("");
		txtCvr.setEnabled(true);
		txtCvr.setColumns(10);
		txtCvr.setBounds(10, 38, 181, 26);
		add(txtCvr);
		
		Label lbl_name = new Label("Name *");
		lbl_name.setForeground(Color.BLACK);
		lbl_name.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_name.setBounds(10, 75, 564, 22);
		add(lbl_name);
		
		txtName = new JTextField();
		txtName.setText("");
		txtName.setColumns(10);
		txtName.setBounds(10, 103, 399, 26);
		add(txtName);
		
		Label lbl_zipCode = new Label("Zipcode *");
		lbl_zipCode.setForeground(Color.BLACK);
		lbl_zipCode.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_zipCode.setBounds(10, 140, 129, 22);
		add(lbl_zipCode);
		
		txtZipCode = new JTextField();
		txtZipCode.setText("");
		txtZipCode.setColumns(4);
		txtZipCode.setBounds(10, 168, 48, 26);
		add(txtZipCode);
		
		txtCity = new JTextField();
		txtCity.setText("");
		txtCity.setEditable(false);
		txtCity.setColumns(10);
		txtCity.setBounds(60, 168, 131, 26);
		add(txtCity);
		
		Label lbl_address = new Label("Address *");
		lbl_address.setForeground(Color.BLACK);
		lbl_address.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_address.setBounds(10, 200, 129, 22);
		add(lbl_address);
		
		txtAddress = new JTextField();
		txtAddress.setText("");
		txtAddress.setColumns(10);
		txtAddress.setBounds(10, 228, 399, 26);
		add(txtAddress);
		
		Label lbl_phone = new Label("Phone *");
		lbl_phone.setForeground(Color.BLACK);
		lbl_phone.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_phone.setBounds(10, 260, 129, 22);
		add(lbl_phone);
		
		txtPhone = new JTextField();
		txtPhone.setText("");
		txtPhone.setColumns(15);
		txtPhone.setBounds(10, 288, 181, 26);
		add(txtPhone);
		
		Label lbl_email = new Label("Email");
		lbl_email.setForeground(Color.BLACK);
		lbl_email.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_email.setBounds(10, 318, 129, 22);
		add(lbl_email);
		
		txtEmail = new JTextField();
		txtEmail.setText("");
		txtEmail.setColumns(10);
		txtEmail.setBounds(10, 346, 399, 26);
		add(txtEmail);

		btn_submit = new JButton("Create");
		btn_submit.setBounds(536, 457, 122, 32);
		add(btn_submit);

		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 457, 122, 32);
		add(btn_cancel);

		txtCvr.addCaretListener(this);
		txtName.addCaretListener(this);
		txtZipCode.addCaretListener(this);
		txtAddress.addCaretListener(this);
		txtPhone.addCaretListener(this);
		txtEmail.addCaretListener(this);
		btn_submit.addActionListener(this);
		btn_cancel.addActionListener(this);
		reset();
	}
	private void fill(Supplier supplier) {
		this.supplier = supplier;

		txtCvr.setText(supplier.getCvr().trim());
		txtCvr.setEnabled(false);
		txtName.setText(supplier.getName().trim());
		txtAddress.setText(supplier.getAddress().trim());
		txtZipCode.setText(supplier.getCity().getZipCode().trim());
		txtCity.setText(supplier.getCity().getName().trim());
		txtPhone.setText(supplier.getPhone().trim());
		txtEmail.setText(supplier.getEmail().trim());

		creatingSupplier = false;
		btn_submit.setText("Update");
		btn_submit.setEnabled(true);
	}
	private void reset() {
		supplier = null;
		creatingSupplier = true;

		txtCvr.setEnabled(true);
		txtCvr.setText("");
		txtName.setText("");
		txtAddress.setText("");
		txtZipCode.setText("");
		txtCity.setText("");
		txtPhone.setText("");
		txtEmail.setText("");

		btn_submit.setText("Create");
		btn_submit.setEnabled(false);
	}
	public void create() {
		open();
	}
	public void update(Supplier supplier) {
		fill(supplier);
		open();
	}
	private void open() {
		setVisible(true);
	}
	private void close() {
		setVisible(false);
		reset();
	}

	private boolean isFilled() {
		if (!txtCvr.getText().trim().matches("[0-9]{8}"))
			return false;
		if (txtName.getText().trim().isEmpty())
			return false;
		if (txtAddress.getText().trim().isEmpty())
			return false;
		if (!txtZipCode.getText().trim().matches("[0-9]+"))
			return false;
		if (txtAddress.getText().trim().isEmpty())
			return false;
		if (txtCity.getText().trim().isEmpty())
			return false;
		if (!txtPhone.getText().trim().matches("^\\+?[0-9]{1,15}$"))  
			return false;
		if (!txtEmail.getText().trim().matches(".+@.+\\..+"))
			return false;
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_submit) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", (creatingSupplier ?  "Creating" : "Updating") + " supplier", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			
			String cvr = txtCvr.getText().trim();
			String name = txtName.getText().trim();
			String address = txtAddress.getText().trim();
			String zipCode = txtZipCode.getText().trim();
			String phone = txtPhone.getText().trim();
			String email = txtEmail.getText().trim();

			if (creatingSupplier) {
				if (!supplierCtrl.createSupplier(cvr, name, address, zipCode, phone, email)) {
					JOptionPane.showMessageDialog(this, "An error occured while creating the Supplier!", "Error!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(this, "The Supplier was successfully created!", "Success!",
						JOptionPane.INFORMATION_MESSAGE);

				triggerPerformListeners();
			} else {
				supplier.setName(name);
				supplier.setAddress(address);
				supplier.setCity(new City(zipCode));
				supplier.setPhone(phone);
				supplier.setEmail(email);
				if (!supplierCtrl.updateSupplier(supplier)) {
					JOptionPane.showMessageDialog(this, "An error occured while creating the Supplier!", "Error!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(this, "The Supplier was successfully edited!", "Success!",
						JOptionPane.INFORMATION_MESSAGE);

				triggerPerformListeners();
			}
			close();
		} else if (e.getSource() == btn_cancel) {
			triggerCancelListeners();
			close();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txtZipCode) {
			City c = supplierCtrl.getCity(txtZipCode.getText().trim());
			txtCity.setText((c == null) ? "" : c.getName());
		} else if (e.getSource() == txtCvr || e.getSource() == txtName || e.getSource() == txtAddress
				|| e.getSource() == txtZipCode || e.getSource() == txtPhone || e.getSource() == txtEmail) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
