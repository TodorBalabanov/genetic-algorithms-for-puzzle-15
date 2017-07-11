import java.util.Random;

/**
 * Class for the genetic algorithm.
 * 
 * @author Todor Balabanov
 * @author tdb@tbsoft.eu
 */
class GeneticAlgorithm {
	/**
	 * Pseudo-random number generator.
	 */
	private static final Random prng = new Random();

	/**
	 * Population size
	 */
	private final int POPULATION_SIZE = 131;

	/**
	 * Average chromosome size.
	 */
	private final int AVERAGE_CHROMOSOME_SIZE = 150;

	/**
	 * Population
	 */
	private int[][] population;

	/**
	 * Fitness value for each chromosome.
	 */
	private double fitness[];

	/**
	 * Constructor without parameters.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public GeneticAlgorithm() {
		population = new int[4 * POPULATION_SIZE][];
		fitness = new double[4 * POPULATION_SIZE];
	}

	/**
	 * 
	 * @return String representation.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public String toString() {
		String str = "";

		for (int i = 0; i < fitness.length / 4 && i < population.length / 4; i++) {
			str += fitness[i] + "\t";

			for (int j = 0; j < population[i].length; j++)
				str += population[i][j] + " ";

			str += "\n";
		}

		return (str);
	}

	/**
	 * Generate random chromosome.
	 * 
	 * @return Chromosome with random size and values.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	private int[] generateRandomChromosome() {
		int[] chrom = null;

		chrom = new int[1 + prng.nextInt(AVERAGE_CHROMOSOME_SIZE)];
		for (int i = 0; i < chrom.length; i++)
			switch (prng.nextInt(4)) {
			case (0):
				chrom[i] = Globals.RIGHT;
				break;
			case (1):
				chrom[i] = Globals.DOWN;
				break;
			case (2):
				chrom[i] = Globals.LEFT;
				break;
			case (3):
				chrom[i] = Globals.UP;
				break;
			}

		return (chrom);
	}

	/**
	 * Crossover two chromosomes.
	 * 
	 * @param a
	 *            Parent 1.
	 * @param b
	 *            Parent 2.
	 * 
	 * @return Child.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	private int[] crossover(int[] a, int[] b) {
		int[] c = null;

		int min = Math.min(a.length, b.length);
		int max = Math.max(a.length, b.length);

		int pos = prng.nextInt(min + 1);

		if (a.length == max) {
			c = new int[min];
		} else if (b.length == max) {
			c = new int[max];
		}

		// TODO Better crossover than single cut point can be used.
		for (int i = 0; i < pos; i++) {
			c[i] = a[i];
		}

		for (int i = pos; i < c.length; i++) {
			c[i] = b[i];
		}

		return (c);
	}

	/**
	 * Fitness array setter.
	 * 
	 * @param values
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void setFitness(double[] values) {
		for (int i = 0; values != null && i < fitness.length && i < values.length; i++) {
			fitness[i] = values[i];
		}
	}

	/**
	 * Population getter.
	 * 
	 * @return Population cloning.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public int[][] getPopulation() {
		int[][] values = new int[population.length][];

		for (int i = 0; i < population.length; i++) {
			values[i] = new int[population[i].length];
		}

		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < population[i].length; j++) {
				values[i][j] = population[i][j];
			}
		}

		return (values);
	}

	/**
	 * Initialize.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void init() {
		for (int i = 0; i < population.length; i++) {
			population[i] = generateRandomChromosome();
		}
	}

	/**
	 * Do crossover into population.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void crossover() {
		for (int i = 0; i < population.length / 2; i++) {
			population[population.length / 2 + i] = crossover(population[i],
					population[prng.nextInt(population.length / 2)]);
		}
	}

	/**
	 * Do mutation into population.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void mutate() {
		for (int i = population.length / 2; i < population.length; i++) {
			int pos = prng.nextInt(population[i].length);

			switch (prng.nextInt(4)) {
			case (0):
				population[i][pos] = Globals.RIGHT;
				break;
			case (1):
				population[i][pos] = Globals.DOWN;
				break;
			case (2):
				population[i][pos] = Globals.LEFT;
				break;
			case (3):
				population[i][pos] = Globals.UP;
				break;
			}
		}
	}

	/**
	 * Survival selection into population.
	 * 
	 * @author Todor Balabanov
	 * @author tdb@tbsoft.eu
	 */
	public void select() {
		boolean done;
		do {
			done = true;

			for (int i = 0; i < population.length - 1; i++) {
				if (population[i] == null) {
					continue;
				}

				if (population[i + 1] == null) {
					continue;
				}

				if (fitness[i] <= fitness[i + 1]) {
					continue;
				}

				int[] buffer = population[i];
				double value = fitness[i];
				population[i] = population[i + 1];
				fitness[i] = fitness[i + 1];
				population[i + 1] = buffer;
				fitness[i + 1] = value;

				done = false;
			}
		} while (done == false);

		/*
		 * Renew second fourth into population.
		 */
		for (int i = population.length / 4; i < population.length / 2; i++) {
			population[i] = generateRandomChromosome();
		}

		/*
		 * Clear second half into population.
		 */
		for (int i = population.length / 2; i < population.length; i++) {
			population[i] = null;
		}
	}
}