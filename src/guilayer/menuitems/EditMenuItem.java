package guilayer.menuitems;

import java.awt.Label;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ctrllayer.MenuItemController;
import ctrllayer.ItemController;
import guilayer.ManagerWindow;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.PerformPanel;
import modlayer.Ingredient;
import modlayer.MenuItem;
import modlayer.MenuItemCategory;
import modlayer.Item;

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
	private InventoryTableModel mdl_inventory;
	private JButton btn_add;
	private JButton btn_remove;
	private JTable tbl_menuItem;
	private MenuItemTableModel mdl_menuItem;
	private JButton btn_submit;
	private JButton btn_cancel;
	private boolean creatingMenuItem;
	private MenuItem menuItem;
	private JTextField txtId;
	private JTextField txtName;
	private JTextField txt_category;
	private JTextField txtPrice;
	private ButtonGroup rdbtn_group;
	private JComboBox<MenuItemCategory> cmb_category;
	private JScrollPane scrlPane_menuItem;
	private JRadioButton rdbtn_createCategory;
	private JRadioButton rdbtn_selectCategory;
	private boolean creatingCategory;
	private boolean fetchingData;
	private String lastKeyword;

	public EditMenuItem() {
		super();
		
		menuItemCtrl = new MenuItemController();
		itemCtrl = new ItemController();
		
		creatingMenuItem = false;
		creatingCategory = false;
		fetchingData = false;
		lastKeyword = "";

		initialize();
	}
	//Layout
	private void initialize() {

		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);

		mdl_inventory = new InventoryTableModel();
		mdl_menuItem = new MenuItemTableModel();

		Label lbl_id = new Label("ID *");
		lbl_id.setBounds(29, 16, 83, 22);
		add(lbl_id);

		txtId = new JTextField();
		txtId.setBounds(29, 44, 83, 20);
		add(txtId);

		Label lbl_name = new Label("Name *");
		lbl_name.setBounds(29, 76, 83, 22);
		add(lbl_name);

		txtName = new JTextField();
		txtName.setBounds(29, 104, 316, 20);
		add(txtName);
		
		Label lbl_price = new Label("Price *");
		lbl_price.setBounds(29, 136, 83, 22);
		add(lbl_price);

		txtPrice = new JTextField();
		txtPrice.setBounds(29, 164, 83, 20);
		add(txtPrice);

		txt_search = new JTextField();
		txt_search.setBounds(29, 224, 179, 20);
		add(txt_search);

		btn_search = new JButton("Search");
		btn_search.setBounds(208, 224, 73, 20);
		add(btn_search);


		JScrollPane scrlPane_inventory = new JScrollPane();
		scrlPane_inventory = new JScrollPane();
		scrlPane_inventory.setBounds(23, 256, 316, 167);
		add(scrlPane_inventory);

		tbl_inventory = new JTable();
		tbl_inventory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_inventory.getTableHeader().setReorderingAllowed(false);
		tbl_inventory.setAutoCreateRowSorter(true);
		tbl_inventory.setModel(mdl_inventory);
		scrlPane_inventory.setViewportView(tbl_inventory);

		btn_add = new JButton("Add");
		btn_add.setBounds(347, 307, 73, 23);
		add(btn_add);

		btn_remove = new JButton("Remove");
		btn_remove.setBounds(347, 341, 73, 23);
		add(btn_remove);

		scrlPane_menuItem = new JScrollPane();
		scrlPane_menuItem.setBounds(432, 256, 316, 167);
		add(scrlPane_menuItem);

		tbl_menuItem = new JTable();
		tbl_menuItem.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_menuItem.getTableHeader().setReorderingAllowed(false);
		tbl_menuItem.setAutoCreateRowSorter(true);
		tbl_menuItem.setModel(mdl_menuItem);
		scrlPane_menuItem.setViewportView(tbl_menuItem);

		btn_submit = new JButton("Create");
		btn_submit.setBounds(494, 427, 122, 32);
		add(btn_submit);

		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(626, 427, 122, 32);
		add(btn_cancel);
		
		rdbtn_group = new ButtonGroup();
		
		rdbtn_selectCategory = new JRadioButton("Select from existing");
		rdbtn_selectCategory.setSelected(true);
		rdbtn_selectCategory.setBounds(432, 84, 316, 19);
		rdbtn_group.add(rdbtn_selectCategory);
		add(rdbtn_selectCategory);

		cmb_category = new JComboBox<MenuItemCategory>();
		cmb_category.setSelectedIndex(-1);
		cmb_category.setBounds(432, 110, 316, 20);
		add(cmb_category);

		rdbtn_createCategory = new JRadioButton("Create new");
		rdbtn_createCategory.setBounds(432, 137, 316, 20);
		rdbtn_group.add(rdbtn_createCategory);
		add(rdbtn_createCategory);
		
		Label lbl_category = new Label("Category *");
		lbl_category.setBounds(432, 56, 83, 22);
		add(lbl_category);

		txt_category = new JTextField();
		txt_category.setBounds(432, 164, 316, 20);
		add(txt_category);

		reset();
		
		btn_search.addActionListener(this);
		btn_add.addActionListener(this);
		btn_remove.addActionListener(this);
		btn_submit.addActionListener(this);
		btn_cancel.addActionListener(this);
		tbl_inventory.getSelectionModel().addListSelectionListener(this);
		tbl_menuItem.getSelectionModel().addListSelectionListener(this);
		mdl_menuItem.addTableModelListener(this);
		txt_search.addCaretListener(this);
		txtName.addCaretListener(this);
		txtId.addCaretListener(this);
		txtPrice.addCaretListener(this);
		txt_category.addCaretListener(this);
		cmb_category.addItemListener(this);
		rdbtn_selectCategory.addItemListener(this);
		rdbtn_createCategory.addItemListener(this);
	}
	@Override
	public void prepare() {
		new FetchWorker().execute();
		
		cmb_category.setModel(new DefaultComboBoxModel(menuItemCtrl.getCategories().toArray()));
		cmb_category.setSelectedIndex(-1);
	}
	@Override
	public void reset() {
		txtId.setText("");
		txtName.setText("");
		txtPrice.setText("");
		mdl_menuItem.setItems(new ArrayList<Ingredient>());
		menuItem = null;
		creatingCategory = false;
		creatingMenuItem = true;

		rdbtn_selectCategory.setSelected(true);
		cmb_category.setSelectedIndex(-1);
		txt_category.setText("");
		txt_category.setEnabled(false);

		txt_search.setText("");
		btn_add.setEnabled(false);
		btn_remove.setEnabled(false);
		btn_submit.setEnabled(false);
	}
	public void openToCreate() {
		open();
	}
	public void openToUpdate(MenuItem menuItem) {
		open();
		
		this.menuItem = menuItem;
		creatingMenuItem = false;

		txtId.setText(String.valueOf(menuItem.getId()));
		txtName.setText(menuItem.getName());
		txtPrice.setText(String.valueOf(new DecimalFormat("#0.00").format(menuItem.getPrice())));
		cmb_category.setSelectedItem(menuItem.getCategory());
		for (Ingredient i : menuItem.getIngredients())
			mdl_menuItem.addItem(i);
		btn_submit.setText("Update");
		btn_submit.setEnabled(true);
	}
	//Functionalities
	private void createMenuItem(int id, String name, double price, MenuItemCategory category, ArrayList<Ingredient> ingredients) {
		String message, title;
		int messageType;
		
		if (!menuItemCtrl.createMenuItem(id, name, price, category, ingredients)) {
			message = "An error occured while creating the Menu Item!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Menu Item was successfully created!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			triggerPerformListeners();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void updateMenuItem(MenuItem menuItem) {
		String message, title;
		int messageType;
		
		if (!menuItemCtrl.updateMenuItem(menuItem)) {
			message = "An error occured while updating the Menu Item!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Menu Item was successfully updated!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			triggerPerformListeners();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void cancel() {
		triggerCancelListeners();
		close();
	}
	private MenuItemCategory getCategory() {
		MenuItemCategory category = null;
		
		if (creatingCategory) {
			String name = txt_category.getText().trim();
			if (!name.isEmpty()) {
				category = new MenuItemCategory();
				category.setName(name);
			}
		} else {
			category = (MenuItemCategory)cmb_category.getSelectedItem();
		}
		
		return category;
	}
	private void addToMenuItem() {
		int[] selection = tbl_inventory.getSelectedRows();
		Ingredient ingredient;

		for (int i = 0; i < selection.length; i++) {
			selection[i] = tbl_inventory.convertRowIndexToModel(selection[i]);
			ingredient = new Ingredient();
			ingredient.setMenuItem(new MenuItem());
			ingredient.setItem((Item) mdl_inventory.getItem(selection[i]));
			ingredient.setQuantity(1.0);
			ingredient.setWaste(0);
			mdl_menuItem.addItem(ingredient);
		}
	}
	private void removeFromMenuItem() {
		int[] selection = tbl_menuItem.getSelectedRows();

		for (int i = selection.length; i >= 0; i--) {
			selection[i] = tbl_menuItem.convertRowIndexToModel(selection[i]);
			mdl_menuItem.removeItem(mdl_menuItem.getItem(selection[i]));
		}
	}
	private boolean isFilled() {
		if (mdl_menuItem.getItems().isEmpty())
			return false;
		if (!txtId.getText().trim().matches("[0-9]+"))
			return false;
		if (txtName.getText().trim().isEmpty())
			return false;
		if (!txtPrice.getText().trim().matches("[0-9]+\\.[0-9]{2}"))
			return false;
		if (getCategory() == null)
			return false;
		
		return true;
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
			addToMenuItem();
		} else if (source == btn_remove) {
			removeFromMenuItem();
		} else if (source == btn_submit) {
			String message = creatingMenuItem ?  "Creating" : "Updating" + " a Menu Item";
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", message, JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			
			int id = Integer.valueOf(txtId.getText());
			String name = txtName.getText();
			Double price = Double.valueOf(txtPrice.getText());
			MenuItemCategory category = getCategory();
			ArrayList<Ingredient> ingredients = mdl_menuItem.getItems();
			
			if (creatingMenuItem) {
				createMenuItem(id, name, price, category, ingredients);;
			} else {
				menuItem.setId(id);
				menuItem.setName(name);
				menuItem.setPrice(price);
				menuItem.setCategory(category);
				menuItem.setIngredients(ingredients);
				
				updateMenuItem(menuItem);
			}
			close();
		} else if (source == btn_cancel) {
			cancel();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		final Object source = e.getSource();
		if (source == rdbtn_selectCategory || source == rdbtn_createCategory) {
			creatingCategory = rdbtn_createCategory.isSelected();
			cmb_category.setEnabled(!creatingCategory);
			txt_category.setEnabled(creatingCategory);

			btn_submit.setEnabled(isFilled());
		} else if (source == cmb_category) {
			btn_submit.setEnabled(isFilled());
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		final Object source = e.getSource();
		if (source == txt_search) {
			searchInventory();
		}
		if (source == txtId || source == txtName || source == txtPrice
				|| source == txt_category) {
			btn_submit.setEnabled(isFilled());
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		final ListSelectionModel source = (ListSelectionModel) e.getSource();
		boolean empty = source.isSelectionEmpty();

		if (source == tbl_inventory.getSelectionModel()) {
			btn_add.setEnabled(!empty);
		} else if (source == tbl_menuItem.getSelectionModel()) {
			btn_remove.setEnabled(!empty);
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == mdl_menuItem) {
			btn_submit.setEnabled(isFilled());
		}
	}
	//Classes
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

			columns = new String[] { "Name", "Quantity", "Unit", "Waste %", "Category" };
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
				return ingredient.getWaste();
			case 4:
				return item.getCategory().getName();
			}

			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == 1||columnIndex == 3);
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 1) {
				double quantity = (double) value;
				if (quantity > 0) {
					getItem(rowIndex).setQuantity(quantity);
				}
			} else if (columnIndex == 3) {
				double waste = (double) value;
				if (waste >= 0 && waste <= 100) {
					getItem(rowIndex).setWaste(waste);
				}
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
