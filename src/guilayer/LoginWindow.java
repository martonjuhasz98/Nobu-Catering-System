package guilayer;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import ctrllayer.SessionSingleton;

public class LoginWindow extends JFrame {

	public static final int totalWidth = 1000;
	public static final int totalHeight = 500;
	public static final int contentWidth = 800;
	public static final Color contentFontColour = Color.DARK_GRAY;
	public static final Color contentBackgroundColour = Color.WHITE;
	public static final Font contentFont = new Font("Segoe UI", Font.PLAIN, 14);
	private JTextField txt_username;
	private JPasswordField txt_password;
	private JFrame window;
	boolean loggedIn;
	private JButton btn_login;

	public LoginWindow() {
		super();
		loggedIn = false;
		
		new LoadWorker().execute();
		
		initialize();
	}
	
	private void initialize() {
		
		setVisible(true);
		
		setBounds(100, 100, totalWidth, totalHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Nobu inventory system");
		setFont(contentFont);
		setResizable(false);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setBackground(contentBackgroundColour);
		panel.setLayout(null);

		JPanel center = new JPanel();
		center.setBounds(350, 125, 300, 250);
		center.setBackground(contentBackgroundColour);
		center.setLayout(null);
		panel.add(center);
		
		JLabel lbl_username = new JLabel("Username");
		lbl_username.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_username.setBounds(100, 40, 100, 16);
		lbl_username.setFont(contentFont);
		center.add(lbl_username);

		txt_username = new JTextField();
		txt_username.setBounds(50, 55, 200, 30);
		txt_username.setColumns(10);
		center.add(txt_username);
		
		JLabel lbl_password = new JLabel("Password");
		lbl_password.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_password.setBounds(100, 125, 100, 16);
		lbl_password.setFont(contentFont);
		center.add(lbl_password);

		txt_password = new JPasswordField();
		txt_password.setBounds(50, 140, 200, 30);
		center.add(txt_password);
		
		btn_login = new JButton("Log in");
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		btn_login.setBounds(0, 205, 300, 45);
		center.add(btn_login);
		
		JLabel helpLbl = new JLabel("Use username: a and password: a to log in");
		helpLbl.setBounds(694, 221, 300, 16);
		panel.add(helpLbl);
	}
	
	private void login() {
		btn_login.setEnabled(false);
		btn_login.setText("Logging in...");
		
		if (SessionSingleton.getInstance().logIn(txt_username.getText().trim(),
				new String(txt_password.getPassword()))) {
			if (window != null) {
				window.setVisible(true);
				setVisible(false);
				dispose();
			} else {
				loggedIn = true;
			}
		} else {
			btn_login.setEnabled(true);
			btn_login.setText("Log in");
			JOptionPane.showMessageDialog(LoginWindow.this,
				    "Incorrect username or password.",
				    "Login failed",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

	public class LoadWorker extends SwingWorker<Boolean, Void> {

		private long time;
		
		@Override
		protected Boolean doInBackground() throws Exception {
			time = System.nanoTime();
			window = new ManagerWindow();
//			window = new WaiterWindow();	
			return true;
		}

		@Override
		protected void done() {
			try {
				time = System.nanoTime() - time;
				System.out.println(String.format("Loaded in %f ms.", time / Math.pow(10, 6)));
				if(loggedIn && get()) {
					window.setVisible(true);
					setVisible(false);
					dispose();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
