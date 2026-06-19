package com.sistema_contable.sistema.contable.services;

import com.sistema_contable.sistema.contable.model.Client;

import java.util.List;

public interface ClientService {
    void create(Client client) throws Exception;
    List<Client> getAll() throws Exception;
    Client searchById(Long id) throws Exception;
    void modifyById(Long id, Client client) throws Exception;
    void deleteById(Long id) throws Exception;
}
