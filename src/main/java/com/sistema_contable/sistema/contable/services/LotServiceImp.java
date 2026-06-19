package com.sistema_contable.sistema.contable.services;

import com.sistema_contable.sistema.contable.exceptions.LotNotFindException;
import com.sistema_contable.sistema.contable.model.Lot;
import com.sistema_contable.sistema.contable.repository.LotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotServiceImp implements LotService {

    //dependencies
    @Autowired
    private LotRepository repository;

    //CRUD
    @Override
    public void delete(Long id) throws Exception {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new LotNotFindException("ERROR : Lot not found to DELETE");
        }
    }

    //SEARCHES
    @Override
    public Lot searchById(Long id) throws Exception {
        Lot lot = repository.searchById(id);
        if (lot == null) {
            throw new LotNotFindException("ERROR : Lot not found by id");
        }
        return lot;
    }
}
