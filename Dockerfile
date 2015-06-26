FROM java:7

MAINTAINER Michael Ruggiero mjamesruggiero@gmail.com

RUN         apt-get update && apt-get install -y redis-server

EXPOSE      6379

ENTRYPOINT  ["/usr/bin/redis-server"]

COPY target/scala-2.11/taft-assembly-0.1-SNAPSHOT.jar /opt/taft.jar

COPY bin/run_server /opt/start

RUN chmod +x /opt/start

ENTRYPOINT [ "/opt/start" ]
