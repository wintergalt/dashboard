package com.vgs.dashboard.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.vgs.dashboard.model.custom.HttpdGauge;

public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 794, 553);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnfile = new JMenu("File");
		mnfile.setMnemonic('F');
		menuBar.add(mnfile);

		JMenu mnedit = new JMenu("Edit");
		mnedit.setMnemonic('E');
		menuBar.add(mnedit);

		JMenu mntools = new JMenu("Tools");
		mntools.setMnemonic('T');
		menuBar.add(mntools);

		JMenu mnhelp = new JMenu("Help");
		mnhelp.setMnemonic('H');
		menuBar.add(mnhelp);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		this.initialize();

	}

	private void initialize() {

		HttpdGauge httpd = new HttpdGauge();

		this.getContentPane().add(httpd.getInstrument().getGauge(),
				BorderLayout.SOUTH);
		this.pack();

	}

}
