package scrabble;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.BoxLayout.Y_AXIS;

public class MembersMenu extends JPanel {

	//Might want to use an actual arraylist to save members?(do we need that anywhere else?)
	//ArrayList<String> members = new ArrayList<String>();
	private JPanel parentPanel;
	private JLabel[] memberLabels;
	private Main main;
	private InviteGameMenu inviteGameMenu;
	private boolean invited = false;

	/**
	 * Create the panel.
	 * @param main
	 */
	public MembersMenu(Main main) {
		this.main = main;
		this.memberLabels = new JLabel[8];

		parentPanel = new JPanel();
		parentPanel.setLayout(new BoxLayout(parentPanel, Y_AXIS));

		JLabel membersLabel = new JLabel("Current Members:");
		membersLabel.setFont(new Font("Arial", Font.BOLD, 20));
		membersLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

		parentPanel.add(membersLabel);
		// add some space
		parentPanel.add(new JLabel("   "));


		initMemberLabelList();

		JButton initButton = new JButton("Initiate a Game");
		parentPanel.add(initButton);
		initButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inviteGameMenu = new InviteGameMenu(main);
					inviteGameMenu.setVisible(true);
				}
			});

		initButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.add(parentPanel);
	}


	// todo: consider player left waiting room
	public void initMemberLabelList() {
		ArrayList<String> list = this.main.getMemberList();
		for (String name : list) {
			if (name!= null) {
				JLabel nameLabel = new JLabel(name);
				nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
				parentPanel.add(nameLabel);
			}
		}
		// add some space (I'm lazy)
		parentPanel.add(new JLabel("   "));

	}

	public void initInvitationDialog(String inviter) {
		int n = JOptionPane.showConfirmDialog(
				this,
				"Player: " + inviter + " invite you to join the game.",
				"Invitation",
				JOptionPane.YES_NO_OPTION);

		if (n == JOptionPane.YES_OPTION) {

			this.main.notifyAcceptInvitation(inviter);
		}
	}

	public boolean isInvited() {
		return invited;
	}

	public void addInvitee(String invitee) {
		this.inviteGameMenu.addInvitee(invitee);
		this.inviteGameMenu.initInviteeLabels();
	}

	public InviteGameMenu getInviteGameMenu() {
		return this.inviteGameMenu;
	}
	
	public void setInvited() {
		this.invited = true;
	}
}
