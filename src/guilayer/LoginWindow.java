package guilayer;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ctrllayer.SessionSingleton;

public class LoginWindow extends JFrame implements ActionListener, CaretListener {

	public static final int totalWidth = 1000;
	public static final int totalHeight = 500;
	public static final int contentWidth = 800;
	public static final Color contentFontColour = Color.DARK_GRAY;
	public static final Color contentBackgroundColour = Color.WHITE;
	public static final Font contentFont = new Font("Segoe UI", Font.PLAIN, 14);
	
	private SessionSingleton session;
	private JFrame frm_load;
	private JTextField txt_username;
	private JPasswordField txt_password;
	private JButton btn_login;
	private boolean loggedIn;

	public LoginWindow() {
		super();
		
		session = SessionSingleton.getInstance();
		loggedIn = false;
		
		new LoadWorker().execute();
		
		initialize();
	}
	//Layout
	private void initialize() {
		
		setBounds(100, 100, totalWidth, totalHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Nobu inventory system");
		setResizable(false);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setBackground(contentBackgroundColour);
		panel.setLayout(null);

		JPanel center = new JPanel();
		center.setBounds(351, 89, 300, 250);
		center.setBackground(contentBackgroundColour);
		center.setLayout(null);
		panel.add(center);
		
		JLabel lbl_username = new JLabel("Username");
		lbl_username.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_username.setBounds(100, 40, 100, 16);
		lbl_username.setFont(lbl_username.getFont().deriveFont(14f));
		center.add(lbl_username);

		txt_username = new JTextField();
		txt_username.setBounds(50, 55, 200, 30);
		center.add(txt_username);
		
		JLabel lbl_password = new JLabel("Password");
		lbl_password.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_password.setBounds(100, 125, 100, 16);
		center.add(lbl_password);

		txt_password = new JPasswordField();
		txt_password.setBounds(50, 140, 200, 30);
		txt_password.setFont(new Font("Arial", Font.PLAIN, 14));
		center.add(txt_password);
		
		btn_login = new JButton("Login");
		btn_login.setEnabled(false);
		btn_login.setBounds(0, 205, 300, 45);
		center.add(btn_login);
		
		getRootPane().setDefaultButton(btn_login);
		
		setVisible(true);
		
		txt_username.addCaretListener(this);
		txt_password.addCaretListener(this);
		btn_login.addActionListener(this);
	}
	//Functionalities
	private void login() {
		if (!isFilled()) return;
		
		//Disable login button
		btn_login.setEnabled(false);
		btn_login.setText("Logging in...");
		
		String username = txt_username.getText();
		String password = new String(txt_password.getPassword());
		if (!(loggedIn = session.logIn(username, password))) {
			JOptionPane.showMessageDialog(this,
				    "Incorrect username or password!\nPlease try again!",
				    "Login Failed",
				    JOptionPane.ERROR_MESSAGE);
			
			//Enable login button
			btn_login.setEnabled(true);
			btn_login.setText("Login");
			return;
		}
		
		//The window has already loaded
		if (frm_load != null) {
			frm_load.setVisible(true);
			setVisible(false);
			dispose();
		}
	}
	private boolean isFilled() {
		if (txt_username.getText().isEmpty())
			return false;
		if (txt_password.getPassword().length < 1)
			return false;
		
		return true;
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_login) {
			login();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		final Object source = e.getSource();
		if (source == txt_username || source == txt_password) {
			btn_login.setEnabled(isFilled());
		}
	}
	//Classes
	private class LoadWorker extends SwingWorker<JFrame, Void> {
		//Log loading time
		private long time;
		
		@Override
		protected JFrame doInBackground() throws Exception {
			//Log loading time
			time = System.nanoTime();
			
			return new ManagerWindow();
			//return new WaiterWindow();
		}

		@Override
		protected void done() {
			try {
				//Log loading time
				time = System.nanoTime() - time;
				System.out.println(String.format("Loaded in %f ms.", time / Math.pow(10, 6)));
				
				frm_load = get();
				if (loggedIn && frm_load != null) {
					login();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
