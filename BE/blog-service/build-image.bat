CALL .\mvnw clean install -DskipTests=true
@REM docker rm dev-dl-blog
@REM docker rmi dev-dl-blog
CALL docker build -t dev-dl-blog .
CALL docker-compose up