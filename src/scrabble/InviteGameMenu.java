package scrabble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;


/**
 * This class generates UI for invitation and game initialization.
 */
public class InviteGameMenu extends JFrame {

    private JPanel parentPanel;
    private MembersMenu memberMenu;
    private Main main;
    private ArrayList<String> inviteeList;
    private ArrayList<String> displayedLabels = new ArrayList<>();


    // Init the invitation window
    public InviteGameMenu(Main main) {
        this.main = main;
        this.memberMenu = main.membersMenu;
        this.setTitle("Invitations -- inviter is : " + main.getPlayer());
        parentPanel = new JPanel();
        parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));

        parentPanel.setPreferredSize(new Dimension(400, 600));
        inviteeList = memberMenu.getInviteeList();

        // add some space
        parentPanel.add(new JLabel("   "));

        // Inviting a single player by clicking this button at a time.
        JButton inviteBtn = new JButton("Send invitation");
        inviteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        inviteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // player who accepted other invitation should not invite other again
                initInvitationDialog();
                main.membersMenu.setInvited();
            }
        });
        parentPanel.add(inviteBtn);


        // by pressing this button, the game will start with players being invited
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                inviteeList.add(main.getPlayer());
                main.notifyGameStart();


                main.membersMenu.getInviteGameMenu().setVisible(false);
            }
        });
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        parentPanel.add(startButton);


        // it will display names of those who pressed accept button
        JLabel inviteeName = new JLabel("Accepted invitees: ");
        inviteeName.setFont(new Font("Arial", Font.BOLD, 20));
        inviteeName.setAlignmentX(Component.CENTER_ALIGNMENT);
        parentPanel.add(inviteeName);


        // close invitation window will reset init button
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                memberMenu.resetInvitBtn();
            }
        });

        initInviteeLabels();


        this.getContentPane().add(parentPanel);
        this.pack();
        this.setLocationRelativeTo(null);
    }


    // This function initialize list of names of who accepted invitation
    public void initInviteeLabels() {
        for (String invitee : inviteeList) {
        	
            // if this name has been displayed then ignore it
            if (displayedLabels.contains(invitee)) continue;
            JLabel inviteeLabel = new JLabel(invitee);
            // add to displayed list to avoid duplicates
            displayedLabels.add(invitee);

            inviteeLabel.setFont(new Font("Arial", Font.BOLD, 18));
            inviteeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            inviteeLabel.setForeground(Color.BLUE);
            parentPanel.add(inviteeLabel);
        }
        this.pack();
    }


    // filtered out those who available members to be invited
    private void initInvitationDialog() {

        System.out.println(main.getMemberList());
        // copy the member list from Main
        ArrayList<String> tempList = new ArrayList<>(main.getMemberList());
        // remove current player
        tempList.remove(main.getPlayer());
        // remove those who already accepted invitation
        tempList.removeAll(inviteeList);
        // remove any null value
        tempList.removeAll(Collections.singleton(null));
        // turn it into an array
        Object[] memberNames = tempList.toArray();

        if (memberNames.length == 0) {
            JOptionPane.showConfirmDialog(parentPanel,
                    "There is no other member available","OK",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            return;
        }
        String invitee = (String) JOptionPane.showInputDialog(
                parentPanel,
                "Please choose the member you wish to invite:\n"
                ,
                "Current logged in members:",
                JOptionPane.PLAIN_MESSAGE,
                null, memberNames,
                memberNames[0]);

        this.main.notifyInvitation(invitee);
    }

}
