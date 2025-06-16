package com.oceansense.controller;

import java.util.Map;  // Import Map

public class QuizSubmissionRequest {
    private String userName;
    private Map<String, Object> answers;  // Ensure Map is resolved here

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, Object> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Object> answers) {
        this.answers = answers;
    }
}
