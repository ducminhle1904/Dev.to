CALL cd facehub-domain
CALL mvnw.cmd clean install -DskipTests=true
CALL cd ..
CALL cd facehub-infrastructure
CALL mvnw.cmd clean install -DskipTests=true
CALL cd ..
CALL cd facehub-kafka
CALL mvnw.cmd clean install -DskipTests=true
CALL cd ..
CALL cd facehub-grpc-proto
CALL mvnw.cmd clean install -DskipTests=true
CALL cd ..
CALL cd facehub-application
CALL mvnw.cmd clean install -DskipTests=true
CALL cd ..
CALL cd facehub-integration
CALL mvnw.cmd clean install -DskipTests=true
CALL cd ..
CALL cd facehub
CALL mvnw.cmd clean install -DskipTests=true