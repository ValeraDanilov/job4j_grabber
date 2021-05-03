# Grabber
[![Build Status](https://travis-ci.com/ValeraDanilov/job4j_grabber.svg?branch=main)](https://travis-ci.com/ValeraDanilov/job4j_grabber)
[![codecov](https://codecov.io/gh/ValeraDanilov/job4j_grabber/branch/main/graph/badge.svg)](https://codecov.io/gh/ValeraDanilov/job4j_grabber)

<h2> Аргегатор Java вакансий</h2>

<h2>Техническое задание</h2>
<p>Приложение парсит сайты с вакансиями. Первый сайт будет sql.ru. В нем есть раздел job. Программа должна считывать все вакансии относящиеся к Java и записывать их в базу.</p>
<ul>
  <li>В проекте нужно использовать maven, travis, jacoco, checkstyle.</li>
  <li>Приложение должно собираться в jar.</li>
  <li>Система запускается по расписанию. Период запуска указывается в настройках - app.properties.</li>
  <li>Доступ к интерфейсу будет через REST API.</li>
</ul>
<p><strong>расширение:</strong></p>
<ul>
  <li>В проект можно добавить новые сайты без изменения кода.</li>
  <li>В проекте можно сделать параллельный парсинг сайтов.</li>
</ul>

<h2>Использованные средства</h2>
<p><a href="https://www.oracle.com/java/technologies/javase-jdk15-downloads.html">Open JDK 16</a> - компилятор\интерпритатор</p>
<p><a href="http://maven.apache.org/index.html">Maven</a> - сборка и управление проектом</p>
<p><a href="https://www.postgresql.org/download/">PostgreSQL</a> - база данных</p>
<p>JSOUP для парсинга</p>
<p>JDBC для работы с базой данных</p>

<h2>Компиляция</h2>
<p>Для работы приложения нужно создать базу данных и добавить настройки для подключения в файл pom.xml 
в раздел profiles</p>
<pre>
<code>$ cd job4j_grabber
$ mvn package -Pproduction</code>
</pre>
Появится папка target, a в ней файл grabber.jar

<h2>Запуск</h2>
<pre>
<code>$ java -jar grabber.jar</code>
</pre>
<p>После запуска программы результат можно посмотреть по адресу http://localhost:9000/</p>