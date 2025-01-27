package com.beech.taskmanager.service;

import com.beech.taskmanager.dto.RequestTaskDto;
import com.beech.taskmanager.entity.Status;
import com.beech.taskmanager.entity.Task;

import java.util.List;

public interface TaskService {
    Task getTaskById(Long id);

    List<Task> getAllTasks();

    List<Task> getAllTasksByAssigneeId(String userEmail);

    List<Task> getAllTasksByCreatorId(String userEmail);

    Task createTask(RequestTaskDto requestTaskDto);

    Task updateTask(RequestTaskDto requestTaskDto, Long id);

    void deleteTask(Long id);

    Task assignTaskToUser(Long taskId, String userEmail);

    void changeTaskStatus(Long taskId, Status status);
}
