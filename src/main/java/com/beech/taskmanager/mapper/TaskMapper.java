package com.beech.taskmanager.mapper;

import com.beech.taskmanager.dto.RequestTaskDto;
import com.beech.taskmanager.dto.ResponseTaskDto;
import com.beech.taskmanager.entity.Status;
import com.beech.taskmanager.entity.Task;

import java.time.LocalDateTime;

public class TaskMapper {
    public static Task toTask (RequestTaskDto requestTaskDto) {
        Task task = new Task();
        task.setTitle(requestTaskDto.getTitle());
        task.setDescription(requestTaskDto.getDescription());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setStatus(Status.TO_DO);
        return task;
    }

    public static ResponseTaskDto toDto (Task task) {
        ResponseTaskDto responseTaskDto = new ResponseTaskDto();
        responseTaskDto.setId(task.getId());
        responseTaskDto.setTitle(task.getTitle());
        responseTaskDto.setDescription(task.getDescription());
        responseTaskDto.setCreatedAt(task.getCreatedAt());
        responseTaskDto.setUpdatedAt(task.getUpdatedAt());
        responseTaskDto.setStatus(task.getStatus());
        if(task.getAssignee()!=null){
            responseTaskDto.setAssignee(task.getAssignee().getEmail());
        }
        responseTaskDto.setCreatedBy(task.getCreatedBy().getEmail());
        return responseTaskDto;
    }
}
