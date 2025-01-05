package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PauseMenuTest {

    private PauseMenu pauseMenu;

    @BeforeEach
    public void setUp() {
        pauseMenu = new PauseMenu();
    }

    @Test
    public void testIsSelectedContinue() {
        assertTrue(pauseMenu.isSelectedContinue());
    }

    @Test
    public void testIsSelectedInstructions() {
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedInstructions());
    }

    @Test
    public void testIsSelectedRestart() {
        pauseMenu.nextOption();
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedRestart());
    }

    @Test
    public void testIsSelectedExit() {
        pauseMenu.nextOption();
        pauseMenu.nextOption();
        pauseMenu.nextOption();
        assertTrue(pauseMenu.isSelectedExit());
    }

    @Test
    public void testNextOption() {
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
    public void testPreviousOption() {
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