package scrabble;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends JFrame{

    private board gameBoard;
    private Main main;
    private AlphabetPanel alphabetPanel;
    private GameInfoBoard gameInfoBoard;

    public Game(Main main) {


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
            this.main.server.sendMessageToAll("updateBoard#" +rowNum + "#"+ columnNum + "#" + letter);
            System.out.println("host notification sent");
        } else {
            this.main.client.sendToServer("updateBoard#" +rowNum + "#"+ columnNum + "#" + letter);
            System.out.println("client notification sent");
        }
    }


    public void notifySocreChanges(){


    }


    public board getGameBoard() {
        return gameBoard;
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


    public String getCurrentPlayer() {
        return main.getPlayer();
    }

    public ArrayList<String> getMembersList() {
        return main.getMemberList();
    }

}
