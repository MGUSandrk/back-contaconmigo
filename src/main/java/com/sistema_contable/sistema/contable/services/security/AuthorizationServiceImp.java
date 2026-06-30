package com.sistema_contable.sistema.contable.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema_contable.sistema.contable.exceptions.AuthorizationException;
import com.sistema_contable.sistema.contable.model.Role;
import com.sistema_contable.sistema.contable.model.User;
import com.sistema_contable.sistema.contable.services.interfaces.UserService;
import com.sistema_contable.sistema.contable.services.security.interfaces.AuthorizationService;
import com.sistema_contable.sistema.contable.util.JwtTokenUtil;

@Service
public class AuthorizationServiceImp implements AuthorizationService {

    //dependencies
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService service;


    //methods
    @Override
    public User authorize(String token) throws Exception {
        //search the user in the db
        User userDB = service.findByUsername(jwtTokenUtil.getSubject(token));
        //check the token and the user existence in the db
        if (!jwtTokenUtil.verify(token) || userDB == null ){throw new AuthorizationException("ERROR : Invalid token or User not found");}
        else{return userDB;}
    }

    @Override
    public User adminAuthorize(String token) throws Exception {
        //search the user in the db
        User userDB = service.findByUsername(jwtTokenUtil.getSubject(token));
        //check the token, user existence and the role
        if (!jwtTokenUtil.verify(token) || userDB == null || !userDB.getRole().equals(Role.ADMIN)){throw new AuthorizationException("ERROR : Invalid token or User not role required");}
        else{return userDB;}
    }

    @Override
    public User countableAuthorize(String token) throws Exception {
        //search the user in the db
        User userDB = service.findByUsername(jwtTokenUtil.getSubject(token));
        //check the token, user existence and the role
        if (!jwtTokenUtil.verify(token) || userDB == null || (!userDB.getRole().equals(Role.COUNTABLE) && !userDB.getRole().equals(Role.ADMIN))){throw new AuthorizationException("ERROR : Invalid token or User not role required");}
        else{return userDB;}
    }

    @Override
    public User sellerAuthorize(String token) throws Exception {
        //search the user in the db
        User userDB = service.findByUsername(jwtTokenUtil.getSubject(token));
        //check the token, user existence and the role
        if (!jwtTokenUtil.verify(token) || userDB == null || (!userDB.getRole().equals(Role.SELLER) && !userDB.getRole().equals(Role.ADMIN))){throw new AuthorizationException("ERROR : Invalid token or User not role required");}
        else{return userDB;}
    }





}
