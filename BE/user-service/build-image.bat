CALL .\mvnw clean install -DskipTests=true
CALL docker rm dev-dl-user
CALL docker rmi dev-dl-user
CALL docker build -t dev-dl-user .
CALL docker-compose up