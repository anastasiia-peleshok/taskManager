package com.beech.taskmanager.controller;

import com.beech.taskmanager.dto.RequestTaskDto;
import com.beech.taskmanager.dto.ResponseTaskDto;
import com.beech.taskmanager.entity.Status;
import com.beech.taskmanager.entity.Task;
import com.beech.taskmanager.mapper.TaskMapper;
import com.beech.taskmanager.service.TaskServiceImplementation;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskServiceImplementation taskService;

    @GetMapping()
    public ResponseEntity<List<ResponseTaskDto>> getAllTasks() {
        List<ResponseTaskDto> tasks = taskService.getAllTasks().stream().map(TaskMapper::toDto).toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{userEmail}/creator")
    public ResponseEntity<List<ResponseTaskDto>> getAllTasksByCreator(@PathVariable String userEmail) {
        List<ResponseTaskDto> tasks = taskService.getAllTasksByCreatorId(userEmail).stream().map(TaskMapper::toDto).toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{userEmail}/assingee")
    public ResponseEntity<List<ResponseTaskDto>> getAllTasksByAssignee(@PathVariable String userEmail) {
        List<ResponseTaskDto> tasks = taskService.getAllTasksByAssigneeId(userEmail).stream().map(TaskMapper::toDto).toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseTaskDto> getTask(@PathVariable Long taskId) {
        Task taskById = taskService.getTaskById(taskId);

        return new ResponseEntity<>(TaskMapper.toDto(taskById), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseTaskDto> createTask(@Nullable @RequestParam String assigneeEmail, @RequestBody RequestTaskDto requestTaskDto) {
        ResponseTaskDto taskDto;
        Task task = taskService.createTask(requestTaskDto);
        taskDto = TaskMapper.toDto(task);
        if (assigneeEmail != null) {
            taskDto = TaskMapper.toDto(taskService.assignTaskToUser(task.getId(), assigneeEmail));
            return new ResponseEntity<>(taskDto, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(taskDto, HttpStatus.CREATED);
    }

    @PostMapping("/{assigneeEmail}/{taskId}")
    public ResponseEntity<ResponseTaskDto> assignTask(@PathVariable String assigneeEmail, @PathVariable Long taskId) {
        ResponseTaskDto taskDto = TaskMapper.toDto(taskService.assignTaskToUser(taskId, assigneeEmail));
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ResponseTaskDto> updateTask(@PathVariable Long taskId, @RequestBody RequestTaskDto requestTaskDto) {
        ResponseTaskDto taskDto = TaskMapper.toDto(taskService.updateTask(requestTaskDto, taskId));
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    @PutMapping("/{taskId}/changeStatus")
    public ResponseEntity updateTask(@PathVariable Long taskId, @RequestParam Status status) {
        taskService.changeTaskStatus(taskId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
