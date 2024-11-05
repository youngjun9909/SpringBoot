package org.example.springbootdeveloper.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springbootdeveloper.entity.Task;

@Data
@NoArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String task;
    private boolean status;

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.task = task.getTask();
        this.status = task.isStatus();
    }


}
