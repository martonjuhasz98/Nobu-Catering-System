package guilayer.inventory;

import java.awt.Label;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ctrllayer.ItemController;
import guilayer.ManagerWindow;
import guilayer.essentials.PerformPanel;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class EditItem extends PerformPanel implements ActionListener, CaretListener, ItemListener {

	private ItemController itemCtrl;
	private Item item;
	private JTextField txt_barcode;
	private JTextField txt_name;
	private JSpinner spr_quantity;
	private JComboBox<Unit> cmb_unit;
	private JComboBox<ItemCategory> cmb_category;
	private JTextField txt_category;
	private JRadioButton rdbtn_selectCategory;
	private JRadioButton rdbtn_createCategory;
	private ButtonGroup rdbtn_group;
	private JButton btn_submit;
	private JButton btn_cancel;
	private boolean creatingCategory;
	private boolean creatingItem;
	
	public EditItem() {
		super();
		
		itemCtrl = new ItemController();
		
		initialize();
	}
	//Layout
	private void initialize() {
		
		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);
		
		Label lbl_barcode = new Label("Barcode *");
		lbl_barcode.setBounds(16, 16, 129, 22);
		add(lbl_barcode);
		
		txt_barcode = new JTextField();
		txt_barcode.setBounds(16, 44, 376, 20);
		add(txt_barcode);
		
		Label lbl_name = new Label("Name *");
		lbl_name.setBounds(16, 75, 129, 22);
		add(lbl_name);
		
		txt_name = new JTextField();
		txt_name.setBounds(16, 103, 376, 20);
		add(txt_name);
		
		Label lbl_quantity = new Label("Quantity");
		lbl_quantity.setBounds(16, 134, 197, 22);
		add(lbl_quantity);
		
		spr_quantity = new JSpinner(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(1)));
		spr_quantity.setBounds(16, 162, 310, 20);
		add(spr_quantity);
		
		Label lbl_unit = new Label("Unit *");
		lbl_unit.setBounds(333, 134, 59, 22);
		add(lbl_unit);
		
		cmb_unit = new JComboBox<Unit>();
		cmb_unit.setBounds(333, 162, 59, 20);
		add(cmb_unit);
		
		Label lbl_category = new Label("Category *");
		lbl_category.setBounds(16, 193, 197, 22);
		add(lbl_category);
		
		rdbtn_group = new ButtonGroup();
		
		rdbtn_selectCategory = new JRadioButton("Select from existing");
		rdbtn_selectCategory.setBounds(16, 221, 376, 19);
		rdbtn_group.add(rdbtn_selectCategory);
		add(rdbtn_selectCategory);
		
		cmb_category = new JComboBox<ItemCategory>();
		cmb_category.setBounds(16, 247, 376, 20);
		add(cmb_category);
		
		rdbtn_createCategory = new JRadioButton("Create new");
		rdbtn_createCategory.setBounds(16, 274, 376, 20);
		rdbtn_group.add(rdbtn_createCategory);
		add(rdbtn_createCategory);
		
		txt_category = new JTextField();
		txt_category.setBounds(16, 301, 376, 20);
		add(txt_category);
		
		btn_submit = new JButton("Create");
		btn_submit.setBounds(520, 444, 122, 32);
		add(btn_submit);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(654, 444, 122, 32);
		add(btn_cancel);
		
		reset();
		
		txt_barcode.addCaretListener(this);
		txt_name.addCaretListener(this);
		txt_category.addCaretListener(this);
		cmb_unit.addItemListener(this);
		cmb_category.addItemListener(this);
		rdbtn_selectCategory.addItemListener(this);
		rdbtn_createCategory.addItemListener(this);
		btn_submit.addActionListener(this);
		btn_cancel.addActionListener(this);
	}
	@Override
	public void prepare() {
		cmb_unit.setModel(new DefaultComboBoxModel(itemCtrl.getUnits().toArray()));
		cmb_unit.setSelectedIndex(-1);
		cmb_category.setModel(new DefaultComboBoxModel(itemCtrl.getCategories().toArray()));
		cmb_category.setSelectedIndex(-1);
	}
	@Override
	public void reset() {
		item = null;
		creatingCategory = false;
		creatingItem = true;
		
		txt_barcode.setText("");
		txt_barcode.setEnabled(true);
		txt_name.setText("");
		spr_quantity.setValue(new Double(0.0));
		rdbtn_selectCategory.setSelected(true);
		txt_category.setText("");
		txt_category.setEnabled(false);
		
		btn_submit.setText("Create");
		btn_submit.setEnabled(false);
	}
	public void openToCreate() {
		open();
	}
	public void openToUpdate(Item item) {
		open();
		
		this.item = item;
		creatingCategory = false;
		creatingItem = false;
		
		txt_barcode.setText(item.getBarcode());
		txt_barcode.setEnabled(false);
		txt_name.setText(item.getName());
		spr_quantity.setValue(item.getQuantity());
		cmb_unit.setSelectedItem(item.getUnit());
		cmb_category.setSelectedItem(item.getCategory());
		
		btn_submit.setText("Update");
	}
	//Functionalities
	private void createItem(String barcode, String name, double quantity, Unit unit, ItemCategory category) {
		String message, title;
		int messageType;
		
		if (!itemCtrl.createItem(barcode, name, quantity, unit, category)) {
			message = "An error occured while creating the Item!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Item was successfully created!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			triggerPerformListeners();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void updateItem(Item item) {
		String message, title;
		int messageType;
		
		if (!itemCtrl.updateItem(item)) {
			message = "An error occured while updating the Item!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Item was successfully updated!";
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
	private ItemCategory getCategory() {
		ItemCategory category = null;
		
		if (creatingCategory) {
			String name = txt_category.getText().trim();
			if (!name.isEmpty()) {
				category = new ItemCategory();
				category.setName(name);
			}
		} else {
			category = (ItemCategory)cmb_category.getSelectedItem();
		}
		
		return category;
	}
	private boolean isFilled() {
		if (!txt_barcode.getText().matches("[0-9]+"))
			return false;
		if (txt_name.getText().trim().isEmpty())
			return false;
		if (cmb_unit.getSelectedIndex() < 0)
			return false;
		if (getCategory() == null)
			return false;
		
		return true;
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_submit) {
			String message = creatingItem ?  "Creating" : "Updating" + " an item";
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", message, JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			
			String barcode = txt_barcode.getText().trim();
			String name = txt_name.getText().trim();
			double quantity = (Double)spr_quantity.getValue();
			Unit unit = (Unit)cmb_unit.getSelectedItem();
			ItemCategory category = getCategory();
			
			if (creatingItem) {
				createItem(barcode, name, quantity, unit, category);
			} else {
				item.setBarcode(barcode);
				item.setName(name);
				item.setQuantity(quantity);
				item.setUnit(unit);
				item.setCategory(category);
				
				updateItem(item);
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
		} else if (source == cmb_unit || source == cmb_category) {
			btn_submit.setEnabled(isFilled());
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		final Object source = e.getSource();
		if (source == txt_barcode || source == txt_name || source == txt_category) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
