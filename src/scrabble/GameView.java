package scrabble;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame{


    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 9000;

    private board gameBoard;
    private AlphabetPanel alphabetPanel;
    private AlphabetBag alphabetBag;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI_main frame = new GUI_main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GameView() {
        this.setLayout(new BorderLayout());

        this.alphabetPanel = new AlphabetPanel(this);
        this.gameBoard = new board(this.alphabetPanel);
        JPanel parentPanel = new JPanel();
        parentPanel.add(this.gameBoard, BorderLayout.NORTH);
        parentPanel.add(this.alphabetPanel, BorderLayout.SOUTH);
        this.getContentPane().add(parentPanel);
        this.pack();
    }


    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_WIDTH);
    }
}
