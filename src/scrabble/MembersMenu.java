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
	private Main main;
	private InviteGameMenu inviteGameMenu;
	private boolean invited = false;
	private JButton initButton;
	private JLabel messageLabel = new JLabel();
	private ArrayList<String> inviteeList;




	/**
	 * Create the panel.
	 * @param main
	 */
	public MembersMenu(Main main) {
		this.main = main;
		this.inviteeList = new ArrayList<>();

		parentPanel = new JPanel();
		parentPanel.setLayout(new BoxLayout(parentPanel, Y_AXIS));

		JLabel membersLabel = new JLabel("Current Members:");
		membersLabel.setFont(new Font("Arial", Font.BOLD, 20));
		membersLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

		parentPanel.add(membersLabel);
		// add some space
		parentPanel.add(new JLabel("   "));


		initMemberLabelList();

		initButton = new JButton("Initiate a Game");
		parentPanel.add(initButton);
		initButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inviteGameMenu = new InviteGameMenu(main);
					inviteGameMenu.setVisible(true);
					initButton.setEnabled(false);
				}
			});

		initButton.setAlignmentY(Component.CENTER_ALIGNMENT);

		parentPanel.add(messageLabel);
		this.add(parentPanel);
	}



	public void initMemberLabelList() {
		ArrayList<String> list = this.main.getMemberList();
		for (String name : list) {
			if (name == null) continue;
			JLabel nameLabel = new JLabel(name);
			nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
			parentPanel.add(nameLabel);

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
		inviteeList.add(invitee);
		this.inviteGameMenu.initInviteeLabels();
	}


	public InviteGameMenu getInviteGameMenu() {
		return this.inviteGameMenu;
	}
	
	public void setInvited() {
		this.invited = true;
	}

	public void banInvitationFunction() {
		initButton.setEnabled(false);
		messageLabel.setText("Waiting...");
		messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
		messageLabel.setForeground(Color.BLUE);
		parentPanel.revalidate();

	}

	public ArrayList<String> getInviteeList() {
		return inviteeList;
	}

	public void resetInvitBtn() {
		this.initButton.setEnabled(true);
	}
}
