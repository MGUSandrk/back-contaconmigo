package com.sistema_contable.sistema.contable.services.sales;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sistema_contable.sistema.contable.dto.SaleItemDTO;
import com.sistema_contable.sistema.contable.dto.SaleRequestDTO;
import com.sistema_contable.sistema.contable.model.Client;
import com.sistema_contable.sistema.contable.model.CostingMethodType;
import com.sistema_contable.sistema.contable.model.EntityModel;
import com.sistema_contable.sistema.contable.model.Product;
import com.sistema_contable.sistema.contable.model.User;
import com.sistema_contable.sistema.contable.model.sales.Invoice;
import com.sistema_contable.sistema.contable.model.sales.InvoiceItem;
import com.sistema_contable.sistema.contable.model.sales.Sale;
import com.sistema_contable.sistema.contable.model.sales.SaleProduct;

public class SaleServiceImpTest {

    @Test
    void createInvoiceStoresImmutableStructuredItems() throws Exception {
        SaleServiceImp service = new SaleServiceImp();

        Product product = new Product();
        product.setId(7L);
        product.setName("Lapicera azul");
        product.setSalePrice(150.0);

        SaleProduct saleProduct = new SaleProduct();
        saleProduct.setProduct(product);
        saleProduct.setQuantity(2);
        saleProduct.setPrice(150.0);

        Sale sale = new Sale();
        sale.setId(3L);
        sale.setDateCreated(new Date());
        sale.setSaleProducts(List.of(saleProduct));

        Client client = new Client();
        client.setFullName("Juan Perez");
        client.setCuit("20123456789");

        User seller = new User();
        seller.setUsername("vendedor");

        EntityModel entity = new EntityModel();
        entity.setName("Empresa");
        entity.setCostingMethod(CostingMethodType.FIFO);

        SaleItemDTO item = new SaleItemDTO();
        item.setProductId(7L);
        item.setQuantity(2);

        SaleRequestDTO request = new SaleRequestDTO();
        request.setPaymentMethod("Efectivo");
        request.setInstallments(1);
        request.setItems(List.of(item));

        Method createInvoice = SaleServiceImp.class.getDeclaredMethod(
                "createInvoice",
                Sale.class,
                Client.class,
                User.class,
                EntityModel.class,
                Double.class,
                Double.class,
                Double.class,
                SaleRequestDTO.class,
                Double.class);
        createInvoice.setAccessible(true);

        Invoice invoice = (Invoice) createInvoice.invoke(
                service,
                sale,
                client,
                seller,
                entity,
                300.0,
                0.0,
                300.0,
                request,
                90.0);

        assertEquals(1, invoice.getItems().size());
        InvoiceItem invoiceItem = invoice.getItems().get(0);
        assertEquals(7L, invoiceItem.getProductId());
        assertEquals("Lapicera azul", invoiceItem.getProductName());
        assertEquals(2, invoiceItem.getQuantity());
        assertEquals(150.0, invoiceItem.getUnitPrice());
        assertEquals(300.0, invoiceItem.getSubtotal());
    }
}
