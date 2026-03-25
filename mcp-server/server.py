from typing import Any

import httpx
from mcp.server.fastmcp import FastMCP
from pydantic import BaseModel, Field

mcp = FastMCP("Order System MCP Server", host="0.0.0.0", port=8080)

INVENTORY_SERVICE_URL = "http://inventory-service:8080"
PRODUCT_SERVICE_URL = "http://product-service:8080"
ORDER_SERVICE_URL = "http://order-service:8080"
USER_SERVICE_URL = "http://user-service:8080"

# ─────────────────────────────────────────────
# TOOLS
# ─────────────────────────────────────────────


# ─────────────────────────────────────────────
# USER
# ─────────────────────────────────────────────

def format_user(user: dict[str,Any]) -> str:
    return (
        f"UserId: {user.get("id")} | "
        f"Username: {user.get("name")} |"
        f"Email: {user.get("email")}"
    )

@mcp.tool()
async def get_users() -> str:
    """
    Retrieve all users.
    """
    async with httpx.AsyncClient() as client:
        try:
            response = await client.get(f"{USER_SERVICE_URL}/users", timeout=5.0)
            response.raise_for_status()
            users = response.json()
            return "\n".join([format_user(user) for user in users])
        except httpx.HTTPStatusError as e:
            return f"User service error: {e.response.status_code} - {e.response.text}"
        except httpx.RequestError as e:
            return f"Could not reach the user service: {str(e)}"
        
@mcp.tool()
async def get_user_by_id(
    user_id: int = Field(description="ID of the user", gt=0)
) -> str:
    """Retrieve full details of an user by their id"""
    async with httpx.AsyncClient() as client:
        try:
            response = await client.get(f"{USER_SERVICE_URL}/users/{user_id}", timeout=5.0)
            response.raise_for_status()
            return format_user(response.json())
        except httpx.HTTPStatusError as e:
            return f"User service error: {e.response.status_code} - {e.response.text}"
        except httpx.RequestError as e:
            return f"Could not reach the user service: {str(e)}"


# ─────────────────────────────────────────────
# PRODUCT
# ─────────────────────────────────────────────

def format_product(product: dict[str, Any]) -> str:
    ''' Format a product into a readable string'''
    return f"""
    Name: {product["name"]}
    Category: {product["category"]}
    Price: {product["price"]}
    Status: {product["status"]}
    """

@mcp.tool()
async def get_products(
    name: str | None = Field(default=None, description="The name of the product to search for"),
    category: str | None = Field(default=None, description="The category which the product belongs to")

) -> str:
    """
    Retrieve products. Optionally filter by name or category.
    """
    async with httpx.AsyncClient() as client:
        try:
            params: dict[str, str] = {}
            if name:
                params["name"] = name
            if category:
                params["category"] = category

            response = await client.get(
                f"{PRODUCT_SERVICE_URL}/products",
                params=params,
                timeout=5.0
            )

            if response.status_code != 200:
                return f"Product service error: HTTP {response.status_code}"

            products = response.json()
            return "\n".join([format_product(prod) for prod in products])
        except httpx.TimeoutException:
            return "Error: Product service timed out"
        
@mcp.tool()
async def get_products_by_id(
    product_id: int = Field(description="Id of the product")
) -> str:
    """Retrieve full details of a single product by its ID"""
    async with httpx.AsyncClient() as client:
        try:
            response = await client.get(
                f"{PRODUCT_SERVICE_URL}/products/{product_id}",
                timeout=5.0
            )

            if response.status_code != 200:
                return f"Product service error: HTTP {response.status_code}"

            products = response.json()
            return "\n".join([format_product(prod) for prod in products])
        except httpx.TimeoutException:
            return "Error: Product service timed out"

# ─────────────────────────────────────────────
# INVENTORY
# ─────────────────────────────────────────────

def format_inventory(inventory: dict[str, Any]) -> str:
    """Format an inventory item into a readable string."""
    return f"Product ID: {inventory['productId']} | Stock: {inventory.get('stock', 0)} units"


@mcp.tool()
async def get_all_inventory() -> str:
    """
    Retrieve the full inventory list for all products.
    Returns stock quantities for every product in the system.
    """
    async with httpx.AsyncClient() as client:
        try:
            response = await client.get(f"{INVENTORY_SERVICE_URL}/inventory", timeout=5.0)
            response.raise_for_status()

            items = response.json()
            if not items:
                return "No inventory records found."

            return "\n".join(format_inventory(inv) for inv in items)

        except httpx.HTTPStatusError as e:
            return f"Inventory service error: HTTP {e.response.status_code}"
        except httpx.TimeoutException:
            return "Inventory service is not responding. Please try again later."
        except httpx.RequestError as e:
            return f"Could not reach the inventory service: {e}"

@mcp.tool()
async def check_inventory(
    product_id: int = Field(description = "Id of the product")
) -> str:
    """
    Check stock levels for a specific product.
    Requires the numeric product ID.
    Returns current stock quantity and product details.
    """
    async with httpx.AsyncClient() as client:
        try:
            response = await client.get(
                f"{INVENTORY_SERVICE_URL}/inventory/{product_id}",
                timeout=5.0
            )

            if response.status_code == 404:
                return f"No inventory record found for product ID {product_id}."

            if response.status_code != 200:
                return f"Inventory service error: HTTP {response.status_code}."

            data = response.json()

            pid = data["productId"]
            quantity = data["stock"]

            return (
                f"Product ID: {pid}\n"
                f"Available quantity: {quantity} units"
            )

        except httpx.RequestError as e:
           return f"Could not reach inventory service: {e}"

@mcp.tool()
async def increase_stock(
    productId: int = Field(description="The id of the product that has new stock"),
    quantity:int = Field(description="Units to be added to the stock of the product")
) -> str:
    """
    Increase the stock of a product by a given quantity.
    Use this when new inventory arrives for an existing product.
    """
    async with httpx.AsyncClient() as client:
        try:
            params: dict[str,str] = {"quantity": str(quantity)}
            
            response = await client.patch(
                f"{INVENTORY_SERVICE_URL}/inventory/{productId}/increase",
                params=params,
                timeout=5.0
            )

            if response.status_code != 200:
                return f"Error on the Inventory Service: HTTP {response.status_code}"

            inventory = response.json()

            return (
                f"Product Id: {inventory['productId']}\n"
                f"Stock: {inventory['stock']}"
            )
        except httpx.TimeoutException:
            return "Inventory service is not responding. Please try again later."

@mcp.tool()
async def decrease_stock(
    productId: int = Field(description="The id of the product that has new stock"),
    quantity:int = Field(description="Units to be deducted from the product stock")
) -> str:
    """
    Decrease the stock of a product by a given quantity.
    Use this when an existing product is sold.
    """
    async with httpx.AsyncClient() as client:
        try:
            params: dict[str,str] = {"quantity": str(quantity)}
            
            response = await client.patch(
                f"{INVENTORY_SERVICE_URL}/inventory/{productId}/decrease",
                params=params,
                timeout=5.0
            )

            if response.status_code != 200:
                return f"Error on the Inventory Service: HTTP {response.status_code}"

            inventory = response.json()

            return (
                f"Product Id: {inventory['productId']}\n"
                f"Stock: {inventory['stock']}"
            )
        except httpx.TimeoutException:
            return "Inventory service is not responding. Please try again later."

# ─────────────────────────────────────────────
# ORDER
# ─────────────────────────────────────────────

class OrderItem(BaseModel):
    productId: int = Field(description="The Id of the product",gt=0)
    quantity: int = Field(description="The quantity desired",gt=0)

@mcp.tool()
async def create_order(
        customer_id : int = Field(description="Customer ID"),
        items : list[OrderItem] = Field(description="List of itemsin the order")
) -> str:
    """
    Create a new order for a customer.
    Requires a customer ID and at least one item with a productId and a quantity.
    """
    if not items:
        return "Error: an order must have at least one item"
    
    async with httpx.AsyncClient() as client:
        try:
            payload: dict[str, Any] = {
                "customerId": customer_id,
                "items": [item.model_dump() for item in items]
            }
            response = await client.post(f"{ORDER_SERVICE_URL}/orders", json=payload)
            response.raise_for_status()
            return f"Order created successfully: {response.json()}"
        except httpx.HTTPStatusError as e:
            return f"Order creation failed: {e.response.status_code} - {e.response.text}"
        except httpx.RequestError as e:
            return f"Could not reach the order service: {str(e)}"

# ─────────────────────────────────────────────
# ENTRYPOINT
# ─────────────────────────────────────────────

if __name__ == "__main__":
    mcp.run(transport="sse")