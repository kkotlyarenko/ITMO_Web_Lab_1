FROM openjdk:17-jdk-slim

WORKDIR /app

COPY lib/fastcgi-lib.jar /app/lib/

COPY src /app/src/

RUN javac -cp "/app/lib/fastcgi-lib.jar" /app/src/*.java -d /app/bin

EXPOSE 9000

CMD ["java", "-DFCGI_PORT=9000", "-cp", "/app/bin:/app/lib/fastcgi-lib.jar", "Main"]