package scrabble;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameView extends JFrame{



    private board gameBoard;


    private AlphabetPanel alphabetPanel;
    private GameInfoBoard gameInfoBoard;
    private ArrayList<String> members;
    private String currentPlayer;

    public GameView() {

        this.members = new ArrayList<>();

        this.alphabetPanel = new AlphabetPanel(this);
        this.gameBoard = new board(this);
        this.gameInfoBoard = new GameInfoBoard(this);

        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(this.gameBoard, BorderLayout.CENTER);
        parentPanel.add(this.alphabetPanel, BorderLayout.SOUTH);
        parentPanel.add(this.gameInfoBoard, BorderLayout.EAST);

        this.getContentPane().add(parentPanel);
        this.pack();


    }

    public AlphabetPanel getAlphabetPanel() {
        return alphabetPanel;
    }

    public GameInfoBoard getGameInfoBoard() {
        return this.gameInfoBoard;
    }

    public void initInfoBoard() {
        this.gameInfoBoard.initPlayerInfo();
    }

    // this is hard coded for now
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void addMember(String memberName) {
        members.add(memberName);
        this.currentPlayer = members.get(0);
    }

    public ArrayList<String> getMembers() {
        return members;
    }
}
