package m.heim.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User {@link Entity} Modell in der Java Persistence API
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
public class User {
    /**
     * Konstruktor des User
     * @param publicAddress Public Address des Ethereum Wallets des Users
     */
    public User(String publicAddress) {
        this.publicAddress = publicAddress.toLowerCase();
    }

    /**
     * Public Address des Ethereum Wallets des Users
     * {@link Id} - Primary Key
     * {@link Column} - Spalte, nicht null und einzigartig
     */
    @Id
    @Column(nullable = false, unique = true)
    private String publicAddress;

    /**
     * Benutzername des Users
     * {@link Column} - Spalte, einzigartig
     */
    @Column(unique = true)
    @Setter private String userName;

    /**
     * Nachricht zur Überprüfung der Signatur des Users
     * {@link Column} - Spalte
     */
    @Column
    @Setter private String nonce;
}
