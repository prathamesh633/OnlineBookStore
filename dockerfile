FROM maven as builder
COPY . /mnt/onlinebookstore/.
WORKDIR /mnt/onlinebookstore
RUN mvn package

FROM tomcat 
COPY --from=builder /mnt/onlinebookstore/target/*.war webapps/.
