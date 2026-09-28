# ShopLite — Small Shop Inventory & Billing (Spring Boot)

## Run in VS Code
1. Install JDK 17+ and Maven (or just install the "Extension Pack for Java" + "Spring Boot Extension Pack" — VS Code will prompt).
2. Open this folder in VS Code.
3. Run `ShopLiteApplication.java` (▶ Run), or in the terminal: `mvn spring-boot:run`
4. Open **http://localhost:8080** — the website.
   Database viewer (H2): http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:file:./data/shoplite`, user `sa`, blank password)

On startup, ShopLite adds the 60 grocery catalog products when they are missing, backfills category and unit for legacy products, and preserves existing stock and sales. The dashboard is the home page; Products includes search, category and stock filters, and price/stock sorting.

## Use MySQL instead of H2
Set the MySQL environment variables and activate the MySQL profile before starting the app. The database is created if needed; product records are seeded through Spring Data JPA.

PowerShell example:
```powershell
$env:SHOPLITE_DB_USERNAME = "root"
$env:SHOPLITE_DB_PASSWORD = "<your MySQL password>"
$env:SPRING_PROFILES_ACTIVE = "mysql"
mvn spring-boot:run
```

The URL can be overridden with `SHOPLITE_DB_URL`. Do not commit database credentials.

## REST API
| Method | URL | Purpose |
|---|---|---|
| POST | /api/products | Add product |
| GET | /api/products, /api/products/{id} | List products or view one product |
| PUT | /api/products/{id} | Update product |
| DELETE | /api/products/{id} | Delete product (only if no bill references it) |
| GET | /api/products/low-stock | Products below reorder threshold |
| GET | /api/dashboard | Product, category, low-stock, and today's sales totals |
| POST | /api/bills | Create bill (validates stock, does not deduct) |
| PUT | /api/bills/{id} | Update a draft bill |
| DELETE | /api/bills/{id} | Delete a draft bill |
| POST | /api/bills/{id}/finalize | Finalize bill → stock deducted |
| GET | /api/bills, /api/bills/{id} | View bills |
| GET | /api/sales/daily?date=YYYY-MM-DD | Total sales for a day |

Sample body — POST /api/products:
`{"name":"Ponni Rice","category":"Rice & Grains","price":70,"stockQuantity":50,"reorderThreshold":10,"unit":"kg"}`
Sample body — POST /api/bills:
`{"items":[{"productId":1,"quantity":2},{"productId":2,"quantity":1}]}`
