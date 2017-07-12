package eu.veldsoft.puzzle.fifteen;

/*========================================================================
 =                                                                       =
 =     The Game Fifteen 0.51                                             =
 =     Written by: (c) Todor Balabanov - todor.balabanov@gmail.com       =
 =                                                                       =
 =     New Bulgarian University, Sofia, 2004-2017                        =
 =                                                                       =
 =========================================================================
 =                                                                       =
 =     This distribution is free, and comes with no                      =
 =     warranty. The program is open source provided under               =
 =     the GNU General Public License.                                   =
 =                                                                       =
 ========================================================================*/

import java.util.Random;

/**
 * Puzzle class.
 * 
 * @author Todor Balabanov
 * @author tdb@tbsoft.eu
 */
class The15 {
	/**
	 * Pseudo-random number generator.
	 */
	private static final Random prng = new Random();

	/**
	 * Blocks.
	 */
	private int[][] table;

	/**
	 * Solved state.
	 */
	private int[][] solved;

	/**
	 * Position as x and y.
	 */
	private int[] position;

	/**
	 * Constructor without parameters.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public The15() {
		table = new int[4][];

		for (int i = 0; i < table.length; i++) {
			table[i] = new int[4];
		}

		solved = new int[4][];
		for (int i = 0; i < solved.length; i++) {
			solved[i] = new int[4];
		}

		position = new int[2];

		init();
	}

	/**
	 * 
	 * 
	 * @return Text representation.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public String toString() {
		String text = "";

		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] < 10) {
					text += " ";
				}

				text += table[i][j] + " ";
			}

			text += "\n";
		}

		return (text);
	}

	/**
	 * Initialize.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void init() {
		for (int i = 0, k = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++, k++) {
				if (k == 15) {
					table[i][j] = Util.EMPTY;
				} else {
					table[i][j] = k + 1;
				}
			}
		}

		for (int i = 0, k = 0; i < solved.length; i++) {
			for (int j = 0; j < solved[i].length; j++, k++) {
				if (k == 15) {
					solved[i][j] = 0;
				} else
					solved[i][j] = k + 1;
			}
		}

		findEmptyCellPostion();
	}

	/**
	 * Mark empty cell position.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	private void findEmptyCellPostion() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == Util.EMPTY) {
					position[0] = i;
					position[1] = j;
					return;
				}
			}
		}
	}

	/**
	 * Check for possible valid move.
	 * 
	 * @param direction
	 *            Direction to check for.
	 * 
	 * @return True if there is valid move, false otherwise.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public boolean isValidMove(int direction) {
		boolean valid = false;

		switch (direction) {
		case (Util.RIGHT):
			if ((position[1] + 1) >= 0 && (position[1] + 1) < table[position[0]].length) {
				valid = true;
			}
			break;
		case (Util.DOWN):
			if ((position[0] + 1) >= 0 && (position[0] + 1) < table.length) {
				valid = true;
			}
			break;
		case (Util.LEFT):
			if ((position[1] - 1) >= 0 && (position[1] - 1) < table[position[0]].length) {
				valid = true;
			}
			break;
		case (Util.UP):
			if ((position[0] - 1) >= 0 && (position[0] - 1) < table.length) {
				valid = true;
			}
			break;
		}

		return (valid);
	}

	/**
	 * Game state getter.
	 * 
	 * @return Game state as integer matrix.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public int[][] getState() {
		int[][] state = new int[table.length][];

		for (int i = 0; i < state.length; i++) {
			state[i] = new int[table[i].length];
		}

		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				state[i][j] = table[i][j];
			}
		}

		return (state);
	}

	/**
	 * Game state setter.
	 * 
	 * @param state
	 *            New state.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void setState(int[][] state) {
		try {
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[i].length; j++) {
					table[i][j] = state[i][j];
				}
			}

			findEmptyCellPostion();
		} catch (Exception ex) {
			init();
		}
	}

	/**
	 * Check for solved puzzle.
	 * 
	 * @return True if it is solved, false otherwise.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public boolean isDone() {
		if (checkDifference() == 0.0) {
			return (true);
		}

		return (false);
	}

	/**
	 * Calculate space distnace.
	 * 
	 * @return How far is the solution to the perfect solution.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public double checkDifference() {
		// TODO Better space distance formula can be used.
		double difference = 0.0;

		int k = 1;
		for (int i = 0; i < table.length && i < solved.length; i++) {
			for (int j = 0; j < table[i].length && j < solved[i].length; j++, k++) {
				difference += Math.pow(solved[i][j] - table[i][j], 2);
			}
		}

		difference = Math.sqrt(difference) / k;

		return (difference);
	}

	/**
	 * Puzzle shuffle.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void shuffle() {
		int[] posTo = new int[2];
		posTo[0] = position[0];
		posTo[1] = position[1];

		for (int i = 0; i < 10000 || posTo[0] != position[0] || posTo[1] != position[1]; i++) {
			switch (prng.nextInt(4)) {
			case (0):
				makeMove(Util.RIGHT);
				break;
			case (1):
				makeMove(Util.DOWN);
				break;
			case (2):
				makeMove(Util.LEFT);
				break;
			case (3):
				makeMove(Util.UP);
				break;
			}
		}
	}

	/**
	 * Do move.
	 * 
	 * @param direction
	 *            In wich direction move to be done.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void makeMove(int direction) {
		switch (direction) {
		case (Util.RIGHT):
			makeMove(position[0], position[1] + 1);
			break;
		case (Util.DOWN):
			makeMove(position[0] + 1, position[1]);
			break;
		case (Util.LEFT):
			makeMove(position[0], position[1] - 1);
			break;
		case (Util.UP):
			makeMove(position[0] - 1, position[1]);
			break;
		}
	}

	/**
	 * Do move.
	 * 
	 * @param x
	 *            Cell x coordinate.
	 * @param y
	 *            Cell y coordinate.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void makeMove(int x, int y) {
		if (x < 0) {
			return;
		}
		if (y < 0) {
			return;
		}
		if (x >= table.length) {
			return;
		}
		if (y >= table[x].length) {
			return;
		}

		for (int i = x - 1; i <= x + 1; i++) {
			if (i < 0) {
				continue;
			}
			if (i >= table.length) {
				continue;
			}

			for (int j = y - 1; j <= y + 1; j++) {
				if (j < 0) {
					continue;
				}
				if (j >= table[i].length) {
					continue;
				}

				if (table[i][j] != Util.EMPTY) {
					/*
					 * Empty cell is not moving.
					 */
					continue;
				}
				if (x == i && y == j) {
					/*
					 * THere is no need to move cell to itself.
					 */
					continue;
				}
				if (x != i && y != j) {
					/*
					 * Only top, down, left and right postiotions are valid.
					 */
					continue;
				}

				int value;
				value = table[i][j];
				table[i][j] = table[x][y];
				table[x][y] = value;
				position[0] = x;
				position[1] = y;
				return;
			}
		}
	}
}
