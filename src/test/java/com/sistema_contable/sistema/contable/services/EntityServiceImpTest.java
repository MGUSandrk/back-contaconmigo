package com.sistema_contable.sistema.contable.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sistema_contable.sistema.contable.exceptions.BadEntityException;
import com.sistema_contable.sistema.contable.exceptions.EntityNotFindException;
import com.sistema_contable.sistema.contable.model.EntityModel;
import com.sistema_contable.sistema.contable.model.VatCondition;
import com.sistema_contable.sistema.contable.model.costing_method.CostingMethodType;
import com.sistema_contable.sistema.contable.repository.EntityRepository;

public class EntityServiceImpTest {

    private EntityServiceImp service;

    @Mock
    private EntityRepository repository;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new EntityServiceImp();
        inject("repository", repository);
    }

    @Test
    void getEntityReturnsStoredApplicationEntity() throws Exception {
        EntityModel entity = entity(1L, "Contaconmigo", CostingMethodType.FIFO);

        when(repository.searchEntity()).thenReturn(entity);

        assertEquals(entity, service.getEntity());
    }

    @Test
    void getEntityRejectsMissingApplicationEntity() {
        when(repository.searchEntity()).thenReturn(null);

        assertThrows(EntityNotFindException.class, () -> service.getEntity());
    }

    @Test
    void getCostingMethodReturnsConfiguredApplicationCostingMethod() throws Exception {
        EntityModel entity = entity(1L, "Contaconmigo", CostingMethodType.WAC);

        when(repository.searchEntity()).thenReturn(entity);

        assertEquals(CostingMethodType.WAC, service.getCostingMethod());
    }

    @Test
    void modifyUpdatesTheStoredApplicationEntity() throws Exception {
        EntityModel storedEntity = entity(1L, "Vieja empresa", CostingMethodType.FIFO);
        EntityModel request = entity(null, " Nueva empresa ", CostingMethodType.WAC);
        request.setCuit(" 30123456789 ");
        request.setCommercialAddress(" Calle 123 ");
        request.setGrossIncomeNumber(" 12345 ");
        request.setVatCondition(VatCondition.IVA_RESPONSABLE_INSCRIPTO);
        request.setActivityStartDate(new Date());
        request.setSalesPoint(2);

        when(repository.searchEntity()).thenReturn(storedEntity);

        service.modify(request);

        ArgumentCaptor<EntityModel> captor = ArgumentCaptor.forClass(EntityModel.class);
        verify(repository).save(captor.capture());
        EntityModel savedEntity = captor.getValue();

        assertEquals(1L, savedEntity.getId());
        assertEquals("Nueva empresa", savedEntity.getName());
        assertEquals(CostingMethodType.WAC, savedEntity.getCostingMethod());
        assertEquals("30123456789", savedEntity.getCuit());
        assertEquals("Calle 123", savedEntity.getCommercialAddress());
        assertEquals("12345", savedEntity.getGrossIncomeNumber());
        assertEquals(VatCondition.IVA_RESPONSABLE_INSCRIPTO, savedEntity.getVatCondition());
        assertEquals(2, savedEntity.getSalesPoint());
    }

    @Test
    void modifyRejectsBlankEntityName() {
        EntityModel request = entity(null, " ", CostingMethodType.WAC);

        assertThrows(BadEntityException.class, () -> service.modify(request));
    }

    @Test
    void modifyRejectsMissingCostingMethod() {
        EntityModel request = entity(null, "Contaconmigo", null);

        assertThrows(BadEntityException.class, () -> service.modify(request));
    }

    @Test
    void modifyRejectsMissingFiscalData() {
        EntityModel request = entity(null, "Contaconmigo", CostingMethodType.WAC);
        request.setCuit(null);

        assertThrows(BadEntityException.class, () -> service.modify(request));
    }

    private EntityModel entity(Long id, String name, CostingMethodType costingMethod) {
        EntityModel entity = new EntityModel();
        entity.setId(id);
        entity.setName(name);
        entity.setCostingMethod(costingMethod);
        entity.setCuit("30123456789");
        entity.setCommercialAddress("Calle 123");
        entity.setGrossIncomeNumber("12345");
        entity.setVatCondition(VatCondition.IVA_RESPONSABLE_INSCRIPTO);
        entity.setActivityStartDate(new Date());
        entity.setSalesPoint(1);
        return entity;
    }

    private void inject(String fieldName, Object value) throws Exception {
        Field field = EntityServiceImp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(service, value);
    }
}
