#TAG: llalon/jgitbackup
FROM maven:3.8.3-eclipse-temurin-11 AS build

WORKDIR /build

COPY pom.xml /build/pom.xml
COPY src/ /build/src

RUN mvn clean package -Dmaven.test.skip -Dspotless.check.skip=true

FROM eclipse-temurin:11

RUN apt-get update && apt-get install -y git

WORKDIR /app

RUN mkdir -p /git/

ENV JGITBACKUP_DIRECTORY '/git/'
ENV JGITBACKUP_PROVIDER 'github'
ENV JGITBACKUP_TOKEN ''
ENV JGITBACKUP_USERNAME ''
ENV JGITBACKUP_SCHEDULE '-'

COPY --from=build /build/target/JGitBackup.jar ./JGitBackup.jar
CMD ["java", "-jar", "JGitBackup.jar"]



