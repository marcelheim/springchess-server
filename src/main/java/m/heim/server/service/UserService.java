package m.heim.server.service;

import lombok.Getter;
import m.heim.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * {@link Service} zur Verwaltung der User
 */
@Service
@Transactional
public class UserService {
    /**
     * Referenz zur Instanz des {@link UserRepository}
     */
    @Getter
    private final UserRepository userRepository;

    /**
     * Konstruktor von {@link UserService} mit Dependency Injection
     * @param userRepository Instanz von {@link UserRepository}
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
