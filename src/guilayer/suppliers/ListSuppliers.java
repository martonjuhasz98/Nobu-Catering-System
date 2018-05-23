package guilayer.suppliers;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ctrllayer.SupplierController;
import guilayer.MainWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.EditListener;
import guilayer.inventory.ListInventory;
import modlayer.Supplier;
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

public class ListSuppliers extends JPanel implements ActionListener, MouseListener, EditListener, CaretListener {

	private EditSupplier supplierEditor;
	private SupplierController supplierCtrl;
	private JTable table;
	private SupplierTableModel model;
	private JButton btn_search;
	private JButton btn_create;
	private JTextField txt_search;

	public ListSuppliers(EditSupplier editSupplier) {
		this.supplierEditor = editSupplier;
		supplierCtrl = new SupplierController();

		editSupplier.addEditListener(this);

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setBounds(0, 0, MainWindow.contentWidth, MainWindow.totalHeight);

		model = new SupplierTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		add(txt_search);
		txt_search.setColumns(10);

		btn_search = new JButton("Search");
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);

		btn_create = new JButton("Create");
		btn_create.setBounds(517, 3, 73, 23);
		add(btn_create);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 28, 580, 473);
		add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		model.setSuppliers(supplierCtrl.getSuppliers());
		table.setModel(model);
		scrollPane.setViewportView(table);

		AbstractAction delete = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				Supplier supplier = model.getSupplierAt(modelRowIndex);

				if (!supplierCtrl.deleteSupplier(supplier)) {
					JOptionPane.showMessageDialog(ListSuppliers.this, "An error occured while deleting the Supplier!",
							"Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(ListSuppliers.this, "The Supplier was successfully deleted!", "Success!",
						JOptionPane.INFORMATION_MESSAGE);
				model.setSuppliers(supplierCtrl.getSuppliers());
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
		model.setSuppliers(supplierCtrl.searchSuppliers(keyword));
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
			supplierEditor.createSupplier();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Supplier supplier = model.getSupplierAt(modelRowIndex);

			setVisible(false);
			supplierEditor.updateSupplier(supplier);
		}
	}

	@Override
	public void created() {
		model.setSuppliers(supplierCtrl.getSuppliers());
		setVisible(true);
	}

	@Override
	public void updated() {
		model.setSuppliers(supplierCtrl.getSuppliers());
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

	private class SupplierTableModel extends AbstractTableModel {

		private String[] columns = new String[] { "CVR", "Name", "Address", "Zip code", "Name", "Phone", "Email", "" };
		private ArrayList<Supplier> suppliers;

		public SupplierTableModel() {
			this(new ArrayList<Supplier>());
		}

		public SupplierTableModel(ArrayList<Supplier> suppliers) {
			this.suppliers = suppliers;
			update();
		}

		@Override
		public int getRowCount() {
			return suppliers.size();
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {

			Supplier supplier = suppliers.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return supplier.getCvr();
			case 1:
				return supplier.getName();
			case 2:
				return supplier.getAddress();
			case 3:
				return supplier.getCity().getZipCode();
			case 4:
				return supplier.getCity().getName();
			case 5:
				return supplier.getPhone();
			case 6:
				return supplier.getEmail();
			case 7:
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

		public Supplier getSupplierAt(int rowIndex) {
			return suppliers.get(rowIndex);
		}

		public void setSuppliers(ArrayList<Supplier> suppliers) {
			this.suppliers = suppliers;
			update();
		}
	}
}
