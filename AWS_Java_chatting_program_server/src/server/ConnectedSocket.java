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
//					System.out.println(username + "님이 " + roomName + "방을 생성하였습니다.");
					reflashRoomList();
					break;

				case "join":

					String selectRoom = requestDto.getBody();
					for (Room roomItem : rooms) {
						if (roomItem.getRoomTitle().equals(selectRoom)) {
							roomItem.getUsers().add(this);
							System.out.println(selectRoom + "방에" + username + "님이 입장하셨습니다.");
							System.out.println("현재 방 유저 리스트");
							System.out.println(roomItem.getUsers());
							ResponseDto responseDto = new ResponseDto("join", "ok", username);
							sendToRoom(responseDto, roomItem);
						}
						break;
					}
					break;

				case "sendMessage":
					MessageReqDto messageReqDto = gson.fromJson(requestDto.getBody(), MessageReqDto.class);

//						if(messageReqDto.getToUser().equalsIgnoreCase("all")) {
//							String message = messageReqDto.getFromUser() + "[전체]: " + messageReqDto.getMessageValue();
//							MessageRespDto messageRespDto = new MessageRespDto(message);
////							/sendToAll(requestDto.getResource(), "ok", gson.toJson(messageRespDto));
//						} else {
					String message = username + "  >   " + messageReqDto.getMessageValue();
					MessageRespDto messageRespDto = new MessageRespDto(message);
//					sendToAll(messageRespDto, );
//							sendToUser(requestDto.getResource(), "ok", gson.toJson(messageRespDto), messageReqDto.getToUser());
//						}

					break;
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
		sendToAll(responseDto, socketList);
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

	private void sendToRoom(ResponseDto responseDto, Room room) {
		try {
			if (room != null) { // room 객체가 null이 아닌 경우에만 응답을 보냅니다.
				for (ConnectedSocket user : room.getUsers()) {
					OutputStream outputStream = user.getSocket().getOutputStream();
					PrintWriter out = new PrintWriter(outputStream, true);
					out.println(gson.toJson(responseDto));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
