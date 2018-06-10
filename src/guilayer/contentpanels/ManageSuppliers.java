package guilayer.contentpanels;

import guilayer.essentials.NavigationPanel;
import guilayer.suppliers.*;

public class ManageSuppliers extends NavigationPanel {
	
	private EditSupplier supEditor;
	private ListSuppliers supLister;

	public ManageSuppliers() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		supEditor = new EditSupplier();
		add(supEditor);
		
		supLister = new ListSuppliers(supEditor);
		add(supLister);
	}

	@Override
	public void prepare() {
		supEditor.prepare();
		supLister.prepare();
	}
	@Override
	public void reset() {
		supEditor.reset();
		supLister.reset();
	}
}
