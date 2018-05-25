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
	private JLabel lblCity;
	private JTextField txtName;
	private JPasswordField passwordField;
	private JPasswordField passwordRepeatField;
	private JLabel lblUpdateTooltip;
	private JSpinner spinnerAccLvl;

	public EditEmployee() {
		employeeCtrl = new EmployeeController();
		creatingEmployee = false;

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

		txtCpr = new JTextField();
		txtCpr.setColumns(10);
		txtCpr.setBounds(16, 45, 150, 26);
		add(txtCpr);

		txtName = new JTextField();
		txtName.setBounds(200, 45, 259, 26);
		add(txtName);
		txtName.setColumns(10);

		Label lbl_name = new Label("Name *");
		lbl_name.setForeground(Color.BLACK);
		lbl_name.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_name.setBounds(16, 75, 129, 22);
		add(lbl_name);

		txtUsername = new JTextField();
		txtUsername.setText("");
		txtUsername.setColumns(10);
		txtUsername.setBounds(16, 105, 200, 26);
		add(txtUsername);

		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBounds(16, 225, 400, 26);
		add(txtAddress);

		txtZipCode = new JTextField();
		txtZipCode.setBounds(16, 285, 90, 26);
		add(txtZipCode);
		txtZipCode.setColumns(10);

		lblCity = new JLabel("City");
		lblCity.setForeground(Color.DARK_GRAY);
		lblCity.setBounds(113, 285, 279, 26);
		add(lblCity);

		txtPhone = new JTextField();
		txtPhone.setBounds(16, 345, 204, 26);
		add(txtPhone);
		txtPhone.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setBounds(246, 345, 376, 26);
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

		passwordField = new JPasswordField();
		passwordField.setBounds(16, 165, 200, 26);
		add(passwordField);

		passwordRepeatField = new JPasswordField();
		passwordRepeatField.setBounds(246, 165, 204, 26);
		add(passwordRepeatField);

		lblUpdateTooltip = new JLabel("*Leave empty for no changes");
		lblUpdateTooltip.setForeground(Color.GRAY);
		lblUpdateTooltip.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblUpdateTooltip.setBounds(16, 186, 218, 16);
		add(lblUpdateTooltip);

		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3, 0, 3, 1);
		spinnerAccLvl = new JSpinner(spinnerModel);
		spinnerAccLvl.setBounds(16, 405, 33, 26);
		add(spinnerAccLvl);
		
		txtCpr.addCaretListener(this);
		txtName.addCaretListener(this);
		txtUsername.addCaretListener(this);
		passwordField.addCaretListener(this);
		passwordRepeatField.addCaretListener(this);
		txtAddress.addCaretListener(this);
		txtUsername.addCaretListener(this);
		txtZipCode.addCaretListener(this);
		txtPhone.addCaretListener(this);
		txtEmail.addCaretListener(this);
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
		lblCity.setText("City: " + employee.getCity().getName().trim());
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
		lblCity.setText("City: ");
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
		System.out.println("start");
		if (txtCpr.getText().trim().length() != 10 || !txtCpr.getText().trim().matches("[0-9]+"))
			return false;
		System.out.println("a1");
		if (txtUsername.getText().trim().isEmpty())
			return false;
		System.out.println("a1");
		if (txtName.getText().trim().isEmpty())
			return false;
		System.out.println("a1");
		if (passwordField.getPassword().length==0||!new String(passwordField.getPassword()).equals(new String(passwordRepeatField.getPassword())))
			return false;
		System.out.println("a1");
		if (txtAddress.getText().trim().isEmpty())
			return false;
		System.out.println("a1");
		if (!txtZipCode.getText().trim().matches("[0-9]+"))
			return false;
		System.out.println("a1");
		if (txtAddress.getText().trim().isEmpty())
			return false;
		System.out.println("a1");
		if (lblCity.getText().trim().equals("City:"))
			return false;
		System.out.println("a1");
		if (txtPhone.getText().trim().isEmpty() || !txtPhone.getText().trim().matches("[0-9]+"))
			return false;
		System.out.println("a1");
		if (txtEmail.getText().trim().isEmpty() || !txtEmail.getText().trim().contains("@"))
			return false;
		System.out.println("a1");
		System.out.println("end");
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_submit) {
			String cpr = txtCpr.getText().trim();
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
			lblCity.setText((c == null) ? "City: " : "City: " + c.getName());
		} else if (e.getSource() == txtCpr || e.getSource() == txtName || e.getSource() == txtUsername
				|| e.getSource() == passwordField || e.getSource() == passwordRepeatField || e.getSource() == txtAddress
				|| e.getSource() == txtZipCode || e.getSource() == txtPhone || e.getSource() == txtEmail) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
