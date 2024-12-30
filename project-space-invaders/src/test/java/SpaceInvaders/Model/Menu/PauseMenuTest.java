package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PauseMenuTest {

    private PauseMenu pauseMenu;

    @BeforeEach
    void setUp() {
        pauseMenu = new PauseMenu();
    }

    @Test
    void testIsSelectedContinue() {
        assertTrue(pauseMenu.isSelectedContinue());
    }

    @Test
    void testIsSelectedInstructions() {
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedInstructions());
    }

    @Test
    void testIsSelectedRestart() {
        pauseMenu.nextOption();
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedRestart());
    }

    @Test
    void testIsSelectedExit() {
        pauseMenu.nextOption();
        pauseMenu.nextOption();
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedExit());
    }

    @Test
    void testNextOption() {
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedInstructions());
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedRestart());
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedExit());
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedContinue());
    }

    @Test
    void testPreviousOption() {
        pauseMenu.previousOption();
        assertTrue(pauseMenu.isSelectedExit());
        pauseMenu.previousOption();
        assertTrue(pauseMenu.isSelectedRestart());
        pauseMenu.previousOption();
        assertTrue(pauseMenu.isSelectedInstructions());
        pauseMenu.previousOption();
        assertTrue(pauseMenu.isSelectedContinue());
    }
}