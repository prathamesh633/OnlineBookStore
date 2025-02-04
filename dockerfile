FROM maven as builder
COPY . /mnt/onlinebookstore/.
WORKDIR /mnt/onlinebookstore
RUN mvn clean verify sonar:sonar \
  -Dsonar.projectKey=demo \
  -Dsonar.projectName='demo' \
  -Dsonar.host.url=http://3.144.82.191:32768 \
  -Dsonar.token=sqp_ed5e703d1972148c91849cc7c06aea81882c3121

RUN mvn package

FROM tomcat
COPY --from=builder /mnt/onlinebookstore/target/*.war webapps/.
