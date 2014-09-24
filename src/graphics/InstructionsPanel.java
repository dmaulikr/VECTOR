package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InstructionsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private boolean doneCheck = false;

	public InstructionsPanel()
	{
		super();
		this.setLayout(null);

		JTextArea instructionsArea = new JTextArea();
		instructionsArea.setEditable(false);
		instructionsArea.setBounds(40, 100, 800, 400);
		
		instructionsArea.setFont(new Font("Lucida", 0, 18));
		instructionsArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		instructionsArea.setLineWrap(true);
		instructionsArea.setWrapStyleWord(true);
		instructionsArea.setText("   Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut " +
				"labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
				"ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
				"dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
				"deserunt mollit anim id est laborum." +
				"\n\n   Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut " +
				"labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
				"ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
				"dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
				"deserunt mollit anim id est laborum.");
		this.add(instructionsArea);

		JLabel instructionsLabel = new JLabel();
		instructionsLabel.setFont(new Font("Lucida", 0, 60));
		instructionsLabel.setText("Instructions");
		instructionsLabel.setBounds(275, 0, 400, 100);
		this.add(instructionsLabel);

		JButton doneButton = new JButton("Done");
		doneButton.setBounds(380, 520, 100, 20);
		doneButton.setFont(new Font("Lucida", 1, 12));
		doneButton.setFocusable(false);
		doneButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				doneCheck = true;
			}});
		this.add(doneButton);

		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent me) {
				int d = me.getKeyCode();
				if(d == KeyEvent.VK_ENTER)
					System.out.println("ENTER");
				if(d == KeyEvent.VK_ESCAPE)
					System.out.println("ESCAPE");
			}
		});
	}

	public void setDoneCheck(boolean bool)
	{
		doneCheck = bool;
	}

	public boolean getDoneCheck()
	{
		return doneCheck;
	}
}