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
				//checks if host or not then do relevant stuff

				if (nameField.getText().equals("") || nameField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(main, "Name should not be empty");
					return;
				}

				if (!main.isHost()) {

					main.client.sendToServer("getMemberList");
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							checkName();
						}
					});

				} else {
					main.setPlayer(nameField.getText());
					// do member menu stuff
					main.initMemberMenu();
				}
			}
		});

	}


	private void checkName() {
		if( main.checkNameTaken(nameField.getText())) {
			JOptionPane.showMessageDialog(main, "Name taken,choose another");
		}
		else {
			//reset current player to new name
			main.setPlayer(nameField.getText());
			main.notifyServerJoiningGame();
		}
	}



}