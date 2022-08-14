CALL .\mvnw clean install -DskipTests=true
CALL docker rm dev-dl-blog
CALL docker rmi dev-dl-blog
CALL docker build -t dev-dl-blog .
CALL docker-compose up