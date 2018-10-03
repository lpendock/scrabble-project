package scrabble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * This class is part of the game view. It
 * offers buttons for players to choose alphabets
 * from AlphabetBag
 */
public class AlphabetPanel extends JPanel{

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 50;

    private final JButton btnNewButton = new JButton("a");
    private final JButton btnNewButton_1 = new JButton("b");
    private final JButton btnNewButton_2 = new JButton("c");
    private final JButton btnNewButton_3 = new JButton("d");
    private final JButton btnNewButton_4 = new JButton("e");
    private final JButton btnNewButton_5 = new JButton("f");
    private final JButton btnNewButton_6 = new JButton("g");

    private AlphabetBag alphabetBag;
    private Game game;
    private JButton tileChosen;
    String setLetter = "";


    ArrayList<String> hand = new ArrayList<String>();
    JPanel rackPanel = new JPanel();

    /**
     * By overriding this function the system knows the
     * default size of this panel.
     * @return
     */
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public AlphabetPanel(Game game) {
        this.game = game;
        this.alphabetBag = new AlphabetBag();
        this.hand = alphabetBag.firstDraw();

        this.add(rackPanel);
        initHand();
        initRackPanel();
    }


    /**
     * Board may call this function to get the
     * reference of the button being chosen in
     * AlphabetPanel.
     * @return
     */
    public JButton getTheChosenTile() {
        return this.tileChosen;
    }


    /**
     * Provide reference of the AlphabetBag for other
     * classes.
     * @return
     */
    public AlphabetBag getAlphabetBag() {
        return this.alphabetBag;
    }


    /**
     * This method will be called when player plugged one
     * alphabet to the board. The tile on AlphabetPanel will
     * be updated with a new alphabet from the bag.
     */
    public void setNewTile() {
        String letter = this.alphabetBag.getLetter();
        tileChosen.setText(letter);
        tileChosen = null;
    }


    private void initRackPanel() {
//        rackPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        rackPanel.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLetter = btnNewButton.getText();
                tileChosen = btnNewButton;
            }
        });

        rackPanel.add(btnNewButton_1);
        btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLetter = btnNewButton_1.getText();
                tileChosen = btnNewButton_1;
                System.out.println("button 1 pressed");
            }
        });

        rackPanel.add(btnNewButton_2);
        btnNewButton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLetter = btnNewButton_2.getText();
                tileChosen = btnNewButton_2;
            }
        });

        rackPanel.add(btnNewButton_3);
        btnNewButton_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLetter = btnNewButton_3.getText();
                tileChosen = btnNewButton_3;
            }
        });

        rackPanel.add(btnNewButton_4);
        btnNewButton_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLetter = btnNewButton_4.getText();
                tileChosen = btnNewButton_4;
            }
        });

        rackPanel.add(btnNewButton_5);
        btnNewButton_5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLetter = btnNewButton_5.getText();
                tileChosen = btnNewButton_5;
            }
        });

        rackPanel.add(btnNewButton_6);
        btnNewButton_6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLetter = btnNewButton_6.getText();
                tileChosen = btnNewButton_6;
            }
        });
    }

    //start first hand(display correct letters)
    private void initHand() {
        btnNewButton.setText(hand.get(0));
        btnNewButton_1.setText(hand.get(1));
        btnNewButton_2.setText(hand.get(2));
        btnNewButton_3.setText(hand.get(3));
        btnNewButton_4.setText(hand.get(4));
        btnNewButton_5.setText(hand.get(5));
        btnNewButton_6.setText(hand.get(6));
    }



}
