package SpaceInvaders.State;

import SpaceInvaders.Controller.Controller;
import SpaceInvaders.Controller.Game.ArenaController;
import SpaceInvaders.Controller.Menu.GameOverController;
import SpaceInvaders.Controller.Menu.OnlyTextMenuController;
import SpaceInvaders.Controller.Menu.PauseMenuController;
import SpaceInvaders.Controller.Menu.StartMenuController;
import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Game;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.RegularGameElements.AlienShip;
import SpaceInvaders.Model.Sound.Sound_Options;
import SpaceInvaders.Viewer.Game.GameViewer;
import SpaceInvaders.Viewer.Menu.*;
import SpaceInvaders.Viewer.Viewer;
import com.googlecode.lanterna.input.KeyStroke;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StateTest {
    private State state;
    @Mock
    private GUI gui;
    @Mock
    private Game game;
    @Mock
    private SoundManager soundManager;
    @Mock
    private MockedStatic<SoundManager> soundManagerMock;

    @BeforeEach
    void setUp() {
        Field instance = assertDoesNotThrow(() -> State.class.getDeclaredField("instance"));
        instance.setAccessible(true);
        assertDoesNotThrow(() -> instance.set(null, null));
        state = State.getInstance();
        gui = Mockito.mock(GUI.class);
        game = Mockito.mock(Game.class);

        soundManagerMock = mockStatic(SoundManager.class);
        soundManager = mock(SoundManager.class);
        soundManagerMock.when(SoundManager::getInstance).thenReturn(soundManager);
    }

    @AfterEach
    void tearDown() {
        soundManagerMock.close();
    }

    @Test
    void testGetInstance() {
        assertInstanceOf(State.class, State.getInstance());
    }

    @Test
    void testGetInstanceNull() {
        assertInstanceOf(State.class, State.getInstance());
    }

    @Test
    void testInitialState() {
        state = State.getInstance();
        assertEquals(GameStates.START_MENU, state.getCurrentState());
        assertEquals(GameStates.START_MENU, state.getPreviousState());
        assertInstanceOf(StartMenuController.class, state.getController());
        assertInstanceOf(StartMenuViewer.class, state.getViewer());
    }

    @Test
    void testUpdateState() throws IOException, URISyntaxException {
        state.UpdateState(GameStates.NEW_GAME);
        assertEquals(GameStates.NEW_GAME, state.getCurrentState());
        assertEquals(GameStates.START_MENU, state.getPreviousState());
        assertInstanceOf(ArenaController.class, state.getController());
        assertInstanceOf(GameViewer.class, state.getViewer());
    }

    @Test
    void testUpdateStateStartMenu() throws IOException, URISyntaxException {
        state.UpdateState(GameStates.START_MENU);
        assertEquals(GameStates.START_MENU, state.getCurrentState());
        assertEquals(GameStates.START_MENU, state.getPreviousState());
        assertInstanceOf(StartMenuController.class, state.getController());
        assertInstanceOf(StartMenuViewer.class, state.getViewer());
    }

    @Test
    void testUpdateToPrevious() throws IOException, URISyntaxException {
        state.UpdateState(GameStates.NEW_GAME);
        state.UpdateToPrevious();
        assertEquals(GameStates.START_MENU, state.getCurrentState());
        assertEquals(GameStates.NEW_GAME, state.getPreviousState());
    }

    @Test
    void testStep() throws IOException, URISyntaxException {
        KeyStroke key = Mockito.mock(KeyStroke.class);
        Controller controller = Mockito.mock(Controller.class);
        Viewer viewer = Mockito.mock(Viewer.class);
        state.setController(controller);
        state.setViewer(viewer);

        when(gui.getNextAction()).thenReturn(key);

        state.step(gui, game, 1000);

        verify(controller).step(game, key, 1000);
        verify(viewer).draw(gui, 1000);
    }

    @Test
    void testSetController() {
        Controller controller = Mockito.mock(Controller.class);
        state.setController(controller);
        assertEquals(controller, state.getController());
    }

    @Test
    void testSetViewer() {
        Viewer viewer = Mockito.mock(Viewer.class);
        state.setViewer(viewer);
        assertEquals(viewer, state.getViewer());
    }

    @Test
    void testSetArena() {
        Arena arena = Mockito.mock(Arena.class);
        state.setArena(arena);
        assertEquals(arena, state.getArena());
    }

    @Test
    void testStateActionsStartMenu() throws IOException, URISyntaxException {
        state.UpdateState(GameStates.START_MENU);
        assertInstanceOf(StartMenuController.class, state.getController());
        assertInstanceOf(StartMenuViewer.class, state.getViewer());
    }

    @Test
    void testStateActionsPause() throws IOException, URISyntaxException {
        state.UpdateState(GameStates.PAUSE);
        assertInstanceOf(PauseMenuController.class, state.getController());
        assertInstanceOf(PauseMenuViewer.class, state.getViewer());
        verify(soundManager).stopAllSounds();
    }

    @Test
    void testStateActionsNewGame() throws IOException, URISyntaxException {
        state.UpdateState(GameStates.NEW_GAME);
        assertInstanceOf(ArenaController.class, state.getController());
        assertInstanceOf(GameViewer.class, state.getViewer());
        verify(soundManager).playSound(Sound_Options.MUSIC);
    }

    @Test
    void testStateActionsLeaderboard() throws IOException, URISyntaxException {
        state.UpdateState(GameStates.LEADERBOARD);
        assertInstanceOf(OnlyTextMenuController.class, state.getController());
        assertInstanceOf(LeaderboardViewer.class, state.getViewer());
    }

    @Test
    void testStateActionsGameOver() throws IOException, URISyntaxException {
        var arenaMock = Mockito.mock(Arena.class);
        state.setArena(arenaMock);
        when(arenaMock.getScore()).thenReturn(100);

        state.UpdateState(GameStates.GAME_OVER);

        assertInstanceOf(GameOverController.class, state.getController());
        assertInstanceOf(GameOverMenuViewer.class, state.getViewer());
        verify(SoundManager.getInstance()).stopAllSounds();
    }

    @Test
    void testStateActionsNewGameRound() throws IOException, URISyntaxException {
        var arena = Mockito.mock(Arena.class);
        state.setArena(arena);
        when(arena.getRound()).thenReturn(1);
        state.UpdateState(GameStates.NEW_GAME_ROUND);
        assertInstanceOf(ArenaController.class, state.getController());
        assertInstanceOf(GameViewer.class, state.getViewer());
    }

    @Test
    void testStateActionsResumeGame() throws IOException, URISyntaxException {
        var arena = Mockito.mock(Arena.class);
        var alienShip = Mockito.mock(AlienShip.class);
        when(arena.getAlienShip()).thenReturn(alienShip);
        state.setArena(arena);

        state.UpdateState(GameStates.RESUME_GAME);

        assertInstanceOf(GameViewer.class, state.getViewer());
        verify(SoundManager.getInstance()).resumePlayingMusic();
        verify(SoundManager.getInstance()).resumePlayingAlienShipSound();
    }

    @Test
    void testStateActionsResumeGameNullAlienShip() throws IOException, URISyntaxException {

        var arena = Mockito.mock(Arena.class);
        when(arena.getAlienShip()).thenReturn(null);
        state.setArena(arena);

        state.UpdateState(GameStates.RESUME_GAME);

        assertInstanceOf(GameViewer.class, state.getViewer());
        verify(SoundManager.getInstance()).resumePlayingMusic();
        verify(SoundManager.getInstance(), times(0)).resumePlayingAlienShipSound();
    }

    @Test
    void testStateActionsInstructions() throws IOException, URISyntaxException {
        state.UpdateState(GameStates.INSTRUCTIONS);
        assertInstanceOf(OnlyTextMenuController.class, state.getController());
        assertInstanceOf(InstructionsViewer.class, state.getViewer());
    }

}