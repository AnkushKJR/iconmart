=> IconMart – E-commerce 

This project is a Spring Boot microservices-based e-commerce system built to understand event-driven architecture and choreography-based saga using Kafka.

The focus is on the order lifecycle and handling failures across services with distributed transactions.

> Services:

  -> Product – products and categories
  
  -> Cart – user cart with product snapshot (price, name)
  
  -> Inventory – stock management and compensation
  
  -> Order – order creation and final state management
  
  -> Payment – async payment processing (mocked)
  
  -> Notification – sends order success/failure emails

> Flow (High level):

  -> Cart snapshot is fetched
  
  -> Order is created
  
  -> Inventory stock is reduced
  
  -> Payment is processed asynchronously
  
  -> Order is confirmed or rejected
  
  -> Inventory compensates on failure
  
  -> User is notified

> Architecture:

  -> Event-driven (Kafka)
  
  -> Choreography-based saga
  
  -> Database per service
  
  -> Eventual consistency
  
  -> No shared databases

> Events:

  -> ORDER_CREATED → Payment
  
  -> PAYMENT_SUCCESS / FAILED → Order
  
  -> ORDER_CONFIRMED / REJECTED → Notification, Inventory

> Tech Stack:

  -> Java 17, Spring Boot, Kafka, JPA, OpenFeign, MySQL/PostgreSQL

> Learning Outcome:

  -> Designed a distributed system with async communication, failure handling, and compensation using the saga pattern
