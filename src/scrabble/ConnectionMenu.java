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

/**
 * This panel handles connection configuration.
 */
public class ConnectionMenu extends JPanel {
	private Main main;
	private JTextField IPaddressField;
	private JTextField portNumField;

	/**
	 * Creates and handles the GUI and logic of the ConnectionMenu.
	 *  
	 */
	public ConnectionMenu(Main main) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		this.main = main;

		JLabel lblIpAddress = new JLabel("IP Address");
		add(lblIpAddress);

		IPaddressField = new JTextField("127.0.0.1");
		add(IPaddressField);
		IPaddressField.setColumns(10);

		JLabel lblPort = new JLabel("Port Number");
		add(lblPort);

		portNumField = new JTextField("8080");
		add(portNumField);
		portNumField.setColumns(10);

		JButton nameSubmitButton = new JButton("submit");
		nameSubmitButton.setHorizontalAlignment(SwingConstants.LEADING);
		add(nameSubmitButton);
		//On press, submits port and IP address to main class
		nameSubmitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//checks if port is a number before doing anything else
				if(checkPort(portNumField.getText()) == true) {

					int portNum =Integer.parseInt(portNumField.getText());
					main.setPort(portNum);
					main.setIPaddress(IPaddressField.getText());

					if (!main.isHost()) {
						//attempt to connect to server if not a host
						// if no server running then do nothing
						if (!main.initClient()) return;
						main.startClientProcess();
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								main.initLogin();
							}
						});
					}
					else {
						//start the server process and transition to next panel
						main.startServerProcess();
						main.initLogin();
					}
				}
			}
		});

	}
	
	
	/**
	 * Checks if port entered is a number,displays popup message if not
	 * @param port Port number entered
	 * @return
	 */
	private boolean checkPort(String port) {
		
		try {
			int portNum = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Port has to be a number!");
			return false;
		}
		return true;
	}

}
