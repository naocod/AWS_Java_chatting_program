package client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.gson.Gson;

import lombok.Data;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
@Data
public class ChattingClient extends JFrame {
	
	private CardLayout mainCard;
	private JPanel mainPanel;
	private JTextField usernameField;
	private JTextField messageInput;
	
	private Socket socket;
	private Gson gson;
	private String username;
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
		
		gson = new Gson();
		
		try {
			socket = new Socket("127.0.0.1", 9090);
			JOptionPane.showMessageDialog(null, 
					socket.getInetAddress() + "서버 접속", 
					"접속성공", 
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ConnectException e1) {
			JOptionPane.showMessageDialog(null, 
					"서버 접속 실패", 
					"접속실패", 
					JOptionPane.ERROR_MESSAGE);
			
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
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
		usernameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		usernameField.setBounds(89, 453, 300, 42);
		loginPanel.add(usernameField);
		usernameField.setColumns(10);
		
		ImageIcon login_icon = new ImageIcon(getClass().getResource("/icon/kakao_login_medium_wide.png"));
		JButton loginButton = new JButton(login_icon);
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					socket = new Socket("127.0.0.1", 9090);
					
					mainCard.show(mainPanel, "chatListPanel");
					
				} catch (UnknownHostException e1) {
					
					e1.printStackTrace();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
				
			}
		});
		
		loginButton.setBorder(BorderFactory.createLineBorder(Color.gray));
		loginButton.setBounds(89, 512, 300, 42);
		loginPanel.add(loginButton);
		
		JPanel chatListPanel = new JPanel();
		chatListPanel.setBackground(new Color(255, 235, 0));
		chatListPanel.setForeground(new Color(0, 0, 0));
		mainPanel.add(chatListPanel, "name_1471051908969100");
		chatListPanel.setLayout(null);
		
		JScrollPane chatListScroll = new JScrollPane();
		chatListScroll.setBounds(98, 0, 366, 761);
		chatListPanel.add(chatListScroll);
		
		JList chatList = new JList();
		chatList.setBorder(null);
		chatListScroll.setViewportView(chatList);
		
		ImageIcon panelIcon2 = new ImageIcon(getClass().getResource("/icon/kakaotalk_sharing_btn_medium2.png"));
		JLabel kakaoIcon2 = new JLabel(panelIcon2);
		kakaoIcon2.setBounds(19, 36, 60, 53);
		chatListPanel.add(kakaoIcon2);
		
		ImageIcon plus_icon = new ImageIcon(getClass().getResource("/icon/plus_icon.png"));
		JButton btnNewButton = new JButton(plus_icon);
		btnNewButton.setBackground(new Color(255, 255, 0));
		btnNewButton.setBounds(33, 110, 31, 31);
		chatListPanel.add(btnNewButton);
		
		
		JPanel chatPanel = new JPanel();
		chatPanel.setBackground(new Color(255, 235, 0));
		mainPanel.add(chatPanel, "name_1471172583665600");
		chatPanel.setLayout(null);
		
		JScrollPane chatScroll = new JScrollPane();
		chatScroll.setBounds(0, 81, 464, 599);
		chatPanel.add(chatScroll);
		
		JTextArea chatView = new JTextArea();
		chatScroll.setViewportView(chatView);
		
		JScrollPane messageScroll = new JScrollPane();
		messageScroll.setBounds(0, 679, 398, 82);
		chatPanel.add(messageScroll);
		
		messageInput = new JTextField();
		messageScroll.setViewportView(messageInput);
		messageInput.setColumns(10);
		
		ImageIcon sendMsgImg = new ImageIcon(getClass().getResource("/icon/msg_put.png"));
		JButton sendButton = new JButton(sendMsgImg);
//		sendButton.setBorderPainted(false);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		sendButton.setBackground(new Color(255, 255, 255));
		sendButton.setBounds(398, 679, 66, 82);
		chatPanel.add(sendButton);
		
		ImageIcon outImg = new ImageIcon(getClass().getResource("/icon/exit_icon.png"));
		JButton outButton = new JButton(outImg);
		outButton.setBorder(BorderFactory.createLineBorder(Color.gray));
		outButton.setBounds(398, 14, 37, 39);
		chatPanel.add(outButton);
		
		ImageIcon panelIcon3 = new ImageIcon(getClass().getResource("/icon/kakaotalk_sharing_btn_medium2.png"));
		JLabel kakaoIcon3 = new JLabel(panelIcon3);
		kakaoIcon3.setBounds(33, 14, 57, 53);
		chatPanel.add(kakaoIcon3);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(new Color(255, 235, 0));
		textArea.setBounds(102, 24, 196, 32);
		chatPanel.add(textArea);
	}
}








