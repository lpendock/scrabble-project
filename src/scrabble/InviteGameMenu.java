package scrabble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import static javax.swing.BoxLayout.Y_AXIS;

public class InviteGameMenu extends JFrame {

    private JPanel parentPanel;
    private MembersMenu memberMenu;
    private ArrayList<String> inviteeList;
    private Main main;

    public InviteGameMenu(Main main) {
        parentPanel = new JPanel();
        parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));

        parentPanel.setPreferredSize(new Dimension(400, 600));
        inviteeList = new ArrayList<>();


        JLabel inviteeName = new JLabel("Accepted invitees: ");
        inviteeName.setFont(new Font("Arial", Font.BOLD, 20));
        inviteeName.setAlignmentX(Component.CENTER_ALIGNMENT);
        parentPanel.add(inviteeName);
        // add some space
        parentPanel.add(new JLabel("   "));

//        initInviteeLabels();


        // Inviting a single player by clicking this button at a time.
        JButton inviteBtn = new JButton("Send invitation");
        inviteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        inviteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initInvitationDialog();
            }
        });

        JButton startButton = new JButton("Start Game");
        parentPanel.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                main.startGame();
            }
        });

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        parentPanel.add(inviteBtn);
        this.getContentPane().add(parentPanel);
        this.pack();
    }

    public void initInviteeLabels() {
        for (String invitee : inviteeList) {
            JLabel inviteeLabel = new JLabel(invitee);
            parentPanel.add(inviteeLabel);
        }
    }

    public void initInvitationDialog() {

        // copy the member list from Main
        ArrayList<String> tempList = new ArrayList<>(main.getMemberList());
        // remove current player
        tempList.remove(main.getPlayer());
        // remove those who already accepted invitation
        tempList.removeAll(inviteeList);
        // remove any null value
        tempList.removeAll(Collections.singleton(null));
        // turn it into an array
        String[] memberNames = (String[]) tempList.toArray();

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

    public void addInvitee(String invitee) {
        inviteeList.add(invitee);
    }

}
