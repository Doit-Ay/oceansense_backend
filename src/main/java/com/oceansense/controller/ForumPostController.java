package com.oceansense.controller;

import com.oceansense.model.ForumPost;
import com.oceansense.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/forum")
@CrossOrigin(origins = "http://localhost:3000") // Adjust origin as per your frontend setup
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    // Get all forum posts
    @GetMapping("/posts")
    public ResponseEntity<List<ForumPost>> getAllPosts() {
        List<ForumPost> posts = forumPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // Create a new forum post
    @PostMapping("/create")
    public ResponseEntity<ForumPost> createPost(@RequestBody ForumPost post) {
        ForumPost createdPost = forumPostService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    // Delete a forum post by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        forumPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // Get a single forum post by ID
    @GetMapping("/post/{id}")
    public ResponseEntity<ForumPost> getPostById(@PathVariable Long id) {
        Optional<ForumPost> post = forumPostService.getPostById(id);
        return post.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
