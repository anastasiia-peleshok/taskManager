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
public class AuthenticationResponse {
    private String token;}
