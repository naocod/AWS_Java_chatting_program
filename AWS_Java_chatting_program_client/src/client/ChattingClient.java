package client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChattingClient extends JFrame {

	private JPanel mainPanel;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChattingClient frame = new ChattingClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChattingClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 800);
		
		ImageIcon frameIcon = new ImageIcon("icon/kakaotalk_sharing_btn_small.png");
	    setIconImage(frameIcon.getImage());
		
		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		mainPanel.setBorder(null);

		setContentPane(mainPanel);
		mainPanel.setLayout(new CardLayout(0, 0));
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBackground(new Color(255, 235, 0));
		mainPanel.add(loginPanel, "name_176944769323700");
		loginPanel.setLayout(null);
	}

}
