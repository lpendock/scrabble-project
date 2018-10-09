package scrabble;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class connectionMenu extends JPanel {
	private Main main;
	private JTextField textField;
	private JTextField textField_1;


	/**
	 * Create the panel.
	 */
	public connectionMenu(Main main) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		this.main = main;
		//only show ip field if not server
				if(main.isHost() == false) {
					JLabel lblIpAddress = new JLabel("IP Address");
					add(lblIpAddress);
				
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
								
									main.setIPaddress(textField.getText());

									// if no server running then do nothing
									if (!main.initClient()) return;
									main.startClientProcess();
									EventQueue.invokeLater(new Runnable() {
										@Override
										public void run() {
											main.initLogin();

										}
									});
							
								
								
							} else {
								
								//start the server process then go login for name input
								main.startServerProcess();
								main.initLogin();
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

}
