package m.heim.server.service;

import m.heim.server.domain.TokenModel;
import m.heim.server.domain.UserPrincipal;

/**
 * Interface als Vorlage für einen TokenService
 */
public interface TokenService {
    /**
     * Generiert einen JSON Web Token aus einem {@link TokenModel}
     * @param tokenModel Modell eines JWT
     * @return JSON Web Token in Form eines Authorization Headers
     */
    String generateToken(TokenModel tokenModel);
    /**
     * Verwandelt einen JSON Web Token in ein {@link TokenModel}
     * @param token JSON Web Token in Form eines Strings
     * @return Modell eines JWT
     */
    TokenModel parseToken(String token);
    /**
     * Entfernt "Bearer " vom Token und führt verwandelt ihn in ein {@link TokenModel}
     * @param authorizationHeader JSON Web Token in Form eines Authorization Headers
     * @return Modell eines JWT
     */
    TokenModel parseTokenFromAuthHeader(String authorizationHeader);
    /**
     * Gibt den User in Form vom {@link UserPrincipal} von einem {@link TokenModel} zurück
     * @param tokenModel Modell eines JWT
     * @return Modell eines User in From von {@link UserPrincipal}
     */
    UserPrincipal getUserPrincipalFromTokenModel(TokenModel tokenModel);
}
