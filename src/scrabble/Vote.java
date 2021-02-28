package scrabble;

/**
 * Server should create a Vote instance when player initiate a vote.
 * author: Zuodong
 */
public class Vote {


    private int threshold;
    private int yesCount = 0;
    private int noCount = 0;
    private String initiator;
    private String targetWord;
    private String index;


    /**
     * The constructor should be provided with the number of player of the game
     * and the initiator of the Vote.We want the number of majority people to
     * be a reasonable integer, so we choose the ceiling value.
     * (floor value may lead to bad outcome)
     */
    public Vote(int numberOfPlayer, String playerName, String targetWord) {
        this.initiator = playerName;
        this.targetWord = targetWord;
        this.threshold = numberOfPlayer - 1;
    }


    public String getTargetWord() {
        return this.targetWord;
    }


    /**
     * store grid index of the target word.
     * @param index
     */
    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {
        return this.index;
    }
    /**
     * Return the initiator of this vote.
     * @return
     */
    public String getInitiator() {
        return this.initiator;
    }

    /**
     * This should be called only when the voting is completed
     * @return the final result of the vote
     */
    public boolean getResult(){
        if (yesCount >= noCount) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Server should use this method to check whether the vote has
     * been completed or not.
     * @return
     */
    public boolean votingCompleted() {

        if (yesCount + noCount == threshold) {
            return true;
        }
        // No result yet
        return false;
    }


    // Player click yes button
    public void voteYes() {
        yesCount++;
    }


    // Player click no button
    public void voteNo() {
        noCount++;
    }
}
