package scrabble;

/**
 * Server should create a Vote instance when player initate a vote.
 * author: Zuodong
 */
public class Vote {


    // 0.7 would be too high I guess
    private final double MAJORITY_RATIO = 0.6;
    private int majority;
    private int playerNums;
    private int yesCount = 0;
    private int noCount = 0;
    private boolean finalResult;
    private String initiator;


    // Just for testing, to see the calculating result.
    public void getMajority () {
        System.out.println(this.majority);
    }


    /**
     * The constructor should be provided with the number of player of the game
     * We want the number of majority people to be a reasonable integer, so we
     * choose the ceiling value. (floor value may lead to bad outcome)
     */
    public Vote(int numberOfPlayer, String playerName) {
        this.playerNums = numberOfPlayer;
        this.initiator = playerName;
        /**
         * Explanation for the formula (a little bit wordy):
         * The player who initiated the game should not
         * engage in voting, so the total player number has to minus one.
         * The double value was down cast to int value first, and then
         * plus one so we can get the ceiling value of original result.
         */
        this.majority = (int)((playerNums - 1) * MAJORITY_RATIO) + 1;
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
    public boolean getResult() {
        return finalResult;
    }


    /**
     * Server should use this method to check whether the vote has
     * been completed or not.
     * @return
     */
    public boolean votingCompleted() {

        // Majority has accepted the word
        if (yesCount >= majority) {
            finalResult = true;
            return true;
        }

        // Enough people vote against the word so that
        // the voting could be ended immediately.
        if (noCount > playerNums - majority) {
            finalResult = false;
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
