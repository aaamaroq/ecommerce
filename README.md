
# Ecommerce Backend

## Project Definition

### Definition
This project aims to develop a backend system composed of several interconnected parts. It will employ an API that, upon receiving certain parameters, processes the request and sends the response to a message queue. Subsequently, a consumer will consume messages from this queue and store the data in a database.

### Technologies to Use
- **Java 17**: Programming language for developing the API and consumer.
- **Spring Boot**: Framework for building Java applications.
- **Kafka**: Messaging platform for managing message queues.
- **PostgreSQL**: Relational database management system for storing processed data.
- **Docker**: Containerization tool for isolating applications and managing the development environment.
- **Git**: Version control system for collaboration in project development.

## Testing the Project

### Cloning the Repository
To obtain a copy of the project, clone the repository from GitHub:

```bash
git clone https://github.com/aaamaroq/ecommerce.git
```

### Running with Docker Compose
Use Docker Compose to launch the application services in an isolated environment:

```bash
docker-compose up -d
```

### Testing the App
To test the application, use any web browser. Enter a product ID, your email, and your name. After submitting, you will receive an email with data for fictional products.
```bash
http://localhost:8080/product/getProducto?id=1&nombre=yourname&email=yourmail@mail.com
```

This command will start all necessary containers specified in the `docker-compose.yml` file, including the API application, Kafka consumer, and PostgreSQL database.

## Justification for the Implementation
The choice of these technologies is based on:

- **Java and Spring Boot**: Provide a robust and mature environment for developing enterprise applications, facilitating the creation of RESTful APIs and consumer services.
  
- **Kafka**: Offers a highly scalable and fault-tolerant distributed messaging system, ideal for managing real-time data streams and integrating different parts of the system.
  
- **PostgreSQL**: A reliable and powerful option for data persistence, supporting ACID transactions and seamless integration with Java applications through Hibernate.
  
- **Docker**: Enables the creation of lightweight and portable containers that encapsulate the application along with its dependencies, simplifying deployment and scalability of the system.

These combined technologies provide a solid foundation for developing an efficient, scalable, and maintainable backend system to securely and reliably process and store data.
