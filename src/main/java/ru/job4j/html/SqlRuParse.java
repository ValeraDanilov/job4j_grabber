package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;

import java.io.IOException;

public class SqlRuParse {

    private static int index = 1, count;

    public static void main(String[] args) throws IOException {
        new SqlRuParse().fillInPost();
    }

    public void outAllJob() throws IOException {
        while (count <= 5) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers" + "/" + count).get();
            Elements row = doc.select(".postslisttopic");
            Elements value = doc.select(".altCol");
            for (Element el : row) {
                Element href = el.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(value.get(index).text());
                index += 2;
            }
            count++;
            index = 1;
        }
    }

    public void fillInPost() throws IOException {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t").get();
        Elements row = doc.select(".msgBody");
        Elements row1 = doc.select(".msgFooter");
        Post post = new Post();
        post.setText(row.get(1).text());
        post.setCreated(row1.get(0).text().split("\\[")[0]);
        System.out.println(post.getText());
        System.out.println(post.getCreated());
    }
}
