package guilayer.employees;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ctrllayer.EmployeeController;
import guilayer.ManagerWindow;
import guilayer.interfaces.PerformPanel;
import modlayer.City;
import modlayer.Employee;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;

public class EditEmployee extends PerformPanel implements ActionListener, CaretListener {

	private EmployeeController employeeCtrl;
	private Employee employee;
	private boolean creatingEmployee;
	private JTextField txtCpr;
	private JTextField txtAddress;
	private JButton btn_submit;
	private JButton btn_cancel;
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
	private Label lbl_username;
	private Label lbl_password;
	private Label lbl_confirmPassword;

	public EditEmployee() {
		employeeCtrl = new EmployeeController();
		creatingEmployee = false;

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		Label lbl_Cpr = new Label("CPR *");
		lbl_Cpr.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_Cpr.setBounds(16, 10, 181, 22);
		add(lbl_Cpr);

		txtCpr = new JTextField();
		txtCpr.setColumns(10);
		txtCpr.setBounds(16, 38, 181, 26);
		add(txtCpr);
		
				Label lbl_name = new Label("Name *");
				lbl_name.setForeground(Color.BLACK);
				lbl_name.setFont(new Font("Dialog", Font.PLAIN, 15));
				lbl_name.setBounds(215, 10, 564, 22);
				add(lbl_name);

		txtName = new JTextField();
		txtName.setBounds(215, 38, 399, 26);
		txtName.setColumns(10);
		add(txtName);
		
		Label lbl_zipCode = new Label("Zipcode *");
		lbl_zipCode.setForeground(Color.BLACK);
		lbl_zipCode.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_zipCode.setBounds(16, 75, 129, 22);
		add(lbl_zipCode);

		txtZipCode = new JTextField();
		txtZipCode.setBounds(16, 103, 48, 26);
		txtZipCode.setColumns(4);
		add(txtZipCode);

		txtCity = new JTextField();
		txtCity.setEditable(false);
		txtCity.setBounds(66, 103, 131, 26);
		txtCity.setColumns(10);
		add(txtCity);
		
		Label lbl_address = new Label("Address *");
		lbl_address.setForeground(Color.BLACK);
		lbl_address.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_address.setBounds(215, 75, 129, 22);
		add(lbl_address);
		
				txtAddress = new JTextField();
				txtAddress.setColumns(10);
				txtAddress.setBounds(215, 103, 399, 26);
				add(txtAddress);
				txtAddress.addCaretListener(this);
		
		Label lbl_phone = new Label("Phone *");
		lbl_phone.setForeground(Color.BLACK);
		lbl_phone.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_phone.setBounds(16, 140, 129, 22);
		add(lbl_phone);

		txtPhone = new JTextField();
		txtPhone.setBounds(16, 168, 181, 26);
		add(txtPhone);
		txtPhone.setColumns(15);
		
		Label lbl_email = new Label("Email");
		lbl_email.setForeground(Color.BLACK);
		lbl_email.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_email.setBounds(215, 140, 129, 22);
		add(lbl_email);

		txtEmail = new JTextField();
		txtEmail.setBounds(215, 168, 399, 26);
		add(txtEmail);
		txtEmail.setColumns(10);
		
		lbl_username = new Label("Username *");
		lbl_username.setForeground(Color.BLACK);
		lbl_username.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_username.setBounds(16, 238, 129, 22);
		add(lbl_username);
		
				txtUsername = new JTextField();
				txtUsername.setText("");
				txtUsername.setColumns(10);
				txtUsername.setBounds(16, 265, 150, 26);
				add(txtUsername);
				txtUsername.addCaretListener(this);
				txtUsername.addCaretListener(this);
		
		lbl_password = new Label("Password *");
		lbl_password.setForeground(Color.BLACK);
		lbl_password.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_password.setBounds(16, 297, 129, 22);
		add(lbl_password);
		
				passwordField = new JPasswordField();
				passwordField.setBounds(16, 320, 181, 26);
				add(passwordField);
				passwordField.addCaretListener(this);
		
		lbl_confirmPassword = new Label("Confirm password*");
		lbl_confirmPassword.setForeground(Color.BLACK);
		lbl_confirmPassword.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_confirmPassword.setBounds(216, 293, 129, 22);
		add(lbl_confirmPassword);
		
				passwordRepeatField = new JPasswordField();
				passwordRepeatField.setBounds(216, 321, 209, 26);
				add(passwordRepeatField);
				passwordRepeatField.addCaretListener(this);
		
				lblUpdateTooltip = new JLabel("*Leave empty for no changes");
				lblUpdateTooltip.setForeground(Color.GRAY);
				lblUpdateTooltip.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
				lblUpdateTooltip.setBounds(16, 348, 200, 16);
				add(lblUpdateTooltip);
		
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3, 0, 3, 1);
		Label lbl_accessLevel = new Label("Access level *");
		lbl_accessLevel.setForeground(Color.BLACK);
		lbl_accessLevel.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_accessLevel.setBounds(17, 375, 129, 22);
		add(lbl_accessLevel);
		spinnerAccLvl = new JSpinner(spinnerModel);
		spinnerAccLvl.setBounds(16, 403, 33, 26);
		add(spinnerAccLvl);

		btn_submit = new JButton("Create");
		btn_submit.setBounds(522, 444, 122, 32);
		add(btn_submit);
		btn_submit.addActionListener(this);

		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(654, 444, 122, 32);
		add(btn_cancel);
		btn_cancel.addActionListener(this);
		
		txtCpr.addCaretListener(this);
		txtName.addCaretListener(this);
		txtZipCode.addCaretListener(this);
		txtPhone.addCaretListener(this);
		txtEmail.addCaretListener(this);
		txtUsername.addCaretListener(this);
		passwordField.addCaretListener(this);
		passwordRepeatField.addCaretListener(this);
		reset();
	}

	private void fill(Employee employee) {
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
	private void reset() {
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
	public void create() {
		open();
	}
	public void update(Employee employee) {
		fill(employee);
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
		System.out.println("#"+txtCity.getText().trim()+"#");
		if (!txtPhone.getText().trim().matches("^\\+?[0-9]{1,15}$"))  
			return false;
		if (!txtEmail.getText().trim().matches(".+@.+\\..+"))
			return false;
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_submit) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", creatingEmployee ? "Creating employee" : "Updating employee", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			
			String cpr = txtCpr.getText().trim().replace("-", "");
			String name = txtName.getText().trim();
			String username = txtUsername.getText().trim();
			String password = new String(passwordField.getPassword());
			String address = txtAddress.getText().trim();
			String zipCode = txtZipCode.getText().trim();
			String phone = txtPhone.getText().trim();
			String email = txtEmail.getText().trim();
			int accessLevel = (int) spinnerAccLvl.getValue();

			if (creatingEmployee) {
				if (!employeeCtrl.createEmployee(cpr, name, username, employeeCtrl.hash(password), address,
						new City(zipCode), phone, email, accessLevel)) {

					JOptionPane.showMessageDialog(this, "An error occured while creating the Employee!", "Error!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(this, "The Employee was successfully created!", "Success!",
						JOptionPane.INFORMATION_MESSAGE);

				triggerPerformListeners();
			} else {
				employee.setName(name);
				employee.setUsername(username);
				if (!password.equals(""))
					employee.setPassword(employeeCtrl.hash(password));
				employee.setAddress(address);
				employee.setCity(new City(zipCode));
				employee.setPhone(phone);
				employee.setEmail(email);
				employee.setAccessLevel(accessLevel);
				if (!employeeCtrl.updateEmployee(employee)) {
					JOptionPane.showMessageDialog(this, "An error occured while creating the Employee!", "Error!",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(this, "The Employee was successfully edited!", "Success!",
						JOptionPane.INFORMATION_MESSAGE);

				triggerPerformListeners();
			}
			close();
		} else if (e.getSource() == btn_cancel){
			triggerCancelListeners();
			close();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txtZipCode) {
			City c = employeeCtrl.getCity(txtZipCode.getText().trim());
			txtCity.setText((c == null) ? "" : c.getName());
		} 
		if (e.getSource() == txtCpr || e.getSource() == txtName || e.getSource() == txtUsername
				|| e.getSource() == passwordField || e.getSource() == passwordRepeatField || e.getSource() == txtAddress
				|| e.getSource() == txtZipCode || e.getSource() == txtPhone || e.getSource() == txtEmail) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
