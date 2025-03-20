FROM --platform=$BUILDPLATFORM gradle:jdk21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/
COPY gradle/libs.versions.toml /app/gradle/
COPY src /app/src

RUN gradle build --no-daemon

FROM --platform=$TARGETPLATFORM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/Gallagher-[0-9]*.[0-9]*.[0-9]*.jar /app/Gallagher.jar

CMD ["java", "-jar", "Gallagher.jar"]
