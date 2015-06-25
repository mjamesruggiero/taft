FROM dockerfile/java

MAINTAINER Michael Ruggiero mjamesruggiero@gmail.com

ADD tmp/ /opt/app/
ADD start /opt/start
RUN chmod +x /opt/start

ENTRYPOINT [ "/opt/start" ]
