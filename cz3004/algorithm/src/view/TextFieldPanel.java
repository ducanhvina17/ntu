/**
 * 
 */
package view;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

/**
 * JPanel that displays a text field that only accepts integers
 * 
 * @author Leroy Lim
 *
 */
public class TextFieldPanel extends JPanel {
	private static final long serialVersionUID = 8705116426777164162L;
	private static final int SIZE = 10;
	private JFormattedTextField formattedTextField;
	private JLabel textFieldLabel;

	public TextFieldPanel(String textLabel, int defaultValue) {
		super();
		textFieldLabel = new JLabel(textLabel);
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		formattedTextField = new JFormattedTextField(formatter);
		formattedTextField.setColumns(SIZE);
		formattedTextField.setText(Integer.toString(defaultValue));
		this.add(textFieldLabel);
		this.add(formattedTextField);
	}

	public int getTextValue() {
		return Integer.parseInt(formattedTextField.getText());
	}
}
