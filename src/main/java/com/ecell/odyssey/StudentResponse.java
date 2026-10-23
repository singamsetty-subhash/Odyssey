package com.ecell.odyssey;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_responses")
public class StudentResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String teamName;

    @Column(nullable = false, length = 128)
    private String leaderName;

    @Column(nullable = false, length = 64)
    private String rollNumber;

    @Column(nullable = false, length = 64)
    private String dept;

    @Column(nullable = false, length = 128)
    private String email;

    @Column(nullable = false, length = 3000, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    public StudentResponse() {
    }

    public StudentResponse(String teamName, String leaderName, String rollNumber, String dept, String email, String message, LocalDateTime submittedAt) {
        this.teamName = teamName;
        this.leaderName = leaderName;
        this.rollNumber = rollNumber;
        this.dept = dept;
        this.email = email;
        this.message = message;
        this.submittedAt = submittedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
