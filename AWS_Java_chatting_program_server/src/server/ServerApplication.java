package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import Dto.RoomReqDto;
import lombok.Data;



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
