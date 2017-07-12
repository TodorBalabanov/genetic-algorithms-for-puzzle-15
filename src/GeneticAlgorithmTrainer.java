package src;
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

/**
 * Genetic algorithm trainer class.
 * 
 * @author Todor Balabanov
 * @author tdb@tbsoft.eu
 */
class GeneticAlgorithmTrainer {
	/**
	 * Training loops limit.
	 */
	private final int TRAIN_LOOPS = 100;

	/**
	 * Game reference.
	 */
	private The15 game;

	/**
	 * Genetic algorithm reference.
	 */
	private GeneticAlgorithm ga;

	/**
	 * Constructor.
	 * 
	 * @param game
	 *            Game object reference.
	 * @param ga
	 *            Genetic algorithm reference.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public GeneticAlgorithmTrainer(The15 game, GeneticAlgorithm ga) {
		this.game = game;
		this.ga = ga;
	}

	/**
	 * Train procedure.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void train() {
		int[][] state = game.getState();

		ga.init();

		for (int k = 0; k < TRAIN_LOOPS; k++) {
			ga.crossover();
			ga.mutate();

			int[][] population = ga.getPopulation();
			double[] fitness = new double[population.length];

			for (int i = 0; i < population.length; i++) {
				game.setState(state);

				for (int j = 0; j < population[i].length; j++) {
					game.makeMove(population[i][j]);
				}

				fitness[i] = game.checkDifference();
			}

			ga.setFitness(fitness);
			ga.select();

			game.setState(state);

			for (int j = 0; j < population[0].length; j++) {
				game.makeMove(population[0][j]);
			}

			if (fitness[0] == 0.0)
				return;
		}
	}
}
