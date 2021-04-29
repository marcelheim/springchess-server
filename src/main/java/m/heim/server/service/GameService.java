package m.heim.server.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import m.heim.server.domain.BoardDTO;
import m.heim.server.domain.Game;
import m.heim.server.domain.GameDTO;
import m.heim.server.domain.User;
import m.heim.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@link Service} zur Verwaltung der Spiele
 */
@Service
public class GameService {
    /**
     * Liste der vorhandenen Spiele
     */
    List<Pair<Board, Integer>> boards = new ArrayList<>();
    /**
     * Referenz zur Instanz des {@link GameRepository}
     */
    GameRepository gameRepository;

    /**
     * Konstruktor von {@link GameService} mit Dependency Injection
     * @param gameRepository Instanz von {@link GameRepository}
     */
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Erzeugt ein neues Spiel
     * @param name Name des Spiels
     */
    public void createGame(String name){
        Integer boardId = boards.size() + 1;
        Game newGame = new Game(boardId, name);
        gameRepository.save(newGame);
        boards.add(Pair.of(new Board(), boardId));
    }

    /**
     * Findet ein Board über die BoardId
     * @param boardId Id des Boards
     * @return {@link Optional} eines {@link Board}
     */
    public Optional<Pair<Board, Integer>> getBoardByBoardId(Integer boardId){
        return boards.stream().filter(c -> c.getSecond().equals(boardId)).findFirst();
    }

    /**
     * Findet ein Board über die GameId
     * @param gameId Id des Spiels
     * @return {@link Optional} eines {@link Board}
     */
    public Optional<Pair<Board, Integer>> getBoardByGameId(Integer gameId){
        Optional<Game> game = gameRepository.findById(gameId);
        if(game.isPresent()){
            Integer boardId = game.get().getBoardId();
            return getBoardByBoardId(boardId);
        }
        return Optional.empty();
    }

    /**
     * Einem Spiel beitreten
     * @param gameId Id des Spiels
     * @param color Seite der beizutreten ist
     * @param user Referenz zum Datenbankeintrag des Users
     * @return Ist der Beitritt erfolgreich
     */
    public boolean joinGameByGameId(Integer gameId, Side color, User user){
        if(!gameRepository.findGamesByBlackOrWhite(user, user).isEmpty()) return false;
        Optional<Game> game = gameRepository.findGameById(gameId);
        if(game.isEmpty()) return false;
        if(color.equals(Side.BLACK) && game.get().getBlack() == null) game.get().setBlack(user);
        if(color.equals(Side.WHITE) && game.get().getWhite() == null) game.get().setWhite(user);
        gameRepository.save(game.get());
        return true;
    }

    /**
     * Ein Spiel verlassen
     * @param gameId Id des Spiels
     * @param user Referenz zum Datenbankeintrag des Users
     * @return Ist das Verlassen erfolgreich
     */
    public boolean leaveGameById(Integer gameId, User user) {
        Optional<Game> game = gameRepository.findGameById(gameId);
        if(game.isEmpty()) return false;
        if(game.get().getBlack() == user) game.get().setBlack(null);
        if(game.get().getWhite() == user) game.get().setWhite(null);
        gameRepository.save(game.get());
        return true;
    }

    /**
     * {@link BoardDTO} Modell zur Darstellung des Boards holen
     * @param gameId Id des Spiels
     * @return {@link Optional} eines {@link BoardDTO}
     */
    public Optional<BoardDTO> getBoardDTO(Integer gameId){
        Optional<Pair<Board, Integer>> board = getBoardByGameId(gameId);
        return board.map(BoardDTO::new);
    }

    /**
     * {@link GameDTO} Modell zur Darstellung des Spiels holen
     * @param gameId Id des Spiels
     * @return {@link Optional} eines {@link GameDTO}
     */
    public Optional<GameDTO> getGame(Integer gameId){
        Optional<Game> game = gameRepository.findGameById(gameId);
        return game.map(GameDTO::new);
    }

    /**
     * Id des beigetretenen Spiels holen
     * @param user Referenz zum Datenbankeintrag des Users
     * @return {@link Optional} einer {@link Integer}
     */
    public Optional<Integer> getJoinedGameId(User user){
        Optional<Game> game = gameRepository.findGameByBlackOrWhite(user, user);
        return game.map(Game::getId);
    }

    /**
     * Eine Liste aller Spiele
     * @return {@link List} von {@link GameDTO}
     */
    public List<GameDTO> getGames() {
        List<GameDTO> games = new ArrayList<>();
        Iterable<Game> gameList = gameRepository.findAll();
        gameList.forEach(game -> {
            games.add(new GameDTO(game));
        });
        return games;
    }


    /**
     * Beigetretenes Spiel holen
     * @param user Referenz zum Datenbankeintrag des Users
     * @return {@link Optional} einer {@link GameDTO}
     */
    public Optional<GameDTO> getJoinedGame(User user) {
        Optional<Game> game = gameRepository.findGameByBlackOrWhite(user, user);
        return game.map(GameDTO::new);
    }
}
