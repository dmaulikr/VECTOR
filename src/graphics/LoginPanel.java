package graphics;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private boolean loginCheck = false;
	private boolean exitCheck = false;

	private static int width = 350;
	private static int height = 200;

	private String username = "";
	private String password = "";

	public LoginPanel()
	{
		super();
		this.setLayout(null);
		this.setBounds(0, 0, width, height);

		JLabel titleLabel = new JLabel("V  E  C  T  O  R");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(60, 10, 220, 32);
		this.add(titleLabel);

		JLabel userLabel = new JLabel("Username:");
		userLabel.setBounds(15, 55, 180, 15);
		this.add(userLabel);

		final JTextField userField = new JTextField();
		userField.setBounds(15, 75, 180, 24);
		this.add(userField);

		JLabel passLabel = new JLabel("Password:");
		passLabel.setBounds(15, 105, 180, 15);
		this.add(passLabel);

		final JPasswordField passField = new JPasswordField();
		passField.setBounds(15, 125, 180, 24);
		this.add(passField);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(220, 75, 120, 20);
		loginButton.setFocusable(false);
		loginButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				username = "";
				password = "";
				username = userField.getText();
				char[] passArr =  passField.getPassword();
				for(int i = 0; i < passArr.length; i++)
				{
					if(passArr[i] == ' ')
						JOptionPane.showMessageDialog(null, "Incorrect username-password combination", "ERROR", 2);
					else
						password += passArr[i];
				}
				loginCheck = true;
			}
		});
		this.add(loginButton);

		JButton exitButton = new JButton("Exit");
		exitButton.setBounds(220, 125, 120, 20);
		exitButton.setFocusable(false);
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exitCheck = true;
			}
		});
		this.add(exitButton);

		this.setFocusable(true);
		KeyListener kl = new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent arg0)
			{
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					username = "";
					password = "";
					username = userField.getText();
					char[] passArr =  passField.getPassword();
					for(int i = 0; i < passArr.length; i++)
						password += passArr[i];
					loginCheck = true;
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0){}
			@Override
			public void keyTyped(KeyEvent arg0){}
		};
		passField.addKeyListener(kl);
		userField.addKeyListener(kl);
	}

	public boolean getExitCheck()
	{
		return exitCheck;
	}

	public String getUsernameInput()
	{
		return username;
	}

	public String getPasswordInput()
	{
		return password;
	}

	public boolean getLoginCheck()
	{
		return loginCheck;
	}
	
	public void setLoginCheck(boolean bool)
	{
		loginCheck = bool;
	}
}