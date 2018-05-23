package guilayer.suppliers;

import java.awt.Font;
import java.awt.Label;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ctrllayer.SupplierController;
import guilayer.MainWindow;
import guilayer.interfaces.EditPanel;
import modlayer.Supplier;
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
import java.awt.Color;

public class EditSupplier extends EditPanel implements ActionListener, CaretListener, ItemListener {

	private SupplierController supplierCtrl;
	private Supplier supplier;
	private JTextField txt_barcode;
	private JTextField txt_name;
	private JButton btn_submit;
	private JButton btn_cancel;
	private SpinnerNumberModel sprm_quantity;
	private ButtonGroup rdbtn_group;
	private boolean creatingCategory;
	private boolean creatingSupplier;
	private JTextField textField;
	
	public EditSupplier() {
		supplierCtrl = new SupplierController();
		
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
		lbl_name.setForeground(Color.BLACK);
		lbl_name.setFont(new Font("Dialog", Font.PLAIN, 15));
		lbl_name.setBounds(16, 75, 129, 22);
		add(lbl_name);
		
		txt_name = new JTextField();
		txt_name.setColumns(10);
		txt_name.setBounds(16, 209, 376, 20);
		add(txt_name);
		sprm_quantity = new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(1));
		
		rdbtn_group = new ButtonGroup();
		
		textField = new JTextField();
		textField.setText("");
		textField.setColumns(10);
		textField.setBounds(16, 103, 376, 20);
		add(textField);
		
		btn_submit = new JButton("Create");
		btn_submit.setBounds(536, 457, 122, 32);
		add(btn_submit);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(668, 457, 122, 32);
		add(btn_cancel);
		
		reset();
		txt_barcode.addCaretListener(this);
		txt_name.addCaretListener(this);
		btn_submit.addActionListener(this);
		btn_cancel.addActionListener(this);
	}
	private void fill(Supplier supplier) {
		this.supplier = supplier;
		creatingCategory = false;
		creatingSupplier = false;
		
//		txt_barcode.setText(supplier.getBarcode());
		txt_name.setText(supplier.getName());
//		sprm_quantity.setValue(supplier.getQuantity());
		
		btn_submit.setText("Update");
		btn_submit.setEnabled(true);
	}
	private void reset() {
		supplier = null;
		creatingCategory = false;
		creatingSupplier = true;
		
		txt_barcode.setText("");
		txt_name.setText("");
		sprm_quantity.setValue(new Double(0.0));
		
		btn_submit.setText("Create");
		btn_submit.setEnabled(false);
	}
	public void createSupplier() {
		open();
	}
	public void updateSupplier(Supplier supplier) {
		open();
		fill(supplier);
	}
	private void open() {
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

		if (!creatingCategory) {

		} else {

		}
		
		
		return true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_submit) {
			String barcode = txt_barcode.getText().trim();
			String name = txt_name.getText().trim();
			double quantity = (Double)sprm_quantity.getValue();
			
			
			if (creatingSupplier) {
				if (!supplierCtrl.createSupplier("","","","","","")) {
					JOptionPane.showMessageDialog(this,
						    "An error occured while creating the Supplier!",
						    "Error!",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JOptionPane.showMessageDialog(this,
					    "The Supplier was successfully created!",
					    "Success!",
					    JOptionPane.INFORMATION_MESSAGE);
				
				triggerCreateListeners();
			} else {
//				supplier.setBarcode(txt_barcode.getText().trim());
				supplier.setName(txt_name.getText().trim());
//				supplier.setQuantity((Double)sprm_quantity.getValue());
//				supplier.setUnit((Unit)cmb_unit.getSelectedSupplier());
				
				if (!supplierCtrl.updateSupplier(supplier)) {
					JOptionPane.showMessageDialog(this,
						    "An error occured while creating the Supplier!",
						    "Error!",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JOptionPane.showMessageDialog(this,
					    "The Supplier was successfully edited!",
					    "Success!",
					    JOptionPane.INFORMATION_MESSAGE);
				
				triggerUpdateListeners();
			}
			close();
		} else if (e.getSource() == btn_cancel) {
			triggerCancelListeners();
			close();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
//		if (e.getSource() == rdbtn_selectCategory || e.getSource() == rdbtn_createCategory) {
//			creatingCategory = rdbtn_createCategory.isSelected();
//			txt_category.setEnabled(creatingCategory);
//			
//			btn_submit.setEnabled(isFilled());
//		} 
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_barcode )  {
			btn_submit.setEnabled(isFilled());
		}
	}


}
