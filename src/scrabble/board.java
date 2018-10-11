package scrabble;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is the right board;
 */
public class Board extends JPanel {
	 

	private static final long serialVersionUID = 1L;

	private JPanel scorePanel = new JPanel();
	private JPanel gridPanel = new JPanel();
	private JButton[][] grid = new JButton[20][20];
	private JPanel[][] squares = new JPanel[20][20];
	private String setLetter = "";
	private String selectedWord = "";
	private boolean letterPlaced = false;
	private int selectedRow;
	private int selectedColumn;
	private boolean firstHand = true;


	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 800;
	private AlphabetPanel alphabetPanel;
	private Game game;




	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * Create the panel.
	 */
	public Board(Game game) {
		this.game = game;
		this.alphabetPanel = game.getAlphabetPanel();
		setLayout(new BorderLayout());


		initGUI();

		// set some padding between other components
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		setVisible(true);
	
	}

	private void initGUI() {
		initGrid();
		add(gridPanel);
		add(scorePanel, BorderLayout.NORTH);

	}


	public String getSelectedWord() {
		return this.selectedWord;
	}


	private void initGrid() {
		gridPanel.setLayout(new GridLayout(20, 20));

		for (int row = 0; row < 20; row++) {
			for (int column = 0; column < 20; column++) {
				//Square square = Board.getSquare(row, column);
				JPanel panel = new JPanel();
				JButton button = new JButton();

				button.setFont(new Font("Arial", Font.BOLD, 20));
				button.setBackground(Color.WHITE);

				//Row and column number can be sent to other players when it updates
				final int rowNum = row;
				final int columnNum = column;
				button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {

	            	if (!game.isPlayerTurn()) return;

					if (letterPlaced) {
						JOptionPane.showConfirmDialog(
								game.getGameInfoBoard(),
								"Please place one tile at each turn",
								"confirm",
								JOptionPane.DEFAULT_OPTION);
						return;
					}

					/**
					 * 'firstHand' is true by default, so if it's true we have to check all tiles to see
					 * if it's still true. Once any tile was occupied, 'firsthand' will be set to false.
					 * By doing so we can avoid looping through all tiles every time.
					 */
					if (firstHand) {
						// loop through all tiles
						if(!isFirstHand()) {
							firstHand = false;
						}
					}

	            	// warn user if tile is placed on wrong place
					if (!firstHand && !hasNeighbour(rowNum,columnNum) &&
							alphabetPanel.getTheChosenTile() != null) {

						JOptionPane.showConfirmDialog(
                        game.getGameInfoBoard(),
                        "Please place the tile near each other",
                        "confirm",
                        JOptionPane.DEFAULT_OPTION);
						return;
					}

					if (alphabetPanel.getTheChosenTile() != null) {
						setLetter = alphabetPanel.getTheChosenTile().getText();
					} else {
						return;
					}

					letterPlaced = true;

					if(button.getText().equals("") && !setLetter.equals("")) {

						button.setText(setLetter);
						button.setBackground(Color.cyan);
						button.setBorderPainted(false);
						button.setOpaque(true);
						selectedColumn = columnNum;
						selectedRow = rowNum;
	            		alphabetPanel.setNewTile();
						game.notifyBoardChanges(rowNum,columnNum,setLetter);

						setLetter = "";

	            	}
	            }
				});

				button.setMargin( new Insets(5, 5, 5, 5) );
				panel.setBackground(Color.white);
				panel.setSize(50, 50);
				panel.setLayout(new BorderLayout(0, 0));
				panel.setBorder(BorderFactory.createEtchedBorder());
				panel.add(button);
				squares[row][column] = panel;
				grid[row][column] = button;
				gridPanel.add(panel);
			}
		}
	}

	


	//Can be used to update Board when we implement that
	public void updateBoard(int rowNum,int columnNum, String letter) {
		grid[rowNum][columnNum].setText(letter);
		grid[rowNum][columnNum].setBackground(Color.cyan);
	}


	/**
	 * we check characters that adjacent to the the chosen tile from
	 * four directions: up, down, left, right.
	 * @param rowNum
	 * @param columnNum
	 */
	public void getWord(int rowNum,int columnNum) {

		// The chosen tile is the starting point.
		// There is only two cases: the word may be in the same row
		// or in the same column.
		String columnWord = grid[rowNum][columnNum].getText();
		String rowWord = grid[rowNum][columnNum].getText();

		int leftColumnNum = columnNum;
		int rightColumnNum = columnNum;
		int aboveRowNum = rowNum;
		int bottomRowNum = rowNum;

		// Select characters below the target tile
		for (int row = rowNum + 1; row < 20; row++) {
			if (!grid[row][columnNum].getText().equals("")) {
				columnWord = columnWord + grid[row][columnNum].getText();
				bottomRowNum = row;
			} else {
				break;
			}
		}

		// Select characters above the target tile
		for (int row = rowNum - 1; row >= 0; row--) {
			if (!grid[row][columnNum].getText().equals("")) {
				columnWord = grid[row][columnNum].getText() + columnWord;
				aboveRowNum = row;
			} else {
				break;
			}
		}

		// Select characters on the right of the target tile
		for (int column = columnNum + 1; column < 20; column++) {
			if (!grid[rowNum][column].getText().equals("")) {
				rowWord = rowWord + grid[rowNum][column].getText();
				rightColumnNum = column;
			} else {
				break;
			}
		}

		// Select characters on the left of the target tile
		for (int column = columnNum - 1; column >= 0; column--) {
			if (!grid[rowNum][column].getText().equals("")) {
				rowWord = grid[rowNum][column].getText() + rowWord;
				leftColumnNum = column;
			} else {
				break;
			}
		}


		String[] words = {rowWord, columnWord};

		String s = (String) JOptionPane.showInputDialog(
				game.getGameInfoBoard(),
				"Words found in two directions are:\n"
				,
				"Words found",
				JOptionPane.PLAIN_MESSAGE,
				null, words,
				words[0]);

		if (s == null) return;

		String index = "";
		this.selectedWord = s;


		if (s.equals(rowWord) ) {
			if (this.game.playerList.size() == 1) {
				this.highlightCompletedWord(rowNum, leftColumnNum, rowNum, rightColumnNum);
				this.game.getGameInfoBoard().updateScore(this.game.getCurrentPlayer(), selectedWord.length());
			} else {
				this.game.notifyWordAttempted(rowNum, leftColumnNum, rowNum, rightColumnNum);
				index = rowNum + "#" + leftColumnNum + "#" + rowNum + "#" + rightColumnNum;
				this.game.notifyInitVote(index);
			}
		} else {
			if (this.game.playerList.size() == 1) {
				this.highlightCompletedWord(aboveRowNum, columnNum, bottomRowNum, columnNum);
				this.game.getGameInfoBoard().updateScore(this.game.getCurrentPlayer(), selectedWord.length());
			} else {
				this.game.notifyWordAttempted(aboveRowNum, columnNum, bottomRowNum, columnNum);
				index = aboveRowNum + "#" + columnNum + "#" + bottomRowNum + "#" + columnNum;
				this.game.notifyInitVote(index);
			}
		}

	}


	// check whether this tile has neighbour
	public boolean hasNeighbour(int row, int column) {
		if (row - 1 >= 0 && !grid[row-1][column].getText().equals("")) return true;
		if (row + 1 < 20 && !grid[row+1][column].getText().equals("")) return true;
		if (column - 1 >= 0 && !grid[row][column-1].getText().equals("")) return true;
		if (column + 1 < 20 && !grid[row][column+1].getText().equals("")) return true;

		return false;
	}


	public void setBackAttemptedWord(int startRow, int startColumn, int endRow, int endColumn) {
		if (startRow == endRow) {
			for (int i = startColumn; i <= endColumn; i++) {
				grid[startRow][i].setBackground(Color.cyan);
				grid[startRow][i].setForeground(Color.BLACK);
			}
		} else {
			for (int i = startRow; i <= endRow; i++) {
				grid[i][endColumn].setBackground(Color.cyan);
				grid[i][endColumn].setForeground(Color.BLACK);
			}
		}
	}

	public void highlightCompletedWord(int startRow, int startColumn, int endRow, int endColumn) {
		if (startRow == endRow) {
			for (int i = startColumn; i <= endColumn; i++) {
				grid[startRow][i].setBackground(Color.PINK);
				grid[startRow][i].setForeground(Color.BLACK);
			}
		} else {
			for (int i = startRow; i <= endRow; i++) {
				grid[i][endColumn].setBackground(Color.PINK);
				grid[i][endColumn].setForeground(Color.BLACK);
			}
		}
	}


	public void highlightAttemptedWord(int startRow, int startColumn, int endRow, int endColumn) {
		if (startRow == endRow) {
			for (int i = startColumn; i <= endColumn; i++) {
				grid[startRow][i].setBackground(Color.RED);
				grid[startRow][i].setForeground(Color.WHITE);
			}
		} else {
			for (int i = startRow; i <= endRow; i++) {
				grid[i][endColumn].setBackground(Color.RED);
				grid[i][endColumn].setForeground(Color.WHITE);
			}
		}
	}


	// check whether it's first hand or not.
	public boolean isFirstHand() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				// find a non-empty tile
				if (!grid[i][j].getText().equals("")) {
					System.out.println("\nThis is not first hand\n");
					System.out.println(i + " " + j);
					return false;
				}
			}
		}
		return true;
	}

	public int getSelectedRow() {
		return this.selectedRow;
	}

	public int getSelectedColumn() {
		return this.selectedColumn;
	}

	public void setLetterPlaced(boolean bool) {
		this.letterPlaced = bool;
	}

	public boolean isLetterPlaced() {
		return letterPlaced;
	}

	public void setFirstHand(boolean bool) {
		this.firstHand = bool;
	}

}
