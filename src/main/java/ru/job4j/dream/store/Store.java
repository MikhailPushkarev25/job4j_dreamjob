package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface Store {

    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    Post findById(int id) throws SQLException;

    void canSave(Candidate candidate);

    Candidate canFindById(int id);

    Collection<User> findAllUsers();

    void userSave(User user);

    User usFindById(int id);

    User findByEmail(String email);

}
