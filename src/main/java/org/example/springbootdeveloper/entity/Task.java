package org.example.springbootdeveloper.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String task;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean status;

    @Builder
    public Task( Long id, String task, boolean status) {
        this.id = id;
        this.task = task;
        this.status = status;
    }
}
