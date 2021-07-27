package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private final static Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
           LOG.error("Exception in log", e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    public static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
            } catch (Exception e) {
           LOG.error("Exception in log ", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() throws SQLException {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it  = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in log ", e);
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    public void canSave(Candidate candidate) {
        if (candidate.getId() == 0) {
            canCreate(candidate);
        } else {
            canUpdate(candidate);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
                     )
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in log ", e);
        }
        return post;
    }

    private Candidate canCreate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidate(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
                     )
        ) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in log ", e);
        }
        return candidate;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("UPDATE post SET name = (?) WHERE id = (?)")
        ) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception in log ", e);
        }
    }

    public void canUpdate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("UPDATE candidate SET name = (?) WHERE id = (?)")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
           LOG.error("Exception in log ", e);
        }
    }

    @Override
    public Post findById(int id) throws SQLException {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = (?)")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = new Post(rs.getInt("id"), rs.getString("name"));
                }
            } catch (Exception e) {
               LOG.error("Exception in log ", e);
            }
        }
        return post;
    }

    public Candidate canFindById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id = (?)")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate = new Candidate(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
           LOG.error("Exception in log ", e);
        }
        return candidate;
    }
}