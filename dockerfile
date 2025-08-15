FROM maven as builder
COPY . /mnt/onlinebookstore/.
WORKDIR /mnt/onlinebookstore

RUN mvn package

FROM tomcat
COPY --from=builder /mnt/onlinebookstore/target/*.war webapps/onlinebookstore.war

# we can face error if running on local
# Just remove 's' from https in the URL and use http instead. It will solve the issue.