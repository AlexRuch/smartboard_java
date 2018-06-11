FROM frolvlad/alpine-oraclejdk8
ADD target/docker-smartboard.jar docker-smartboard.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "docker-smartboard.jar"]