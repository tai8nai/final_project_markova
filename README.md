# Sports Objects Analysis

## ��������

���� ������ ��������� ������ �� �������� ������ �� CSV-�����, ��������� �� � ���� ������ SQLite, ����������� � ������������� ����������:

- ����������� ���������� �������� ������ �� �������� (� ������������ ������+��).
- ������� ���������� �������� ������ � ��������.
- ���-3 ������� �� ���������� �������� ������.

## ������

1. ����������� ����������� � �������� ���� `������� ������.csv` � ������ �������.
2. �������� jar:
    ```
    mvn package
    ```
3. ���������:
    ```
    java -jar target/sports-objects-1.0-SNAPSHOT.jar
    ```
4. � ������� �������� ����������, � ��������� ���������� ��� `histogram.png`.

## ���������

_�������� ���� ��������� ������� � ���������._

## ��������� ������ (3��)

- `region(id, name)`
- `sports_object(id, name, type, address, date, region_id)`

## SQL-�������

- ���������� �������� �� ��������:  
  ```sql
  SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name
  ```
- ������� ����������:
  ```
  SELECT AVG(cnt) FROM (SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name)
  ```
- ���-3 ��������:
  ```
  SELECT region.name, COUNT(sports_object.id) as cnt FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id GROUP BY region.name ORDER BY cnt DESC LIMIT 3
  ```

---

## ��������

MIT