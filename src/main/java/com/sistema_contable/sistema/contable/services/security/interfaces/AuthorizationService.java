package com.sistema_contable.sistema.contable.services.security.interfaces;

import com.sistema_contable.sistema.contable.model.User;

public interface AuthorizationService {
    public User authorize(String token) throws Exception;
    public User adminAuthorize(String token) throws Exception;
    public User countableAuthorize(String token) throws Exception;
    public User sellerAuthorize(String token) throws Exception;
}
