package m.heim.server.repository;

import m.heim.server.domain.Game;
import m.heim.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation eines {@link CrudRepository} zur Speicherung von {@link Game}
 */
public interface GameRepository extends JpaRepository<Game, Integer> {
    /**
     * Funktionsprototyp zum finden eines Spiels über die Id
     * @param id Id des Spiels
     * @return {@link Optional} eines {@link Game}
     */
    Optional<Game> findGameById(Integer id);

    /**
     * Funktionsprototyp zum finden eines Spiels über den Schwarzen oder Weißen User
     * @param black Schwarzer {@link User}
     * @param white Weißer {@link User}
     * @return {@link Optional} eines {@link Game}
     */
    Optional<Game> findGameByBlackOrWhite(User black, User white);
    /**
     * Funktionsprototyp zum finden eines Spiels über den Schwarzen oder Weißen User
     * @param black Schwarzer Spieler - {@link User}
     * @param white Weißer Spieler - {@link User}
     * @return {@link List} von {@link Game}
     */
    List<Game> findGamesByBlackOrWhite(User black, User white);
}
