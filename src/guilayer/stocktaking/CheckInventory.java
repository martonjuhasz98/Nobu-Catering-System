package guilayer.stocktaking;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.ItemController;
import guilayer.ManagerWindow;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.NavigationPanel;
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

public class CheckInventory extends NavigationPanel
		implements ActionListener, CaretListener, ListSelectionListener, TableModelListener {

	private ItemController itemCtrl;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_inventory;
	private InventoryTableModel mdl_inventory;
	private JButton btn_add;
	private JButton btn_remove;
	private JTable tbl_stocktaking;
	private StocktakingTableModel mdl_stocktaking;
	private JButton btn_confirm;
	private boolean fetchingData;
	private String lastKeyword;

	public CheckInventory() {
		super();
		
		itemCtrl = new ItemController();

		initialize();
	}
	//Layout
	private void initialize() {

		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		mdl_inventory = new InventoryTableModel();
		mdl_stocktaking = new StocktakingTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 12, 142, 20);
		add(txt_search);

		btn_search = new JButton("Search");
		btn_search.setBounds(160, 11, 75, 20);
		add(btn_search);

		btn_confirm = new JButton("Confirm");
		btn_confirm.setBounds(688, 11, 75, 23);
		add(btn_confirm);

		JScrollPane scrlPane_inventory = new JScrollPane();
		scrlPane_inventory.setBounds(10, 42, 330, 440);
		add(scrlPane_inventory);

		tbl_inventory = new JTable();
		tbl_inventory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_inventory.getTableHeader().setReorderingAllowed(false);
		tbl_inventory.setAutoCreateRowSorter(true);
		tbl_inventory.setModel(mdl_inventory);
		scrlPane_inventory.setViewportView(tbl_inventory);

		btn_add = new JButton("Add");
		btn_add.setBounds(350, 236, 73, 23);
		add(btn_add);

		btn_remove = new JButton("Remove");
		btn_remove.setBounds(350, 270, 73, 23);
		add(btn_remove);

		JScrollPane scrlPane_stocktaking = new JScrollPane();
		scrlPane_stocktaking.setBounds(433, 42, 330, 440);
		add(scrlPane_stocktaking);

		tbl_stocktaking = new JTable();
		tbl_stocktaking.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_stocktaking.getTableHeader().setReorderingAllowed(false);
		tbl_stocktaking.setAutoCreateRowSorter(true);
		tbl_stocktaking.setModel(mdl_stocktaking);
		scrlPane_stocktaking.setViewportView(tbl_stocktaking);

		reset();
		
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_confirm.addActionListener(this);
		tbl_inventory.getSelectionModel().addListSelectionListener(this);
		tbl_stocktaking.getSelectionModel().addListSelectionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		mdl_stocktaking.addTableModelListener(this);
	}
	@Override
	public void prepare() {
		new FetchWorker().execute();
	}
	@Override
	public void reset() {
		mdl_stocktaking.setItems(new ArrayList<Item>());

		txt_search.setText("");
		lastKeyword = "";
		fetchingData = false;
		
		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
		btn_confirm.setEnabled(false);
	}
	//Functionalities
	private void createStocktaking() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Finalize stock-taking",
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}


		if (!itemCtrl.createStocktaking(mdl_stocktaking.getItems())) {
			JOptionPane.showMessageDialog(this, "An error occured while creating the Stock-taking!", "Error!",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "The Stock-taking was successfully created!", "Success!",
				JOptionPane.INFORMATION_MESSAGE);
		reset();
	}
	private void addToStocktaking() {
		int[] selection = tbl_inventory.getSelectedRows();

		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_inventory.convertRowIndexToModel(selection[i]);
			mdl_stocktaking.addItem(mdl_inventory.getItem(selection[i]));
		}
	}
	private void removeFromStocktaking() {
		int[] selection = tbl_stocktaking.getSelectedRows();

		for (int i = selection.length; i >= 0; i--) {
			selection[i] = tbl_stocktaking.convertRowIndexToModel(selection[i]);
			mdl_stocktaking.removeItem(mdl_stocktaking.getItem(selection[i]));
		}
	}
	private void searchInventory() {
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
			searchInventory();
		} else if (source == btn_add) {
			addToStocktaking();
		} else if (source == btn_remove) {
			removeFromStocktaking();
		} else if (source == btn_confirm) {
			createStocktaking();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchInventory();
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
		if (e.getSource() == mdl_stocktaking) {
			boolean empty = mdl_stocktaking.getItems().isEmpty();
			btn_confirm.setEnabled(!empty);
		}
	}
	//Classes
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
				getItem(rowIndex).setQuantity(quantity);
			} catch (Exception e) {
			}
		}
	}
	private class FetchWorker extends SwingWorker<ArrayList<Item>, Void> {
		
		private String keyword;

		public FetchWorker() {
			this("");
		}
		public FetchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Item> doInBackground() throws Exception {
			return keyword.isEmpty()
					? itemCtrl.getItems()
					: itemCtrl.searchItems(keyword);
		}
		@Override
		protected void done() {
			try {
				mdl_inventory.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchInventory();
		}
	}
}
