
# Ecommerce Backend

## Project Definition
### Motivation
*This portfolio is a personal project I work on during my free time. Whenever I learn new skills, I enjoy applying them by adding improvements, creating a historical record of my progress. It showcases the backend functionality of an e-commerce system.*
### Definition
This project consists of two Spring Boot microservices for an e-commerce platform:
- **ecommerce-api-service:** Exposes REST endpoints to handle product-related requests (GET and POST). It does not directly process business logic but publishes messages to Kafka topics (`product-requests` for product request and `product-creates` for product creations).
- **ecommerce-notification-service:** Consumes messages from Kafka topics, processes the requests by interacting with a PostgreSQL database, and sends email notifications to users with the result of their requests. Emails are sent in the user's preferred language (i18n supported languages: Spanish, English, and German).
The system uses Apache Kafka for asynchronous communication between microservices and is deployed using Docker Compose.
<br>
<p align="center">
  <img src="https://uml.planttext.com/plantuml/png/dPHDRzim38Rl1VeVk5oQ04bysMcdvcljiXRpwlHSq3AR294bJv8R5CF--oZPJfCsABgTR4iYtuVaQrqwZzRNfJe_-oIrK7L1jLMmyRvoNvB4ucwybdvJvxKZAupsfFrOc3Af5EeGDtA-K39FPfz7QLYwlxr9IdIUB2yzp3W2fIOxhgB6-LbArXdT3-c6qBldHu-V6Ud2mTLY1QWBI7zaAx3qgoRd7NW3VaCmIR_r1Y53zipPouUfgNM1zhbFZ5Co776UHVSEpQdU07w7680zIHyhcP5zaZ5W7ppYvP5hrYDHPQLUmzGO7pxl5ep4285cmuoAeBAcg8K7nhIIN2GNbj1BezqOqZfNqcrueoJdS4snak_uk4L-gQHmur3F8PasNZvA4JESGZpSVuUL4xgQHO_4Xe5NC0clAxEMOWYIfsbHS8jtF6AOJuUn1XTdGILAzGPndlUBd2LIu_pQKlRp9ZQ7L6H21NFqcADhiBonhDRrgU2jrnLl0o4VdLGeUCOHE8JUDYs3gJNRnY5VKKWbFNhE61fIwxOHa9FV4MaefR2cxOeBGd_2v9cjDTlHtFU6IuxC5fIkLwiqX8V8mvYEzMMmmPcXkN3HrZZgybn0FvYvAxrR7XnCgwdrFtMYpoZ8HFmFwvnqM2UuzqtZc5DL1N-yWVWdiMYVEyAY-M5sLlhN7k43otIIBEVJsRkfsZiaU_4h79wBmrpY4xPxV4jPlY-QI-IIW-ADz1S0" alt="Architecture diagram" />
</p>

### 🛠️ Technologies to Use

- **Languages & Frameworks:** Java 17, Spring Boot (Data, Kafka, Web), Hibernate, JPA  
- **Architecture & Design:** DDD, REST APIs, SOLID, Clean Code  
- **Database & Migrations:** PostgreSQL, Flyway  
- **DevOps & Infrastructure:** Docker, Docker Compose, Git  
- **Testing & Quality:** JUnit, Mockito, SonarQube

### Microservice architecture

#### ecommerce-api-service

![ecommerce-api-service diagram](https://uml.planttext.com/plantuml/png/pLPHJzmm37xlhuXusI4ufBsUq8a9s8OO0m7QwsHQNoyZJKgIJZ34VrznBjs5-hJidiRBKUVpP_kptVJ8UR2-QrHHl1DdsgFLuANHeEGl-aUKHYzatLbwAae5ph70OyA9vqBqVtlAbA3m6frHFz5ETiLCV4bC0TQWSr3ZZUbiYOIwMBSKBwzfdSFY7iILDLLNecKJ8aX499bCxupnpbjeRz3sSLqVuyPRgUkGGKjE_XeVEdG-Kdo4qXlxH1cziHq6x_sLtLFeazlBEST7XxVOj0fyg2-nv74TM-onswOMl3UGvkV74vB0u32LteLEGXlzB-z-OEddrCx8zTf7RurIQ4CJuy7nO8yjlF_JNN_LtIdfbcWfLyOUN9B16eqL2L6ZN_bEqgRrfP6qgMbVX9cEW_nQuZMwrcY7fzfB_tIuLd7ucQyqXwgAeHBvqu3imNPXKwNniSJMozv5B45N8yLFu_ddS2gUiqKCV9zGevNbm3F1nvaOdhWInZclytq39IlQxu744US5-gMfFjYwQr3xhyOJSW3npEjlaNOO4rA07w5OlsMM-DzMU5gErsoR9OkfdEc5kJMjBDEts3IPAWQmjwexdUf9c-OyUrl4BkSSWaJVDDiPNyHkEe-znSK9vATUF2TrYOthyNrbFm4FmKKRBnTFfmr8DMZGX-VYyJGnjeO6Hp9BMeGJqok2mLQXAwsayNk3VkY05bJCwCAiuxl9Wkyn0ysG7AyTBqKAonU2JXJekeCQ6NqOxlzHdQWBdmWcDIVodLNq9PAln7WcPQJ8ZV-ozcsb1EXAuEFgmQ82UhijPUkAJSkmytv7JATp-iqPu1dThI2rAxX-feGCOZDTCRgZYUTymmNTENIyItgNR8OU7CoPJkJ0eqBvJb65N70gMNGfk6zH9aGMRc0l2kwkpX6JTHYCFPYktB_W-NkNOoduMM04o46c4XoXhhf65Ry1)

- `ProductController`: Main logic entry point. Handles REST POST/GET requests, validates them, and routes to the corresponding Kafka topic  
- `KafkaProductPublisher`: Publishes messages to Kafka topics  
- `KafkaProducerConfig`: Kafka producer configuration  
- `InternationalizationConfig`: i18n configuration (language setup)  
- `ProductExceptionHandler`: Handles API-level exceptions gracefully  
- `ProductCreateRequestDTO`: User request to create a product  
- `ProductRequestDTO`: User request to retrieve a product  
- `ProductKafkaCreateDTO`: Kafka message for creating a product  
- `ProductKafkaDTO`: Kafka message for querying a product  

#### ecommerce-notification-service

![ecommerce-notification-service diagram](https://uml.planttext.com/plantuml/png/nLTDRzms4BthLmYvH6wp0NerWaKQhnsuZPiWDd8jnj98PKgHAaajiYtwtokH6db7r1B60aLyMMduvl5nFeRoM-l0kBwLsSlyIgjQDBq19xJAhoHOYpOhnz_yLZaqog-057_xXu3F_ydouU_75gs51kzrRqeiBlBB_ExOalsRJLI_Glqd3BwsRz5mZixegY-TH-mUFXBe1agdpS6JBcnlxfqHgdcLV-AksvZmoY0uF8-M4snNrVhV3y8wLFvS3eIADYbxOr2v1FQCC_tFcxEJ3sbycGzFL1WBZLCcDmfQ73ODPC64BGYvi4rdkQq6-mTDLWcgwQ4v8g3YMLL5cn9RVhaE-tsoxi31f58xKH-kkR0p973kH0mw8yhHjjFzeyHWgz2MHdI-wJZwhnwK4-wGN_h-Qz04km4tO6QQIDfdj9rMTWvDMiixqUom52t8nAu9dQI7K_aw-G9ILE2qcVIz7ojEbBFvpSEM1Ex9Dj3Cww7WXVM6ed9wM7mOY_bHQucWmYxNtqhqidWKVjFkHlUgcfTp_EPGLVR9WUREAOzY6chpUI_2ZZVQjE0S6hxZ3Xq8QUTLEXiRZT5Wz0fgxnDuWeW9LF2YvjbzbKjTWiInU_p31SiCqTw0a4Y4yPcu3vVoSrKzUIwxDRMzZnCDcPNEeQL5xzwZsOUo_CGC31J5cT3Hoj7a6P8hQk71ryVlZ9d6s9UZQeoATdKiR-An_7mUDc6VKJ8XKMMuizuZ9QjOdCB7XLd61Esrg1X1s9jpB5IT6AcSRsFGQhd7K0j5D48lvZe9rK6fPSKn6HTbyHaxRSLuFmKbtCQWTz1rSqdCxrd0r49La_tTuROgH2ZbeEsZxtcGb9ZflctGBJelTerRC_XQsBF0qWtnZ1igH8iwQQTR42g_qnLA7Z2ArApdUmxyfvnuAbD3YPcWfpXpD837l3mh8HJKU2mUfI5e2ObFwGW8qk0qfIz97kNOwMiVlZCIp0NVgjg0TMOmz8RQWiqgolLNsCFTOBz7LVbQ_uKPfZ0DpuM_CkcJplQFNx5qHvP7NHtcLsfcIgNqN_ds3z7PRFsxwyLFB_8VDXl_cpelnQGC7visozR-2NZzUkjP-0JhBTeKFDwKdBBbnFKkyziCMxsMDfljyWfAu-BBvwG3Ise2jj2VG2wkXnE2OcWIkLBm6Gy9Xr5JdmRHCEO0okJg9rW2csMCbIsjhgJhyxGFYm3BdKSjcYzxE_pshVmF)

- `ProductKafkaListener`: Main logic. Consume from Kafka topics, processes product create/query, sends emails  
- `ProductDetailsFormatter`: Formats user-facing messages  
- `KafkaConsumerConfig`: Kafka topic-to-DTO config  
- `InternationalizationConfig`: i18n setup (supports EN, ES, DE)  
- `ProductService`: Main product logic  
- `ProductMapper`: Maps between DTOs and entities  
- `ProductRepository`: DB access via Spring Data  
- `EmailNotifier`: Sends emails via JavaMailSender  
- `Product`: DB entity  
- `ProductResponseDTO`: Response to user  
- `ProductKafkaCreateDTO`: Create product message  
- `ProductKafkaDTO`: Product query message  


### ✅ Testing the App

To test the application, you can use **Postman**, **cURL**, or a web browser (for GET requests). When testing, you'll receive an email response based on the request type:

- **Product Query (GET)**: Retrieves a product by ID and sends details via email  
- **Product Creation (POST)**: Creates a product and sends confirmation with its ID  

The system supports internationalized email responses. Use the `Accept-Language` HTTP header to specify the language (supported values: `en`, `es`, `de`).

#### 🌍 Language Header Example
```

Accept-Language: es

```

#### 🔎 GET - Query a Product

You can test with a simple URL in the browser:

```

http://localhost:8080/service/query/product?id=2&name=Adrian&email=adrianamaroquero@gmail.com

```

#### 🛒 POST - Create a Product

Example using `curl`:

```

curl -X POST [http://localhost:8080/products](http://localhost:8080/products)&#x20;
-H "Content-Type: application/json"&#x20;
-H "Accept-Language: en"&#x20;
-d '{
"product": {
"name": "Cotton T-shirt",
"price": 25.99,
"description": "Unisex T-shirt made of 100% organic cotton",
"quantity": 100,
"rating": 4.5
},
"notifyEmail": "[customer@email.com](mailto:customer@email.com)"
}'

```


#### 🚀 Running the App

Start all necessary services with Docker Compose:

```

docker-compose up

```

This will launch:

- `ecommerce-api-service`  
- `ecommerce-notification-service`  
- `Apache Kafka`  
- `PostgreSQL`


#### 📥 Postman Collection

You can also import and run tests using this Postman collection:  
[Ecommerce.collection.postman_collection.json](https://github.com/user-attachments/files/21671627/Ecommerce.collection.postman_collection.json)


#### ✉️ Email Result Examples

- 📬 **Product Creation Email**  
  <img width="400" height="120" alt="image" src="https://github.com/user-attachments/assets/552b5297-d082-4d71-8b79-d020ba6d3330" />


- 📬 **Product Query Email**  
  <img width="400" height="230" alt="image" src="https://github.com/user-attachments/assets/131882cd-fa8a-48ad-9ac3-05c0492145c0" />

