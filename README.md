# RetailHub_Project
RetailHub is a full-stack information system developed as a **capstone group project** for academic purposes. It was designed for *RetailHUB*, a rapidly growing retail business operating both physical and online stores.

This system focuses on a complete restructuring of business operations—enhancing the management of customers, products, inventory, and sales.

## Project Objectives

- Digitize business data  
- Automate critical processes  
- Enhance data security  
- Support decision-making with modern reports and analytics  
- Reduce operational costs  
- Support both local store operation and centralized business insights  


The project combines:
- a Java-based backend and GUI-Swing UI for CRUD operations and retail logic
- A Python data analysis module for business logic
- A shared MySQL database for persistent data across both layers

## tech Stack


| Layer          | Requirements                                           |
|----------------|--------------------------------------------------------|
| Backend        | Java (JDK 17+), JDBC, MySQL, Maven                     |
| Frontend       | Java Swing GUI                                         |
| Data Analysis  | Python 3.10+                                           |
| Database       | MySQL Server 8.0+                                      |
| Configuration  | `config.properties` for DB credentials                 |
| DevOps         | Git, `.gitignore`, modular folder structure            |


## Features
#### 1. Client Management
Full CRUD operations
Track total spending and applies loyalty discounts
Export clients in JSON format

#### 2. Product Management
Create/list products by category, price, and cost
Manage inventory by product ID
Export products in JSON format

#### 3. Store Management
Multi-store supported
Stock tracked per store and per product
Export stores in JSON format

#### 4. Stock Management
Add stock to stores, monitor low-cost alerts
Auto-update stock during transactions
JSON exportable inventory data

#### 5. Transactions
Purchases processing and discount application based on loyalty rules
Records product-level details (includes)
Export transactions in JSON format

#### 6. Database connection
JDBC-based database interaction
Configuration via external `.properties` file for avoiding harcoding-secure DB access

#### 7. Python Data Analysis
A separate Python module handles reporting, trends, and forecasting  
Connects to the shared MySQL database  
Offers insights 

## Installation
```
git clone https://github.com/AngelikiMt/RetailHub_Project.git
cd RetailHub_Project
git checkout fullstack_version
```

## Configuration
In java/src/resources/config.properties edit your database credentials:

```
db.url = jdbc:mysql://localhost:3306/retail_hub
db.username = your_username
db.password = your_password
```

## Run the Java App (Maven)
For running the Java project with maven from the root directory:

```
mvn clean install
mvn compile
mvn exec:java
```