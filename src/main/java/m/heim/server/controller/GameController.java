package m.heim.server.controller;

import com.github.bhlangonijr.chesslib.Board;
import m.heim.server.domain.*;
import m.heim.server.enums.Player;
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

import java.util.Optional;

@RestController
@RequestMapping("api/chess")
public class GameController {
    GameService gameService;
    UserRepository userRepository;
    GameRepository gameRepository;

    @Autowired
    public GameController(GameService gameService, UserRepository userRepository, GameRepository gameRepository) {
        this.gameService = gameService;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @GetMapping("createGame")
    public ResponseEntity<String> createGame(@RequestParam String name){
        gameService.createGame(name);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("joinGame")
    public ResponseEntity<String> joinGame(@RequestParam Integer gameId, @RequestParam Player color){
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
        return new ResponseEntity<>("Game not found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("getGame")
    public ResponseEntity<?> getGame(@RequestParam Integer gameId){
        Optional<GameDTO> game = gameService.getGame(gameId);
        if (game.isPresent()) return new ResponseEntity<>(game.get(), HttpStatus.OK);
        return new ResponseEntity<>("Game not found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("getGames")
    public ResponseEntity<?> getGames(){
        return new ResponseEntity<>(gameService.getGames(), HttpStatus.OK);
    }

    @GetMapping("getBoard")
    public ResponseEntity<?> getBoard(@RequestParam Integer gameId){
        Optional<BoardDTO> boardDTO = gameService.getBoardDTO(gameId);
        if (boardDTO.isPresent()) return new ResponseEntity<>(boardDTO.get(), HttpStatus.OK);
        return new ResponseEntity<>("Board not found", HttpStatus.BAD_REQUEST);
    }

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
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

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
        return new ResponseEntity<>("Game not found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("move")
    public ResponseEntity<String> move(@RequestParam Integer gameId, @RequestParam String move){
        Optional<Pair<Board, Integer>> board = gameService.getBoardByGameId(gameId);
        return new ResponseEntity<>("Move not possible", HttpStatus.BAD_REQUEST);
    }
}
