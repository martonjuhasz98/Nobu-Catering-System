package guilayer.inventorypanels;

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
import guilayer.interfaces.EditPanel;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Consumer;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;

public class EditInventory extends EditPanel implements ActionListener, FocusListener {

	private ItemController itemCtrl;
	private Item item;
	private JTextField txt_barcode;
	private JTextField txt_name;
	private JButton btn_update;
	private JButton btn_cancel;
	private SpinnerNumberModel sprm_quantity;
	private JComboBox<Unit> cmb_unit;
	private JComboBox<ItemCategory> cmb_category;
	
	public EditInventory() {
		itemCtrl = new ItemController();
		item = null;
		
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
		
		cmb_category = new JComboBox<ItemCategory>();
		cmb_category.setBounds(16, 221, 376, 20);
		add(cmb_category);
		
		btn_update = new JButton("Update");
		btn_update.setBounds(536, 457, 122, 32);
		add(btn_update);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 457, 122, 32);
		add(btn_cancel);
		
		reset();
		txt_barcode.addFocusListener(this);
		txt_name.addFocusListener(this);
		cmb_unit.addFocusListener(this);
		cmb_category.addFocusListener(this);
		btn_update.addActionListener(this);
		btn_cancel.addActionListener(this);
	}
	private void fill(Item item) {
		this.item = item;
		
		txt_barcode.setText(item.getBarcode());
		txt_name.setText(item.getName());
		sprm_quantity.setValue(item.getQuantity());
		cmb_unit.setSelectedItem(item.getUnit());
		cmb_category.setSelectedItem(item.getCategory());
		
		btn_update.setEnabled(true);
	}
	private void reset() {
		item = null;
		
		txt_barcode.setText("");
		txt_name.setText("");
		sprm_quantity.setValue(new Double(0.0));
		cmb_unit.setModel(new DefaultComboBoxModel(itemCtrl.getUnits().toArray()));
		cmb_unit.setSelectedIndex(-1);
		cmb_category.setModel(new DefaultComboBoxModel(itemCtrl.getCategories().toArray()));
		cmb_category.setSelectedIndex(-1);
		
		btn_update.setEnabled(false);
	}
	public void open(Item item) {
		fill(item);
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
		if (cmb_category.getSelectedIndex() < 0)
			return false;
		
		return true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_update) {
			item.setBarcode(txt_barcode.getText().trim());
			item.setName(txt_name.getText().trim());
			item.setQuantity((Double)sprm_quantity.getValue());
			item.setUnit((Unit)cmb_unit.getSelectedItem());
			item.setCategory((ItemCategory)cmb_category.getSelectedItem());
			
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
			
			triggerEditListeners(true);
			close();
		} else if (e.getSource() == btn_cancel) {
			triggerEditListeners(false);
			close();
		}
	}
	@Override
	public void focusLost(FocusEvent e) {
		btn_update.setEnabled(isFilled());
	}
	@Override
	public void focusGained(FocusEvent e) {}
}
