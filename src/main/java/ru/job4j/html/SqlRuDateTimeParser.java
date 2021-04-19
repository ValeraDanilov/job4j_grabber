package ru.job4j.html;

import com.mchange.v1.util.ArrayUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final String[] MONTH = {"",
            "янв", "фев", "мар", "апр", "май", "июн",
            "июл", "авг", "сен", "окт", "ноя", "дек"};

    @Override
    public LocalDateTime parse(String parse) {
        if (parse == null) {
            throw new NullPointerException("Value is null");
        }
        String[] cut = parse.split(" ");
        if (cut[0].equals("сегодня,")) {
            return LocalDateTime.of(LocalDate.now(), LocalTime.parse(cut[1]));
        } else if (Objects.equals(cut[0], "вчера,")) {
            return LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.parse(cut[1]));
        }
        var month = ArrayUtils.indexOf(MONTH, cut[1]);
        var year = Integer.parseInt(cut[2].substring(0, 2)) + 2000;
        var day = Integer.parseInt(cut[0]);
        return LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.parse(cut[3]));
    }
}
