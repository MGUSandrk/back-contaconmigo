# Sale Create Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add `POST /sales/create` to persist a sale with client, seller, product lines, payments, and backend-calculated total.

**Architecture:** Keep the resource thin and put validation/calculation in `SaleServiceImp`. Persist the aggregate through `SaleRepository`, using existing services to resolve client, product, and payment type references.

**Tech Stack:** Java 17, Spring Boot, Spring MVC, Spring Data JPA, JUnit 5, Mockito.

---

### Task 1: Sale Service Contract And Behavior Test

**Files:**
- Create: `src/main/java/com/sistema_contable/sistema/contable/services/interfaces/SaleService.java`
- Create: `src/test/java/com/sistema_contable/sistema/contable/services/SaleServiceImpTest.java`

- [ ] **Step 1: Write failing tests**

Create tests for `SaleServiceImp.create(Sale sale, User seller)` that verify line price/total calculation and payment total validation.

- [ ] **Step 2: Run tests to verify RED**

Run: `mvn -Dtest=SaleServiceImpTest test`

Expected: compilation fails because `SaleServiceImp`, `SaleService`, and `SaleRepository` do not exist.

### Task 2: Sale Persistence And Validation

**Files:**
- Create: `src/main/java/com/sistema_contable/sistema/contable/repository/SaleRepository.java`
- Create: `src/main/java/com/sistema_contable/sistema/contable/exceptions/BadSaleException.java`
- Create: `src/main/java/com/sistema_contable/sistema/contable/services/SaleServiceImp.java`

- [ ] **Step 1: Implement repository and exception**

Add a JPA repository for `Sale` and a `BadSaleException` returning `HttpStatus.BAD_REQUEST`.

- [ ] **Step 2: Implement service**

Inject `SaleRepository`, `ClientService`, `ProductService`, and `PaymentTypeService`. Validate request shape, resolve references, set `dateCreated`, `seller`, `client`, line product/price, payment type, and total. Save through the repository.

- [ ] **Step 3: Run tests to verify GREEN**

Run: `mvn -Dtest=SaleServiceImpTest test`

Expected: tests pass.

### Task 3: Sale Create HTTP Resource

**Files:**
- Create: `src/main/java/com/sistema_contable/sistema/contable/dto/SaleRequestDTO.java`
- Create: `src/main/java/com/sistema_contable/sistema/contable/dto/SaleProductRequestDTO.java`
- Create: `src/main/java/com/sistema_contable/sistema/contable/resources/SaleResource.java`

- [ ] **Step 1: Implement DTOs**

`SaleRequestDTO` contains `Long idClient`, `List<SaleProductRequestDTO> products`, and `List<PaymentRequestDTO> payments`. `SaleProductRequestDTO` contains `Long idProduct` and `Integer quantity`.

- [ ] **Step 2: Implement resource**

Add `@RestController`, `@RequestMapping("/sales")`, `@CrossOrigin(origins = "${FRONT_URL}")`, and `@PostMapping(path = "/create")`. Authorize with `authService.authorize(token)`, map DTOs to `Sale`, call `service.create(sale, user)`, and return `201 Created`.

- [ ] **Step 3: Run verification**

Run: `mvn test`

Expected: all available tests pass and the project compiles.
