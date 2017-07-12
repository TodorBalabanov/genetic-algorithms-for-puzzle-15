package eu.veldsoft.puzzle.fifteen;

/**
 * Move direction constants.
 * 
 * @author Todor Balabanov
 */
enum Direction {
	RIGHT(1), DOWN(2), LEFT(4), UP(16);

	/**
	 * Numerical value of the constant.
	 */
	private int value = -1;

	/**
	 * Constructor with all parameters.
	 * 
	 * @param value
	 *            Numerical value of the constant.
	 */
	private Direction(int value) {
		this.value = value;
	}
}