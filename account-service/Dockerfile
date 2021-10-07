# if you want to create an image -> from the project root directory run:
# DOCKER_BUILDKIT=1 docker build -f account-service/Dockerfile -t ktor-template/account .

# Build jar

FROM gradle AS build

WORKDIR /appbuild
COPY . /appbuild

RUN gradle :account-service:clean :account-service:assemble

# Deploy jar

FROM openjdk:8-jre-alpine

WORKDIR /app

COPY --from=0 appbuild/account-service/build/libs/account-service.jar application.jar

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "application.jar"]
