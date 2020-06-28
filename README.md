# ToDoAPI
 REST API for Todos

# Overview
The service shall allow to manage Todos, e.g., like you might know it from Wunderlist, Todoist or similar applications. Of course much simpler.
A Todo contains an arbitrary list of subtasks and is structured as follows:

```json
{
  "id" :
  "name" :
  "description" :
  "tasks": [
    {
      "id" :
      "name" : 
      "description" :
    }
  ]
}
```
The service shall support the following endpoints:

`GET` /todos Returns a list of all Todos

`POST` /todos Expects a Todo (without id) and returns a Todo with id

`GET` /todos/{id} Returns a Todo

`PUT` /todos/{id} Overwrites an existing Todo

`DELETE` /todos/{id} Deletes a Todo

# Setup

mvn must be installed to build the project.

``configration.yml`` file has the details of the database.
The database must have the tables set up as follows -

~~~~sql
CREATE TABLE TODO(
  `ID` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `NAME` VARCHAR(255) NOT NULL,
    `DESCRIPTION` VARCHAR(2000) NULL
);

CREATE TABLE TASK(
  `ID` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `NAME` VARCHAR(255) NOT NULL,
    `DESCRIPTION` VARCHAR(2000) NULL,
    `TODO_ID` BIGINT,
     FOREIGN KEY (TODO_ID) REFERENCES TODO(ID)
);
~~~~

``mvn clean install`` inside the project directory to build the project.

``java -jar target\ToDo-1.0.jar server configuration.yml`` to start the server at http://localhost:8080
