package guilayer.menuitems;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ctrllayer.MenuItemController;
import ctrllayer.ItemController;
import ctrllayer.SupplierController;
import guilayer.ManagerWindow;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformPanel;
import guilayer.stocktaking.CheckInventory.SearchWorker;
import modlayer.Employee;
import modlayer.Ingredient;
import modlayer.MenuItem;
import modlayer.MenuItemCategory;
import modlayer.Item;
import modlayer.MenuItem;
import modlayer.Supplier;
import modlayer.Unit;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

public class EditMenuItem extends PerformPanel
		implements ActionListener, CaretListener, ItemListener, ListSelectionListener, TableModelListener {

	private MenuItemController menuItemCtrl;
	private ItemController itemCtrl;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable tbl_inventory;
	private InventoryTableModel inventoryModel;
	private JButton btn_add;
	private JButton btn_remove;
	private JTable tbl_menuItemItem;
	private MenuItemTableModel mdl_menuItem;
	private JButton btn_submit;
	private JButton btn_cancel;
	private boolean isCreatingMenuItem;
	private boolean isSearching;
	private boolean creatingCategory;
	private String lastKeyword;
	private MenuItem menuItem;
	private JTextField txtId;
	private JTextField txtName;
	private JTextField txt_category;
	private JTextField txtPrice;
	private ButtonGroup rdbtn_group;
	private JComboBox<MenuItemCategory> cmb_category;
	private JScrollPane scrlPane_menuItemItem;
	private JRadioButton rdbtn_createCategory;
	private JRadioButton rdbtn_selectCategory;
	private JScrollPane scrlPane_inventory;

	public EditMenuItem() {
		menuItemCtrl = new MenuItemController();
		itemCtrl = new ItemController();
		lastKeyword = "";
		isSearching = false;
		isCreatingMenuItem = false;
		creatingCategory = false;

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);

		inventoryModel = new InventoryTableModel();
		mdl_menuItem = new MenuItemTableModel();

		Label label = new Label("ID *");
		label.setFont(new Font("Dialog", Font.PLAIN, 15));
		label.setBounds(16, 16, 129, 22);
		add(label);

		txtId = new JTextField();
		txtId.setText("");
		txtId.setEnabled(true);
		txtId.setColumns(10);
		txtId.setBounds(16, 44, 83, 20);
		add(txtId);

		Label label_1 = new Label("Name *");
		label_1.setFont(new Font("Dialog", Font.PLAIN, 15));
		label_1.setBounds(116, 16, 129, 22);
		add(label_1);
		rdbtn_group = new ButtonGroup();

		txtName = new JTextField();
		txtName.setText("");
		txtName.setColumns(10);
		txtName.setBounds(16, 104, 316, 20);
		add(txtName);

		txtPrice = new JTextField();
		txtPrice.setText("");
		txtPrice.setEnabled(true);
		txtPrice.setColumns(10);
		txtPrice.setBounds(16, 164, 83, 20);
		add(txtPrice);

		txt_search = new JTextField();
		txt_search.setBounds(16, 224, 179, 20);
		add(txt_search);
		txt_search.setColumns(10);

		btn_search = new JButton("Search");
		btn_search.setBounds(195, 224, 73, 20);
		add(btn_search);

		scrlPane_inventory = new JScrollPane();
		scrlPane_inventory.setBounds(10, 256, 316, 167);
		add(scrlPane_inventory);

		tbl_inventory = new JTable();
		tbl_inventory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_inventory.getTableHeader().setReorderingAllowed(false);
		tbl_inventory.setAutoCreateRowSorter(true);
		inventoryModel.setItems(itemCtrl.getItems());
		tbl_inventory.setModel(inventoryModel);
		scrlPane_inventory.setViewportView(tbl_inventory);

		btn_add = new JButton("Add");
		btn_add.setBounds(334, 307, 73, 23);
		add(btn_add);

		btn_remove = new JButton("Remove");
		btn_remove.setBounds(334, 341, 73, 23);
		add(btn_remove);

		scrlPane_menuItemItem = new JScrollPane();
		scrlPane_menuItemItem.setBounds(419, 256, 371, 167);
		add(scrlPane_menuItemItem);

		tbl_menuItemItem = new JTable();
		tbl_menuItemItem.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_menuItemItem.getTableHeader().setReorderingAllowed(false);
		tbl_menuItemItem.setAutoCreateRowSorter(true);
		mdl_menuItem.setItems(new ArrayList<Ingredient>());
		tbl_menuItemItem.setModel(mdl_menuItem);
		scrlPane_menuItemItem.setViewportView(tbl_menuItemItem);

		btn_submit = new JButton("Create");
		btn_submit.setBounds(536, 432, 122, 32);
		add(btn_submit);

		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 432, 122, 32);
		add(btn_cancel);



		rdbtn_selectCategory = new JRadioButton("Select from existing");
		rdbtn_selectCategory.setSelected(true);
		rdbtn_selectCategory.setBounds(419, 84, 376, 19);
		rdbtn_group.add(rdbtn_selectCategory);
		add(rdbtn_selectCategory);

		cmb_category = new JComboBox<MenuItemCategory>();
		cmb_category.setSelectedIndex(-1);
		cmb_category.setBounds(419, 110, 376, 20);
		add(cmb_category);

		rdbtn_createCategory = new JRadioButton("Create new");
		rdbtn_createCategory.setBounds(419, 137, 376, 20);
		rdbtn_group.add(rdbtn_createCategory);

		add(rdbtn_createCategory);

		txt_category = new JTextField();
		txt_category.setText("");
		txt_category.setEnabled(false);
		txt_category.setColumns(10);
		txt_category.setBounds(419, 164, 376, 20);
		add(txt_category);
		
		btn_search.addActionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		btn_submit.addActionListener(this);
		btn_cancel.addActionListener(this);
		tbl_inventory.getSelectionModel().addListSelectionListener(this);
		tbl_menuItemItem.getSelectionModel().addListSelectionListener(this);
		mdl_menuItem.addTableModelListener(this);
		txt_search.addCaretListener(this);
		txtName.addCaretListener(this);
		txtId.addCaretListener(this);
		txtPrice.addCaretListener(this);
		txt_category.addCaretListener(this);
		cmb_category.addItemListener(this);
		rdbtn_selectCategory.addItemListener(this);
		rdbtn_createCategory.addItemListener(this);

		reset();
	}

	private void reset() {
		txtId.setText("");
		txtName.setText("");
		txtPrice.setText("");
		inventoryModel.setItems(itemCtrl.getItems());
		mdl_menuItem.setItems(new ArrayList<Ingredient>());
		menuItem = null;
		creatingCategory = false;
		isCreatingMenuItem = true;

		rdbtn_selectCategory.setSelected(true);
		cmb_category.setSelectedIndex(-1);
		txt_category.setText("");

		txt_search.setText("");
		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
		btn_submit.setEnabled(false);
	}

	public void create() {
		cmb_category.setModel(new DefaultComboBoxModel(menuItemCtrl.getCategories().toArray()));
		cmb_category.setSelectedIndex(-1);
		open();
	}

	public void update(MenuItem menuItem) {
		cmb_category.setModel(new DefaultComboBoxModel(menuItemCtrl.getCategories().toArray()));
		cmb_category.setSelectedIndex(-1);
		fill(menuItem);
		open();
	}

	public void fill(MenuItem menuItem) {
		this.menuItem = menuItem;
		isCreatingMenuItem = false;

		txtId.setText(String.valueOf(menuItem.getId()));
		txtName.setText(menuItem.getName());
		txtPrice.setText(String.valueOf(new DecimalFormat("#0.00").format(menuItem.getPrice())));
		cmb_category.setSelectedItem(menuItem.getCategory());
		for (Ingredient i : menuItem.getIngredients())
			mdl_menuItem.addItem(i);
		btn_submit.setText("Update");
		btn_submit.setEnabled(true);
	}

	private void open() {

		setVisible(true);
	}

	private void close() {
		setVisible(false);
		reset();
	}

	private void submit() {
		if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Creating menuItem",
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}
		int id = Integer.valueOf(txtId.getText());
		String name = txtName.getText();
		Double price = Double.valueOf(txtPrice.getText());
		MenuItemCategory menuItemCategory;
		if (!creatingCategory) {
			menuItemCategory = (MenuItemCategory) cmb_category.getSelectedItem();
		} else {
			menuItemCategory = new MenuItemCategory();
			menuItemCategory.setName(txt_category.getText().trim());
		}
		ArrayList<Ingredient> ingredients = mdl_menuItem.getItems();

		if (isCreatingMenuItem) {
			if (!menuItemCtrl.createMenuItem(id, name, price, menuItemCategory, ingredients)) {
				JOptionPane.showMessageDialog(this, "An error occured while creating the Item!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			JOptionPane.showMessageDialog(this, "The Item was successfully created!", "Success!",
					JOptionPane.INFORMATION_MESSAGE);

			triggerPerformListeners();
		} else {
			menuItem.setId(id);
			menuItem.setName(name);
			menuItem.setPrice(price);
			menuItem.setCategory(menuItemCategory);
			menuItem.setIngredients(ingredients);

			if (!menuItemCtrl.updateMenuItem(menuItem)) {
				JOptionPane.showMessageDialog(this, "An error occured while creating the Item!", "Error!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			JOptionPane.showMessageDialog(this, "The Item was successfully edited!", "Success!",
					JOptionPane.INFORMATION_MESSAGE);

			triggerPerformListeners();
		}
		close();
	}

	private void cancel() {
		triggerCancelListeners();
		close();
	}

	private boolean isFilled() {
		// Double.valueOf()
		if (mdl_menuItem.getItems().isEmpty())
			return false;
		if (!txtId.getText().trim().matches("[0-9]+"))
			return false;
		if (txtName.getText().trim().isEmpty())
			return false;
		if (!txtPrice.getText().trim().matches("[0-9]+\\.[0-9]{2}"))
			return false;
		if (!creatingCategory) {
			if (cmb_category.getSelectedIndex() < 0)
				return false;
		} else {
			if (txt_category.getText().trim().isEmpty())
				return false;
		}
		return true;
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
		// model.setItems(menuItemCtrl.searchMenuItemHistory(keyword));
		new SearchWorker(keyword).execute();
	}

	private void addToMenuItem() {
		int[] selection = tbl_inventory.getSelectedRows();
		Ingredient ingredient;

		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_inventory.convertRowIndexToModel(selection[i]);
			ingredient = new Ingredient();
			ingredient.setMenuItem(new MenuItem());
			ingredient.setItem((Item) inventoryModel.getItem(selection[i]));
			ingredient.setQuantity(1.0);
			mdl_menuItem.addItem(ingredient);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == rdbtn_selectCategory || e.getSource() == rdbtn_createCategory) {
			creatingCategory = rdbtn_createCategory.isSelected();
			cmb_category.setEnabled(!creatingCategory);
			txt_category.setEnabled(creatingCategory);

			btn_submit.setEnabled(isFilled());
		} else if (e.getSource() == cmb_category) {
			btn_submit.setEnabled(isFilled());
		}
	}

	private void removeFromMenuItem() {
		int[] selection = tbl_menuItemItem.getSelectedRows();

		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_menuItemItem.convertRowIndexToModel(selection[i]);
			mdl_menuItem.removeItem(mdl_menuItem.getItem(selection[i]));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			search();
		} else if (e.getSource() == btn_add) {
			addToMenuItem();
		} else if (e.getSource() == btn_remove) {
			removeFromMenuItem();
		} else if (e.getSource() == btn_submit) {
			submit();
		} else if (e.getSource() == btn_cancel) {
			cancel();
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			search();
		}
		if (e.getSource() == txtId || e.getSource() == txtName || e.getSource() == txtPrice || e.getSource() == txt_category ) {
			btn_submit.setEnabled(isFilled());
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel source = (ListSelectionModel) e.getSource();
		boolean empty = source.isSelectionEmpty();

		if (source == tbl_inventory.getSelectionModel()) {
			btn_add.setEnabled(!empty);
		} else if (source == tbl_menuItemItem.getSelectionModel()) {
			btn_remove.setEnabled(!empty);
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == mdl_menuItem) {
			btn_submit.setEnabled(isFilled());
		}
	}

	private class InventoryTableModel extends ItemTableModel<Item> {

		public InventoryTableModel() {
			super();

			columns = new String[] { "Name", "Unit", "Category" };
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Item item = getItem(rowIndex);

			switch (columnIndex) {
			case 0:
				return item.getName();
			case 1:
				return item.getUnit().getAbbr();
			case 2:
				return item.getCategory().getName();
			}

			return null;
		}
	}

	private class MenuItemTableModel extends ItemTableModel<Ingredient> {

		public MenuItemTableModel() {
			super();

			columns = new String[] { "Name", "Quantity", "Unit", "Category" };
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Ingredient ingredient = getItem(rowIndex);
			Item item = ingredient.getItem();

			switch (columnIndex) {
			case 0:
				return item.getName();
			case 1:
				return ingredient.getQuantity();
			case 2:
				return item.getUnit().getAbbr();
			case 3:
				return item.getCategory().getName();
			}

			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == 1) ? true : false;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 1) {
				double quantity = (double) value;
				if (quantity > 0) {
					getItem(rowIndex).setQuantity(quantity);
				}
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
				inventoryModel.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isSearching = false;
			search();
		}
	}
}
