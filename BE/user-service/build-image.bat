CALL .\mvnw clean install -DskipTests=true
@REM docker rm dev-dl-user
@REM docker rmi dev-dl-user
CALL docker build -t dev-dl-user .
CALL docker-compose up