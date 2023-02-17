package client;

import java.io.InputStream;
import java.net.Socket;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor   //
public class ClientRecive extends Thread{
	
	private final Socket socket; //
	private InputStream inputStream;
	private Gson gson;
	
	
}
