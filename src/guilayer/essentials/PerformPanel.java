package guilayer.essentials;

import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class PerformPanel extends NavigationPanel {
	
	private ArrayList<PerformListener> listeners;
	
	public PerformPanel() {
		super();
		
		listeners = new ArrayList<>();
	}
	
	protected void open() {
		prepare();
		setVisible(true);
	}
	protected void close() {
		setVisible(false);
		reset();
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
