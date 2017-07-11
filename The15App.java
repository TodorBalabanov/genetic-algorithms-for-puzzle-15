
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

		status = new Label("The Game Fifteen by Todor Balabanov - tdb@tbsoft.eu");

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
