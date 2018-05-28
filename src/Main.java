import java.awt.EventQueue;

import javax.swing.UIManager;

import ctrllayer.SessionSingleton;
import guilayer.ManagerWindow;
import modlayer.Employee;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
//					Employee user = new Employee();
//					user.setCpr("0905982257");
					SessionSingleton.getInstance().logIn("a", "a");
					
					ManagerWindow window = new ManagerWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
