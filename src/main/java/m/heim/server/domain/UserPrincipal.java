package m.heim.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dient zur Speicherung des aktuellen angemeldeten Users im Kontext von Spring Security
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal {
    /**
     * Public Address des Ethereum Wallets des Users
     */
    private String publicAddress;
}
