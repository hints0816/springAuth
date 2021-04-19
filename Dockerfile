# 基于java镜像创建新镜像
FROM openjdk:8
# 作者
MAINTAINER Hints
# 将jar包添加到容器中并更名为app.jar
ADD eurekaserver/target/eurekaserver-0.0.1-SNAPSHOT.jar eurekaserver-0.0.1-SNAPSHOT.jar
ADD authserver/target/authserver-0.0.1-SNAPSHOT.jar authserver-0.0.1-SNAPSHOT.jar
ADD client/target/client-0.0.1-SNAPSHOT.jar client-0.0.1-SNAPSHOT.jar
ADD resource/target/resource-0.0.1-SNAPSHOT.jar resource-0.0.1-SNAPSHOT.jar

COPY start_cloud.sh /usr/bin/start_cloud.sh
# 运行jar包
RUN chmod +x /usr/bin/start_cloud.sh
CMD nohup sh -c "start_cloud.sh && java -jar eurekaserver-0.0.1-SNAPSHOT.jar"