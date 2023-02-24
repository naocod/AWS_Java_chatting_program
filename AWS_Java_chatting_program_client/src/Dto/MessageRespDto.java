package Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageRespDto {
	private String username;
	private String messageValue;

}
