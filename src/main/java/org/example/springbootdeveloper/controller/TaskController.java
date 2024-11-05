package org.example.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.common.constant.ApiMappingPattern;
import org.example.springbootdeveloper.dto.request.TaskRequestDto;
import org.example.springbootdeveloper.dto.response.ResponseDto;
import org.example.springbootdeveloper.dto.response.TaskResponseDto;
import org.example.springbootdeveloper.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.TASK)
public class TaskController {

    private final TaskService taskService;


    // 할 일 생성
    @PostMapping
    public ResponseEntity<ResponseDto<TaskResponseDto>> createTask(@RequestBody TaskRequestDto dto) {
        ResponseDto<TaskResponseDto> result = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 할 일 전체 조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<TaskResponseDto>>> getAllTasks() {
        ResponseDto<List<TaskResponseDto>> result = taskService.getAllTasks();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 할 일 수정
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<TaskResponseDto>> updateTask(@PathVariable Long id) {
        ResponseDto<TaskResponseDto> result = taskService.updateTask(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteTask(@PathVariable Long id) {
        ResponseDto<Void> result = taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
