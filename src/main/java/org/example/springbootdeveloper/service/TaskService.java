package org.example.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.common.constant.ResponseMessage;
import org.example.springbootdeveloper.dto.request.TaskRequestDto;
import org.example.springbootdeveloper.dto.response.ResponseDto;
import org.example.springbootdeveloper.dto.response.TaskResponseDto;
import org.example.springbootdeveloper.entity.Task;
import org.example.springbootdeveloper.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;


    // 할 일 생성
    public ResponseDto<TaskResponseDto> createTask(TaskRequestDto dto) {
        TaskResponseDto data = null;

        try{
            Task task = Task.builder()
                    .id(null)
                    .task(dto.getTask())
                    .status(false)
                    .build();

            taskRepository.save(task);

            data = new TaskResponseDto(task);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 할 일 전체 조회
    public ResponseDto<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> data = null;

        try{
            List<Task> tasks = taskRepository.findAll();

            data = tasks.stream()
                    .map(TaskResponseDto :: new)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS,data);
    }

    public ResponseDto<TaskResponseDto> updateTask(Long id) {
        TaskResponseDto data = null;

        try{
            Optional<Task> taskOptional = taskRepository.findById(id);

            if (taskOptional.isPresent()) {
                Task task = taskOptional.get();

                task.setStatus(!task.isStatus());

                taskRepository.save(task);
                data = new TaskResponseDto(task);
            } else {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    public ResponseDto<Void> deleteTask(Long id) {
        try{
            if(!taskRepository.existsById(id)) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }
            taskRepository.deleteById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }
}
