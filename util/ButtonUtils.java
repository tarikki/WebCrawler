package util;

import java.awt.Container;

/**
 * This class is used to easily add and customize button controls
 */
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ButtonUtils {
	/**
	 * Add a button to the given container, including an action listener and a text to be shown
	 * on the button
	 * @param Container c is the container to add the button to
	 * @param String title is the title to show in the button
	 * @param ActionListener listener is the event handler for the button
	 */

	public static void addButton(Container c, String title, ActionListener listener) {
		JButton button = new JButton(title);
		c.add(button);
		button.addActionListener(listener);
	}
}
