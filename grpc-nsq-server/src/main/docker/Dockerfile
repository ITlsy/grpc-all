FROM openjdk:latest

VOLUME ["/opt/app/logs"]

ENV logdir /opt/app/logs

COPY grpc-nsq-server-1.0.jar /opt/app/grpc-nsq-server-1.0.jar

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

EXPOSE 50052

WORKDIR /opt/app

CMD java -jar grpc-nsq-server-1.0.jar