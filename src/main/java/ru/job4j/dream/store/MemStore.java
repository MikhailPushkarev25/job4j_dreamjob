package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {
    private static final AtomicInteger POST_ID = new AtomicInteger(4);

    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(4);

    private static final AtomicInteger USER_ID = new AtomicInteger(4);

    private static final MemStore INST = new MemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    public MemStore() {
        posts.put(1, new Post(1, "Junior Java Job"));
        posts.put(2, new Post(2, "Middle Java Job"));
        posts.put(3, new Post(3, "Senior Java Job"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
        users.put(1, new User(1, "Mike", "Log@mail", "123"));
        users.put(2, new User(2, "Lora", "lor@ref", "lora"));
        users.put(3, new User(3, "Frank", "fr@log", "frank"));
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
   }

   public Collection<Candidate> findAllCandidates() {
        return candidates.values();
   }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    @Override
    public void canSave(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Candidate canFindById(int id) {
        return candidates.get(id);
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public void userSave(User user) {
        if (user.getId() == 0) {
            user.setId(USER_ID.incrementAndGet());
        }
        users.put(user.getId(), user);
    }

    @Override
    public User usFindById(int id) {
        return users.get(id);
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public void deleteCandidate(int id) {
        candidates.remove(id);
    }
}
