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
		
		//only show ip field if not server
		if(main.isHost() == false) {
			JLabel lblIpAddress = new JLabel("IP Address");
			add(lblIpAddress);

			// input default value
			textField = new JTextField("127.0.0.1");
			add(textField);
			textField.setColumns(10);
		}
		JLabel lblPort = new JLabel("Port Number");
		add(lblPort);
		
		textField_1 = new JTextField("8080");

		add(textField_1);
		textField_1.setColumns(10);

		JButton nameSubmitButton = new JButton("submit");
		nameSubmitButton.setHorizontalAlignment(SwingConstants.LEADING);
		add(nameSubmitButton);
		nameSubmitButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {

	            	if(checkPort(textField_1.getText()) == true) {
	            		
	            		int portnum =Integer.parseInt(textField_1.getText());
	            		main.setPort(portnum);
	            		
                        if (!main.isHost()) {
                            //only connect to the server the first time.other times is just to check name
                            if(main.getClientConnected() == 0) {
                                main.setIPaddress(textField.getText());
                                //main.setPlayer(nameField.getText());
                                main.startClientProcess();


                            }

//                            String command = "getMemberList" ;
//                            main.client.sendToServer("getMemberList");
                            EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
//                                    checkName();
									if (main.getMemberList().contains(nameField)) {
										System.out.println("Name has been used");
									} else {
										System.out.println("no body exists");
										main.initMemberMenu();
									}

                                }
                            });

                        } else {
                            main.setPlayer(nameField.getText());
                            //start the server process then do member menu stuff
                            main.startServerProcess();
                            main.initMemberMenu();
                        }
	                }
	            }
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
