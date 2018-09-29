package scrabble;

import javax.swing.JPanel;
import javax.swing.JRootPane;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import javax.swing.JMenuBar;

public class Login extends JPanel {
	private JTextField textField;
	private GUI_main gui;

	/**
	 * Create the panel.
	 * @param gui_main 
	 */
	public Login(GUI_main gui_main) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		gui = gui_main;
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
