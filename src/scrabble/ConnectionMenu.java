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
 * This panel handles connection configuration and login validation.
 */
public class ConnectionMenu extends JPanel {
	private Main main;
	private JTextField IPaddressField;
	private JTextField portNumField;

	/**
	 * Create the panel.
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
		nameSubmitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkPort(portNumField.getText()) == true) {

					int portNum =Integer.parseInt(portNumField.getText());
					main.setPort(portNum);
					main.setIPaddress(IPaddressField.getText());

					if (!main.isHost()) {
						//only connect to the server the first time.other times is just to check name

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
		try {
			int portNum = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Port has to be a number!");
			return false;
		}
		return true;
	}

}
