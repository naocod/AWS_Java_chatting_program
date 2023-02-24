package chatRoom;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import Dto.ResponseDto;
import lombok.Data;
import server.ConnectedSocket;
@Data
public class Room {
	private Gson gson;
	private String roomTitle;
	private String headUser;	
	private List<ConnectedSocket> users;
	
	public Room(String roomTitle, String headUser) {
		this.roomTitle = roomTitle;
		this.headUser = headUser;
		this.users = new ArrayList<>();
	}
	
	
	public void sendToAll(ResponseDto responseDto) {
        for (ConnectedSocket connectedSocket : users) {
            OutputStream outputStream;
            try {
                outputStream = connectedSocket.getSocket().getOutputStream();
                PrintWriter out = new PrintWriter(outputStream, true);
                out.println(gson.toJson(responseDto));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
	

