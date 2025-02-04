FROM maven as builder
COPY . /mnt/onlinebookstore/.
WORKDIR /mnt/onlinebookstore
RUN mvn clean verify sonar:sonar \
 -Dsonar.projectKey=demo \
 -Dsonar.projectName='demo' \
 -Dsonar.host.url=http://13.127.173.164:32770 \
 -Dsonar.token=sqp_59cd2ef3c431d38d74b69634a82d1eb12e67b493

RUN mvn package

FROM tomcat
COPY --from=builder /mnt/onlinebookstore/target/*.war webapps/.
