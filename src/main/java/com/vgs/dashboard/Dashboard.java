package com.vgs.dashboard;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.vgs.dashboard.ui.MainFrame;

public class Dashboard {

	/**
	 * Launch the application.
	 */
	private void go() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// set the steel L&F
		System.getProperties().put("swing.metalTheme", "steel");

		Dashboard d = new Dashboard();
		d.go();
	}

}
