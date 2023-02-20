package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import Dto.LoginReqDto;
import Dto.LoginRespDto;
import Dto.MessageReqDto;
import Dto.MessageRespDto;
import Dto.RequestDto;
import Dto.ResponseDto;
import lombok.Data;

@Data
class ConnectedSocket extends Thread{
	private static List<ConnectedSocket> socketList = new ArrayList<>();
	private static List<String> userList = new ArrayList<>();
	private static List<String> roomList = new ArrayList<>();
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private Gson gson;
	
	private String username, roomname;
	
	public ConnectedSocket(Socket socket) {
		this.socket = socket;
		
		gson = new Gson();
		socketList.add(this);
	}
	
	
	
	@Override
	public void run() {
		try {
			inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			   
			while(true) {
				String request = in.readLine(); // requestDto(JSON)
				RequestDto requestDto = gson.fromJson(request, RequestDto.class);
				
				switch(requestDto.getResource()) {
					case "login":
						LoginReqDto loginReqDto = gson.fromJson(requestDto.getBody(), LoginReqDto.class);
						System.out.println(loginReqDto);
						username = loginReqDto.getUsername();

						
						List<String> connectedUsers = new ArrayList<>();
					    boolean isUsernameDuplicated = false;
					    
					    for(String name : userList) {
//					        String name = user;
//					        System.out.println(username);
//					        System.out.println(name);
					        if(name.equals(username)) {
					            isUsernameDuplicated = true;
					            break;
					        }
					        connectedUsers.add(name);
					    }
					    
					    
					    if (isUsernameDuplicated) {
					    	System.out.println("이미 존재하는 이름");
					        new ResponseDto(requestDto.getResource(), "no", null);
					        return;
					    }
					    
					    userList.add(username);
					    new ResponseDto(requestDto.getResource(), "ok", gson.toJson(null));
					    System.out.println(username + "님이 접속하셨습니다.");
					    break;
				    

//					case "join":
//						JoinReqDto joinReqDto = gson.fromJson(requestDto.getBody(), JoinReqDto.class);
//						username = joinReqDto.getUsername();
//						List<String> connectedUsers = new ArrayList<>();
//						
//						for(ConnectedSocket connectedSocket : socketList) {
//							connectedUsers.add(connectedSocket.getUsername());
//						}
//						
//						JoinRespDto joinRespDto = new JoinRespDto(username + "님이 접속하였습니다.", connectedUsers);
//						System.out.println(joinReqDto);
//						
////						// sendToAll(requestDto.getResource(), "ok", gson.toJson(joinRespDto));
//						break;
//					case "sendMessage" :
//						MessageReqDto messageReqDto = gson.fromJson(requestDto.getBody(), MessageReqDto.class);
//						
//						if(messageReqDto.getToUser().equalsIgnoreCase("all")) {
//							String message = messageReqDto.getFromUser() + "[전체]: " + messageReqDto.getMessageValue();
//							MessageRespDto messageRespDto = new MessageRespDto(message);
////							/sendToAll(requestDto.getResource(), "ok", gson.toJson(messageRespDto));
//						} else {
//							String message = messageReqDto.getFromUser() + "[" + messageReqDto.getToUser() + "]: " + messageReqDto.getMessageValue();
//							MessageRespDto messageRespDto = new MessageRespDto(message);
////							sendToUser(requestDto.getResource(), "ok", gson.toJson(messageRespDto), messageReqDto.getToUser());
//						}
//						
//						
//
//						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	private void checkUserName() {
//		
//	}
	
	
}

public class ServerApplication{
	public static void main(String[] args) {
		ServerSocket serversocket = null;

		
		try {
			serversocket = new ServerSocket(9090);
			System.out.println("==== 서버실행 ====");
			
			while(true) {
				Socket socket = serversocket.accept();
				ConnectedSocket connectedSocket = new ConnectedSocket(socket);
				connectedSocket.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}finally {
			if(serversocket != null) {
				try {
					serversocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("==== 서버종료 ====");
		}
	}
}
