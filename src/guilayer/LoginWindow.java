package guilayer;

import java.awt.Color;
import java.awt.EventQueue;
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
import javax.swing.SwingConstants;

import ctrllayer.SessionSingleton;

public class LoginWindow {

	private JFrame frame;
	public static final int totalWidth = 1000;
	public static final int totalHeight = 500;
	public static final int menuWidth = 200;
	public static final int contentWidth = 800;
	public static final int menuItemHeight = 48;
	public static final Color menuFontColour = Color.WHITE;
	public static final Color menuBackgroundColour = Color.DARK_GRAY;
	public static final Color activeMenuItemBackgroundColour = Color.GRAY;
	public static final Color activeMenuItemSignColour = Color.LIGHT_GRAY;
	public static final Color contentFontColour = Color.DARK_GRAY;
	public static final Color contentBackgroundColour = Color.WHITE;
	public static final Font menuFont = new Font("Segoe UI", Font.BOLD, 16);
	public static final Font contentFont = new Font("Segoe UI", Font.PLAIN, 14);
	private JTextField txtUsername;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow window = new LoginWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, totalWidth + 4, totalHeight + 28);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Nobu inventory system");
		frame.setFont(contentFont);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setBackground(contentBackgroundColour);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(350, 125, 300, 250);
		// panel_1.setBackground(contentBackgroundColour);
		panel.add(panel_1);
		panel_1.setLayout(null);

		JButton btnNewButton = new JButton("Log in");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SessionSingleton.getInstance().logIn(txtUsername.getText().trim(),
						new String(passwordField.getPassword()));
			}
		});
		btnNewButton.setBounds(0, 205, 300, 45);
		panel_1.add(btnNewButton);

		txtUsername = new JTextField();
		txtUsername.setBounds(50, 55, 200, 30);
		panel_1.add(txtUsername);
		txtUsername.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(50, 140, 200, 30);
		panel_1.add(passwordField);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(100, 40, 100, 16);
		panel_1.add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(100, 125, 100, 16);
		panel_1.add(lblPassword);

	}
}
