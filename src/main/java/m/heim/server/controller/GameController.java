package m.heim.server.controller;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import m.heim.server.domain.BoardDTO;
import m.heim.server.domain.GameDTO;
import m.heim.server.domain.User;
import m.heim.server.domain.UserPrincipal;
import m.heim.server.repository.GameRepository;
import m.heim.server.repository.UserRepository;
import m.heim.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * {@link RestController} mit API für das Schach Spiel
 */
@RestController
@RequestMapping("api/chess")
public class GameController {
    /**
     * Instanz von {@link GameService}
     */
    GameService gameService;
    /**
     * Instanz von {@link UserRepository}
     */
    UserRepository userRepository;
    /**
     * Instanz von {@link GameRepository}
     */
    GameRepository gameRepository;

    /**
     * Konstruktor von {@link GameController} mit Dependency Injection
     * @param gameService Instanz von {@link GameService}
     * @param userRepository Instanz von {@link UserRepository}
     * @param gameRepository Instanz von {@link GameRepository}
     */
    @Autowired
    public GameController(GameService gameService, UserRepository userRepository, GameRepository gameRepository) {
        this.gameService = gameService;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * API Endpunkt zur Erstellung eines Spiels
     * @param name Name des Spiels
     * @return {@link ResponseEntity}
     */
    @GetMapping("createGame")
    public ResponseEntity<String> createGame(@RequestParam String name){
        gameService.createGame(name);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     * API Endpunkt zum Beitreten eines Spiels
     * @param gameId Id des Spiels
     * @param color Seite des Spielers
     * @return {@link ResponseEntity}
     */
    @GetMapping("joinGame")
    public ResponseEntity<String> joinGame(@RequestParam Integer gameId, @RequestParam Side color){
        UserPrincipal userPrincipal =
                (UserPrincipal) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        String publicAddress = userPrincipal.getPublicAddress();
        Optional<User> optionalUser = userRepository.findByPublicAddress(publicAddress);
        if(optionalUser.isPresent() && gameService.joinGameByGameId(gameId, color, optionalUser.get())) {
            return new ResponseEntity<>("Ok", HttpStatus.OK);
        }

        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    /**
     * API Endpunkt zum Verlassen eines Spiels
     * @param gameId Id des Spiels
     * @return {@link ResponseEntity}
     */
    @GetMapping("leaveGame")
    public ResponseEntity<String> leaveGame(@RequestParam Integer gameId){
        UserPrincipal userPrincipal =
                (UserPrincipal) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        String publicAddress = userPrincipal.getPublicAddress();
        Optional<User> optionalUser = userRepository.findByPublicAddress(publicAddress);
        if(optionalUser.isPresent() && gameService.leaveGameById(gameId, optionalUser.get())) {
            return new ResponseEntity<>("Ok", HttpStatus.OK);
        }
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    /**
     * API Endpunkt zum holen der Daten eines Spiels
     * @param gameId Id des Spiels
     * @return {@link ResponseEntity} von {@link GameDTO}
     */
    @GetMapping("getGame")
    public ResponseEntity<?> getGame(@RequestParam Integer gameId){
        Optional<GameDTO> game = gameService.getGame(gameId);
        if (game.isPresent()) return new ResponseEntity<>(game.get(), HttpStatus.OK);
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    /**
     * API Endpunkt zum holen einer Liste aller Daten der Spiele
     * @return {@link ResponseEntity} von {@link List} von {@link GameDTO}
     */
    @GetMapping("getGames")
    public ResponseEntity<?> getGames(){
        return new ResponseEntity<>(gameService.getGames(), HttpStatus.OK);
    }

    /**
     * API Endpunkt zum holen der Daten eines Boards
     * @param gameId Id des Spiels
     * @return {@link ResponseEntity} von {@link BoardDTO}
     */
    @GetMapping("getBoard")
    public ResponseEntity<?> getBoard(@RequestParam Integer gameId){
        Optional<BoardDTO> boardDTO = gameService.getBoardDTO(gameId);
        if (boardDTO.isPresent()) return new ResponseEntity<>(boardDTO.get(), HttpStatus.OK);
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    /**
     * API Endpunkt zum holen der Daten des beigetretenen Spiels
     * @return {@link ResponseEntity} von {@link GameDTO}
     */
    @GetMapping("getJoinedGame")
    public ResponseEntity<?> getJoinedGame(){
        UserPrincipal userPrincipal =
                (UserPrincipal) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        String publicAddress = userPrincipal.getPublicAddress();
        Optional<User> optionalUser = userRepository.findByPublicAddress(publicAddress);
        if(optionalUser.isPresent()) {
            Optional<GameDTO> optionalGame = gameService.getJoinedGame(optionalUser.get());
            return optionalGame.<ResponseEntity<?>>map(gameDTO -> new ResponseEntity<>(gameDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new GameDTO(), HttpStatus.OK));
        }
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    /**
     * API Endpunkt zum holen der Daten des beigetretenen Spiels
     * @return {@link ResponseEntity} von der GameId
     */
    @GetMapping("getJoinedGameId")
    public ResponseEntity<?> getJoinedGameId(){
        UserPrincipal userPrincipal =
                (UserPrincipal) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        String publicAddress = userPrincipal.getPublicAddress();
        Optional<User> optionalUser = userRepository.findByPublicAddress(publicAddress);
        if(optionalUser.isPresent()) {
            Optional<Integer> optionalGameId = gameService.getJoinedGameId(optionalUser.get());
            if(optionalGameId.isPresent()) return new ResponseEntity<>(optionalGameId.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    /**
     * API Endpunkt zum Ausführen eines Zuges
     * @param gameId Id des Spiels
     * @param move Zug in der SAN Notation
     * @return {@link ResponseEntity} von {@link GameDTO}
     */
    @GetMapping("move")
    public ResponseEntity<String> move(@RequestParam Integer gameId, @RequestParam String move){
        try {
            UserPrincipal userPrincipal =
                    (UserPrincipal) SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal();
            String publicAddress = userPrincipal.getPublicAddress();
            Optional<User> optionalUser = userRepository.findByPublicAddress(publicAddress);
            Optional<GameDTO> optionalGame = gameService.getGame(gameId);
            Optional<Pair<Board, Integer>> optionalBoard = gameService.getBoardByGameId(gameId);
            if(optionalUser.isPresent() && optionalBoard.isPresent() && optionalGame.isPresent()) {
                Side side = null;
                if (optionalUser.get().getPublicAddress().equals(optionalGame.get().getBlack())) side = Side.BLACK;
                else if (optionalUser.get().getPublicAddress().equals(optionalGame.get().getWhite())) side = Side.WHITE;
                if(side != null && optionalBoard.get().getFirst().getSideToMove() == side) {
                    List<Move> moveList = optionalBoard.get().getFirst().legalMoves();
                    Optional<Move> optionalMove = moveList.stream().filter(c -> move.contains(c.toString())).findFirst();
                    if(optionalMove.isPresent()){
                        optionalBoard.get().getFirst().doMove(optionalMove.get(), true);
                        return new ResponseEntity<>("OK", HttpStatus.OK);
                    }
                }
            }
        } catch (Exception ignored) { }
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }
}
