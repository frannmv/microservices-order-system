# Microservices Order System

A distributed backend system for an online store, built with a microservices architecture. It provides services for managing products, inventory, and customer orders, communicating via REST APIs. 


## Order Flow

When a new order is created, the system performs **cross-service validation** by verifying:

- The customer exists (User Service)
- The requested products exist (Product Service)
- The requested quantities are available (Inventory Service)

## Services

| Service | Responsibility |
|---|---|
| **User Service** | Manages customer identity and contact info |
| **Product Service** | Manages the product catalog (name, price, category) |
| **Inventory Service** | Tracks stock per product |
| **Order Service** | Handles order creation, validation, lifecycle, and history |
| **Gateway Service** | Single entry point for all external traffic |
| **MCP Server** | Exposes the system's business operations for AI agents |

Each service runs in its own Docker container, with a dedicated database container per persistence need, so the full system can be started as a single multi-container application.


## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java |
| **Framework** | Spring Boot |
| **Persistence** | JPA + Hibernate |
| **Containerization** | Docker |
| **API Gateway** | Spring Cloud Gateway |
| **MCP Server** | Python + FastMCP |
| **Version Control** | Git & GitHub |


## MCP Server

The system includes an MCP server built with **Python + FastMCP**, which exposes the system's business operations as tools that AI agents and LLMs can call directly.

### Available tools

| Tool | Description |
|---|---|
| `get_users` | Retrieve all registered users |
| `get_user` | Retrieve a user by their ID |
| `get_products` | List products, optionally filtered by name or category |
| `get_product` | Retrieve a single product by its ID |
| `get_inventory` | Retrieve stock levels for all products |
| `get_inventory_by_product` | Check stock for a specific product |
| `increase_stock` | Add stock units to a product |
| `decrease_stock` | Deduct stock units from a product |
| `create_order` | Create a new order for a customer with one or more items |


## Getting Started

### Run the system

```bash
git clone https://github.com/frannmv/microservices-order-system.git
cd microservices-order-system
docker compose up -d
```
