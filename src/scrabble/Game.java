package scrabble;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * This class handles game logic and UI.
 */
public class Game extends JFrame{

    private Board gameBoard;
    private Main main;
    private AlphabetPanel alphabetPanel;
    private GameInfoBoard gameInfoBoard;
    private boolean playerTurn;
    private Vote vote;
    protected ArrayList<String> playerList;

    public Game(Main main) {

        this.alphabetPanel = new AlphabetPanel(this);
        this.gameBoard = new Board(this);
        this.gameInfoBoard = new GameInfoBoard(this);
        this.main = main;
        playerList = new ArrayList<>();

        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(this.gameBoard, BorderLayout.CENTER);
        parentPanel.add(this.alphabetPanel, BorderLayout.SOUTH);
        parentPanel.add(this.gameInfoBoard, BorderLayout.EAST);


        this.getContentPane().add(parentPanel);
        this.pack();
        this.setLocationRelativeTo(null);

    }


    /**
     * --------------------------------------Voting logic---------------------------------------------
     */

    /**
     * Initiate a vote. Should only be called by server
     */
    public void initVote(String voteInitiator, String word) {
        // Single player should not vote in reality

        // should only be called by server
        if (!isHost()) return;

        this.vote = new Vote(this.playerList.size(), voteInitiator, word);
        this.main.server.sendMessageToAll("initVote#" + voteInitiator);

    }


    public void notifyVoteResult(String result) {
        String commands = "voteOpinion#" + result;
        if (isHost()) {
            this.updateVote(result);
        } else {
            this.main.client.sendToServer(commands);
        }
    }

    // should only be called by host
    public void updateVote(String result) {
        // if already completed then do nothing
        if (vote.votingCompleted()) {
            return;
        }

        if (result.equals("Yes")) {
            this.vote.voteYes();
        } else {
            this.vote.voteNo();
        }

        // if completed
        if (vote.votingCompleted()) {
            if (this.vote.getResult()) {
                this.notifyWordCompleted(true);
                this.notifyScoreChanges();
            } else {
                this.notifyWordCompleted(false);
                // todo: notify user vote failed or ?
            }
        }
    }

    public void notifyInitVote(String index) {
        if (isHost()) {
            initVote(this.getCurrentPlayer(), this.getGameBoard().getSelectedWord());
            this.vote.setIndex(index);
            return;
        }
        this.main.client.sendToServer("initVote#" + this.getCurrentPlayer() + "#" +
                        this.getGameBoard().getSelectedWord() + "#" + index);
    }

    public void displayVote() {
        // player of this turn should not vote
        if (this.playerTurn) return;

        String result;
        int n = JOptionPane.showConfirmDialog(
                this.gameInfoBoard,
                "Do you accept this Word?",
                "Voting Process",
                JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) {
            result = "Yes";
        } else {
            result = "No";
        }
        this.notifyVoteResult(result);
    }

    public Vote getVote() {
        return this.vote;
    }

    /**
     * --------------------------------------Board Update---------------------------------------------
     */


    public void notifyBoardChanges(int rowNum,int columnNum, String letter) {
        if (main.isHost()) {
            this.main.server.sendMessageToAll("updateBoard#" +rowNum + "#"+ columnNum + "#" + letter);
            System.out.println("host notification sent");
        } else {
            this.main.client.sendToServer("updateBoard#" +rowNum + "#"+ columnNum + "#" + letter);
            System.out.println("client notification sent");
        }
    }

    public void notifyWordAttempted(int startRow, int startColumn, int endRow, int endColumn) {
        String command = "attemptedWord#" + startRow + "#" + startColumn + "#" + endRow + "#" +endColumn;
        if (isHost()) {
            this.gameBoard.highlightAttemptedWord(startRow, startColumn, endRow, endColumn);
            this.main.server.sendMessageToAll(command);
        } else {
            this.main.client.sendToServer(command);
        }
    }

    // this should only be called by host
    public void notifyWordCompleted(boolean result) {
        String command = "completedWord#" + result + "#" + this.vote.getIndex();
        String[] indices = this.vote.getIndex().split("#");
        this.main.server.sendMessageToAll(command);
        if (result) {
            this.gameBoard.highlightCompletedWord(Integer.parseInt(indices[0]), Integer.parseInt(indices[1]),
                    Integer.parseInt(indices[2]), Integer.parseInt(indices[3]));
        } else {
            this.gameBoard.setBackAttemptedWord(Integer.parseInt(indices[0]), Integer.parseInt(indices[1]),
                    Integer.parseInt(indices[2]), Integer.parseInt(indices[3]));
        }
    }


    /**
     * --------------------------------------GameInfo Update---------------------------------------------
     */

    private void notifyScoreChanges(){

        if (isHost()) {
            String command = "updateScore#" + this.vote.getInitiator() + "#" + this.vote.getTargetWord().length();
            this.main.server.sendMessageToAll(command);
            this.getGameInfoBoard().updateScore(this.vote.getInitiator(), this.vote.getTargetWord().length());
        }
    }


    /**
     * --------------------------------------Player Turn---------------------------------------------
     */

    public void notifyCompletedTurn(boolean pass) {
        if (isHost()) return;
        this.main.client.sendToServer("finishedTurn#" + this.main.getPlayer() + "#" + pass);
    }

    public String getPlayerNextTurn(String playerThisTurn) {
        int index = this.playerList.indexOf(playerThisTurn);
        if (index == this.playerList.size() - 1) {
            return this.playerList.get(0);
        } else {
            return this.playerList.get(index + 1);
        }
    }

    public void notifyNextPlayer(String nextPlayer) {
        if (!isHost()) return;
        this.main.server.sendMessageToAll("nextPlayer#" + nextPlayer);
    }


    public void setPlayerTurn(boolean bool) {
        this.playerTurn = bool;
        this.getGameInfoBoard().changeStatus(bool);
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }


    /**
     * --------------------------------------Universal----------------------------------------------
     */

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

    public boolean isGameRunning() {
        return main.isGameRunning();
    }

    public void addPass() {
        if (!isHost()) return;
        main.passCount++;
        if (main.passCount == this.playerList.size()) {
            main.notifyClientsEndGame();
            main.initCheckWinner();
        }
    }

}
