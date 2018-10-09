package scrabble;

import javax.swing.*;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Login extends JPanel {
	private JTextField nameField;
	private Main main;
	private JTextField textField;
	private JTextField textField_1;

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
		/**
		 //only show ip field if not server
		 if(main.isHost() == false) {
		 JLabel lblIpAddress = new JLabel("IP Address");
		 add(lblIpAddress);

		 textField = new JTextField();
		 add(textField);
		 textField.setColumns(10);
		 }
		 JLabel lblPort = new JLabel("Port Number");
		 add(lblPort);

		 textField_1 = new JTextField();
		 add(textField_1);
		 textField_1.setColumns(10);
		 **/
		JButton nameSubmitButton = new JButton("submit");
		nameSubmitButton.setHorizontalAlignment(SwingConstants.LEADING);
		add(nameSubmitButton);
		nameSubmitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//checks if host or not then do relevant stuff
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
			//  }
		});

	}

	//checks to make sure port is number
	private boolean checkPort(String port) {
		int portnum;
		try {
			portnum = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Port has to be a number!");
			return false;
		}
		return true;
	}

	private void checkName() {
		if(main.checkNameTaken(nameField.getText())) {
			JOptionPane.showMessageDialog(main, "Name taken,choose another");
		}else {
			System.out.println("moving to menu");
			//reset currentplayer to new name
			main.setPlayer(nameField.getText());
			main.notifyServerJoiningGame();
		}
	}



}