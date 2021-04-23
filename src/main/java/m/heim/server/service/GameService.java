package m.heim.server.service;

import com.github.bhlangonijr.chesslib.Board;
import m.heim.server.domain.BoardDTO;
import m.heim.server.domain.Game;
import m.heim.server.domain.GameDTO;
import m.heim.server.domain.User;
import m.heim.server.enums.Player;
import m.heim.server.repository.GameRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    List<Pair<Board, Integer>> boards = new ArrayList<>();
    GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Integer createGame(String name){
        Integer boardId = boards.size() + 1;
        Game newGame = new Game(boardId, name);
        gameRepository.save(newGame);
        boards.add(Pair.of(new Board(), boardId));
        return newGame.getId();
    }

    public Optional<Pair<Board, Integer>> getBoardByBoardId(Integer boardId){
        return boards.stream().filter(c -> c.getSecond().equals(boardId)).findFirst();
    }

    public Optional<Pair<Board, Integer>> getBoardByGameId(Integer gameId){
        Optional<Game> game = gameRepository.findById(gameId);
        if(game.isPresent()){
            Integer boardId = game.get().getBoardId();
            return getBoardByBoardId(boardId);
        }
        return Optional.empty();
    }

    public boolean joinGameByGameId(Integer gameId, Player player, User user){
        if(!gameRepository.findGamesByBlackOrWhite(user, user).isEmpty()) return false;
        Optional<Game> game = gameRepository.findGameById(gameId);
        if(game.isEmpty()) return false;
        if(player.equals(Player.BLACK) && game.get().getBlack() == null) game.get().setBlack(user);
        if(player.equals(Player.WHITE) && game.get().getWhite() == null) game.get().setWhite(user);
        gameRepository.save(game.get());
        return true;
    }

    public boolean leaveGameById(Integer gameId, User user) {
        Optional<Game> game = gameRepository.findGameById(gameId);
        if(game.isEmpty()) return false;
        if(game.get().getBlack() == user) game.get().setBlack(null);
        if(game.get().getWhite() == user) game.get().setWhite(null);
        gameRepository.save(game.get());
        return true;
    }

    public Optional<BoardDTO> getBoardDTO(Integer gameId){
        Optional<Pair<Board, Integer>> board = getBoardByGameId(gameId);
        return board.map(BoardDTO::new);
    }

    public Optional<GameDTO> getGame(Integer gameId){
        Optional<Game> game = gameRepository.findGameById(gameId);
        return game.map(GameDTO::new);
    }

    public Optional<Integer> getJoinedGameId(User user){
        Optional<Game> game = gameRepository.findGameByBlackOrWhite(user, user);
        return game.map(Game::getId);
    }

    public List<GameDTO> getGames() {
        List<GameDTO> games = new ArrayList<>();
        Iterable<Game> gameList = gameRepository.findAll();
        gameList.forEach(game -> {
            games.add(new GameDTO(game));
        });
        return games;
    }

    public Optional<GameDTO> getJoinedGame(User user) {
        Optional<Game> game = gameRepository.findGameByBlackOrWhite(user, user);
        return game.map(GameDTO::new);
    }
}
