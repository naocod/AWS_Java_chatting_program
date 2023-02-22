package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import Dto.LoginReqDto;
import Dto.RequestDto;
import Dto.ResponseDto;
import chatRoom.Room;
import lombok.Data;

@Data
public class ConnectedSocket extends Thread{
	private static List<ConnectedSocket> socketList = new ArrayList<>();
	private static List<Room> rooms = new ArrayList<>();
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private Gson gson;
	
	private String username, roomTitle;
	
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
//					case "login":
//						LoginReqDto loginReqDto = gson.fromJson(requestDto.getBody(), LoginReqDto.class);
//						System.out.println(loginReqDto);
//						username = loginReqDto.getUsername();
//
//						
//						List<String> connectedUsers = new ArrayList<>();
//					    boolean isUsernameDuplicated = false;
//					    
//					    for(String name : userList) {
//					        if(name.equals(username)) {
//					            isUsernameDuplicated = true;
//					            break;
//					        }
//					        connectedUsers.add(name);
//					    }
//					    
//					    // username 중복일 경우
//					    if (isUsernameDuplicated) {
//					    	System.out.println("이미 존재하는 이름");
//					    	 ResponseDto responseDto = new ResponseDto(requestDto.getResource(), "no", null);
//					        for(ConnectedSocket connectedSocket : socketList) {
//						    	System.out.println(connectedSocket);
//						    	System.out.println(username);
//						    	if(connectedSocket.getInputStream().equals(inputStream)) {
//									OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
//									PrintWriter out = new PrintWriter(outputStream, true);
//									
//									out.println(gson.toJson(responseDto));
//								}
//							}
//					        return;
//					    }
//					    
//					    // username > userList에 추가
//					    userList.add(username);
//					    
//					    ResponseDto responseDto = new ResponseDto(requestDto.getResource(), "ok", gson.toJson(null));
//					    for(ConnectedSocket connectedSocket : socketList) {
//					    	System.out.println(socketList);
//							if(connectedSocket.getInputStream().equals(inputStream)) {
//								OutputStream outputStream = connectedSocket.getSocket().getOutputStream();
//								PrintWriter out = new PrintWriter(outputStream, true);
//								
//								out.println(gson.toJson(responseDto));
//							}
//						}
//					    System.out.println(username + "님이 접속하셨습니다.");
//					    break;
//				    
				case "login":
					LoginReqDto loginReqDto = gson.fromJson(requestDto.getBody(), LoginReqDto.class);
					
					List<String> usernameList = new ArrayList<>();
					socketList.forEach(connectedSocket -> {
						usernameList.add(connectedSocket.getUsername());
					});
					
					// username 공백검사 || 중복검사
					
					if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty() || usernameList.contains(loginReqDto.getUsername())) {
						System.out.println("Username already exists");
						ResponseDto usernameResponseDto = new ResponseDto(requestDto.getResource(), "no", null);
						try {
							outputStream = socket.getOutputStream();
							PrintWriter out = new PrintWriter(outputStream, true);
							out.println(gson.toJson(usernameResponseDto));
						} catch (IOException e) {
							e.printStackTrace();
						}
						continue;
//						return;
					}
					username = loginReqDto.getUsername();
					
					ResponseDto responseDto = new ResponseDto(requestDto.getResource(), "ok", gson.toJson(null));
					try {
						outputStream = socket.getOutputStream();
						PrintWriter out = new PrintWriter(outputStream, true);
						out.println(gson.toJson(responseDto));
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println(username + "님이 접속하셨습니다.");
					break;

					
				case "createRoom":
					String roomName = requestDto.getBody();
					Room room = new Room(roomName, username);
					rooms.add(room);
					ResponseDto createSuccessResponseDto = new ResponseDto("createSuccess", "ok", room.getRoomTitle());
					sendToMe(createSuccessResponseDto);
					reflashRoomList();
					break;
					
//					RoomReqDto roomReqDto = gson.fromJson(requestDto.getBody(), RoomReqDto.class);
//					System.out.println(roomReqDto);
//					roomTitle = roomReqDto.getRoomTitle();
					
					// roomTitle 공백검사 || 중복검사
//					if (roomTitle == null || roomTitle.isEmpty() || roomList.contains(roomTitle)) {
//						System.out.println("RoomTitle already exists");
//						ResponseDto roomResponseDto = new ResponseDto(requestDto.getResource(), "no", null);
//						try {
//							outputStream = socket.getOutputStream();
//							PrintWriter out = new PrintWriter(outputStream, true);
//							out.println(gson.toJson(roomResponseDto));
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						continue;
//					}
//
//					// username > userList에 추가
//					roomList.add(roomTitle);
//
//					ResponseDto roomResponseDto = new ResponseDto(requestDto.getResource(), "ok", gson.toJson(null));
//					try {
//						outputStream = socket.getOutputStream();
//						PrintWriter out = new PrintWriter(outputStream, true);
//						out.println(gson.toJson(roomResponseDto));
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//
//					System.out.println(roomTitle + "방이 생성되었습니다.");
//					break;

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
	private void reflashRoomList() {
		List<String> roomTitleList = new ArrayList<>();
		rooms.forEach(room -> {
			roomTitleList.add(room.getRoomTitle());
		});
		ResponseDto responseDto = new ResponseDto("reflashRoom", "ok", gson.toJson(roomTitleList));
	}
	private void sendToMe(ResponseDto responseDto) {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			out.println(gson.toJson(responseDto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void sendToAll(ResponseDto responseDto, List<ConnectedSocket> connectedSockets) {
		try {
			for(ConnectedSocket connectedSocket : connectedSockets) {
			OutputStream outputStream;
			outputStream = connectedSocket.getSocket().getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			
			out.println(gson.toJson(responseDto));
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
