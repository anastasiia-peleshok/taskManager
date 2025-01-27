package com.beech.taskmanager.service;

import com.beech.taskmanager.dto.RequestTaskDto;
import com.beech.taskmanager.entity.Status;
import com.beech.taskmanager.entity.Task;
import com.beech.taskmanager.entity.User;
import com.beech.taskmanager.exception.EntityNotFoundException;
import com.beech.taskmanager.mapper.TaskMapper;
import com.beech.taskmanager.repository.TaskRepository;
import com.beech.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImplementation extends BaseService implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImplementation(UserRepository userRepository, TaskRepository taskRepository) {
        super(userRepository);
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task with id: " + id + "not found"));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getAllTasksByAssigneeId(String userEmail) {
        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + userEmail + "not found"));

        List<Task> tasksByUserId = taskRepository.findByAssigneeId(user.getId());
        if (tasksByUserId.isEmpty()) {
            throw new EntityNotFoundException("User with email: " + userEmail + " do not have any tasks assigned on");
        }
        return tasksByUserId;
    }

    @Override
    public List<Task> getAllTasksByCreatorId(String userEmail) {
        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + userEmail + "not found"));

        List<Task> tasksByUserId = taskRepository.findByCreatedById(user.getId());
        if (tasksByUserId.isEmpty()) {
            throw new EntityNotFoundException("User with email: " + userEmail + " do not create any tasks");
        }
        return tasksByUserId;
    }

    @Override
    public Task createTask(RequestTaskDto requestTaskDto) {
        Task task = TaskMapper.toTask(requestTaskDto);
        User currentUser = getCurrentUser();
        task.setCreatedBy(currentUser);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(RequestTaskDto requestTaskDto, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + taskId + " not found"));
        if (!getCurrentUser().getId().equals(task.getCreatedBy().getId())) {
            throw new RuntimeException("You are not the owner of this task");
        }
        task.setTitle(requestTaskDto.getTitle());
        task.setDescription(requestTaskDto.getDescription());
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Override
    public void changeTaskStatus(Long taskId, Status status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + taskId + " not found"));
        if (!getCurrentUser().getId().equals(task.getCreatedBy().getId())) {
            throw new RuntimeException("You are not the owner of this task");
        }
        task.setStatus(status);
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + taskId + " not found"));
        if (!getCurrentUser().getId().equals(task.getCreatedBy().getId())) {
            throw new RuntimeException("You are not the owner of this task");
        }
        taskRepository.delete(task);
    }

    @Override
    public Task assignTaskToUser(Long taskId, String userEmail) {
        if (userRepository.findByEmail(userEmail).isEmpty()) {
            throw new EntityNotFoundException("User with email: " + userEmail + "not found");
        }

        if (taskRepository.findById(taskId).isEmpty()) {
            throw new EntityNotFoundException("Task with id: " + taskId + "not found");
        }
        Task task = taskRepository.findById(taskId).get();
        User assignee = userRepository.findByEmail(userEmail).get();
        task.setAssignee(assignee);
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
}
