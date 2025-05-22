Sports Objects Analysis
Описание
Этот проект загружает данные об объектах спорта из CSV-файла, сохраняет их в базу данных SQLite, анализирует и визуализирует статистику:

Гистограмма количества объектов спорта по регионам (с объединением Москва+МО).
Среднее количество объектов спорта в регионах.
Топ-3 региона по количеству объектов спорта.
Запуск
Склонируйте репозиторий и положите файл Объекты спорта.csv в корень проекта.
Соберите jar:
mvn package
Запустите:
java -jar target/sports-objects-1.0-SNAPSHOT.jar
В консоли появится статистика, а диаграмма сохранится как histogram.png.
Скриншоты


Структура таблиц (3НФ)
region(id, name)
sports_object(id, name, type, address, date, region_id)
SQL-запросы
Количество объектов по регионам:
SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name
Среднее количество:
SELECT AVG(cnt) FROM (SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name)
Топ-3 регионов:
SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name ORDER BY cnt DESC LIMIT 3
