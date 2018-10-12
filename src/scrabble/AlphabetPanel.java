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

    // seven buttons with random alphabets
    private final JButton alphabetButton = new JButton("a");
    private final JButton alphabetButton_1 = new JButton("b");
    private final JButton alphabetButton_2 = new JButton("c");
    private final JButton alphabetButton_3 = new JButton("d");
    private final JButton alphabetButton_4 = new JButton("e");
    private final JButton alphabetButton_5 = new JButton("f");
    private final JButton alphabetButton_6 = new JButton("g");

    private AlphabetBag alphabetBag;
    private Game game;
    private JButton tileChosen;
    String setLetter = "";


    private ArrayList<String> hand = new ArrayList<String>();
    private JPanel rackPanel = new JPanel();

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
     * alphabet to the Board. The tile on AlphabetPanel will
     * be updated with a new alphabet from the bag.
     */
    public void setNewTile() {
        String letter = this.alphabetBag.getLetter();
        tileChosen.setText(letter);
        tileChosen = null;
    }

    // initialize the button panel
    private void initRackPanel() {
        rackPanel.add(alphabetButton);
        alphabetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isPlayerTurn()) {
                    JOptionPane.showMessageDialog(game.getGameBoard(), "Please wait for your turn!");
                    return;
                }
                setLetter = alphabetButton.getText();
                tileChosen = alphabetButton;
            }
        });

        rackPanel.add(alphabetButton_1);
        alphabetButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isPlayerTurn()) {
                    JOptionPane.showMessageDialog(game.getGameBoard(), "Please wait for your turn!");
                    return;
                }
                setLetter = alphabetButton_1.getText();
                tileChosen = alphabetButton_1;

            }
        });

        rackPanel.add(alphabetButton_2);
        alphabetButton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isPlayerTurn()) {
                    JOptionPane.showMessageDialog(game.getGameBoard(), "Please wait for your turn!");
                    return;
                }
                setLetter = alphabetButton_2.getText();
                tileChosen = alphabetButton_2;
            }
        });

        rackPanel.add(alphabetButton_3);
        alphabetButton_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isPlayerTurn()) {
                    JOptionPane.showMessageDialog(game.getGameBoard(), "Please wait for your turn!");
                    return;
                }
                setLetter = alphabetButton_3.getText();
                tileChosen = alphabetButton_3;
            }
        });

        rackPanel.add(alphabetButton_4);
        alphabetButton_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isPlayerTurn()) {
                    JOptionPane.showMessageDialog(game.getGameBoard(), "Please wait for your turn!");
                    return;
                }
                setLetter = alphabetButton_4.getText();
                tileChosen = alphabetButton_4;
            }
        });

        rackPanel.add(alphabetButton_5);
        alphabetButton_5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isPlayerTurn()) {
                    System.out.println(game.isPlayerTurn());
                    JOptionPane.showMessageDialog(game.getGameBoard(), "Please wait for your turn!");
                    return;
                }
                setLetter = alphabetButton_5.getText();
                tileChosen = alphabetButton_5;
            }
        });

        rackPanel.add(alphabetButton_6);
        alphabetButton_6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isPlayerTurn()) {
                    JOptionPane.showMessageDialog(game.getGameBoard(), "Please wait for your turn!");
                    return;
                }
                setLetter = alphabetButton_6.getText();
                tileChosen = alphabetButton_6;
            }
        });
    }

    //start first hand by displaying correct letters
    private void initHand() {
        alphabetButton.setText(hand.get(0));
        alphabetButton_1.setText(hand.get(1));
        alphabetButton_2.setText(hand.get(2));
        alphabetButton_3.setText(hand.get(3));
        alphabetButton_4.setText(hand.get(4));
        alphabetButton_5.setText(hand.get(5));
        alphabetButton_6.setText(hand.get(6));
    }

    // to forget the tile being chosen in the panel
    public void resetChosenTile() {
        this.tileChosen = null;
    }
}
