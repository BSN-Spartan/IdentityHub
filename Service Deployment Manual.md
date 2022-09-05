### 1. Prerequisites
- Latest version of Java 1.8 (minor version > 200)
- maven3.6.0
- mongoDB5.0.5

### 2. db Parameter Configuration

The parameters below in `application.properties` file can be edited according to the actual deployment information:

- server.port
- spring.data.mongodb.authentication-database
- spring.data.mongodb.database
- spring.data.mongodb.username
- spring.data.mongodb.password
- spring.data.mongodb.host
- spring.data.mongodb.port
- hub.privateKey
- hub.publicKey


### 3. service Parameter Configuration

The parameters below in `application.properties` file can be edited according to the actual deployment information:

- server.port
- hub.url
- hub.publicKey


### 4. Build db Package

Go to the root directory of the db module and execute the command below: 

` mvn clean package -Dmaven.test.skip=true`

### 5. Build service Package

Go to the root directory of the service module and execute the command below: 

` mvn clean package -Dmaven.test.skip=true`

### 6. Deploy hub.db-0.0.1.jar Package

Go to the directory where the jar package is located and execute the command:

` java -jar hub.db-0.0.1.jar --spring.config.location=/application.properties >>app.log 2>&1 `

### 7. Deploy hub.service-0.0.1.jar Package

Go to the directory where the jar package is located and execute the command:

` java -jar hub.service-0.0.1.jar >>app.log 2>&1 `
