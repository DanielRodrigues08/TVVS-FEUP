package SpaceInvaders;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.GUI.GUILanterna;
import SpaceInvaders.State.GameStates;
import SpaceInvaders.State.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameTest {
    @Mock
    private State mockState;
    @Mock
    private GUI mockGui;
    private Game game;
    private Method startGameMethod;
    private MockedStatic<State> mockedState;
    private MockedConstruction<GUILanterna> mockedGui;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<Game> constructor = Game.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        startGameMethod = Game.class.getDeclaredMethod("startGame");
        startGameMethod.setAccessible(true);

        Field guiField = Game.class.getDeclaredField("gui");
        guiField.setAccessible(true);

        mockedGui = mockConstruction(GUILanterna.class);
        mockedState = mockStatic(State.class);
        mockedState.when(State::getInstance).thenReturn(mockState);
        game = constructor.newInstance();
        guiField.set(game, mockGui);
    }

    @AfterEach
    public void cleanup() {
        mockedGui.close();
        mockedState.close();

    }

    @Test
    void testStartGameStateTransition() throws Exception {
        when(mockState.getCurrentState())
                .thenReturn(GameStates.START_MENU)
                .thenReturn(GameStates.QUIT_GAME);

        startGameMethod.invoke(game);

        verify(mockState, atLeast(2)).getCurrentState();
        verify(mockState).step(eq(mockGui), eq(game), anyLong());
        verify(mockGui).close();
    }

    @Test
    void testStartGameInterruptedException() throws Exception {
        // Setup state transitions
        when(mockState.getCurrentState())
                .thenReturn(GameStates.START_MENU)
                .thenReturn(GameStates.QUIT_GAME);

        doAnswer(invocation -> {
            throw new InterruptedException();
        }).when(mockState).step(any(), any(), anyLong());


        assertThrows(InvocationTargetException.class, () -> {
            startGameMethod.invoke(game);
        });
    }

    @Test
    void testStartGameGuiClose() throws Exception {
        when(mockState.getCurrentState())
                .thenReturn(GameStates.QUIT_GAME);

        startGameMethod.invoke(game);

        verify(mockGui).close();
    }

    @Test
    void testGetState() {
        assertEquals(mockState, game.getState());
    }

    @Test
    void testSetState() throws Exception {
        GameStates newState = GameStates.NEW_GAME;
        game.setState(newState);
        verify(mockState).UpdateState(newState);
    }

    @Test
    void testSetPrevState() throws Exception {
        game.setPrevState();
        verify(mockState).UpdateToPrevious();
    }

    @Test
    void testSleepTimePositive() throws Exception {
        when(mockState.getCurrentState())
                .thenReturn(GameStates.START_MENU)
                .thenReturn(GameStates.QUIT_GAME);

        doAnswer(invocation -> {
            Thread.sleep(10);
            return null;
        }).when(mockState).step(any(), any(), anyLong());

        startGameMethod.invoke(game);

        verify(mockGui).close();
    }

    @Test
    void testSleepTimeNegative() throws Exception {
        when(mockState.getCurrentState())
                .thenReturn(GameStates.START_MENU)
                .thenReturn(GameStates.QUIT_GAME);

        doAnswer(invocation -> {
            Thread.sleep(40);
            return null;
        }).when(mockState).step(any(), any(), anyLong());

        startGameMethod.invoke(game);

        verify(mockGui).close();
    }

    @Test
    void testMain() throws Exception {
        Field stateField = Game.class.getDeclaredField("state");
        stateField.setAccessible(true);

        Field guiField = Game.class.getDeclaredField("gui");
        guiField.setAccessible(true);

        mockedState.when(State::getInstance).thenReturn(mockState);
        when(mockState.getCurrentState()).thenReturn(GameStates.QUIT_GAME);

        Game.main(new String[]{});

        verify(mockState).getCurrentState();
    }
}