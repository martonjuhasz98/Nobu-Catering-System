import java.awt.EventQueue;

import javax.swing.UIManager;

import guilayer.ManagerWindow;
import modlayer.Employee;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
					Employee user = new Employee();
					user.setCpr("0905982257");
					
					ManagerWindow window = new ManagerWindow(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
