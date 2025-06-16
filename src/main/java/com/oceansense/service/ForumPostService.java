package com.oceansense.service;

import com.oceansense.model.ForumPost;
import com.oceansense.repository.ForumPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumPostService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    // Retrieve all forum posts
    public List<ForumPost> getAllPosts() {
        return forumPostRepository.findAll();
    }

    // Create a new forum post
    public ForumPost createPost(ForumPost post) {
        return forumPostRepository.save(post);
    }

    // Delete a forum post by ID
    public void deletePost(Long id) {
        forumPostRepository.deleteById(id);
    }

    // Retrieve a single forum post by ID
    public Optional<ForumPost> getPostById(Long id) {
        return forumPostRepository.findById(id);
    }
}
