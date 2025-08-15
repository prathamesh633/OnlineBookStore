# Connecting MySQL Database to onlinebookstore locally

Follow these steps to set up and connect MySQL with the OnlineBookstore application in your local environment.

Download MySQL from the official website: [MySQL Downloads](https://dev.mysql.com/downloads/mysql/). Once installed, open **PowerShell** as **Administrator** and start the MySQL service using:
```
net start mysql
```

After MySQL is running, create a dummy database by following the instructions in the `dummy_database.md` file.

The default MySQL admin credentials are `Username: root` and `Password: root`. If you want to create a new user for the OnlineBookstore app, run:
```sql
CREATE USER 'bookuser'@'%' IDENTIFIED BY 'bookpass';
GRANT ALL PRIVILEGES ON onlinebookstore.* TO 'bookuser'@'%';
FLUSH PRIVILEGES;
```

Update the src/main/resources/application.properties file with your MySQL credentials:

```db.driver=com.mysql.cj.jdbc.Driver
db.host=jdbc:mysql://localhost
db.port=3306
db.name=onlinebookstore
db.username=bookuser
db.password=bookpass
```

Next, inside the lib/ folder of your Apache Tomcat 9 installation, download the MySQL connector using:
```
curl -O https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
```

From the onlinebookstore folder, build the application using:
```
mvn clean package
```

Once packaged, copy the .war file from the target/ folder to the webapps/ folder in your Apache Tomcat 9 installation. Then, navigate to the /bin folder in Apache Tomcat 9 and start the server using:
```
bash catalina.sh start
```

Finally, open your browser and go to http://localhost:8080/onlinebookstore to access the application. Your OnlineBookstore app should now be connected to the local MySQL database and running successfully.

