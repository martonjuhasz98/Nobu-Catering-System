package guilayer.interfaces;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class AutoComboBox<T> extends JComboBox<T> implements DocumentListener, FocusListener {

	private JTextComponent editor;
	private PlainDocument document;
	private JPopupMenu popup;
	private ComboBoxModel<T> model;
	
	public AutoComboBox() {
		super();
		initialize();
	}
	public AutoComboBox(ComboBoxModel<T> aModel) {
		super(aModel);
		initialize();
	}
	public AutoComboBox(T[] items) {
		super(items);
		initialize();
	}
	public AutoComboBox(Vector<T> items) {
		super(items);
		initialize();
	}
	
	private void initialize() {
		editor = (JTextComponent)this.getEditor().getEditorComponent();
		document = (PlainDocument)editor.getDocument();
		model = this.getModel();
		popup = this.getComponentPopupMenu();
		
		editor.addFocusListener(this);
		document.addDocumentListener(this);
	}
	
	

	@Override
	public void focusGained(FocusEvent e) {
		if (this.isDisplayable())
			this.setPopupVisible(true);
		System.out.println("focused");
	}
	@Override
	public void focusLost(FocusEvent e) {
		if (this.isDisplayable())
			this.setPopupVisible(false);
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		System.out.println("inserted");
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		System.out.println("removed");
	}
	@Override
	public void changedUpdate(DocumentEvent e) {}
	
	public static void main(String[] args) {
	    AutoComboBox comboBox = new AutoComboBox(new Object[] {"Ester", "Jordi", "Sergi"});
	    comboBox.setEditable(true);

	    JFrame frame = new JFrame();
	    frame.setBounds(0, 0, 300, 300);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	    frame.getContentPane().add(comboBox);
	    frame.pack(); frame.show();
	}
}
