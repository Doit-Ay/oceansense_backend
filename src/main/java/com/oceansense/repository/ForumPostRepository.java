package com.oceansense.repository;

import com.oceansense.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
    // Additional query methods can be defined here if needed
}
