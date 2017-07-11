/*============================================================
 =                                                           =
 =     The Game Fifteen 0.51                                 =
 =     Written by: � Todor Balabanov - tdb@tbsoft.eu         =
 =                                                           =
 =     New Bulgarian University, Sofia, 2004-2013            =
 =                                                           =
 =============================================================
 =                                                           =
 =     This distribution is free, and comes with no          =
 =     warranty. The program is open source provided under   =
 =     the GNU General Public License.                       =
 =                                                           =
 ============================================================*/

import java.applet.Applet;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * Global constants class.
 * 
 * @author Todor Balabanov
 * @author tdb@tbsoft.eu
 */
class Globals {
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 4;
	public static final int UP = 16;
	
	public static final int EMPTY = 16;
}

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
		for (int i = 0; values != null && i < fitness.length
				&& i < values.length; i++) {
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

/**
 * Application class.
 * 
 * @author Todor Balabanov
 * @author tdb@tbsoft.eu
 */
public class The15App extends Applet {
	private Button[][] pulls;
	private Button[] ops;
	private Panel[] panels;
	private Label status;
	private The15 game;
	private GeneticAlgorithm ga;
	private GeneticAlgorithmTrainer trainer;

	public void init() {
		game = new The15();
		ga = new GeneticAlgorithm();
		trainer = new GeneticAlgorithmTrainer(game, ga);

		setLayout(new GridLayout(2, 1));

		panels = new Panel[2];
		panels[0] = new Panel(new GridLayout(4, 4));
		panels[1] = new Panel(new GridLayout(4, 1));

		status = new Label(
				"The Game Fifteen by Todor Balabanov - tdb@tbsoft.eu");

		pulls = new Button[4][];
		for (int i = 0; i < pulls.length; i++)
			pulls[i] = new Button[4];

		ops = new Button[3];
		for (int i = 0; i < ops.length; i++) {
			ops[i] = new Button();
			ops[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if ((evt.getActionCommand()).equals("Shuffle"))
						game.shuffle();
					else if ((evt.getActionCommand()).equals("Reset"))
						game.init();
					else if ((evt.getActionCommand()).equals("Solve")) {
						// TODO Solving should be done in separate thread.
						trainer.train();
					}

					update();
				}
			});
			panels[1].add(ops[i]);
		}
		panels[1].add(status);

		ops[0].setLabel("Shuffle");
		ops[1].setLabel("Reset");
		ops[2].setLabel("Solve");

		for (int i = 0, k = 0; i < pulls.length; i++)
			for (int j = 0; j < pulls[i].length; j++, k++) {
				String caption = "";
				if (k == 15)
					caption = "  ";
				else {
					if (k + 1 < 10)
						caption = "0";

					caption += k + 1;
				}

				pulls[i][j] = new Button(caption);
				pulls[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						int a = 0, b = 0;
						Object source = evt.getSource();
						for (int m = 0; m < pulls.length; m++)
							for (int n = 0; n < pulls[m].length; n++)
								if (source == pulls[m][n]) {
									a = m;
									b = n;
								}

						game.makeMove(a, b);
						update();
					}
				});
			}

		for (int i = 0, k = 0; i < pulls.length; i++) {
			for (int j = 0; j < pulls[i].length; j++, k++) {
				panels[0].add(pulls[i][j]);
			}
		}

		for (int i = 0; i < panels.length; i++) {
			add(panels[i]);
		}

		update();
	}

	private void update() {
		int[][] state = game.getState();

		for (int i = 0; i < pulls.length; i++)
			for (int j = 0; j < pulls[i].length; j++) {
				String caption = "";

				if (state[i][j] == Globals.EMPTY)
					caption = "  ";
				else {
					if (state[i][j] < 10)
						caption = "0";
					caption += state[i][j];
				}

				pulls[i][j].setLabel(caption);
			}
	}

	public static void main(String[] args) {
		Frame theApp = new Frame("");
		theApp.setSize(380, 280);
		theApp.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		The15App the15 = new The15App();
		the15.init();
		the15.start();

		theApp.add(the15);
		theApp.setVisible(true);
	}
}
