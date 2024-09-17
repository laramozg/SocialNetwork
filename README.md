### Тестовое покрытие 
- mvn clean test jacoco:report
- ![img.png](img.png)

### Диаграмма
![img_1.png](img_1.png)

### Докер для Dao тестов и проверки в Postman
docker run -d --name postgres-test -e POSTGRES_DB=test -e POSTGRES_USER=test -e POSTGRES_PASSWORD=test -p 5432:5432 postgres:15

### Postman
![img_2.png](img_2.png)
