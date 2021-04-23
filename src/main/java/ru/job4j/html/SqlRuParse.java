package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {

    private static int index = 1, count;

    public static void main(String[] args) throws Exception {
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
}
