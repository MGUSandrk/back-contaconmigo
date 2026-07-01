# Invoice Fiscal Simulation Design

## Objetivo

Completar la factura existente con datos fiscales simulados para que el PDF se vea similar a una factura argentina tipo A, B o C, sin integracion real con ARCA/AFIP y sin validez fiscal.

## Alcance

La factura ya se crea desde una venta con cliente, entidad emisora, vendedor, productos, importes, medio de pago y `InvoiceType`. Este cambio no reemplaza ese flujo. Solo agrega datos simulados comunes y mejora el PDF.

Incluido:

- Numero legal con formato de punto de venta y comprobante, por ejemplo `00004-00000001`.
- CAE simulado.
- Fecha de vencimiento de CAE simulada.
- QR random decorativo/educativo, sin validacion real.
- Diseno PDF similar a comprobante argentino con bloque A/B/C, datos del emisor, datos del cliente, detalle, totales y bloque inferior fiscal simulado.
- Leyenda educativa para aclarar que no es comprobante valido fiscalmente.

No incluido:

- Conexion con ARCA/AFIP.
- Emision de CAE real.
- Validacion real del QR.
- Cambio del flujo de venta, stock o asientos contables.

## Arquitectura

Los datos propios de la factura siguen viviendo en `Invoice` como snapshot inmutable de la venta. Los nuevos datos fiscales simulados tambien se guardan en `Invoice` para que el mismo comprobante no cambie entre descargas.

`SaleServiceImp` sigue creando la factura mediante `Invoice.fromSale(...)`. La configuracion fiscal simulada se agrega dentro del armado de `Invoice`, manteniendo el controller y el repositorio sin reglas de negocio.

`InvoicePdfService` se mantiene como servicio de renderizado: recibe una `Invoice`, prepara el contexto Thymeleaf y genera el PDF. La plantilla `factura.html` consume los nuevos campos y aplica el formato visual.

## Datos Simulados

Los datos simulados pueden ser iguales para todos o derivados de datos existentes:

- `legalInvoiceNumber`: combina `salesPoint` con el id de la venta o factura en formato argentino. Si no hay id disponible al crear el snapshot, usa el id de `Sale`, que ya existe antes de crear `Invoice`.
- `cae`: valor numerico simulado estable.
- `caeExpirationDate`: fecha de factura mas 10 dias.
- `qrCodeBase64`: imagen QR random generada para mostrar en el PDF.

El QR no necesita funcionar. Debe ser una imagen valida embebible en HTML como `data:image/png;base64,...`.

## PDF

La plantilla debe parecerse a una factura argentina:

- Cabecera con datos del emisor a la izquierda.
- Centro destacado con letra `A`, `B` o `C` segun `invoiceType`.
- Derecha con `FACTURA`, punto de venta, numero, fecha y CUIT.
- Bloque de cliente con nombre, CUIT y condicion frente al IVA si esta disponible.
- Tabla de items con producto, cantidad, precio unitario y subtotal.
- Totales al pie.
- Bloque inferior con QR, CAE, vencimiento de CAE y leyenda educativa.

## Errores Y Validaciones

No se agregan errores nuevos. Si falta algun dato opcional, el PDF debe mostrar un valor razonable como `-` o `Consumidor Final` segun corresponda, sin romper la generacion.

## Testing

Agregar o actualizar tests de `Invoice` para verificar que `fromSale(...)` completa:

- Tipo de factura A/B/C existente.
- Numero legal con punto de venta.
- CAE simulado.
- Vencimiento de CAE posterior a la fecha de factura.
- QR base64 no vacio.

Actualizar tests de `InvoiceServiceImp` solo si cambia la firma o el contrato de renderizado. La generacion PDF debe seguir delegando en `InvoicePdfService`.

## Decisiones

- La factura es educativa y no fiscalmente valida.
- Los datos fiscales simulados se persisten en `Invoice`.
- El QR es random/decorativo y no se valida.
- No se modifica la separacion por capas del proyecto.
