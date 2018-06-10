package guilayer.employees;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ctrllayer.EmployeeController;
import guilayer.ManagerWindow;
import guilayer.essentials.PerformPanel;
import modlayer.City;
import modlayer.Employee;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;

public class EditEmployee extends PerformPanel implements ActionListener, CaretListener {

	private EmployeeController employeeCtrl;
	private Employee employee;
	private boolean creatingEmployee;
	private JTextField txtCpr;
	private JTextField txtAddress;
	private JTextField txtUsername;
	private JTextField txtZipCode;
	private JTextField txtPhone;
	private JTextField txtEmail;
	private JTextField txtCity;
	private JTextField txtName;
	private JPasswordField passwordField;
	private JPasswordField passwordRepeatField;
	private JLabel lblUpdateTooltip;
	private JSpinner spinnerAccLvl;
	private JButton btn_submit;
	private JButton btn_cancel;

	public EditEmployee() {
		super();
		
		employeeCtrl = new EmployeeController();
		creatingEmployee = false;

		initialize();
	}

	private void initialize() {

		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		JLabel lbl_Cpr = new JLabel("CPR *");
		lbl_Cpr.setBounds(16, 10, 181, 22);
		add(lbl_Cpr);

		txtCpr = new JTextField();
		txtCpr.setBounds(16, 38, 181, 26);
		add(txtCpr);
		
		JLabel lbl_name = new JLabel("Name *");
		lbl_name.setBounds(215, 10, 564, 22);
		add(lbl_name);

		txtName = new JTextField();
		txtName.setBounds(215, 38, 399, 26);
		add(txtName);
		
		JLabel lbl_zipCode = new JLabel("Zipcode *");
		lbl_zipCode.setBounds(16, 75, 129, 22);
		add(lbl_zipCode);

		txtZipCode = new JTextField();
		txtZipCode.setBounds(16, 103, 48, 26);
		add(txtZipCode);

		txtCity = new JTextField();
		txtCity.setEditable(false);
		txtCity.setBounds(66, 103, 131, 26);
		add(txtCity);
		
		JLabel lbl_address = new JLabel("Address *");
		lbl_address.setBounds(215, 75, 129, 22);
		add(lbl_address);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(215, 103, 399, 26);
		add(txtAddress);
		
		JLabel lbl_phone = new JLabel("Phone *");
		lbl_phone.setBounds(16, 140, 129, 22);
		add(lbl_phone);

		txtPhone = new JTextField();
		txtPhone.setBounds(16, 168, 181, 26);
		add(txtPhone);
		
		JLabel lbl_email = new JLabel("Email");
		lbl_email.setBounds(215, 140, 129, 22);
		add(lbl_email);

		txtEmail = new JTextField();
		txtEmail.setBounds(215, 168, 399, 26);
		add(txtEmail);
		
		JLabel lbl_username = new JLabel("Username *");
		lbl_username.setBounds(16, 238, 129, 22);
		add(lbl_username);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(16, 265, 150, 26);
		add(txtUsername);
		
		JLabel lbl_password = new JLabel("Password *");
		lbl_password.setBounds(16, 297, 129, 22);
		add(lbl_password);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(16, 320, 181, 26);
		add(passwordField);
		
		JLabel lbl_confirmPassword = new JLabel("Confirm password*");
		lbl_confirmPassword.setBounds(216, 293, 129, 22);
		add(lbl_confirmPassword);
		
		passwordRepeatField = new JPasswordField();
		passwordRepeatField.setBounds(216, 321, 209, 26);
		add(passwordRepeatField);

		lblUpdateTooltip = new JLabel("*Leave empty for no changes");
		lblUpdateTooltip.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblUpdateTooltip.setBounds(16, 348, 200, 16);
		add(lblUpdateTooltip);
		
		JLabel lbl_accessLevel = new JLabel("Access level *");
		lbl_accessLevel.setBounds(17, 375, 129, 22);
		add(lbl_accessLevel);
		
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3, 0, 3, 1);
		spinnerAccLvl = new JSpinner(spinnerModel);
		spinnerAccLvl.setBounds(16, 403, 33, 26);
		add(spinnerAccLvl);

		btn_submit = new JButton("Create");
		btn_submit.setBounds(522, 444, 122, 32);
		add(btn_submit);

		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(654, 444, 122, 32);
		add(btn_cancel);

		reset();
		
		txtAddress.addCaretListener(this);
		txtCpr.addCaretListener(this);
		txtName.addCaretListener(this);
		txtZipCode.addCaretListener(this);
		txtPhone.addCaretListener(this);
		txtEmail.addCaretListener(this);
		txtUsername.addCaretListener(this);
		passwordField.addCaretListener(this);
		passwordRepeatField.addCaretListener(this);
		btn_submit.addActionListener(this);
		btn_cancel.addActionListener(this);
	}
	@Override
	public void prepare() {}
	@Override
	public void reset() {
		employee = null;
		creatingEmployee = true;

		txtCpr.setEnabled(true);
		txtCpr.setText("");
		txtName.setText("");
		passwordField.setText("");
		passwordRepeatField.setText("");
		txtUsername.setText("");
		txtAddress.setText("");
		txtZipCode.setText("");
		txtCity.setText("");
		txtPhone.setText("");
		txtEmail.setText("");
		spinnerAccLvl.setValue(0);

		lblUpdateTooltip.setVisible(false);
		btn_submit.setText("Create");
		btn_submit.setEnabled(false);
	}
	public void openToCreate() {
		open();
	}
	public void openToUpdate(Employee employee) {
		open();
		
		this.employee = employee;

		txtCpr.setText(employee.getCpr().trim());
		txtCpr.setEnabled(false);
		txtUsername.setText(employee.getUsername().trim());
		txtName.setText(employee.getName().trim());
		txtAddress.setText(employee.getAddress().trim());
		txtZipCode.setText(employee.getCity().getZipCode().trim());
		txtCity.setText(employee.getCity().getName().trim());
		txtPhone.setText(employee.getPhone().trim());
		txtEmail.setText(employee.getEmail().trim());
		spinnerAccLvl.setValue(employee.getAccessLevel());

		creatingEmployee = false;
		lblUpdateTooltip.setVisible(true);
		btn_submit.setText("Update");
		btn_submit.setEnabled(true);
	}
	//Functionalities
	private void createEmployee(String cpr, String name, String username, String password, String address, City city,
			String phone, String email, int accessLevel) {
		String message, title;
		int messageType;
		
		if (!employeeCtrl.createEmployee(cpr, name, username, password, address, city, phone, email, accessLevel)) {
			message = "An error occured while creating the Employee!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Employee was successfully created!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			triggerPerformListeners();
			close();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void updateEmployee(Employee employee) {
		String message, title;
		int messageType;
		
		if (!employeeCtrl.updateEmployee(employee)) {
			message = "An error occured while updating the Employee!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Employee was successfully updated!";
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
		if (!txtCpr.getText().trim().matches("^[0-9]{6}\\-?[0-9]{4}$"))
			return false;
		if (txtUsername.getText().trim().isEmpty())
			return false;
		if (txtName.getText().trim().isEmpty())
			return false;
		if (creatingEmployee && passwordField.getPassword().length == 0
				|| !new String(passwordField.getPassword()).equals(new String(passwordRepeatField.getPassword())))
			return false;
		if (txtAddress.getText().trim().isEmpty())
			return false;
		if (!txtZipCode.getText().trim().matches("^[0-9]{1,4}$"))  
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
		if (e.getSource() == btn_submit) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", (creatingEmployee ? "Creating" : "Updating") + " an Employee", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			
			String cpr = txtCpr.getText().trim().replace("-", "");
			String name = txtName.getText().trim();
			String username = txtUsername.getText().trim();
			String password = new String(passwordField.getPassword());
			String address = txtAddress.getText().trim();
			String phone = txtPhone.getText().trim();
			String email = txtEmail.getText().trim();
			int accessLevel = (int)spinnerAccLvl.getValue();
			City city = new City();
			city.setZipCode(txtZipCode.getText().trim());

			if (creatingEmployee) {
				createEmployee(cpr, name, username, employeeCtrl.hash(password), address, city, phone, email, accessLevel);
			} else {
				employee.setName(name);
				employee.setUsername(username);
				if (!password.equals(""))
					employee.setPassword(employeeCtrl.hash(password));
				employee.setAddress(address);
				employee.setCity(city);
				employee.setPhone(phone);
				employee.setEmail(email);
				employee.setAccessLevel(accessLevel);
				
				updateEmployee(employee);
			}
		} else if (e.getSource() == btn_cancel){
			cancel();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		final Object source = e.getSource();
		if (source == txtZipCode) {
			City c = employeeCtrl.getCity(txtZipCode.getText().trim());
			txtCity.setText((c == null) ? "" : c.getName());
		} 
		if (source == txtCpr || source == txtName || source == txtUsername
				|| source == passwordField || source == passwordRepeatField || source == txtAddress
				|| source == txtZipCode || source == txtPhone || source == txtEmail) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
