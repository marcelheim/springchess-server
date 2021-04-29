package m.heim.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Modell zur Darstellung eines {@link Game}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GameDTO {
    /**
     * Id des Spiels
     */
    private Integer id;
    /**
     * Id des zugehörigen Boards
     */
    private Integer boardId;
    /**
     * Name des Spiels
     */
    private String name;
    /**
     * Public Address des weißen Spielers
     */
    private String white;
    /**
     * Public Address des schwarzen Spielers
     */
    private String black;
    /**
     * Status des Spiels
     */
    private String gameStatus;


    /**
     * Konstruktor eines GameDTO
     * @param game Instanz eines {@link Game}
     */
    public GameDTO(Game game) {
        id = game.getId();
        boardId = game.getBoardId();
        name = game.getName();
        white = game.getWhite() == null ? null : game.getWhite().getPublicAddress();
        black = game.getBlack() == null ? null : game.getBlack().getPublicAddress();
        gameStatus = game.getGameStatus();
    }
}
