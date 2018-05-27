package guilayer.employees;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.EmployeeController;
import guilayer.ManagerWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformListener;
import guilayer.invoices.ListInvoiceHistory.SearchWorker;
import modlayer.Employee;
import modlayer.Invoice;

import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
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
	private boolean isSearching;
	private String lastKeyword;

	public ListEmployees(EditEmployee editEmployee) {
		this.employeeEditor = editEmployee;
		employeeCtrl = new EmployeeController();
		lastKeyword = "";
		isSearching = false;

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
		model.setItems(employeeCtrl.getEmployees());
		table.setModel(model);
		scrollPane.setViewportView(table);

		AbstractAction delete = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(ListEmployees.this, "Are you sure?", "Deleting employee",
						JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					return;
				}

				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				Employee employee = model.getItem(modelRowIndex);

				if (!employeeCtrl.deleteEmployee(employee)) {
					JOptionPane.showMessageDialog(ListEmployees.this, "An error occured while deleting the Employee!",
							"Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(ListEmployees.this, "The Employee was successfully deleted!", "Success!",
						JOptionPane.INFORMATION_MESSAGE);
				reset();
			}
		};

		ButtonColumn btnColumn = new ButtonColumn(table, delete, model.getColumnCount() - 1);
		btnColumn.setMnemonic(KeyEvent.VK_D);

		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);
	}

	private void reset() {
		model.setItems(employeeCtrl.getEmployees());
		txt_search.setText("");
	}

	private void search() {
		if (isSearching)
			return;
		isSearching = true;
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) {
			isSearching = false;
			return;
		}
		lastKeyword = keyword;
		// model.setItems(invoiceCtrl.searchInvoiceHistory(keyword));
		new SearchWorker(keyword).execute();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			search();
		}
		if (e.getSource() == btn_create) {
			setVisible(false);
			employeeEditor.create();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Employee employee = model.getItem(modelRowIndex);

			employeeEditor.update(employee);
			setVisible(false);
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			search();
		}
	}

	@Override
	public void performed() {
		model.setItems(employeeCtrl.getEmployees());
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

	private class EmployeeTableModel extends ItemTableModel<Employee> {

		public EmployeeTableModel() {
			super();

			columns = new String[] { "CPR", "Name", "Username", "Address", "Zip code", "City", "Phone", "Email",
					"Access", "" };
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Employee employee = items.get(rowIndex);

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
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == getColumnCount() - 1;
		}
	}

	public class SearchWorker extends SwingWorker<ArrayList<Employee>, Void> {
		private String keyword;

		public SearchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Employee> doInBackground() throws Exception {
			// Start
			return employeeCtrl.searchEmployees(keyword);
		}

		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isSearching = false;
			search();
		}
	}
}
