package batcher.green;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class View extends JFrame {

	private JLabel lblresult;
	private JLabel lbltarget;
	private JLabel lblOriginal;
	private JLabel lblInfo;

	public View(final Batcher batcher) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		lblresult = new JLabel("result");
		springLayout.putConstraint(SpringLayout.NORTH, lblresult, 10,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblresult, 10,
				SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblresult, -281,
				SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblresult, 202,
				SpringLayout.WEST, getContentPane());
		getContentPane().add(getResult());

		lbltarget = new JLabel("target");
		springLayout.putConstraint(SpringLayout.NORTH, lbltarget, 21,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lbltarget, -183,
				SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lbltarget, 183,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lbltarget, -31,
				SpringLayout.EAST, getContentPane());
		getContentPane().add(lbltarget);

		lblOriginal = new JLabel("original");
		springLayout.putConstraint(SpringLayout.NORTH, getOriginal(), 40,
				SpringLayout.SOUTH, lblresult);
		springLayout.putConstraint(SpringLayout.WEST, getOriginal(), -179,
				SpringLayout.EAST, lblresult);
		springLayout.putConstraint(SpringLayout.SOUTH, getOriginal(), 211,
				SpringLayout.SOUTH, lblresult);
		springLayout.putConstraint(SpringLayout.EAST, getOriginal(), 0,
				SpringLayout.EAST, lblresult);
		getContentPane().add(getOriginal());

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				batcher.onStop();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnStop, 77,
				SpringLayout.SOUTH, lbltarget);
		springLayout.putConstraint(SpringLayout.WEST, btnStop, 91,
				SpringLayout.EAST, lblOriginal);
		getContentPane().add(btnStop);

		lblInfo = new JLabel("New label");
		springLayout.putConstraint(SpringLayout.WEST, lblInfo, 10,
				SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblInfo, -26,
				SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblInfo, 462,
				SpringLayout.WEST, getContentPane());
		getContentPane().add(lblInfo);

	}

	public void setInfo(String info) {
		lblInfo.setText(info);
	}

	public JLabel getTarget() {
		return lbltarget;
	}

	public JLabel getResult() {
		return lblresult;
	}

	public JLabel getOriginal() {
		return lblOriginal;
	}

	public void appendInfo(String info) {
		lblInfo.setText(lblInfo.getText() + info);
	}
}
