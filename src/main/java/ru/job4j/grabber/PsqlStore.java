package ru.job4j.grabber;

import ru.job4j.html.SqlRuParse;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            try (var io = PsqlStore.class.getClassLoader().getResourceAsStream("app.properties")) {
                cfg.load(io);
            }
            Class.forName(cfg.getProperty("jdbc.driver"));
            this.cnn = DriverManager.getConnection(cfg.getProperty("jdbc.url"), cfg.getProperty("jdbc.userName"), cfg.getProperty("jdbc.userPassword"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public void save(Post post) {
        try (PreparedStatement pr = this.cnn.prepareStatement("insert into post(name, text, link, created) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            pr.setString(1, post.getName());
            pr.setString(2, post.getText());
            pr.setString(3, post.getLink());
            pr.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            pr.execute();
            try (ResultSet set = pr.getGeneratedKeys()) {
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
        try (PreparedStatement pr = this.cnn.prepareStatement("select * from post")) {
            try (ResultSet set = pr.executeQuery()) {
                while (set.next()) {
                    int id = set.getInt("id");
                    String name = set.getString("name");
                    String text = set.getString("text");
                    String link = set.getString("link");
                    LocalDateTime date = set.getTimestamp("created").toLocalDateTime();
                    posts.add(new Post(id, name, text, link, date));
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
        try (PreparedStatement pr = cnn.prepareStatement("select * from post where id = ?")) {
            pr.setInt(1, Integer.parseInt(id));
            try (ResultSet set = pr.executeQuery()) {
                if (set.next()) {
                    int idPost = set.getInt("id");
                    String name = set.getString("name");
                    String text = set.getString("text");
                    String link = set.getString("link");
                    LocalDateTime date = set.getTimestamp("created").toLocalDateTime();
                    post = new Post(idPost, name, text, link, date);
                }
            }
        } catch (Exception eo) {
            eo.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        PsqlStore ps = new PsqlStore(new Properties());
        ConnectionRollback.create(ps.cnn);
        SqlRuParse parse = new SqlRuParse();
        parse.list("https://www.sql.ru/forum/job-offers").forEach(ps::save);
        ps.getAll().forEach(System.out::println);
        System.out.println(ps.findById("160"));
    }
}
