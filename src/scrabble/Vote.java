package scrabble;

/**
 * author: Zuodong
 */
public class Vote {

    private final double MAJORITY_RATIO = 0.6;
    private int majority = 0;
    private int playerNums;

    public void getMajority () {
        System.out.println(this.majority);
    }


    public Vote() {
        playerNums = 10;
        // get the ceiling value of player number
        this.majority = (int)((playerNums - 1) * MAJORITY_RATIO) + 1;
    }

    public static void main(String[] args) {
        Vote a = new Vote();
        a.getMajority();
    }
}
