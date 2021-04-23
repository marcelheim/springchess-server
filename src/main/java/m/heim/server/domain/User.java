package m.heim.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table
public class User {
    public User(String publicAddress) {
        this.publicAddress = publicAddress.toLowerCase();
    }
    @Id
    @Column(nullable = false, unique = true)
    private String publicAddress;

    @Column(unique = true)
    @Setter private String userName;

    @Column
    @Setter private String nonce;
}
