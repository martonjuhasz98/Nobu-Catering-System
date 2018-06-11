package guilayer.suppliers;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.SupplierController;
import guilayer.ManagerWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import modlayer.Supplier;

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
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class ListSuppliers extends NavigationPanel implements ActionListener, MouseListener, PerformListener, CaretListener {

	private EditSupplier supplierEditor;
	private SupplierController supplierCtrl;
	private JTable table;
	private SupplierTableModel model;
	private JButton btn_search;
	private JButton btn_create;
	private JTextField txt_search;
	private boolean fetchingData;
	private String lastKeyword;
	private ButtonColumn btn_delete;
	
	public ListSuppliers(EditSupplier editSupplier) {
		super();
		
		this.supplierEditor = editSupplier;
		
		supplierCtrl = new SupplierController();
		lastKeyword = "";
		fetchingData = false;

		editSupplier.addPerformListener(this);

		initialize();
	}

	private void initialize() {

		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		model = new SupplierTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		add(txt_search);

		btn_search = new JButton("Search");
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);

		btn_create = new JButton("Create");
		btn_create.setBounds(707, 3, 73, 23);
		add(btn_create);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 28, 770, 450);
		add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrollPane.setViewportView(table);

		btn_delete = new ButtonColumn(table, model.getColumnCount() - 1, this);
		btn_delete.setMnemonic(KeyEvent.VK_DELETE);

		reset();
		
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);
	}
	@Override
	public void prepare() {
		new FetchWorker().execute();
	}
	@Override
	public void reset() {
		txt_search.setText("");
		lastKeyword = "";
		fetchingData = false;
	}
	@Override
	public void performed() {
		prepare();
		setVisible(true);
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
	//Funcionalities
	private void deleteSupplier(Supplier supplier) {
		String message, title;
		int messageType;
		
		if (!supplierCtrl.deleteSupplier(supplier)) {
			message = "An error occured while deleting the Supplier!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Supplier was successfully deleted!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			prepare();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void searchSuppliers() {
		if (fetchingData) return;
		
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) return;
		
		fetchingData = true;
		lastKeyword = keyword;
		
		new FetchWorker(keyword).execute();
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_search) {
			searchSuppliers();
		} else if (source == btn_delete ) {
			if (JOptionPane.showConfirmDialog(ListSuppliers.this, "Are you sure?", "Deleting supplier", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			
			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			Supplier supplier = model.getItem(modelRowIndex);

			deleteSupplier(supplier);
		} else if (source == btn_create) {
			supplierEditor.openToCreate();
			setVisible(false);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Supplier supplier = model.getItem(modelRowIndex);

			supplierEditor.openToUpdate(supplier);
			setVisible(false);
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchSuppliers();
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	//Classes
	private class SupplierTableModel extends ItemTableModel<Supplier> {

		public SupplierTableModel() {
			super();
			
			columns = new String[] { "CVR", "Name", "Address", "Zip code", "Citt", "Phone", "Email", "" };
		}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Supplier supplier = items.get(rowIndex);

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
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == getColumnCount() - 1;
		}
	}
	private class FetchWorker extends SwingWorker<ArrayList<Supplier>, Void> {
		
		private String keyword;

		public FetchWorker() {
			this("");
		}
		public FetchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Supplier> doInBackground() throws Exception {
			return keyword.isEmpty()
					? supplierCtrl.getSuppliers()
					: supplierCtrl.searchSuppliers(keyword);
		}
		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchSuppliers();
		}
	}
}
