package guilayer.employees;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ctrllayer.EmployeeController;
import guilayer.ManagerWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.PerformListener;
import guilayer.inventory.ListInventory;
import modlayer.Employee;
import modlayer.Unit;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class ListEmployees extends JPanel implements ActionListener, MouseListener, PerformListener, CaretListener {

	private EditEmployee employeeEditor;
	private EmployeeController employeeCtrl;
	private JTable table;
	private EmployeeTableModel model;
	private JButton btn_search;
	private JButton btn_create;
	private JTextField txt_search;

	public ListEmployees(EditEmployee editEmployee) {
		this.employeeEditor = editEmployee;
		employeeCtrl = new EmployeeController();

		editEmployee.addPerformListener(this);

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		model = new EmployeeTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		add(txt_search);
		txt_search.setColumns(10);

		btn_search = new JButton("Search");
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);

		btn_create = new JButton("Create");
		btn_create.setBounds(717, 3, 73, 23);
		add(btn_create);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 28, 780, 473);
		add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		model.setEmployees(employeeCtrl.getEmployees());
		table.setModel(model);
		scrollPane.setViewportView(table);

		AbstractAction delete = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				Employee employee = model.getEmployeeAt(modelRowIndex);

				if (!employeeCtrl.deleteEmployee(employee)) {
					JOptionPane.showMessageDialog(ListEmployees.this, "An error occured while deleting the Employee!",
							"Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(ListEmployees.this, "The Employee was successfully deleted!", "Success!",
						JOptionPane.INFORMATION_MESSAGE);
				model.setEmployees(employeeCtrl.getEmployees());
			}
		};

		ButtonColumn btnColumn = new ButtonColumn(table, delete, model.getColumnCount() - 1);
		btnColumn.setMnemonic(KeyEvent.VK_D);

		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);
	}

	private void searchInventory() {

		String keyword = txt_search.getText().trim();
		model.setEmployees(employeeCtrl.searchEmployees(keyword));
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchInventory();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			searchInventory();
		}
		if (e.getSource() == btn_create) {
			setVisible(false);
			employeeEditor.createEmployee();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Employee employee = model.getEmployeeAt(modelRowIndex);

			setVisible(false);
			employeeEditor.updateEmployee(employee);
		}
	}
	@Override
	public void performed() {
		model.setEmployees(employeeCtrl.getEmployees());
		setVisible(true);
	}

	@Override
	public void cancelled() {
		setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	private class EmployeeTableModel extends AbstractTableModel {

		private String[] columns = new String[] { "CPR", "Name", "Username", "Address", "Zip code", "City", "Phone", "Email", "Access", "" };
		private ArrayList<Employee> employees;

		public EmployeeTableModel() {
			this(new ArrayList<Employee>());
		}

		public EmployeeTableModel(ArrayList<Employee> employees) {
			this.employees = employees;
			update();
		}

		@Override
		public int getRowCount() {
			return employees.size();
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {

			Employee employee = employees.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return employee.getCpr();
			case 1:
				return employee.getName();
			case 2:
				return employee.getUsername();
			case 3:
				return employee.getAddress();
			case 4:
				return employee.getCity().getZipCode();
			case 5:
				return employee.getCity().getName();
			case 6:
				return employee.getPhone();
			case 7:
				return employee.getEmail();
			case 8:
				return employee.getAccessLevel();
			case 9:
				return "Delete";
			}

			return null;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}

		@Override
		public Class getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			case 3:
				return String.class;
			case 4:
				return String.class;
			case 5:
				return String.class;
			case 6:
				return String.class;
			case 7:
				return String.class;
			case 8:
				return Integer.class;
			case 9:
				return JButton.class;
			}

			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == getColumnCount() - 1;
		}

		public void update() {
			fireTableDataChanged();
		}

		public Employee getEmployeeAt(int rowIndex) {
			return employees.get(rowIndex);
		}

		public void setEmployees(ArrayList<Employee> employees) {
			this.employees = employees;
			update();
		}
	}
}
