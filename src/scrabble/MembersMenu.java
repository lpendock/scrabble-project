package scrabble;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.SwingConstants;

public class MembersMenu extends JPanel {

	//Might want to use an actual arraylist to save members?(do we need that anywhere else?)
	//ArrayList<String> members = new ArrayList<String>();
	DefaultListModel<String> members = new DefaultListModel<>();
	String membersDisplay = "";
	private Main gui;
	JList list;
	/**
	 * Create the panel.
	 * @param main
	 */
	public MembersMenu(Main main) {
		gui = main;
		JLabel lblMembers = new JLabel("Members:");
		lblMembers.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblMembers);

		list = new JList(members);
		addMembers(gui.getPlayer());
		add(list);
		
		
		JButton btnNewButton = new JButton("Start Game");
		add(btnNewButton);
		 btnNewButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	gui.setPlayers(members.size());
	            	gui.changePanel(2);
	                
	            }
	        });

	}
	
	public void addMembers(String name) {
		members.addElement(name);
		gui.setPlayers(members.size());
	}

}
