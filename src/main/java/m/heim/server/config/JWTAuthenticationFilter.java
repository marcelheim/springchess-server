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


// ToDo: Blacklist
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    public JWTAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
        return authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ");
    }

    private UsernamePasswordAuthenticationToken createToken(TokenModel tokenModel) {
        return new UsernamePasswordAuthenticationToken(tokenService.getUserPrincipalFromTokenModel(tokenModel), null, null);
    }

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