# Masi STP
The project was implemented during the classes of Modelowanie i analiza systemów informatycznych.  

## Basic information

### Project description
The goal of the project was to support the users' selection process in the Amazon store with use of the chatbot. After lunch of the application two windows pops up - Amazon store page and conversion window. The user could pass his requirements for the item which he was interested in, then the chatbot redirected the user to a page where was the list of items which met the requirements.

### Technologies, frameworks and tools
Project was created with use of:
 - Angular framework,
 - Spring Boot framework,
 - MongoDB,
 - Amazon API,
 - Watson IBM (natural language processor).
 
### Gained skills and knowledge
During the implementation of the project I learned:
 - to create friendly UI with use of the Angular framework,
 - to communicate with use of the REST API,
 - to parse retrieved data from the server (the content of the message was in proper format, which allowed to place buttons in the view),
 - to integrate with Amazon API,
 - to integrate MongoDB database with the Spring Boot project.

## Lunching the SpringBoot project
### JAVA SDK
Firstly, install and add to PATH JAVA SDK.
Look: https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html

### Before build
In the constructor of the `ConversationServiceImpl` class replace the following fragment:
```JAVA
conversation = new Conversation(
        "2018-02-16",
        "login",
        "password");
```
Login and password should be replaced with access data provided by the Conversation application from Watson IBM.

### Build
In order to build the project, go to the `masi-STP-backend` directory and execute the following command:
 - linux/osx
```bash
./gradlew build (linux/osx)
```
 - windows
```bash
./gradlew.bat build
```

### Run
In order to run the project, go to the `masi-STP-backend` directory and execute the following command:
 - linux
```bash
./gradlew bootRun
```
 - windows
```bash
./gradlew.bat bootRun
```

## Lunching the Angular project
* Install the newest version of the Node.js. Look: https://nodejs.org/en/
* Go into the directory `masi-STP-interface`
* Install all dependecies with command:
```bash
npm install
```
* After the installation is completed you can run the project with the following command
```bash
npm start
```
* Project run under the address: localhost:4200

## MongoDB
### Installation
 1. Go to the: https://www.mongodb.com/ and download MongoDB installer in Community Server version.
 2. Install downloaded software (I suggest to install MongoDB Compass Comunity with MongoDB database - it allows to manage database from the graphical UI).
 3. Run the terminal (`Windows + S`, and then `cmd`).
 4. Execute command `mongod`. When following message pops out `'mongod'is not recognized as an internal or external command, operable program or batch file.` you need to add `<installation_path>\MongoDB\Server\3.6\bin` directory to the `PATH`.
 5. If error poped out go to the step 3. Now, Mongo database is sarting. When you see message `I NETWORK  [initandlisten] waiting for connections on port 27017`, everything end up successfully and database had been properly configured.

### Restoration process of the database state
 1. Copy directory `masi-STP-database` to the same directory as `data` (It was created during database installation).
 2. Run the datatabse with command `mongod`.
 3. Run another terminal and execute command `mongorestore masi-STP-database`.
 4. Database state was successfully restored..

#### Listing of the successful restoration process of the database state
```bash
C:\>mongorestore masi-STP-database
2018-04-17T16:48:26.058+0200    preparing collections to restore from
2018-04-17T16:48:26.066+0200    reading metadata for stp.logs from masi-STP-database\stp\logs.metadata.json
2018-04-17T16:48:26.082+0200    restoring stp.logs from masi-STP-database\stp\logs.bson
2018-04-17T16:48:26.085+0200    no indexes to restore
2018-04-17T16:48:26.085+0200    finished restoring stp.logs (2 documents)
2018-04-17T16:48:26.085+0200    done
```

#### How to check correctness of the restoration process
##### Method 1
 1. When MongoDB is running, open another terminal and execute command `mongo`. Database console opened up.
 2. Execute command `show dbs`. In listed databases should exist the record `stp`.
 3. Execute two following commands: `use stp` and `db.logs.find()`. You should see all the records from the `logs` collection.

##### Method 2 (MongoDB Compass Community required)
 1. Run installed MongoDB Compass Community.
 2. Press Connect button.
 3. Choose `stp` database from the left panel.
 4. Expand list `stp`. There should be `logs` collection.
 5. Check if there are any records in the `logs` collection.

