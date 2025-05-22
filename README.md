# Sports Objects Analysis

## Описание

Этот проект загружает данные об объектах спорта из CSV-файла, сохраняет их в базу данных SQLite, анализирует и визуализирует статистику:

- Гистограмма количества объектов спорта по регионам (с объединением Москва+МО).
- Среднее количество объектов спорта в регионах.
- Топ-3 региона по количеству объектов спорта.

## Запуск

1. Склонируйте репозиторий и положите файл `Объекты спорта.csv` в корень проекта.
2. Соберите jar:
    ```
    mvn package
    ```
3. Запустите:
    ```
    java -jar target/sports-objects-1.0-SNAPSHOT.jar
    ```
4. В консоли появится статистика, а диаграмма сохранится как `histogram.png`.

## Скриншоты

_Вставьте сюда скриншоты консоли и диаграммы._

## Структура таблиц (3НФ)

- `region(id, name)`
- `sports_object(id, name, type, address, date, region_id)`

## SQL-запросы

- Количество объектов по регионам:  
  ```sql
  SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name
  ```
- Среднее количество:
  ```
  SELECT AVG(cnt) FROM (SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name)
  ```
- Топ-3 регионов:
  ```
  SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name ORDER BY cnt DESC LIMIT 3
  ```

---

## Лицензия

MIT