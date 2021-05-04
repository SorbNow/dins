
implemented requests:

ContactController

GET
/contacts - get contacts list by customer id. If phone number not empty - find contact with number in customer phone book


POST
/contacts - save contact to customer phone book

Example Request body:

{

    "firstName" : "Alice",
    "lastName" : "Madness",
    "phoneNumber":13772,
    "customer" : {"id":1}
}

GET
/contacts/{id} - get contact from database by id


PUT
/contacts/{id} - edit contact from database by id


DELETE
/contacts/{id} - delete contact from database by id





CustomerController

GET
/customers - get customer list by full/part of first and last name. If parameters empty-return all customers


POST
/customers - save customer to database

Example Request body:

{

    "firstName" : "Alice",
    "lastName" : "Madness"
}

GET
/customers/{id} - get customer from database by id

PUT
/customers/{id} - edit customer in database


DELETE
/customers/{id} - delete customer from database



Можно посмотреть запросы в Swagger:

http://localhost:8080/swagger.html


start program command:

java -jar dins-0.0.2-SNAPSHOT.jar
