package guilayer.suppliers;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JLabel;
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
	private JTextField txtAddress;
	private JButton btn_submit;
	private JButton btn_cancel;
	private JTextField txtName;
	private JTextField txtZipCode;
	private JTextField txtPhone;
	private JTextField txtEmail;
	private JLabel lblCity;

	public EditSupplier() {
		supplierCtrl = new SupplierController();

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		Label lblbarcode = new Label("Barcode *");
		lblbarcode.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblbarcode.setBounds(16, 1, 129, 22);
		add(lblbarcode);

		txtCvr = new JTextField();
		txtCvr.setColumns(10);
		txtCvr.setBounds(16, 45, 376, 20);
		add(txtCvr);

		Label lblname = new Label("Name *");
		lblname.setForeground(Color.BLACK);
		lblname.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblname.setBounds(16, 75, 129, 22);
		add(lblname);

		txtName = new JTextField();
		txtName.setText("");
		txtName.setColumns(10);
		txtName.setBounds(16, 105, 376, 20);
		add(txtName);

		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBounds(16, 165, 376, 20);
		add(txtAddress);

		txtZipCode = new JTextField();
		txtZipCode.setBounds(16, 225, 90, 26);
		add(txtZipCode);
		txtZipCode.setColumns(10);

		lblCity = new JLabel("City");
		lblCity.setForeground(Color.DARK_GRAY);
		lblCity.setBounds(113, 230, 279, 16);
		add(lblCity);

		txtPhone = new JTextField();
		txtPhone.setBounds(16, 285, 204, 26);
		add(txtPhone);
		txtPhone.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setBounds(16, 345, 376, 26);
		add(txtEmail);
		txtEmail.setColumns(10);

		btn_submit = new JButton("Create");
		btn_submit.setBounds(536, 457, 122, 32);
		add(btn_submit);
		btn_submit.addActionListener(this);

		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 457, 122, 32);
		add(btn_cancel);
		
		btn_cancel.addActionListener(this);
		txtCvr.addCaretListener(this);
		txtName.addCaretListener(this);
		txtAddress.addCaretListener(this);
		txtName.addCaretListener(this);
		txtZipCode.addCaretListener(this);
		txtPhone.addCaretListener(this);
		txtEmail.addCaretListener(this);
		reset();
	}
	private void fill(Supplier supplier) {
		this.supplier = supplier;

		txtCvr.setText(supplier.getCvr().trim());
		txtCvr.setEnabled(false);
		txtName.setText(supplier.getName().trim());
		txtAddress.setText(supplier.getAddress().trim());
		txtZipCode.setText(supplier.getCity().getZipCode().trim());
		lblCity.setText("City: " + supplier.getCity().getName().trim());
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
		lblCity.setText("City: ");
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
		if (lblCity.getText().trim().equals("City:"))
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
			lblCity.setText((c == null) ? "City: " : "City: " + c.getName());
		} else if (e.getSource() == txtCvr || e.getSource() == txtName || e.getSource() == txtAddress
				|| e.getSource() == txtZipCode || e.getSource() == txtPhone || e.getSource() == txtEmail) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
