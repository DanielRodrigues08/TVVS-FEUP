package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class BoardFactoryTest {
    private static BoardFactory boardFactory;

    @BeforeAll
    public static void setUp() {
        PacManSprites pacManSprites = new PacManSprites();
        boardFactory = new BoardFactory(pacManSprites);
    }

    private static Square[][] createFakeGrid(int num_rows, int num_columns) {
        Square[][] grid = new Square[num_rows][num_columns];
        for (int i = 0; i < num_rows; i++) {
            for (int j = 0; j < num_columns; j++) {
                grid[i][j] = new MySquare();
            }
        }
        return grid;
    }

    public static int[][] outOfBoundsIndexes() {
        return new int[][]{
                {-10, -11},
                {-10, 4},
                {-10, Integer.MAX_VALUE + 11},
                {3, -11},
                {3, Integer.MAX_VALUE + 11},
                {Integer.MAX_VALUE + 10, -11},
                {Integer.MAX_VALUE + 10, 4},
                {Integer.MAX_VALUE + 10, Integer.MAX_VALUE + 11},
                {-1, -1},
                {-1, 0},
                {-1, Integer.MAX_VALUE},
                {-1, Integer.MAX_VALUE + 1},
                {0, -1},
                {0, Integer.MAX_VALUE},
                {0, Integer.MAX_VALUE + 1},
                {Integer.MAX_VALUE, -1},
                {Integer.MAX_VALUE, 0},
                {Integer.MAX_VALUE, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE + 1},
                {Integer.MAX_VALUE + 1, -1},
                {Integer.MAX_VALUE + 1, 0},
                {Integer.MAX_VALUE + 1, Integer.MAX_VALUE},
                {Integer.MAX_VALUE + 1, Integer.MAX_VALUE + 1},

        };
    }

    @Test
    public void testNullGrid() {
        assertThrows(AssertionError.class, () -> boardFactory.createBoard(null));
    }

    @Test
    public void testZeroRowsZeroColumns() {
        Square[][] grid = new Square[0][0];
        boardFactory.createBoard(grid);
    }


    @ParameterizedTest
    @MethodSource(value = "outOfBoundsIndexes")
    public void testOutOfBounds(int[] data) {
        int num_rows = data[0];
        int num_columns = data[1];
        try {
            Square[][] grid = createFakeGrid(num_rows, num_columns);
            boardFactory.createBoard(grid);
            fail("It should have thrown any error or exception");
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private static class MySquare extends Square {


        @Override
        public boolean isAccessibleTo(Unit unit) {
            return false;
        }

        @Override
        public Sprite getSprite() {
            return null;
        }
    }

}
