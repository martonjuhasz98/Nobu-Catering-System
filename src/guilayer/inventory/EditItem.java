package guilayer.inventory;

import java.awt.Font;
import java.awt.Label;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ctrllayer.ItemController;
import guilayer.MainWindow;
import guilayer.interfaces.PerformPanel;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Consumer;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class EditItem extends PerformPanel implements ActionListener, CaretListener, ItemListener {

	private ItemController itemCtrl;
	private Item item;
	private JTextField txt_barcode;
	private JTextField txt_name;
	private JButton btn_submit;
	private JButton btn_cancel;
	private SpinnerNumberModel sprm_quantity;
	private JComboBox<Unit> cmb_unit;
	private JComboBox<ItemCategory> cmb_category;
	private JTextField txt_category;
	private JRadioButton rdbtn_selectCategory;
	private JRadioButton rdbtn_createCategory;
	private ButtonGroup rdbtn_group;
	private boolean creatingCategory;
	private boolean creatingItem;
	
	public EditItem() {
		itemCtrl = new ItemController();
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setVisible(false);
		setBounds(0, 0, MainWindow.contentWidth, MainWindow.totalHeight);
		
		Label lbl_barcode = new Label("Barcode *");
		lbl_barcode.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_barcode.setBounds(16, 16, 129, 22);
		add(lbl_barcode);
		
		txt_barcode = new JTextField();
		txt_barcode.setColumns(10);
		txt_barcode.setBounds(16, 44, 376, 20);
		add(txt_barcode);
		
		Label lbl_name = new Label("Name *");
		lbl_name.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_name.setBounds(16, 75, 129, 22);
		add(lbl_name);
		
		txt_name = new JTextField();
		txt_name.setColumns(10);
		txt_name.setBounds(16, 103, 376, 20);
		add(txt_name);
		
		Label lbl_quantity = new Label("Quantity");
		lbl_quantity.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_quantity.setBounds(16, 134, 197, 22);
		add(lbl_quantity);
		
		JSpinner spr_quantity = new JSpinner();
		sprm_quantity = new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(1));
		spr_quantity.setModel(sprm_quantity);
		spr_quantity.setBounds(16, 162, 310, 20);
		add(spr_quantity);
		
		Label lbl_unit = new Label("Unit *");
		lbl_unit.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_unit.setBounds(333, 134, 59, 22);
		add(lbl_unit);
		
		cmb_unit = new JComboBox<Unit>();
		cmb_unit.setBounds(333, 162, 59, 20);
		add(cmb_unit);
		
		Label lbl_category = new Label("Category *");
		lbl_category.setFont(new Font("Dialog", Font.PLAIN, 15));
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
		txt_category.setEnabled(false);
		txt_category.setBounds(16, 301, 376, 20);
		add(txt_category);
		txt_category.setColumns(10);
		
		btn_submit = new JButton("Create");
		btn_submit.setBounds(536, 457, 122, 32);
		add(btn_submit);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 457, 122, 32);
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
	private void fill(Item item) {
		this.item = item;
		creatingCategory = false;
		creatingItem = false;
		
		txt_barcode.setText(item.getBarcode());
		txt_name.setText(item.getName());
		sprm_quantity.setValue(item.getQuantity());
		cmb_unit.setSelectedItem(item.getUnit());
		cmb_category.setSelectedItem(item.getCategory());
		
		btn_submit.setText("Update");
		btn_submit.setEnabled(true);
	}
	private void reset() {
		item = null;
		creatingCategory = false;
		creatingItem = true;
		
		txt_barcode.setText("");
		txt_name.setText("");
		sprm_quantity.setValue(new Double(0.0));
		cmb_unit.setSelectedIndex(-1);
		rdbtn_selectCategory.setSelected(true);
		cmb_category.setSelectedIndex(-1);
		txt_category.setText("");
		
		btn_submit.setText("Create");
		btn_submit.setEnabled(false);
	}
	public void createItem() {
		open();
	}
	public void updateItem(Item item) {
		open();
		fill(item);
	}
	private void open() {
		cmb_unit.setModel(new DefaultComboBoxModel(itemCtrl.getUnits().toArray()));
		cmb_unit.setSelectedIndex(-1);
		cmb_category.setModel(new DefaultComboBoxModel(itemCtrl.getCategories().toArray()));
		cmb_category.setSelectedIndex(-1);
		setVisible(true);
	}
	private void close() {
		setVisible(false);
		reset();
	}
	private boolean isFilled() {
		if (txt_barcode.getText().trim().isEmpty())
			return false;
		if (txt_name.getText().trim().isEmpty())
			return false;
		if (cmb_unit.getSelectedIndex() < 0)
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_submit) {
			String barcode = txt_barcode.getText().trim();
			String name = txt_name.getText().trim();
			double quantity = (Double)sprm_quantity.getValue();
			Unit unit = (Unit)cmb_unit.getSelectedItem();
			ItemCategory category;
			if (!creatingCategory) {
				category = (ItemCategory)cmb_category.getSelectedItem();
			} else {
				category = new ItemCategory();
				category.setName(txt_category.getText().trim());
			}
			
			if (creatingItem) {
				if (!itemCtrl.createItem(barcode, name, quantity, unit, category)) {
					JOptionPane.showMessageDialog(this,
						    "An error occured while creating the Item!",
						    "Error!",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JOptionPane.showMessageDialog(this,
					    "The Item was successfully created!",
					    "Success!",
					    JOptionPane.INFORMATION_MESSAGE);
				
				triggerPerformListeners();
			} else {
				item.setBarcode(txt_barcode.getText().trim());
				item.setName(txt_name.getText().trim());
				item.setQuantity((Double)sprm_quantity.getValue());
				item.setUnit((Unit)cmb_unit.getSelectedItem());
				item.setCategory(category);
				
				if (!itemCtrl.updateItem(item)) {
					JOptionPane.showMessageDialog(this,
						    "An error occured while creating the Item!",
						    "Error!",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JOptionPane.showMessageDialog(this,
					    "The Item was successfully edited!",
					    "Success!",
					    JOptionPane.INFORMATION_MESSAGE);
				
				triggerPerformListeners();
			}
			close();
		} else if (e.getSource() == btn_cancel) {
			triggerCancelListeners();
			close();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == rdbtn_selectCategory || e.getSource() == rdbtn_createCategory) {
			creatingCategory = rdbtn_createCategory.isSelected();
			cmb_category.setEnabled(!creatingCategory);
			txt_category.setEnabled(creatingCategory);
			
			btn_submit.setEnabled(isFilled());
		} else if (e.getSource() == cmb_unit || e.getSource() == cmb_category) {
			btn_submit.setEnabled(isFilled());
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_barcode || e.getSource() == txt_name || e.getSource() == txt_category) {
			btn_submit.setEnabled(isFilled());
		}
	}
}
