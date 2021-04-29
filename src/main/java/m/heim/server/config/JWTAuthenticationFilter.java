package m.heim.server.config;

import m.heim.server.domain.TokenModel;
import m.heim.server.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;


/**
 * Benutzerdefinierte Implementation des {@link OncePerRequestFilter} zur Verwendung von JSON Web Tokens
 */
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    /**
     * Instanz einer Implementation von {@link TokenService}
     */
    private final TokenService tokenService;

    /**
     * Konstruktor von {@link JWTAuthenticationFilter}
     * @param tokenService Instanz einer Implementation von {@link TokenService}
     */
    public JWTAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Überprüft ob der JSON Web Token im richtigen Format übergeben wurde
     * @param authorizationHeader JWT Token
     * @return {@link Boolean}
     */
    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
        return authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ");
    }

    /**
     * Erstellt einen neuen {@link UsernamePasswordAuthenticationToken} aus einem {@link TokenModel}
     * @param tokenModel Instanz eines {@link TokenModel}
     * @return Instanz eines {@link UsernamePasswordAuthenticationToken}
     */
    private UsernamePasswordAuthenticationToken createToken(TokenModel tokenModel) {
        return new UsernamePasswordAuthenticationToken(tokenService.getUserPrincipalFromTokenModel(tokenModel), null, null);
    }

    /**
     * Konfiguration der internen RequestFilters
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeaderIsInvalid(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        TokenModel tokenModel = tokenService.parseTokenFromAuthHeader(authorizationHeader);
        if(tokenModel.getExpirationDate().before(Date.from(Instant.now()))) return;
        UsernamePasswordAuthenticationToken token = createToken(tokenModel);

        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}