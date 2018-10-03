package scrabble;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends JFrame{

    private Board gameBoard;
    private Main main;
    private AlphabetPanel alphabetPanel;
    private GameInfoBoard gameInfoBoard;
    private boolean playerTurn;

    public Game(Main main) {

        this.alphabetPanel = new AlphabetPanel(this);
        this.gameBoard = new Board(this);
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


    public void notifyCompletedTurn() {
        if (isHost()) return;
        this.main.client.sendToServer("finishedTurn#" + this.main.getPlayer());
    }

    public String getPlayerNextTurn(String playerThisTurn) {
        int index = this.getMembersList().indexOf(playerThisTurn);
        if (index == this.getMembersList().size() - 1) {
            return this.getMembersList().get(0);
        } else {
            return this.getMembersList().get(index + 1);
        }
    }

    public void notifyNextPlayer(String nextPlayer) {

        if (!isHost()) return;
        this.main.server.sendMessageToAll("nextPlayer#" + nextPlayer);
    }


    public void setPlayerTurn(boolean bool) {
        this.playerTurn = bool;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void notifySocreChanges(){


    }


    public Board getGameBoard() {
        return gameBoard;
    }

    public boolean isHost() {
        return main.isHost();
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
