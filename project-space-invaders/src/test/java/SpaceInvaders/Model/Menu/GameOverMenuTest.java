package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameOverMenuTest {

    private GameOverMenu gameOverMenu;

    @BeforeEach
    void setUp() {
        gameOverMenu = new GameOverMenu(100);
    }

    @Test
    void testGetScore() {
        assertEquals(100, gameOverMenu.getScore());
    }

    @Test
    void testGetUsername() {
        assertEquals("", gameOverMenu.getUsername());
    }

    @Test
    void testAddLetter() {
        gameOverMenu.addLetter('A');
        assertEquals("A", gameOverMenu.getUsername());
        gameOverMenu.addLetter('B');
        assertEquals("AB", gameOverMenu.getUsername());
    }

    @Test
    void testAddLetterLimit() {
        gameOverMenu.addLetter('A');
        gameOverMenu.addLetter('B');
        gameOverMenu.addLetter('C');
        gameOverMenu.addLetter('D');
        gameOverMenu.addLetter('E');
        gameOverMenu.addLetter('F');
        gameOverMenu.addLetter('G');
        assertEquals("ABCDEF", gameOverMenu.getUsername());
    }

    @Test
    void testRemoveLetter() {
        gameOverMenu.addLetter('A');
        gameOverMenu.addLetter('B');
        gameOverMenu.removeLetter();
        assertEquals("A", gameOverMenu.getUsername());
        gameOverMenu.removeLetter();
        assertEquals("", gameOverMenu.getUsername());
    }

    @Test
    void testRemoveLetterEmpty() {
        gameOverMenu.removeLetter();
        assertEquals("", gameOverMenu.getUsername());
    }

    @Test
    void testIsSelectedRestart() {
        assertTrue(gameOverMenu.isSelectedRestart());
    }

    @Test
    void testIsSelectedRestartOverflow() {
        gameOverMenu.nextOption();
        gameOverMenu.nextOption();
        gameOverMenu.nextOption();
        assertTrue(gameOverMenu.isSelectedRestart());
    }

    @Test
    void testIsSelectedLeaderboard() {
        gameOverMenu.nextOption();
        assertTrue(gameOverMenu.isSelectedLeaderboard());
    }

    @Test
    void testIsSelectedExit() {
        gameOverMenu.nextOption();
        gameOverMenu.nextOption();
        assertTrue(gameOverMenu.isSelectedExit());
    }

    @Test
    void testSetUsername() {
        StringBuilder username = new StringBuilder("ABC");
        gameOverMenu.setUsername(username);
        assertEquals("ABC", gameOverMenu.getUsername());
    }
}