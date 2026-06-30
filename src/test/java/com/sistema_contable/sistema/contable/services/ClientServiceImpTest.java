package com.sistema_contable.sistema.contable.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sistema_contable.sistema.contable.exceptions.BadClientException;
import com.sistema_contable.sistema.contable.model.Client;
import com.sistema_contable.sistema.contable.model.DocumentType;
import com.sistema_contable.sistema.contable.model.VatCondition;
import com.sistema_contable.sistema.contable.repository.ClientRepository;

public class ClientServiceImpTest {

    private ClientServiceImp service;

    @Mock
    private ClientRepository repository;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new ClientServiceImp();
        inject("repository", repository);
    }

    @Test
    void createSavesIdentifiedClientWithFiscalData() throws Exception {
        Client client = client();

        service.create(client);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(repository).save(captor.capture());
        Client savedClient = captor.getValue();

        assertEquals("Juan Perez", savedClient.getFullName());
        assertEquals("juan@example.com", savedClient.getEmail());
        assertEquals("30123456789", savedClient.getCuit());
        assertEquals(VatCondition.IVA_RESPONSABLE_INSCRIPTO, savedClient.getVatCondition());
        assertEquals(DocumentType.CUIT, savedClient.getDocumentType());
        assertEquals("30123456789", savedClient.getDocumentNumber());
        assertEquals("Calle 123", savedClient.getCommercialAddress());
    }

    @Test
    void modifyUpdatesIdentifiedClientFiscalData() throws Exception {
        Client storedClient = client();
        Client request = client();
        request.setFullName(" Maria Gomez ");
        request.setEmail(" maria@example.com ");
        request.setCuit(" 27234567890 ");
        request.setDocumentNumber(" 27234567890 ");
        request.setCommercialAddress(" Avenida 456 ");

        when(repository.searchById(1L)).thenReturn(storedClient);

        service.modifyById(1L, request);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(repository).save(captor.capture());
        Client savedClient = captor.getValue();

        assertEquals("Maria Gomez", savedClient.getFullName());
        assertEquals("maria@example.com", savedClient.getEmail());
        assertEquals("27234567890", savedClient.getCuit());
        assertEquals("27234567890", savedClient.getDocumentNumber());
        assertEquals("Avenida 456", savedClient.getCommercialAddress());
    }

    @Test
    void createAllowsConsumerFinalWithoutDocumentAndAddress() throws Exception {
        Client client = new Client();
        client.setFullName("Consumidor Final");
        client.setEmail("cf@example.com");
        client.setVatCondition(VatCondition.CONSUMIDOR_FINAL);

        service.create(client);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(repository).save(captor.capture());
        assertEquals(VatCondition.CONSUMIDOR_FINAL, captor.getValue().getVatCondition());
    }

    @Test
    void createRejectsIdentifiedClientWithoutDocumentData() {
        Client client = client();
        client.setDocumentType(null);
        client.setDocumentNumber(null);

        assertThrows(BadClientException.class, () -> service.create(client));
    }

    @Test
    void createRejectsClientWithoutVatCondition() {
        Client client = client();
        client.setVatCondition(null);

        assertThrows(BadClientException.class, () -> service.create(client));
    }

    private Client client() {
        Client client = new Client();
        client.setFullName(" Juan Perez ");
        client.setEmail(" juan@example.com ");
        client.setCuit(" 30123456789 ");
        client.setVatCondition(VatCondition.IVA_RESPONSABLE_INSCRIPTO);
        client.setDocumentType(DocumentType.CUIT);
        client.setDocumentNumber(" 30123456789 ");
        client.setCommercialAddress(" Calle 123 ");
        return client;
    }

    private void inject(String fieldName, Object value) throws Exception {
        Field field = ClientServiceImp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(service, value);
    }
}
