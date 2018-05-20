package guilayer.interfaces;

import java.util.ArrayList;

import javax.swing.JPanel;

public class CreatePanel extends JPanel {
	
	private ArrayList<CreateListener> listeners;
	
	public CreatePanel() {
		listeners = new ArrayList<>();
	}
	
	public void addCreateListener(CreateListener listener) {
		listeners.add(listener);
	}
	public void removeCreateListener(CreateListener listener) {
		listeners.remove(listener);
	}
	
	protected void triggerCreateListeners(boolean success) {
		for (CreateListener listener : listeners) {
			listener.created(success);
		}
	}
}
