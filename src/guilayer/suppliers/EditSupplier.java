package guilayer.suppliers;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ctrllayer.SupplierController;
import guilayer.ManagerWindow;
import guilayer.essentials.PerformPanel;
import modlayer.City;
import modlayer.Employee;
import modlayer.Supplier;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.Color;

public class EditSupplier extends PerformPanel implements ActionListener, CaretListener {

	private SupplierController supplierCtrl;
	private Supplier supplier;
	private boolean creatingSupplier;
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
		super();
		
		supplierCtrl = new SupplierController();

		initialize();
	}
	//Layout
	private void initialize() {

		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);
		
		Label lbl_cvr = new Label("CVR *");
		lbl_cvr.setBounds(10, 10, 181, 22);
		add(lbl_cvr);
		
		txtCvr = new JTextField();
		txtCvr.setBounds(10, 38, 181, 26);
		add(txtCvr);
		
		Label lbl_name = new Label("Name *");
		lbl_name.setBounds(10, 75, 564, 22);
		add(lbl_name);
		
		txtName = new JTextField();
		txtName.setBounds(10, 103, 399, 26);
		add(txtName);
		
		Label lbl_zipCode = new Label("Zipcode *");
		lbl_zipCode.setBounds(10, 140, 129, 22);
		add(lbl_zipCode);
		
		txtZipCode = new JTextField();
		txtZipCode.setBounds(10, 168, 48, 26);
		add(txtZipCode);
		
		txtCity = new JTextField();
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
		lbl_email.setBounds(10, 318, 129, 22);
		add(lbl_email);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(10, 346, 399, 26);
		add(txtEmail);

		btn_submit = new JButton("Create");
		btn_submit.setBounds(522, 444, 122, 32);
		add(btn_submit);

		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(654, 444, 122, 32);
		add(btn_cancel);

		reset();
		
		txtCvr.addCaretListener(this);
		txtName.addCaretListener(this);
		txtZipCode.addCaretListener(this);
		txtAddress.addCaretListener(this);
		txtPhone.addCaretListener(this);
		txtEmail.addCaretListener(this);
		btn_submit.addActionListener(this);
		btn_cancel.addActionListener(this);
	}
	@Override
	public void prepare() {}
	@Override
	public void reset() {
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
	public void openToCreate() {
		open();
	}
	public void openToUpdate(Supplier supplier) {
		open();
		
		this.supplier = supplier;
		creatingSupplier = false;

		txtCvr.setText(supplier.getCvr().trim());
		txtCvr.setEnabled(false);
		txtName.setText(supplier.getName().trim());
		txtAddress.setText(supplier.getAddress().trim());
		txtZipCode.setText(supplier.getCity().getZipCode().trim());
		txtCity.setText(supplier.getCity().getName().trim());
		txtPhone.setText(supplier.getPhone().trim());
		txtEmail.setText(supplier.getEmail().trim());

		btn_submit.setText("Update");
		btn_submit.setEnabled(true);
	}
	//Functionalities
	private void createSupplier(String cvr, String name, String address, City city, String phone, String email) {
		String message, title;
		int messageType;
		
		if (!supplierCtrl.createSupplier(cvr, name, address, city, phone, email)) {
			message = "An error occured while creating the Supplier!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Supplier was successfully created!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			triggerPerformListeners();
			close();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void updateSupplier(Supplier supplier) {
		String message, title;
		int messageType;
		
		if (!supplierCtrl.updateSupplier(supplier)) {
			message = "An error occured while updating the Supplier!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Supplier was successfully updated!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			triggerPerformListeners();
			close();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void cancel() {
		triggerCancelListeners();
		close();
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
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_submit) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", (creatingSupplier ?  "Creating" : "Updating") + " a Supplier", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			
			String cvr = txtCvr.getText().trim();
			String name = txtName.getText().trim();
			String address = txtAddress.getText().trim();
			City city = new City();
			city.setZipCode(txtZipCode.getText().trim());
			String phone = txtPhone.getText().trim();
			String email = txtEmail.getText().trim();

			if (creatingSupplier) {
				createSupplier(cvr, name, address, city, phone, email);
			} else {
				supplier.setName(name);
				supplier.setAddress(address);
				supplier.setCity(city);
				supplier.setPhone(phone);
				supplier.setEmail(email);
				
				updateSupplier(supplier);
			}
		} else if (source == btn_cancel) {
			cancel();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		final Object source = e.getSource();
		if (source == txtZipCode) {
			City c = supplierCtrl.getCity(txtZipCode.getText().trim());
			txtCity.setText((c == null) ? "" : c.getName());
		}
		if (source == txtCvr || source == txtName || source == txtAddress
				|| source == txtZipCode || source == txtPhone || source == txtEmail) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
