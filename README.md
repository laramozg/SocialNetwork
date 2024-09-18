### Тестовое покрытие 
- mvn clean test jacoco:report
- ![img.png](img.png)

### Диаграмма
![img_1.png](img_1.png)

### Докер для тестов и запуска сервера
docker run --name my_postgres_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=oqv620 -e POSTGRES_DB=postgres -p 5432:5432 -d postgres:latest

### Postman
![img_2.png](img_2.png)
