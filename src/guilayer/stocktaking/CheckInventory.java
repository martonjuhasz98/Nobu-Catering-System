package guilayer.stocktaking;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.ItemController;
import ctrllayer.SessionSingleton;
import guilayer.ManagerWindow;
import guilayer.interfaces.ItemTableModel;
import modlayer.Employee;
import modlayer.Item;

import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class CheckInventory extends JPanel
		implements ActionListener, CaretListener, ListSelectionListener, TableModelListener {

	private ItemController itemCtrl;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_inventory;
	private InventoryTableModel mdl_inventory;
	private JButton btn_add;
	private JButton btn_remove;
	private JTable tbl_stocktaking;
	private StocktakingTableModel model;
	private JButton btn_confirm;
	private boolean isSearching;
	private String lastKeyword;

	public CheckInventory() {
		itemCtrl = new ItemController();
		lastKeyword = "";
		isSearching = false;

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		mdl_inventory = new InventoryTableModel();
		model = new StocktakingTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		txt_search.setColumns(10);
		add(txt_search);

		btn_search = new JButton("Search");
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);

		btn_confirm = new JButton("Confirm");
		btn_confirm.setBounds(718, 3, 73, 23);
		add(btn_confirm);

		JScrollPane scrlPane_inventory = new JScrollPane();
		scrlPane_inventory.setBounds(10, 28, 300, 461);
		add(scrlPane_inventory);

		tbl_inventory = new JTable();
		tbl_inventory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_inventory.getTableHeader().setReorderingAllowed(false);
		tbl_inventory.setAutoCreateRowSorter(true);
		tbl_inventory.setModel(mdl_inventory);
		scrlPane_inventory.setViewportView(tbl_inventory);

		btn_add = new JButton("Add");
		btn_add.setBounds(320, 236, 73, 23);
		add(btn_add);

		btn_remove = new JButton("Remove");
		btn_remove.setBounds(320, 270, 73, 23);
		add(btn_remove);

		JScrollPane scrlPane_stocktaking = new JScrollPane();
		scrlPane_stocktaking.setBounds(400, 28, 391, 461);
		add(scrlPane_stocktaking);

		tbl_stocktaking = new JTable();
		tbl_stocktaking.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_stocktaking.getTableHeader().setReorderingAllowed(false);
		tbl_stocktaking.setAutoCreateRowSorter(true);
		tbl_stocktaking.setModel(model);
		scrlPane_stocktaking.setViewportView(tbl_stocktaking);

		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_confirm.addActionListener(this);
		tbl_inventory.getSelectionModel().addListSelectionListener(this);
		tbl_stocktaking.getSelectionModel().addListSelectionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		model.addTableModelListener(this);

		reset();
	}

	private void reset() {
		mdl_inventory.setItems(itemCtrl.getItems());
		model.setItems(new ArrayList<Item>());

		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
		btn_confirm.setEnabled(false);
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

	private void addToStocktaking() {
		int[] selection = tbl_inventory.getSelectedRows();

		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_inventory.convertRowIndexToModel(selection[i]);
			model.addItem(mdl_inventory.getItem(selection[i]));
		}
	}

	private void removeFromStocktaking() {
		int[] selection = tbl_stocktaking.getSelectedRows();

		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_stocktaking.convertRowIndexToModel(selection[i]);
			model.addItem(model.getItem(selection[i]));
		}
	}

	private void createStocktaking() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Finalize stock-taking",
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}


		if (!itemCtrl.createStocktaking(model.getItems())) {
			JOptionPane.showMessageDialog(this, "An error occured while creating the Stock-taking!", "Error!",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "The Stock-taking was successfully created!", "Success!",
				JOptionPane.INFORMATION_MESSAGE);
		reset();
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			search();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel source = (ListSelectionModel) e.getSource();
		boolean empty = source.isSelectionEmpty();

		if (source == tbl_inventory.getSelectionModel()) {
			btn_add.setEnabled(!empty);
		} else if (source == tbl_stocktaking.getSelectionModel()) {
			btn_remove.setEnabled(!empty);
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == model) {
			boolean empty = model.getItems().isEmpty();
			btn_confirm.setEnabled(!empty);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			search();
		} else if (e.getSource() == btn_add) {
			addToStocktaking();
		} else if (e.getSource() == btn_remove) {
			removeFromStocktaking();
		} else if (e.getSource() == btn_confirm) {
			createStocktaking();
		}
	}

	private class InventoryTableModel extends ItemTableModel<Item> {

		public InventoryTableModel() {
			super();

			columns = new String[] { "Name", "Quantity", "Unit", "Category" };
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Item item = items.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return item.getName();
			case 1:
				return item.getQuantity();
			case 2:
				return item.getUnit().getAbbr();
			case 3:
				return item.getCategory().getName();
			}

			return null;
		}
	}

	private class StocktakingTableModel extends InventoryTableModel {

		public StocktakingTableModel() {
			super();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			try {
				double quantity = (double) value;
				super.items.get(rowIndex).setQuantity(quantity);
			} catch (Exception e) {
			}
		}
	}

	public class SearchWorker extends SwingWorker<ArrayList<Item>, Void> {
		private String keyword;

		public SearchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Item> doInBackground() throws Exception {
			// Start
			return itemCtrl.searchItems(keyword);
		}

		@Override
		protected void done() {
			try {
				mdl_inventory.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isSearching = false;
			search();
		}
	}
}
