package guilayer.interfaces;

import java.util.ArrayList;

import javax.swing.JPanel;

public class EditPanel extends JPanel {
	
	private ArrayList<EditListener> listeners;
	
	public EditPanel() {
		listeners = new ArrayList<>();
	}
	
	public void addEditListener(EditListener listener) {
		listeners.add(listener);
	}
	public void removeEditListener(EditListener listener) {
		listeners.remove(listener);
	}
	
	protected void triggerEditListeners(boolean success) {
		for (EditListener listener : listeners) {
			listener.edited(success);
		}
	}
}
