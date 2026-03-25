from typing import Any

import httpx
from mcp.server.fastmcp import FastMCP
from pydantic import Field

mcp = FastMCP("Order System MCP Server", host="0.0.0.0", port=8080)

INVENTORY_SERVICE_URL = "http://inventory-service:8080"
PRODUCT_SERVICE_URL = "http://product-service:8080"

# ─────────────────────────────────────────────
# PROMPT
# ─────────────────────────────────────────────

@mcp.prompt()
def inventory_assistant_prompt(low_stock_threshold: int = 10) -> str:
    """
    Guides the AI to behave as an inventory assistant
    and use the inventory tools effectively.
    """
    return f"""
    You are an inventory management assistant for an order system.

    When the user asks about stock or inventory, you must:
    - Call 'get_all_inventory' to retrieve the full product list
    - Call 'check_inventory' when the user mentions a specific product ID
    - Always sort results by quantity ascending unless the user specifies otherwise
    - Flag any product with {low_stock_threshold} units or fewer as low stock ⚠️
    - Flag any product with 0 units as out of stock ❌

    When presenting results:
    - Be concise and use a clear list format
    - Always include the product ID and quantity
    - If asked to increase or decrease stock, confirm the product ID and quantity before acting
    - Never assume a product ID — ask the user if it's not provided

    If the inventory service is unavailable, inform the user clearly and suggest retrying.
    """


# ─────────────────────────────────────────────
# TOOLS
# ─────────────────────────────────────────────

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
# ENTRYPOINT
# ─────────────────────────────────────────────

if __name__ == "__main__":
    mcp.run(transport="sse")