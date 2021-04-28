package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private SqlRuDateTimeParser data = new SqlRuDateTimeParser();
    private static int index = 1;

    public static void main(String[] args) throws IOException {
        for (var el : new SqlRuParse().list("https://www.sql.ru/forum/job-offers")) {
            System.out.println(el);
        }
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        Elements value = doc.select(".altCol");
        for (var el : row) {
            Element href = el.child(0);
            String text = detail(href.attr("href")).getText();
            posts.add(new Post(href.text(), text, href.attr("href"), this.data.parse(value.get(index).text())));
            index += 2;
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".msgBody");
        Elements row1 = doc.select(".msgFooter");
        return new Post(row.get(1).text(), this.data.parse(row1.get(0).text().split("\\[")[0]));
    }
}
