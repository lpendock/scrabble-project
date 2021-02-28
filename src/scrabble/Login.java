package scrabble;

import javax.swing.*;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


/**
 * Deal with login validation and communication
 */
public class Login extends JPanel {
	private JTextField nameField;
	private Main main;

	/**
	 * Creates and handles the GUI and logic of the Login panel.
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
			
				// displays a popup error if user attempts to submit an empty name field
				if (nameField.getText().equals("") || nameField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(main, "Name should not be empty");
					return;
				}

				
				if (!main.isHost()) {
					//tries to get an up-to-date list of all users from the server to check name entered.
					main.client.sendToServer("getMemberList");
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							checkName();
						}
					});

				} else {
					
					main.setPlayer(nameField.getText());
					
					main.initMemberMenu();
				}
			}
		});

	}


	/**
	 * checks if the name entered is already taken by another player
	 */
	private void checkName() {
		//if take display popup to try another name
		if( main.checkNameTaken(nameField.getText())) {
			JOptionPane.showMessageDialog(main, "Name taken,choose another");
		}
		else {
			//sets name as current player and tries to enter game lobby
			main.setPlayer(nameField.getText());
			main.notifyServerJoiningGame();
		}
	}



}