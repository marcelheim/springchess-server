package m.heim.server.repository;

import m.heim.server.domain.Game;
import m.heim.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findGameById(Integer id);
    Optional<Game> findGameByBlackOrWhite(User black, User white);
    List<Game> findGamesByBlackOrWhite(User black, User white);
}
