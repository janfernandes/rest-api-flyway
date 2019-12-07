Executar o comando abaixo a partir do seguinte caminho: ...\app-everis\src\main\resources\docker
```
docker-compose up
```

Criando o usuario de acesso ao banco
```
docker exec -it my_postgres_docker psql -U postgres -c "CREATE USER postgresdev WITH SUPERUSER PASSWORD 'postgresdev'";
```

Criando a data base.
```
docker exec -it my_postgres_docker psql -U postgres -c "create database database_estudo"
```

Flyway: executando os scripts
Acessar a raiz app-everis do projeto e executar o seguinte comando:
 
```
mvn flyway:migrate
```

Executando o projeto
```
java -jar target/app-spring-rest.jar
```
