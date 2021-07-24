package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {
    private static final Store INST = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job",
                "Опыт работы от 6 месяцев", "20.07.2021"));
        posts.put(2, new Post(2, "Middle Java Job",
                "Опыт работы от 1 - 3 года", "10.05.2021"));
        posts.put(3, new Post(3, "Senior Java Job",
                "Опыт работы от 3 лет", "24.07.2021"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
   }

   public Collection<Candidate> findAllCandidates() {
        return candidates.values();
   }
}
