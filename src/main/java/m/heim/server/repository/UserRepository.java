package m.heim.server.repository;

import m.heim.server.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation eines {@link CrudRepository} zur Speicherung von {@link User}
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    /**
     * Funktionsprototyp zum finden eines Users über seine Public Address
     * @param publicAddress Public Address des Ethereum Wallets eines Users
     * @return {@link Optional} eines {@link User}
     */
    Optional<User> findByPublicAddress(String publicAddress);
}

