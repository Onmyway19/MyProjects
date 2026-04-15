package com.studentms.dao;

import com.studentms.models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations.
 * Uses an in-memory ArrayList to store student records.
 */
public class StudentDAO {
    private final List<Student> students;
    private int nextId;

    public StudentDAO() {
        this.students = new ArrayList<>();
        this.nextId = 1;
    }

    /**
     * Adds a new student to the system.
     * @param name    Student name
     * @param email   Student email
     * @param course  Student course/programme
     * @param age     Student age
     * @return The created Student object
     */
    public Student addStudent(String name, String email, String course, int age) {
        Student student = new Student(nextId++, name, email, course, age);
        students.add(student);
        return student;
    }

    /**
     * Returns all students in the system.
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Deletes a student by their ID.
     * @param id The student ID to delete
     * @return true if the student was found and deleted, false otherwise
     */
    public boolean deleteStudent(int id) {
        return students.removeIf(s -> s.getId() == id);
    }

    /**
     * Finds a student by their ID.
     * @param id The student ID to search for
     * @return The Student if found, null otherwise
     */
    public Student findById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the total number of students.
     * @return student count
     */
    public int getStudentCount() {
        return students.size();
    }
}
