package guilayer.essentials;

import java.util.ArrayList;

import javax.swing.JPanel;

public class PerformPanel extends JPanel {
	
	private ArrayList<PerformListener> listeners;
	
	public PerformPanel() {
		listeners = new ArrayList<>();
	}
	
	public void addPerformListener(PerformListener listener) {
		listeners.add(listener);
	}
	public void removePerformListener(PerformListener listener) {
		listeners.remove(listener);
	}
	
	protected void triggerPerformListeners() {
		for (PerformListener listener : listeners) {
			listener.performed();
		}
	}
	protected void triggerCancelListeners() {
		for (PerformListener listener : listeners) {
			listener.cancelled();
		}
	}
}
