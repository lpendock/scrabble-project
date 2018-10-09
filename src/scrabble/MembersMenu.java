package scrabble;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.BoxLayout.Y_AXIS;

public class MembersMenu extends JPanel {

	//Might want to use an actual arraylist to save members?(do we need that anywhere else?)
	//ArrayList<String> members = new ArrayList<String>();

	private HashMap<String, JLabel> memberNamesMap;
	private JLabel[] memberLabels;
	private Main main;
	/**
	 * Create the panel.
	 * @param main
	 */
	public MembersMenu(Main main) {
		this.main = main;
		this.memberLabels = new JLabel[8];

		JPanel parentPanel = new JPanel();

		parentPanel.setLayout(new BoxLayout(parentPanel, Y_AXIS));

		JLabel membersLabel = new JLabel("Members:");
		membersLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		parentPanel.add(membersLabel);



		this.initMemberLabelList(parentPanel);
		// only host player has the right to start game
		if (main.isHost()) {
			JButton startButton = new JButton("Start Game");
			parentPanel.add(startButton);
			startButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					main.startGame();
				}
			});

			startButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		}


//		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(parentPanel);
	}


	public void initMemberLabelList(JPanel panel) {
		ArrayList<String> list = this.main.getMemberList();
		for (String name : list) {
			if (name!= null) {
				panel.add(new JLabel(name));
			}
		}

//		for (int i = 0; i < memberLabels.)
//		for (JLabel label : memberLabels) {
//			if (!list.isEmpty() && label == null) {
//				label = new JLabel();
//				panel.add(label);
//			}
//		}
	}

}
