package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartMenuTest {

    private StartMenu startMenu;

    @BeforeEach
    void setUp() {
        startMenu = new StartMenu();
    }

    @Test
    void testIsSelectedPlay() {
        assertTrue(startMenu.isSelectedPlay());
    }

    @Test
    void testIsSelectedLeaderboard() {
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedLeaderboard());
    }

    @Test
    void testIsSelectedInstructions() {
        startMenu.nextOption();
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedInstructions());
    }

    @Test
    void testIsSelectedExit() {
        startMenu.nextOption();
        startMenu.nextOption();
        startMenu.nextOption();
        assertTrue(startMenu.isSelectedExit());
    }

    @Test
    void testNextOption() {
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
    void testPreviousOption() {
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