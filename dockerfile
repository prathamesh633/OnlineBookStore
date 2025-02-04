FROM maven as builder
COPY . /mnt/onlinebookstore/.
WORKDIR /mnt/onlinebookstore

RUN mvn clean verify sonar:sonar \
 -Dsonar.projectKey=demo \
 -Dsonar.projectName='demo' \
 -Dsonar.host.url=http://13.127.173.164:32770 \
 -Dsonar.token=sqp_59cd2ef3c431d38d74b69634a82d1eb12e67b493

RUN mvn package

FROM amazon/aws-cli:2.4.5 AS aws
COPY --from=builder /mnt/onlinebookstore/target/*.war /tmp/onlinebookstore.war
WORKDIR /tmp
RUN aws s3 cp onlinebookstore.war s3://onlinebookstore-jenkins/onlinebookstore.war

FROM tomcat
COPY --from=aws /tmp/onlinebookstore.war webapps/.
