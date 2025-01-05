package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartMenuTest {

    private StartMenu startMenu;

    @BeforeEach
    public void setUp() {
        startMenu = new StartMenu();
    }

    @Test
    public void testIsSelectedPlay() {
        assertTrue(startMenu.isSelectedPlay());
    }

    @Test
    public void testIsSelectedLeaderboard() {
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedLeaderboard());
    }

    @Test
    public void testIsSelectedInstructions() {
        startMenu.nextOption();
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedInstructions());
    }

    @Test
    public void testIsSelectedExit() {
        startMenu.nextOption();
        startMenu.nextOption();
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedExit());
    }

    @Test
    public void testNextOption() {
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedLeaderboard());
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedInstructions());
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedExit());
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedPlay());
    }

    @Test
    public void testPreviousOption() {
        startMenu.previousOption();
        assertTrue(startMenu.isSelectedExit());
        startMenu.previousOption();
        assertTrue(startMenu.isSelectedInstructions());
        startMenu.previousOption();
        assertTrue(startMenu.isSelectedLeaderboard());
        startMenu.previousOption();
        assertTrue(startMenu.isSelectedPlay());
    }
}