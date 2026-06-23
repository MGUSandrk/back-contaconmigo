package com.sistema_contable.sistema.contable.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema_contable.sistema.contable.exceptions.BadClientException;
import com.sistema_contable.sistema.contable.exceptions.ClientNotFindException;
import com.sistema_contable.sistema.contable.model.Client;
import com.sistema_contable.sistema.contable.repository.ClientRepository;
import com.sistema_contable.sistema.contable.services.interfaces.ClientService;

@Service
public class ClientServiceImp implements ClientService {

    //dependencies
    @Autowired
    private ClientRepository repository;

    //CRUD
    @Override
    public void create(Client client) throws Exception {
        this.validateClient(client);
        client.setFullName(client.getFullName().strip());
        client.setEmail(client.getEmail().strip());
        client.setCuit(client.getCuit().strip());
        repository.save(client);
    }

    @Override
    public void modifyById(Long id, Client client) throws Exception {
        this.validateClient(client);
        Client storedClient = this.searchById(id);
        storedClient.setFullName(client.getFullName().strip());
        storedClient.setEmail(client.getEmail().strip());
        storedClient.setCuit(client.getCuit().strip());
        repository.save(storedClient);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ClientNotFindException("ERROR : Client not found to DELETE");
        }
    }

    //GETTERS
    @Override
    public List<Client> getAll() throws Exception {
        return repository.findAll();
    }

    //SEARCHES
    @Override
    public Client searchById(Long id) throws Exception {
        Client client = repository.searchById(id);
        if (client == null) {
            throw new ClientNotFindException("ERROR : Client not found by id");
        }
        return client;
    }

    //SECONDARY METHODS
    private void validateClient(Client client) throws Exception {
        if (client == null) {
            throw new BadClientException("ERROR : Client is required");
        }
        if (client.getFullName() == null || client.getFullName().isBlank()) {
            throw new BadClientException("ERROR : Client full name is required");
        }
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new BadClientException("ERROR : Client email is required");
        }
        if (client.getCuit() == null || client.getCuit().isBlank()) {
            throw new BadClientException("ERROR : Client cuit is required");
        }
    }
}
