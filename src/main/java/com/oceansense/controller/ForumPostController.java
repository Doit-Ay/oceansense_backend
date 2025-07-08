package com.oceansense.controller;

import com.oceansense.model.ForumPost;
import com.oceansense.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/forum")

public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @GetMapping("/posts")
    public ResponseEntity<List<ForumPost>> getAllPosts() throws ExecutionException, InterruptedException {
        List<ForumPost> posts = forumPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/create")
    public ResponseEntity<ForumPost> createPost(@RequestBody ForumPost post) throws ExecutionException, InterruptedException {
        ForumPost createdPost = forumPostService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) throws ExecutionException, InterruptedException {
        forumPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<ForumPost> getPostById(@PathVariable String id) throws ExecutionException, InterruptedException {
        ForumPost post = forumPostService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
