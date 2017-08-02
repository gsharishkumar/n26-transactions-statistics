# N26 Backend Coding Challenge

The task is to build two RESTful APIs. One of them is called every time a transaction is made and it is also the sole input of this rest API. And the other one returns the statistic based of the transactions of the last 60 seconds.

## Technology Stack used to build the application 

Java Programming
Eclipse IDE
Java v1.8.0_92
Apache Maven v3.3.9
Spring Boot v1.4.3.RELEASE

## Application testing

### To build and test the application
mvn clean install

### To run the application
mvn spring-boot:run

## API URLs If the application is ran to test the operations using external tools other than Junit tests:

1. transactions (POST):
	http://localhost:8080/transactions
2. statistics (GET):
	http://localhost:8080/statistics
	
## Other details:

1. ConcurrentHashMap data structure is used to persist the transactions.
2. APIs are tested using Junit and Google Chrome plugin Postman.

## Author
Harish Kumar Govindaswamy Saravanam
 