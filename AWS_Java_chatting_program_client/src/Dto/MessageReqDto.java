package Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageReqDto {
	private String roomTitle;
	private String messageValue;
}
