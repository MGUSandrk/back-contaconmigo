package com.sistema_contable.sistema.contable.services.sales;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sistema_contable.sistema.contable.exceptions.InvoiceNotFindException;
import com.sistema_contable.sistema.contable.model.sales.Invoice;
import com.sistema_contable.sistema.contable.repository.InvoiceRepository;

public class InvoiceServiceImpTest {

    @Test
    void searchByIdReturnsInvoiceFromRepository() throws Exception {
        InvoiceServiceImp service = new InvoiceServiceImp();
        InvoiceRepository invoiceRepository = Mockito.mock(InvoiceRepository.class);
        inject(service, "invoiceRepository", invoiceRepository);

        Invoice invoice = new Invoice();
        invoice.setId(5L);
        invoice.setInvoiceNumber("INV-5");
        when(invoiceRepository.searchById(5L)).thenReturn(invoice);

        Invoice result = service.searchById(5L);

        assertEquals(invoice, result);
    }

    @Test
    void searchByIdThrowsWhenInvoiceDoesNotExist() throws Exception {
        InvoiceServiceImp service = new InvoiceServiceImp();
        InvoiceRepository invoiceRepository = Mockito.mock(InvoiceRepository.class);
        inject(service, "invoiceRepository", invoiceRepository);
        when(invoiceRepository.searchById(99L)).thenReturn(null);

        assertThrows(InvoiceNotFindException.class, () -> service.searchById(99L));
    }

    @Test
    void generatePdfSearchesInvoiceAndDelegatesRendering() throws Exception {
        InvoiceServiceImp service = new InvoiceServiceImp();
        InvoiceRepository invoiceRepository = Mockito.mock(InvoiceRepository.class);
        InvoicePdfService invoicePdfService = Mockito.mock(InvoicePdfService.class);
        inject(service, "invoiceRepository", invoiceRepository);
        inject(service, "invoicePdfService", invoicePdfService);

        Invoice invoice = new Invoice();
        invoice.setId(7L);
        invoice.setInvoiceNumber("INV-7");
        byte[] expectedPdf = new byte[] {1, 2, 3};
        when(invoiceRepository.searchById(7L)).thenReturn(invoice);
        when(invoicePdfService.generarPdf(invoice)).thenReturn(expectedPdf);

        byte[] result = service.generatePdf(7L);

        assertArrayEquals(expectedPdf, result);
        verify(invoicePdfService).generarPdf(invoice);
    }

    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = InvoiceServiceImp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
