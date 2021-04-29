package m.heim.server.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import m.heim.server.domain.TokenModel;
import m.heim.server.domain.UserPrincipal;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * {@link Service} mit Hilfsfunktionen für JSON Web Tokens
 */
@Service
public class JWTTokenService implements TokenService {
    /**
     * Generiert einen JSON Web Token aus einem {@link TokenModel}
     * @param tokenModel Modell eines JWT
     * @return JSON Web Token in Form eines Authorization Headers
     */
    @Override
    public String generateToken(TokenModel tokenModel) {
        Key key = Keys.hmacShaKeyFor("gUkXp2s5v8y/B?E(H+MbQeShVmYq3t6w".getBytes());

        String compactTokenString = Jwts.builder()
                .claim("publicAddress", tokenModel.getPublicAddress())
                .setExpiration(tokenModel.getExpirationDate())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return "Bearer " + compactTokenString;
    }
    /**
     * Entfernt "Bearer " vom Token und führt verwandelt ihn in ein {@link TokenModel}
     * @param authorizationHeader JSON Web Token in Form eines Authorization Headers
     * @return Modell eines JWT
     */
    @Override
    public TokenModel parseTokenFromAuthHeader(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return parseToken(token);
    }
    /**
     * Verwandelt einen JSON Web Token in ein {@link TokenModel}
     * @param token JSON Web Token in Form eines Strings
     * @return Modell eines JWT
     */
    @Override
    public TokenModel parseToken(String token) {
        byte[] secretBytes = "gUkXp2s5v8y/B?E(H+MbQeShVmYq3t6w".getBytes();
        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token);
        String publicAddress = jwsClaims.getBody().get("publicAddress", String.class);
        Date expirationDate = jwsClaims.getBody().getExpiration();
        return new TokenModel(publicAddress, expirationDate);
    }
    /**
     * Gibt den User in Form vom {@link UserPrincipal} von einem {@link TokenModel} zurück
     * @param tokenModel Modell eines JWT
     * @return Modell eines User in From von {@link UserPrincipal}
     */
    @Override
    public UserPrincipal getUserPrincipalFromTokenModel(TokenModel tokenModel) {
        return new UserPrincipal(tokenModel.getPublicAddress());
    }
}
