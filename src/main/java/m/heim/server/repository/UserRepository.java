package m.heim.server.repository;

import m.heim.server.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByPublicAddress(String publicAddress);
    Optional<User> findByUserName(String userName);
}
