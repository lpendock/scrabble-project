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
    private Vote vote;

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


    /**
     * Initiate a vote. Should only be called by server
     */
    public void initVote(String voteInitiator) {
        // Single player should not vote in reality

        // should only be called by server
        if (!isHost()) return;

        this.vote = new Vote(this.getMembersList().size(), voteInitiator);
        this.main.server.sendMessageToAll("initVote#" + voteInitiator);

//        if (vote.votingCompleted()) {
//
//            if (vote.getResult() == true){
//                int score = this.gameBoard.getSelectedWord().length();
//                this.gameInfoBoard.updateScore(this.getCurrentPlayer(), score);
//                System.out.println(score);
//            } else {
//                JOptionPane.showMessageDialog(gameInfoBoard, "Error", "Vote failed", JOptionPane.ERROR_MESSAGE);
//            }
//        }
    }



    public void notifyInitVote() {
        if (isHost()) {
            initVote(this.getCurrentPlayer());
            return;
        }
        this.main.client.sendToServer("initVote#" + this.getCurrentPlayer());
    }

    public void displayVote() {
        // player of this turn show not vote
        if (this.playerTurn) return;

        int n = JOptionPane.showConfirmDialog(
                this.gameInfoBoard,
                "Do you accept this Word?",
                "Voting Process",
                JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) {

        } else {

        }

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


    public void notifyWordCompleted(int startRow, int startColumn, int endRow, int endColumn) {
        String command = "completedWord#" + startRow + "#" + startColumn + "#" + endRow + "#" +endColumn;
        if (isHost()) {
            this.gameBoard.highlightWord(startRow, startColumn, endRow, endColumn);
            this.main.server.sendMessageToAll(command);
        } else {
            this.main.client.sendToServer(command);
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

    public void notifyVoteResult(String result) {
        String commands = "voteOpinion#" + result;
        if (isHost()) {
            // change vote result locally
        } else {
            this.main.client.sendToServer(commands);
        }
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
