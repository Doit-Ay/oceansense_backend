package com.oceansense.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.oceansense.model.ForumPost;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ForumPostService {

    private static final String COLLECTION_NAME = "posts";

    public List<ForumPost> getAllPosts() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(COLLECTION_NAME).get().get().getDocuments().stream()
                .map(document -> {
                    ForumPost post = document.toObject(ForumPost.class);
                    post.setId(document.getId());
                    return post;
                })
                .collect(Collectors.toList());
    }

    public ForumPost createPost(ForumPost post) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        post.setCreatedAt(LocalDateTime.now());
        DocumentReference docRef = db.collection(COLLECTION_NAME).add(post).get();
        post.setId(docRef.getId());
        return post;
    }

    public void deletePost(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public ForumPost getPostById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            ForumPost post = document.toObject(ForumPost.class);
            post.setId(document.getId());
            return post;
        } else {
            return null;
        }
    }
}
