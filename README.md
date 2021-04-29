
Реализованные запросы:

ContactController

GET
/contacts


POST
/contacts


GET
/contacts/{id}


PUT
/contacts/{id}


DELETE
/contacts/{id}





CustomerController

GET
/customers


POST
/customers

Example Request body:

{
  
    "firstName" : "Alice",
    "lastName" : "Madness",
    "phoneNumber":13772,
    "customer" : {"id":1}
}

PUT
/customers/{id}


DELETE
/customers/{id}


GET
/customers/{id}



Можно посмотреть запросы в Swagger:

http://localhost:8080/swagger.html


start program command:

java -jar dins-0.0.1-SNAPSHOT.jar