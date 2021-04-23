package m.heim.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GameDTO {
    private Integer id;
    private Integer boardId;
    private String name;
    private String white;
    private String black;
    private String gameStatus;

    public GameDTO(Game game) {
        id = game.getId();
        boardId = game.getBoardId();
        name = game.getName();
        white = game.getWhite() == null ? null : game.getWhite().getPublicAddress();
        black = game.getBlack() == null ? null : game.getBlack().getPublicAddress();
        gameStatus = game.getGameStatus();
    }
}
