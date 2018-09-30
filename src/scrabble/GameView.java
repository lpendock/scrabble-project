package scrabble;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame{



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


        this.alphabetPanel = new AlphabetPanel(this);
        this.gameBoard = new board(this.alphabetPanel);
        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(this.gameBoard, BorderLayout.CENTER);
        parentPanel.add(this.alphabetPanel, BorderLayout.SOUTH);
        this.getContentPane().add(parentPanel);
        this.pack();
    }



}
