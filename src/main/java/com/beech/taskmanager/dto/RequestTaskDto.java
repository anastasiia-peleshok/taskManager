package com.beech.taskmanager.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestTaskDto {
    private String title;
    private String description;
}
