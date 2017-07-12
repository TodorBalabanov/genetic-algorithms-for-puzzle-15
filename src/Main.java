package src;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

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
