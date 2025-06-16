package com.oceansense.controller;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;  // Import Map and List

@CrossOrigin(origins = "https://ocean-conservation-541d0.web.app/")
@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    // Store questions and correct answers in memory for simplicity (could be fetched from a database)
    private final List<Map<String, Object>> questions = new ArrayList<>();

    public QuizController() {
        // Question 1
        Map<String, Object> question1 = new HashMap<>();
        question1.put("id", "1");
        question1.put("text", "What is the largest ocean?");
        question1.put("options", List.of("Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"));
        question1.put("correctAnswer", "Pacific Ocean");
        questions.add(question1);

        // Question 2
        Map<String, Object> question2 = new HashMap<>();
        question2.put("id", "2");
        question2.put("text", "Which ocean is between Africa and Australia?");
        question2.put("options", List.of("Pacific Ocean", "Indian Ocean", "Southern Ocean", "Arctic Ocean"));
        question2.put("correctAnswer", "Indian Ocean");
        questions.add(question2);

        // Question 3
        Map<String, Object> question3 = new HashMap<>();
        question3.put("id", "3");
        question3.put("text", "Which ocean is the saltiest?");
        question3.put("options", List.of("Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"));
        question3.put("correctAnswer", "Atlantic Ocean");
        questions.add(question3);

        // Question 4
        Map<String, Object> question4 = new HashMap<>();
        question4.put("id", "4");
        question4.put("text", "What is the smallest ocean?");
        question4.put("options", List.of("Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"));
        question4.put("correctAnswer", "Arctic Ocean");
        questions.add(question4);

        // Question 5
        Map<String, Object> question5 = new HashMap<>();
        question5.put("id", "5");
        question5.put("text", "Which ocean is the deepest?");
        question5.put("options", List.of("Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "Southern Ocean"));
        question5.put("correctAnswer", "Pacific Ocean");
        questions.add(question5);

        // Question 6
        Map<String, Object> question6 = new HashMap<>();
        question6.put("id", "6");
        question6.put("text", "Which ocean is the most biodiverse?");
        question6.put("options", List.of("Pacific Ocean", "Indian Ocean", "Atlantic Ocean", "Southern Ocean"));
        question6.put("correctAnswer", "Pacific Ocean");
        questions.add(question6);

        // Question 7
        Map<String, Object> question7 = new HashMap<>();
        question7.put("id", "7");
        question7.put("text", "Which ocean is home to the Great Barrier Reef?");
        question7.put("options", List.of("Indian Ocean", "Pacific Ocean", "Atlantic Ocean", "Southern Ocean"));
        question7.put("correctAnswer", "Pacific Ocean");
        questions.add(question7);

        // Question 8
        Map<String, Object> question8 = new HashMap<>();
        question8.put("id", "8");
        question8.put("text", "Which ocean has the Bermuda Triangle?");
        question8.put("options", List.of("Pacific Ocean", "Indian Ocean", "Atlantic Ocean", "Arctic Ocean"));
        question8.put("correctAnswer", "Atlantic Ocean");
        questions.add(question8);

        // Question 9
        Map<String, Object> question9 = new HashMap<>();
        question9.put("id", "9");
        question9.put("text", "What percentage of Earth's surface is covered by oceans?");
        question9.put("options", List.of("50%", "60%", "70%", "80%"));
        question9.put("correctAnswer", "70%");
        questions.add(question9);

        // Question 10
        Map<String, Object> question10 = new HashMap<>();
        question10.put("id", "10");
        question10.put("text", "Which ocean current is the strongest?");
        question10.put("options", List.of("Gulf Stream", "Kuroshio Current", "California Current", "Antarctic Circumpolar Current"));
        question10.put("correctAnswer", "Antarctic Circumpolar Current");
        questions.add(question10); }

        @GetMapping("/questions")
    public Map<String, Object> getQuestions() {
        // Return the questions as a map
        Map<String, Object> response = new HashMap<>();
        response.put("questions", questions);
        return response;
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitAnswers(@RequestBody QuizSubmissionRequest quizSubmissionRequest) {
        String userName = quizSubmissionRequest.getUserName();
        Map<String, Object> userAnswers = quizSubmissionRequest.getAnswers();
        int score = 0;

        // Compare the user's answers to the correct answers
        for (Map<String, Object> question : questions) {
            String questionId = (String) question.get("id");
            String correctAnswer = (String) question.get("correctAnswer");
            String userAnswer = (String) userAnswers.get(questionId);

            // Check if the user's answer matches the correct answer
            if (correctAnswer.equals(userAnswer)) {
                score++;
            }
        }

        // Save the user's answers to Firebase (optional)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("quiz-responses");
        ref.push().setValueAsync(userAnswers);

        // Prepare the response with the score and user's name
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Quiz submitted successfully!");
        response.put("score", score);
        response.put("totalQuestions", questions.size());
        response.put("userName", userName);

        return ResponseEntity.ok(response);
    }
}
