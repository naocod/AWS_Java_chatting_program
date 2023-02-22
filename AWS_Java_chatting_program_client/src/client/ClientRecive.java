package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import Dto.LoginRespDto;
import Dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor   //
public class ClientRecive extends Thread{
	
	private final Socket socket; //
	private InputStream inputStream;
	private Gson gson;
	
	@Override
	public void run() {
		try {
			inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			gson = new Gson();
			
			ChattingClient chattingClient = ChattingClient.getInstance();
			
			
			while(true) {
				String request = in.readLine();
				ResponseDto responseDto = gson.fromJson(request, ResponseDto.class);
				switch (responseDto.getResource()) {
					case "login":
						System.out.println("recive login");
						LoginRespDto loginRespDto = gson.fromJson(responseDto.getBody(), LoginRespDto.class);
						System.out.println(responseDto.getStatus());

						if(responseDto.getStatus().equalsIgnoreCase("no")) {
							JOptionPane.showMessageDialog(null, "Username already exists", "Duplicate Username", JOptionPane.ERROR_MESSAGE);

							break;
						} else if(responseDto.getStatus().equalsIgnoreCase("ok")) {
							chattingClient.getMainCard().show(chattingClient.getMainPanel(), "roomListPanel");
							break;
						}
						break;
					case "reflshRoom":
						List<String> roomTitleList = gson.fromJson(responseDto.getBody(), List.class);
						chattingClient.getChatListModel().clear();
						chattingClient.getChatListModel().addAll(roomTitleList);
						
						break;
					case "createSuccess" :
						String roomName = responseDto.getBody();
						chattingClient.getRoomTitle().setText(roomName); //이구간이 말썽..~~!~!!~!~!~!~!
						chattingClient.getMainCard().show(chattingClient.getMainPanel(), "chatPanel");
						System.out.println("test");
						break;
						
//						System.out.println("recive create room");
//						RoomRespDto roomRespDto = gson.fromJson(responseDto.getBody(), RoomRespDto.class);
//						System.out.println(responseDto.getStatus());
//						
//						if(responseDto.getStatus().equalsIgnoreCase("no")) {
//							JOptionPane.showMessageDialog(null, "RoomTitle already exists", "Duplicate RoomTitle", JOptionPane.ERROR_MESSAGE);
//							break;
//						} else if(responseDto.getStatus().equalsIgnoreCase("ok")) {
//							chattingClient.getMainCard().show(chattingClient.getMainPanel(), "chatPanel");
//							break;
//						}
//						break;
						
						
//					case "sendMessage":
//						MessageRespDto messageRespDto = gson.fromJson(responseDto.getBody(), MessageRespDto.class);
//						ChattingClient.getInstance().getContentView().append(messageRespDto.getMessageValue() + "\n");
					
						
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
