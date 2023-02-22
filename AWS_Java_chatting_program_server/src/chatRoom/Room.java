package chatRoom;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import server.ConnectedSocket;
@Data
public class Room {
	private String roomTitle;
	private String headUser;	
	private List<ConnectedSocket> users;
	
	public Room(String roomTitle, String headUser) {
		this.roomTitle = roomTitle;
		this.headUser = headUser;
		this.users = new ArrayList<>();
	}
	
	
}
