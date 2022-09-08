CALL .\mvnw clean install -DskipTests=true
@REM docker rm dev-dl-auth
@REM docker rmi dev-dl-auth
CALL docker build -t dev-dl-auth .
CALL docker-compose up