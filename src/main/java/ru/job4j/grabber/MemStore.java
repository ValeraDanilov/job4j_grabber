package ru.job4j.grabber;

import ru.job4j.html.SqlRuDateTimeParser;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MemStore implements Store, AutoCloseable {

    private Connection cn;
    private final Properties pr;

    public MemStore(Properties pr) {
        this.pr = pr;
        init();
    }

    private void init() {
        try {
            try (var io = MemStore.class.getClassLoader().getResourceAsStream("app.properties")) {
                this.pr.load(io);
            }
            Class.forName(this.pr.getProperty("jdbc.driver"));
            this.cn = DriverManager.getConnection(this.pr.getProperty("jdbc.url"), this.pr.getProperty("jdbc.userName"), this.pr.getProperty("jdbc.userPassword"));
        } catch (Exception eo) {
            eo.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps = this.cn.prepareStatement("insert into post(name, text, link, created_date) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getText());
            ps.setString(3, post.getLink());
            ps.setString(4, post.getCreated().toString());
            ps.execute();
            try (ResultSet set = ps.getGeneratedKeys()) {
                if (set.next()) {
                    post.setId(set.getInt(1));
                }
            }
        } catch (Exception eo) {
            eo.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new LinkedList<>();
        try (PreparedStatement pr = this.cn.prepareStatement("select * from post")) {
            try (ResultSet set = pr.executeQuery()) {
                if (set.next()) {
                    LocalDateTime date = new SqlRuDateTimeParser().parse(set.getString("created_date"));
                    posts.add(new Post(set.getInt("id"), set.getString("name"), set.getString("text"), set.getString("link"), date));
                }
            }
        } catch (Exception eo) {
            eo.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        Post post = new Post();
        try (PreparedStatement pr = this.cn.prepareStatement("select * from post where id = ?")) {
            pr.setInt(1, Integer.parseInt(id));
            try (ResultSet set = pr.executeQuery()) {
                if (set.next()) {
                    LocalDateTime date = new SqlRuDateTimeParser().parse(set.getString("created_date"));
                    post = new Post(set.getInt("id"), set.getString("name"), set.getString("text"), set.getString("link"), date);
                }
            }
        } catch (Exception eo) {
            eo.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws SQLException {
        if (this.cn != null) {
            this.cn.close();
        }
    }
}
