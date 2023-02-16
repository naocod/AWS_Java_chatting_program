package client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChattingClient extends JFrame {

	private JPanel mainPanel;
	private JTextField usernameField;
	

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
		
		// 프레임 바
		ImageIcon frameIcon = new ImageIcon(getClass().getResource("/icon/kakaotalk_sharing_btn_small.png"));
		setIconImage(frameIcon.getImage());
		
		setTitle("kakao talk");
		
		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		mainPanel.setBorder(null);

		setContentPane(mainPanel);
		mainPanel.setLayout(new CardLayout(0, 0));
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBackground(new Color(255, 235, 0));
		mainPanel.add(loginPanel, "loginPanel");
		loginPanel.setLayout(null);
		
		ImageIcon panelIcon = new ImageIcon(getClass().getResource("/icon/kakaotalk_scale2.png"));
		JLabel kakaoIcon = new JLabel(panelIcon);
		kakaoIcon.setBounds(160, 215, 136, 101);
		loginPanel.add(kakaoIcon);
		
		usernameField = new JTextField();
		usernameField.setBounds(89, 453, 300, 42);
		loginPanel.add(usernameField);
		usernameField.setColumns(10);
		
		ImageIcon login_icon = new ImageIcon(getClass().getResource("/icon/kakao_login_medium_wide.png"));
		JButton loginButton = new JButton(login_icon);
		loginButton.setBorder(BorderFactory.createLineBorder(Color.gray));
		loginButton.setBounds(89, 512, 300, 42);
		loginPanel.add(loginButton);
	}
}








