package com.sistema_contable.sistema.contable.resources;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_contable.sistema.contable.dto.accounting.EntryResponseDTO;
import com.sistema_contable.sistema.contable.exceptions.ModelExceptions;
import com.sistema_contable.sistema.contable.model.accounting.Entry;
import com.sistema_contable.sistema.contable.services.accounting.interfaces.JournalService;
import com.sistema_contable.sistema.contable.services.security.interfaces.AuthorizationService;
import com.sistema_contable.sistema.contable.util.DateFormatter;

@RestController
@RequestMapping("/journal")
@CrossOrigin(origins = "${FRONT_URL}")
public class JournalResource {


    //dependencies
    @Autowired
    private JournalService service;
    @Autowired
    private AuthorizationService authService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private DateFormatter dateFormatter;


    //end points
    @GetMapping
    public ResponseEntity<?> getLastEntrys(@RequestHeader("Authorization") String token){
        try {
            authService.authorize(token);
            return new ResponseEntity<>(entryResponse(service.getLastEntrys()), HttpStatus.OK);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/between")
    public ResponseEntity<?> getBetweenDates(
            @RequestHeader("Authorization") String token,
            @RequestParam("before") String before,
            @RequestParam("after")String after) {
        try {
            authService.authorize(token);
            return new ResponseEntity<>(
                    entryResponse(service.getJournalBetween(
                        dateFormatter.beforeDate(before),
                        dateFormatter.afterDate(after))),
                    HttpStatus.OK);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //secondary methods
    //create the body for the response and map the entry
    private List<EntryResponseDTO> entryResponse(List<Entry> entrys) {
        return entrys.stream().map(entry-> mapper.map(entry, EntryResponseDTO.class)).toList();
    }
}
