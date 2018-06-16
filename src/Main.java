import java.awt.EventQueue;
import java.awt.Font;
import java.io.InputStream;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import guilayer.LoginWindow;
import guilayer.orders.EditOrder2;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Font defaultFont = importFont("/font/helvetica.otf").deriveFont(14f);
					Main.setDefaultFont(new FontUIResource(defaultFont));
					
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
	private static Font importFont(String path) {
		try {
			InputStream is = Main.class.getResourceAsStream(path);
	        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
	        return font;
		} catch (Exception e) {
			e.printStackTrace();
	    }
		return null;
	}
}
