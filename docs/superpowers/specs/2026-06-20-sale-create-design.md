# Sale Create Design

## Scope

Add the first sales endpoint with create only. The frontend will send the client id, the sold products with quantities, and the payments. The backend will calculate all sale prices from stored products.

Out of scope for this first step:

- Listing, searching, updating, or deleting sales.
- Stock movement or lot consumption.
- Accounting entries for sales.
- Detailed sale response DTOs.

## HTTP Contract

Endpoint:

```http
POST /sales/create
Authorization: Bearer <token>
Content-Type: application/json
```

Request body:

```json
{
  "idClient": 1,
  "products": [
    {
      "idProduct": 10,
      "quantity": 2
    }
  ],
  "payments": [
    {
      "id": 3,
      "amount": 3000.0
    }
  ]
}
```

`payments[].id` follows the existing `PaymentRequestDTO` convention and means payment type id.

Successful response:

```http
201 Created
```

with an empty body, matching the existing create style in nearby resources.

## Data Flow

`SaleResource` receives the request, authorizes with `AuthorizationService.authorize(token)`, maps DTOs to a `Sale` model shape, and delegates to `SaleService.create(sale, user)`.

`SaleServiceImp` owns the sale rules:

- Validate that the sale request exists.
- Search the client by id.
- Search each product by id.
- Search each payment type by id.
- Validate at least one product line.
- Validate product quantities are positive.
- Validate at least one payment.
- Validate payment amounts are positive.
- Calculate each line using the current `Product.salePrice`.
- Store `SaleProduct.price` as the unit price at the moment of the sale.
- Calculate `Sale.totalPrice` as the sum of `SaleProduct.price * quantity`.
- Validate that the sum of payments equals `Sale.totalPrice`.
- Set `Sale.dateCreated` to the current date.
- Set `Sale.client` and `Sale.seller`.
- Save the sale through `SaleRepository`.

`SaleProduct` is the sale line snapshot. It stores the product reference, quantity, and the unit sale price used for that sale, so later product price changes do not rewrite historical sale values.

## Components

New DTOs:

- `SaleRequestDTO`
- `SaleProductRequestDTO`

Existing DTO reused:

- `PaymentRequestDTO`

New repository:

- `SaleRepository extends JpaRepository<Sale, Long>`

New service interface and implementation:

- `services/interfaces/SaleService`
- `services/SaleServiceImp`

New resource:

- `resources/SaleResource`

New exception:

- `BadSaleException` with `HttpStatus.BAD_REQUEST`

## Error Handling

The resource follows the current project pattern:

- Catch `ModelExceptions`, print the message, and return the exception status.
- Catch unexpected `Exception` and return `HttpStatus.INTERNAL_SERVER_ERROR`.

Domain validation errors use `BadSaleException`. Missing related entities reuse existing service exceptions, such as client, product, and payment type not found.

## Testing

This project currently has no visible test structure. Verification for this first step will use at least:

- `mvn test` or the repository's available Maven verification command.
- If compilation is the only practical check, use `mvn -DskipTests package`.

Manual request verification can be done after the backend starts, using an authorized token and known ids for client, product, and payment type.
