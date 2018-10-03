package scrabble;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JPanel {
	private JTextField textField;
	private Main gui;

	/**
	 * Create the panel.
	 * @param main
	 */
	public Login(Main main) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		gui = main;



		JLabel lblNewLabel = new JLabel("Name");
		add(lblNewLabel);
		
		textField = new JTextField();
		add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("submit");
		btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
		add(btnNewButton);
		  btnNewButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	//gui.setPlayers(Integer.parseInt(textField_1.getText()));
	            	gui.setPlayer(textField.getText());
	            	gui.changePanel(3);
	                
	            }
	        });

	}

}
