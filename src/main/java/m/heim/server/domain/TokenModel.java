package m.heim.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Modell zur Erstellung eines JSON Web Tokens
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenModel {
    /**
     * Public Address des Ethereum Wallets des Users
     */
    private String publicAddress;
    /**
     * Ablaufdatum des JSON Web Tokens
     */
    private Date expirationDate;
}
