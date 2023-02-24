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
import Dto.MessageReqDto;
import Dto.MessageRespDto;
import Dto.RequestDto;
import Dto.ResponseDto;
import chatRoom.Room;
import client.ChattingClient;
import lombok.Data;

@Data
public class ConnectedSocket extends Thread {
	private static List<ConnectedSocket> socketList = new ArrayList<>();
	private static List<Room> rooms = new ArrayList<>();
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private Gson gson;

	private Room room;
	private String username, roomTitle;

	public ConnectedSocket(Socket socket) {
		this.socket = socket;

		gson = new Gson();
		socketList.add(this);
	}
	/*
	 * Client { socket: socket, gson: new Gson(), username: String,
	 * joinedChatRoomName: String }
	 * 
	 * Rooms { RoomName: String, adminName: String }
	 * 
	 * 
	 * Server { RoomName: String, adminName: String, membersName: socket[], }
	 * 
	 */

	@Override
	public void run() {
		try {
			inputStream = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			while (true) {
				String request = in.readLine(); // requestDto(JSON)
				RequestDto requestDto = gson.fromJson(request, RequestDto.class);

				switch (requestDto.getResource()) {

				case "login":
					LoginReqDto loginReqDto = gson.fromJson(requestDto.getBody(), LoginReqDto.class);
					List<String> userList = new ArrayList<>();

					socketList.forEach(connectedSocket -> {
						userList.add(connectedSocket.getUsername());
					});

					// username 공백검사 || 중복검사

					if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty()
							|| userList.contains(loginReqDto.getUsername())) {
						System.out.println("Username already exists");
						ResponseDto unableUsernameResponseDto = new ResponseDto(requestDto.getResource(), "no", null);
						sendToMe(unableUsernameResponseDto);
						continue;
					}

					username = loginReqDto.getUsername();
					ResponseDto ableUsernameResponseDto = new ResponseDto(requestDto.getResource(), "ok",
							gson.toJson(null));
					sendToMe(ableUsernameResponseDto);
					System.out.println(username + "님이 접속하셨습니다.");
					reflashRoomList();
					break;

				case "createRoom":

					String roomName = requestDto.getBody();
					Room room = new Room(roomName, username);
					rooms.add(room);
					ResponseDto createSuccessResponseDto = new ResponseDto("createSuccess", "ok", room.getRoomTitle());
					sendToMe(createSuccessResponseDto);
					System.out.println(username + "님이 " + roomName + "방을 생성하였습니다.");
					reflashRoomList();
					break;

				case "join":
					String selectedRoom = requestDto.getBody();
					if (this.username == null) {
						ResponseDto getUsernameResponseDto = new ResponseDto("getUsername", "ok", ChattingClient.getInstance().getUsername());
						sendToMe(getUsernameResponseDto);
						RequestDto getUsernameRequestDto = gson.fromJson(in.readLine(), RequestDto.class);
						this.username = gson.fromJson(getUsernameRequestDto.getBody(), String.class);
					}
					joinRoom(selectedRoom, this.username);
					break;

				case "sendMessage":
					MessageReqDto messageReqDto = gson.fromJson(requestDto.getBody(), MessageReqDto.class);
					sendMessage(messageReqDto);
					break;

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reflashRoomList() {
		List<String> roomTitleList = new ArrayList<>();
		rooms.forEach(room -> {
			roomTitleList.add(room.getRoomTitle());
		});
		ResponseDto responseDto = new ResponseDto("reflashRoom", "ok", gson.toJson(roomTitleList));
		sendToAll(responseDto, socketList);
	}

	private void sendToMe(ResponseDto responseDto) {
		try {
			OutputStream outputStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outputStream, true);
			out.println(gson.toJson(responseDto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendToAll(ResponseDto responseDto, List<ConnectedSocket> connectedSockets) {
		try {
			for (ConnectedSocket connectedSocket : connectedSockets) {
				OutputStream outputStream;
				outputStream = connectedSocket.getSocket().getOutputStream();
				PrintWriter out = new PrintWriter(outputStream, true);

				out.println(gson.toJson(responseDto));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void joinRoom(String roomTitle, String username) {
	    for (Room room : rooms) {
	        if (room.getRoomTitle().equals(roomTitle)) {
	            room.getUsers().add(this);
	            if (this.username == null) { // username이 null이면 전역변수에서 가져옴
	                this.username = this.getUsername();
	            }
	            this.room = room;
	            ResponseDto responseDto = new ResponseDto("join", "ok", null);
	            sendToMe(responseDto);
	            ResponseDto joinedResponseDto = new ResponseDto("joined", "ok", gson.toJson(room));
	            sendToMe(joinedResponseDto);
	            sendToRoom(new ResponseDto("memberList", "ok", gson.toJson(room.getUsers())), roomTitle);
	            return;
	        }
	    }
	    // 방이 없을 경우 처리
	    ResponseDto responseDto = new ResponseDto("join", "no", null);
	    sendToMe(responseDto);
	    this.room = null; // null로 할당해주어야 함
	}

	private void sendMessage(MessageReqDto messageReqDto) {
		if (this.room == null) {
			return;
		}
		MessageRespDto messageRespDto = new MessageRespDto(username, messageReqDto.getMessageValue());
		ResponseDto responseDto = new ResponseDto("sendMessage", "ok", gson.toJson(messageRespDto));
		sendToRoom(responseDto, room.getRoomTitle());
	}

	private void sendToRoom(ResponseDto responseDto, String roomTitle) {
		for (Room room : rooms) {
			if (room.getRoomTitle().equals(roomTitle)) {
				room.sendToAll(responseDto);
				return;
			}
		}
	}
//	
//	private void sendMessage(MessageRespDto messageRespDto, String roomTitle) {
//		ResponseDto responseDto = new ResponseDto("sendMessage", "ok", gson.toJson(messageRespDto));
//		sendToRoom(responseDto, roomTitle);
//	}
//	
//
//	private void sendToRoom(ResponseDto responseDto, String roomTitle) {
//	    for (Room roomItem : rooms) {
//	        if (roomItem.getRoomTitle().equals(roomTitle)) {
//	            for (ConnectedSocket connectedSocket : roomItem.getUsers()) {
//	                connectedSocket.sendMessage(responseDto, roomTitle);
//	            }
//	            break;
//	        }
//	    }
//	}

//	private void joinRoom(String selectedRoom) {
//		for (Room roomItem : rooms) {
//			if (roomItem.getRoomTitle().equals(roomTitle)) {
//				roomItem.getUsers().add(this);
//				this.room = roomItem;
//				this.roomTitle = roomTitle;
//				System.out.println(roomTitle + "방에 " + username + "님이 입장하셨습니다.");
//				System.out.println("현재 방 유저 리스트");
//				System.out.println(roomItem.getUsers());
//				ResponseDto responseDto = new ResponseDto("join", "ok", username + "님이 입장했습니다.");
//				sendToRoom(responseDto, roomItem.getUsers());
//				break;
//			}
//		}
//	}

}
