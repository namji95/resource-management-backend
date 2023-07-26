package com.douzone.wehago.dto;

import com.douzone.wehago.jwt.TokenDTO;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDTO {

    private TokenDTO tokenDTO;
    private int statusCode;
    private String message;

    private UserDTO userDTO;

}
