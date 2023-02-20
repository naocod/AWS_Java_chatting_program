package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;

import Dto.LoginRespDto;
import Dto.MessageRespDto;
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
			
			
			while(true) {
				System.out.println("while");
				
				String request = in.readLine();
				ResponseDto responseDto = gson.fromJson(request, ResponseDto.class);
				switch (responseDto.getResource()) {
					case "login":
						System.out.println("login");
						LoginRespDto loginRespDto = gson.fromJson(responseDto.getBody(), LoginRespDto.class);
						ChattingClient.getInstance().getUserListModel().addAll(loginRespDto.getConnectedUsers());
						ChattingClient.getInstance().getUserList().setSelectedIndex(0);;
						System.out.println(responseDto.getStatus());
						break;
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
