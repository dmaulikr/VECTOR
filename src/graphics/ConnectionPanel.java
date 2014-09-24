package graphics;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ConnectionPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static int width = 350;
	private static int height = 200;
	
	private boolean cancelCheck = false;

	public ConnectionPanel()
	{
		super();
		this.setLayout(null);
		this.setBounds(0, 0, width, height);
		
		JLabel textLabel = new JLabel("Contacting server... Please wait...");
		textLabel.setBounds(55, 30, 320, 25);
		textLabel.setFont(new Font(textLabel.getFont().getFontName(), 1, 14));
		this.add(textLabel);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds((width - width * 5 / 6) / 2, height * 1 / 3, width * 5 / 6, 25);
		progressBar.setIndeterminate(true);
		this.add(progressBar);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(100, 100, 140, 20);
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					cancelCheck = true;
				}
		});
		this.add(cancelButton);
	}
	
	public void setCancelCheck(boolean bool)
	{
		cancelCheck = bool;
	}
	
	public boolean getCancelCheck()
	{
		return cancelCheck;
	}
}