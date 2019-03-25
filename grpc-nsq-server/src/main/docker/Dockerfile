
FROM openjdk:latest

VOLUME ["/opt/app/logs"]

ENV logdir /opt/app/logs

COPY grpc-server-1.0.jar /opt/app/grpc-server-1.0.jar

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

EXPOSE 8088

WORKDIR /opt/app

CMD java -jar grpc-server-1.0.jar