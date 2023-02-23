package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



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
