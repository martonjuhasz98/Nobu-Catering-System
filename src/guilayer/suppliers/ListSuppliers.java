package guilayer.suppliers;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.SupplierController;
import guilayer.ManagerWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.PerformListener;
import guilayer.invoices.ListInvoiceHistory.SearchWorker;
import modlayer.Invoice;
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
import javax.swing.AbstractAction;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class ListSuppliers extends JPanel implements ActionListener, MouseListener, PerformListener, CaretListener {

	private EditSupplier supplierEditor;
	private SupplierController supplierCtrl;
	private JTable table;
	private SupplierTableModel model;
	private JButton btn_search;
	private JButton btn_create;
	private JTextField txt_search;
	private boolean isSearching;
	private String lastKeyword;

	public ListSuppliers(EditSupplier editSupplier) {
		this.supplierEditor = editSupplier;
		supplierCtrl = new SupplierController();
		lastKeyword = "";
		isSearching = false;

		editSupplier.addPerformListener(this);

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		model = new SupplierTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		add(txt_search);
		txt_search.setColumns(10);

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

		AbstractAction delete = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(ListSuppliers.this, "Are you sure?", "Deleting supplier", JOptionPane.YES_NO_OPTION)
						!= JOptionPane.YES_OPTION) {
					return;
				}
				
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				Supplier supplier = model.getItem(modelRowIndex);

				if (!supplierCtrl.deleteSupplier(supplier)) {
					JOptionPane.showMessageDialog(ListSuppliers.this, "An error occured while deleting the Supplier!",
							"Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(ListSuppliers.this, "The Supplier was successfully deleted!", "Success!",
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
		
		reset();
	}
	private void reset() {
		model.setItems(supplierCtrl.getSuppliers());
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
			supplierEditor.create();
			setVisible(false);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Supplier supplier = model.getItem(modelRowIndex);

			supplierEditor.update(supplier);
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
		model.setItems(supplierCtrl.getSuppliers());
		setVisible(true);
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

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
	public class SearchWorker extends SwingWorker<ArrayList<Supplier>, Void> {
		private String keyword;

		public SearchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Supplier> doInBackground() throws Exception {
			// Start
			return supplierCtrl.searchSuppliers(keyword);
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
