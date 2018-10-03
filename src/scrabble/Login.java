package scrabble;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JPanel {
	private JTextField nameField;
	private Main main;

	/**
	 * Create the panel.
	 * @param main
	 */
	public Login(Main main) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		this.main = main;

		JLabel playerNameLabel = new JLabel("Name");
		add(playerNameLabel);
		
		nameField = new JTextField();
		add(nameField);
		nameField.setColumns(10);

		JButton nameSubmitButton = new JButton("submit");
		nameSubmitButton.setHorizontalAlignment(SwingConstants.LEADING);
		add(nameSubmitButton);
		nameSubmitButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
					main.setPlayer(nameField.getText());
					if (!main.isHost()) {
						main.notifyServerJoiningGame();
					} else {
						main.initMemberMenu();
					}
	            }
	        });

	}

}
