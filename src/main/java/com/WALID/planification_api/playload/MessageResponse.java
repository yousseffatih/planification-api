package com.WALID.planification_api.playload;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageResponse {

	private String message;
	private String type;

    public MessageResponse(String message, String type) {
        this.message = message;
        this.type = type;
    }
}

