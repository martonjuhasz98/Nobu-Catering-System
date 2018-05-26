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
	private JTextField txt_Cvr;
	private JTextField txt_Address;
	private JButton btn_submit;
	private JButton btn_cancel;
	private JTextField txt_Name;
	private JTextField txt_ZipCode;
	private JTextField txt_Phone;
	private JTextField txt_Email;
	private JLabel lbl_City;

	public EditSupplier() {
		supplierCtrl = new SupplierController();

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		Label lbl_barcode = new Label("Barcode *");
		lbl_barcode.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_barcode.setBounds(16, 1, 129, 22);
		add(lbl_barcode);

		txt_Cvr = new JTextField();
		txt_Cvr.setColumns(10);
		txt_Cvr.setBounds(16, 45, 376, 20);
		add(txt_Cvr);

		Label lbl_name = new Label("Name *");
		lbl_name.setForeground(Color.BLACK);
		lbl_name.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_name.setBounds(16, 75, 129, 22);
		add(lbl_name);

		txt_Name = new JTextField();
		txt_Name.setText("");
		txt_Name.setColumns(10);
		txt_Name.setBounds(16, 105, 376, 20);
		add(txt_Name);

		txt_Address = new JTextField();
		txt_Address.setColumns(10);
		txt_Address.setBounds(16, 165, 376, 20);
		add(txt_Address);

		txt_ZipCode = new JTextField();
		txt_ZipCode.setBounds(16, 225, 90, 26);
		add(txt_ZipCode);
		txt_ZipCode.setColumns(10);

		lbl_City = new JLabel("City");
		lbl_City.setForeground(Color.DARK_GRAY);
		lbl_City.setBounds(113, 230, 279, 16);
		add(lbl_City);

		txt_Phone = new JTextField();
		txt_Phone.setBounds(16, 285, 204, 26);
		add(txt_Phone);
		txt_Phone.setColumns(10);

		txt_Email = new JTextField();
		txt_Email.setBounds(16, 345, 376, 26);
		add(txt_Email);
		txt_Email.setColumns(10);

		btn_submit = new JButton("Create");
		btn_submit.setBounds(536, 457, 122, 32);
		add(btn_submit);
		btn_submit.addActionListener(this);

		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 457, 122, 32);
		add(btn_cancel);
		
		btn_cancel.addActionListener(this);
		txt_Cvr.addCaretListener(this);
		txt_Name.addCaretListener(this);
		txt_Address.addCaretListener(this);
		txt_Name.addCaretListener(this);
		txt_ZipCode.addCaretListener(this);
		txt_Phone.addCaretListener(this);
		txt_Email.addCaretListener(this);
		reset();
	}
	private void fill(Supplier supplier) {
		this.supplier = supplier;

		txt_Cvr.setText(supplier.getCvr().trim());
		txt_Cvr.setEnabled(false);
		txt_Name.setText(supplier.getName().trim());
		txt_Address.setText(supplier.getAddress().trim());
		txt_ZipCode.setText(supplier.getCity().getZipCode().trim());
		lbl_City.setText("City: " + supplier.getCity().getName().trim());
		txt_Phone.setText(supplier.getPhone().trim());
		txt_Email.setText(supplier.getEmail().trim());

		creatingSupplier = false;
		btn_submit.setText("Update");
		btn_submit.setEnabled(true);
	}
	private void reset() {
		supplier = null;
		creatingSupplier = true;

		txt_Cvr.setEnabled(true);
		txt_Cvr.setText("");
		txt_Name.setText("");
		txt_Address.setText("");
		txt_ZipCode.setText("");
		lbl_City.setText("City: ");
		txt_Phone.setText("");
		txt_Email.setText("");

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
			String cvr = txt_Cvr.getText().trim();
			String name = txt_Name.getText().trim();
			String address = txt_Address.getText().trim();
			String zipCode = txt_ZipCode.getText().trim();
			String phone = txt_Phone.getText().trim();
			String email = txt_Email.getText().trim();

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
		if (e.getSource() == txt_ZipCode) {
			City c = supplierCtrl.getCity(txt_ZipCode.getText().trim());
			lbl_City.setText((c == null) ? "City: " : "City: " + c.getName());
		} else if (e.getSource() == txt_Cvr || e.getSource() == txt_Name || e.getSource() == txt_Address
				|| e.getSource() == txt_ZipCode || e.getSource() == txt_Phone || e.getSource() == txt_Email) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
