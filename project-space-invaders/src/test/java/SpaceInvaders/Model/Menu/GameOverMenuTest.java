package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameOverMenuTest {

    private GameOverMenu gameOverMenu;

    @BeforeEach
    public void setUp() {
        gameOverMenu = new GameOverMenu(100);
    }

    @Test
    public void testGetScore() {
        assertEquals(100, gameOverMenu.getScore());
    }

    @Test
    public void testGetUsername() {
        assertEquals("", gameOverMenu.getUsername());
    }

    @Test
    public void testAddLetter() {
        gameOverMenu.addLetter('A');
        assertEquals("A", gameOverMenu.getUsername());
        gameOverMenu.addLetter('B');
        assertEquals("AB", gameOverMenu.getUsername());
    }

    @Test
    public void testAddLetterLimit() {
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
    public void testRemoveLetter() {
        gameOverMenu.addLetter('A');
        gameOverMenu.addLetter('B');
        gameOverMenu.removeLetter();
        assertEquals("A", gameOverMenu.getUsername());
        gameOverMenu.removeLetter();
        assertEquals("", gameOverMenu.getUsername());
    }

    @Test
    public void testRemoveLetterEmpty() {
        gameOverMenu.removeLetter();
        assertEquals("", gameOverMenu.getUsername());
    }

    @Test
    public void testIsSelectedRestart() {
        assertTrue(gameOverMenu.isSelectedRestart());
    }

    @Test
    public void testIsSelectedRestartOverflow() {
        gameOverMenu.nextOption();
        gameOverMenu.nextOption();
        gameOverMenu.nextOption();
        assertTrue(gameOverMenu.isSelectedRestart());
    }

    @Test
    public void testIsSelectedLeaderboard() {
        gameOverMenu.nextOption();
        assertTrue(gameOverMenu.isSelectedLeaderboard());
    }

    @Test
    public void testIsSelectedExit() {
        gameOverMenu.nextOption();
        gameOverMenu.nextOption();
        assertTrue(gameOverMenu.isSelectedExit());
    }

    @Test
    public void testSetUsername() {
        StringBuilder username = new StringBuilder("ABC");
        gameOverMenu.setUsername(username);
        assertEquals("ABC", gameOverMenu.getUsername());
    }
}