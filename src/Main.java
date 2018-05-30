import java.awt.EventQueue;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import guilayer.LoginWindow;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Main.setDefaultFont(new FontUIResource("Segoe UI", Font.PLAIN, 14));
					
					new LoginWindow();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private static void setDefaultFont(FontUIResource font) {
		Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put (key, font);
		}
	}
}
