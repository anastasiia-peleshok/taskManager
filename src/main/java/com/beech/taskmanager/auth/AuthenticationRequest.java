package com.beech.taskmanager.auth;

import lombok.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;
}
