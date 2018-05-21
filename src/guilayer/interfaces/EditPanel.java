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
	
	protected void triggerCreateListeners() {
		for (EditListener listener : listeners) {
			listener.created();
		}
	}
	protected void triggerUpdateListeners() {
		for (EditListener listener : listeners) {
			listener.updated();
		}
	}
	protected void triggerCancelListeners() {
		for (EditListener listener : listeners) {
			listener.cancelled();
		}
	}
}
