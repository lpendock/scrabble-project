package scrabble;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends JFrame{



    private board gameBoard;
    private Main main;

    private AlphabetPanel alphabetPanel;
    private GameInfoBoard gameInfoBoard;
    private ArrayList<String> members;
    private String currentPlayer;

    public Game(Main main) {

        this.members = new ArrayList<>();

        this.alphabetPanel = new AlphabetPanel(this);
        this.gameBoard = new board(this);
        this.gameInfoBoard = new GameInfoBoard(this);
        this.main = main;
        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(this.gameBoard, BorderLayout.CENTER);
        parentPanel.add(this.alphabetPanel, BorderLayout.SOUTH);
        parentPanel.add(this.gameInfoBoard, BorderLayout.EAST);

        this.getContentPane().add(parentPanel);
        this.pack();


    }

    public void notifyBoardChanges(int rowNum,int columnNum, String letter) {
        if (main.isHost()) {
            this.main.server.sendMessageToAll("board has been added with: " + letter);
            System.out.println("notification sent");
        }
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
