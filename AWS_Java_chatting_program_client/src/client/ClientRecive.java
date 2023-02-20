package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;

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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
