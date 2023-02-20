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

import com.google.gson.Gson;

import lombok.Data;

@Data
class ConnectedSocket extends Thread{
	private static List<ConnectedSocket> socketList = new ArrayList<>();
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private Gson gson;
	
	private String username;
	
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
			   
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
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
