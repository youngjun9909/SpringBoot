package org.example.springbootdeveloper.service;

import org.example.springbootdeveloper.dto.StudentDto;
import org.example.springbootdeveloper.entity.Student;
import org.example.springbootdeveloper.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service // 비즈니스 로작을 처리하는 역할
public class StudentService {
    private final StudentRepository studentRepository;

    // 생성자 주입 (DI)
    public StudentService (StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        // 생성자를 통해 의존성 주입
        // : 객체 간의 결합도 낮춤 + 테스트 용이
    }


    // 1) 모든 학생 조회
    public List<StudentDto> getAllStudents() {
        try{
            // 모든 학생 데이터를 가져옴
            List<Student> students = studentRepository.findAll();
            List<StudentDto> studentDto = students.stream() // stream API로 데이터 처리
                    .map(student -> new StudentDto (
                            student.getId(),
                            student.getName(),
                            student.getEmail()
                    )).collect(Collectors.toList());
            return studentDto;

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while fetching students", e
            ); // 데이터 조회 중 에러 발생 시 예외 처리
        }
    }
    // 2) 특정 학생 조회
    public StudentDto getStudentById(Long id) {
        try{
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new Error("Student Not Found With Id: "+ id ));

            StudentDto studentDto = new StudentDto(
                    student.getId(),
                    student.getName(),
                    student.getEmail()
            );
            return studentDto;
        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while fetching student", e
            ); // 데이터 조회 중 에러 발생 시 예외 처리
        }
    }

    // 3) 학생 정보 등록
    public StudentDto createStudent(StudentDto studentDto) {
        try{
            // StudentDto 에서 전달된 데이터를 사용하여 Entity 생성
            Student student = new Student(studentDto.getName(), studentDto.getEmail());

            // 생성한 Student 객체를 DB에 저장
            Student savedStudent = studentRepository.save(student);

            // 저장된 Student 객체를 StudentDto로 변환하여 반환
            return new StudentDto(savedStudent.getId(), savedStudent.getName(), savedStudent.getEmail());

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Failed to create student",e
            ); // 등록 중 에러 발생 시 예외 처리
        }
    }

    // 4) 학생 정보 수정
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        try{
            // 수정할 학생 데이터를 ID로 조회
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new Error("Student Not Found With Id: "  + id));

            // 학생 정보 수정
            student.setName(studentDto.getName());
            student.setEmail(studentDto.getEmail());

            // 수정된 내용을 DB에 저장
            Student updateStudent = studentRepository.save(student);

            // 수정된 객체를 DTO로 변환하여 반환
            return new StudentDto(updateStudent.getId(),updateStudent.getName(),updateStudent.getEmail());

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while updating student",e
            );
        }
    }
    // 특정 id 학생 삭제
    public void deleteStudent(Long id) {
        try{
            // 삭제할 학생 데이터를 ID로 조회
            Student student = studentRepository.findById(id)
                    .orElseThrow(() ->
                            new Error("Student Not Found With ID: " + id));

            // 조회한 학생 객체를 DB에서 삭제
            studentRepository.delete(student);

        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while deleting student",e
            );
        }
    }
}
