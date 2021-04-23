package m.heim.server.service;

import m.heim.server.domain.TokenModel;
import m.heim.server.domain.UserPrincipal;

public interface TokenService {
    String generateToken(TokenModel user);
    TokenModel parseToken(String token);
    TokenModel parseTokenFromAuthHeader(String token);
    UserPrincipal getUserPrincipalFromTokenModel(TokenModel tokenModel);
}
