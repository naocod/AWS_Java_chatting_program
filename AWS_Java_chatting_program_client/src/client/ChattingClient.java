package client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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

import Dto.LoginReqDto;
import Dto.RequestDto;
import Dto.ResponseDto;
import lombok.Getter;

@Getter
public class ChattingClient extends JFrame {
	private static ChattingClient instance; 
		
	public static ChattingClient getInstance() {
		if(instance == null) {
			instance = new ChattingClient();
		}
		
		return instance;
	}
   
   private CardLayout mainCard;
   private JPanel mainPanel;
   private JTextField usernameField;
   private JTextField messageInput;
   
   private JList<String> userList;
   private DefaultListModel<String> userListModel;
   
   private JList<String> roomList;
   private DefaultListModel<String> chatListModel;

   private Socket socket;
   private Gson gson;
   private String username;
   private String chatListName;
   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               ChattingClient frame = ChattingClient.getInstance();
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
   private ChattingClient() {
      
      gson = new Gson();
      
      try {
         socket = new Socket("127.0.0.1", 9090);
         // 서버 통신
         
         
         JOptionPane.showMessageDialog(null, 
               socket.getInetAddress() + "서버 접속", 
               "접속성공", 
               JOptionPane.INFORMATION_MESSAGE);
         System.out.println("socket test");



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
      mainCard = new CardLayout();//mainCard 생성
      mainPanel.setLayout(mainCard);
      
      
      JPanel loginPanel = new JPanel();
      loginPanel.setBackground(new Color(255, 235, 0));
      mainPanel.add(loginPanel, "loginPanel");
      loginPanel.setLayout(null);
      
      ImageIcon panelIcon = new ImageIcon(getClass().getResource("/icon/kakaotalk_scale2.png"));
      JLabel kakaoIcon = new JLabel(panelIcon);
      kakaoIcon.setBounds(160, 215, 136, 101);
      loginPanel.add(kakaoIcon);
      
      usernameField = new JTextField();
//      usernameField.addKeyListener(new KeyAdapter() {
//         @Override
//         public void keyPressed(KeyEvent e) {
//            if(e.getKeyCode()==KeyEvent.VK_ENTER) {
//               mainCard.show(mainPanel, "roomListPanel");
//            }
//         }
//      });
      usernameField.setBounds(89, 453, 300, 42);
      loginPanel.add(usernameField);
      usernameField.setColumns(10);
      
      ImageIcon login_icon = new ImageIcon(getClass().getResource("/icon/kakao_login_medium_wide.png"));
      JButton loginButton = new JButton(login_icon);
      loginButton.addMouseListener(new MouseAdapter() {
    	  
    	  /**
    	   * @ 로그인
    	   */
    	  
         @Override
         public void mouseClicked(MouseEvent e) {
        	
            try {
           
            	
                // TODO : save username
            	username = usernameField.getText();
            	
            	
            	LoginReqDto loginReqDto = new LoginReqDto(username);
            	String loginReqDtoJson = gson.toJson(loginReqDto);
            	RequestDto requestDto = new RequestDto("login", loginReqDtoJson);
            	String requestDtoJson = gson.toJson(requestDto);
            	System.out.println(requestDto.getResource());
            	System.out.println(loginReqDtoJson);
            	
            	
            	OutputStream outputStream = socket.getOutputStream();
            	PrintWriter out = new PrintWriter(outputStream, true);
				out.println(requestDtoJson);
				

		         ClientRecive clientRecive = new ClientRecive(socket);
		         clientRecive.start();
		         
				
//				mainCard.show(mainPanel, "roomListPanel"); //roomListPanel로 화면전환
				
//				if ("no".equals(responseDto.getStatus())) {
//				    JOptionPane.showMessageDialog(null, "Username already exists", "Duplicate Username", JOptionPane.ERROR_MESSAGE);
//				} else {
//				    mainCard.show(mainPanel, "roomListPanel"); //roomListPanel로 화면전환
//				}
//				
            } catch (IOException e1) {
               e1.printStackTrace();
            }
            
         }
      });
      
      loginButton.setBorder(BorderFactory.createLineBorder(Color.gray));
      loginButton.setBounds(89, 512, 300, 42);
      loginPanel.add(loginButton);
      
      JPanel roomListPanel = new JPanel();
      roomListPanel.setBackground(new Color(255, 235, 0));
      roomListPanel.setForeground(new Color(0, 0, 0));
      mainPanel.add(roomListPanel, "roomListPanel");
      roomListPanel.setLayout(null);
      
      JScrollPane roomListScroll = new JScrollPane();
      roomListScroll.setBounds(98, 0, 366, 761);
      roomListPanel.add(roomListScroll);
      
      chatListModel = new DefaultListModel<>();
      roomList = new JList<String>(chatListModel);
      roomListScroll.setViewportView(roomList); // 방리스트
      

      
      ImageIcon panelIcon2 = new ImageIcon(getClass().getResource("/icon/kakaotalk_sharing_btn_medium2.png"));
      JLabel kakaoIcon2 = new JLabel(panelIcon2);
      kakaoIcon2.setBounds(19, 36, 60, 53);
      roomListPanel.add(kakaoIcon2);
      
      ImageIcon plus_icon = new ImageIcon(getClass().getResource("/icon/plus_icon.png"));
      JButton btnNewButton = new JButton(plus_icon);
      btnNewButton.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            chatListName = JOptionPane.showInputDialog(null, "방의 제목을 입력하시오.", "방 생성", JOptionPane.INFORMATION_MESSAGE);
            
         }
      });
      btnNewButton.setBackground(new Color(255, 255, 0));
      btnNewButton.setBounds(33, 110, 31, 31);
      roomListPanel.add(btnNewButton);
      
      
      JPanel chatPanel = new JPanel();
      chatPanel.setBackground(new Color(255, 235, 0));
      mainPanel.add(chatPanel, "chatPanel");
      chatPanel.setLayout(null);
      
      JScrollPane chatScroll = new JScrollPane();
      chatScroll.setBounds(0, 81, 464, 599);
      chatPanel.add(chatScroll);
      
      
      JScrollPane messageScroll = new JScrollPane();
      messageScroll.setBounds(0, 679, 398, 82);
      chatPanel.add(messageScroll);
      
      messageInput = new JTextField();
      messageScroll.setViewportView(messageInput);
      messageInput.setColumns(10);
      
      ImageIcon sendMsgImg = new ImageIcon(getClass().getResource("/icon/msg_put.png"));
      JButton sendButton = new JButton(sendMsgImg);
//      sendButton.setBorderPainted(false);
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
   
   private void sendRequest(String resource, String body) {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			
			RequestDto requestDto = new RequestDto(resource, body);
			
			out.println(gson.toJson(requestDto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   
   
		
}