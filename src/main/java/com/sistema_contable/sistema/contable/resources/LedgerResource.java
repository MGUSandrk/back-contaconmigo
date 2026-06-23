package com.sistema_contable.sistema.contable.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_contable.sistema.contable.dto.Mapper;
import com.sistema_contable.sistema.contable.dto.accounting.LedgerResponseDTO;
import com.sistema_contable.sistema.contable.dto.accounting.MovementLedgerResponseDTO;
import com.sistema_contable.sistema.contable.exceptions.ModelExceptions;
import com.sistema_contable.sistema.contable.model.accounting.Movement;
import com.sistema_contable.sistema.contable.services.accounting.interfaces.LedgerService;
import com.sistema_contable.sistema.contable.services.security.interfaces.AuthorizationService;
import com.sistema_contable.sistema.contable.util.DateFormatter;

@RestController
@RequestMapping("/ledger")
@CrossOrigin(origins = "${FRONT_URL}")
public class LedgerResource {
    //dependencies
    @Autowired
    private LedgerService ledgerService;
    @Autowired
    private AuthorizationService authService;
    @Autowired
    private Mapper modelMapper;
    @Autowired
    private DateFormatter dateFormatter;

    //endpoints
    @GetMapping(path = "/{id}",produces = "application/json")
    public ResponseEntity<?> ledgerByAccountBetweenDates(
        @RequestHeader("Authorization") String token,
        @PathVariable Long id,
        @RequestParam("before") String before,
        @RequestParam("after")String after) {
        try {
            authService.authorize(token);
            return new ResponseEntity<>(responseDTOS(
                    ledgerService.LadgerByAccountBetweem(id,
                            dateFormatter.beforeDate(before),
                            dateFormatter.afterDate(after))),
                    HttpStatus.OK);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);}}

    //secondary methods
    private LedgerResponseDTO responseDTOS(List<Movement> movements) {
        LedgerResponseDTO ledgerResponseDTO = new LedgerResponseDTO();
        ledgerResponseDTO.setMovements(movements.stream()
                .map(movement -> modelMapper.map(movement, MovementLedgerResponseDTO.class)).toList());
        ledgerResponseDTO.setInitialBalance(calculateInitialBalance(movements.get(0)));
        return ledgerResponseDTO;
    }

    private String calculateInitialBalance(Movement movement) {
        if(movement.getAccount().isPlus()){
            return String.valueOf(movement.getAccountBalance()-movement.getDebit()+movement.getCredit());
        }
        else{
            return String.valueOf(movement.getAccountBalance()-movement.getCredit()+movement.getDebit());}
    }

}
