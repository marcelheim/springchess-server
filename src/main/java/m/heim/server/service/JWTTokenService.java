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

@Service
public class JWTTokenService implements TokenService {
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

    @Override
    public TokenModel parseTokenFromAuthHeader(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return parseToken(token);
    }

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

    @Override
    public UserPrincipal getUserPrincipalFromTokenModel(TokenModel tokenModel) {
        return new UserPrincipal(tokenModel.getPublicAddress());
    }
}
