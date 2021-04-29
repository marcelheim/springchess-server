package m.heim.server.domain;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

/**
 * Modell zur Darstellung eines Boards
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardDTO {
    /**
     * Position des Boards als FEN String
     */
    private String position;
    /**
     * Anzahl der vergangenen Halbzüge
     */
    private Integer halfMoveCounter;
    /**
     * Id des Boards
     */
    private Integer boardId;
    /**
     * Aktiver Spieler
     */
    private Side activePlayer;
    /**
     * Anzahl der vergangenen Züge
     */
    private Integer moveCounter;
    /**
     * Ist ein Schach Matt eingetreten
     */
    private boolean checkMate;

    /**
     * Konstruktor eines BoardDTO
     * @param board Ein {@link Board}, {@link Integer} {@link Pair}
     */
    public BoardDTO(Pair<Board, Integer> board) {
        position = board.getFirst().getFen();
        halfMoveCounter = board.getFirst().getHalfMoveCounter();
        boardId = board.getSecond();
        activePlayer = board.getFirst().getSideToMove();
        moveCounter = board.getFirst().getMoveCounter();
        checkMate = board.getFirst().isMated();
    }
}
