FROM openjdk:13-alpine
EXPOSE 80

ADD target/litmgmt-1.0-SNAPSHOT-jar-with-dependencies.jar /app/litmgmt.jar
ADD savefile.json /app/savefile.json
ADD html/ /app/html/

WORKDIR /app
ENTRYPOINT ["java", "-cp", "litmgmt.jar", "litmgmt.App"]
