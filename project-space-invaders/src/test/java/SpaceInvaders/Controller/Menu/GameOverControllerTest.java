package SpaceInvaders.Controller.Menu;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Game;
import SpaceInvaders.Model.Menu.GameOverMenu;
import SpaceInvaders.Model.Sound.Sound_Options;
import SpaceInvaders.State.GameStates;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.util.stream.Stream;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GameOverControllerTest {
    /*private static SoundManager mockSoundManager;
    private static MockedStatic<SoundManager> mockedStatic;
    @Mock private Game game;

    @BeforeAll
    static void setup() {
        mockSoundManager = mock(SoundManager.class);
        mockedStatic = mockStatic(SoundManager.class);
        mockedStatic.when(SoundManager::getInstance).thenReturn(mockSoundManager);
    }

    @AfterAll
    static void cleanup() {
        mockedStatic.close();
    }

    @AfterEach
    void clearInvocations() {
        clearInvocations(mockSoundManager);
        clearInvocations(game);
    }

    private static Stream<Arguments> testStepParams() {
        KeyStroke keyUp = mock(KeyStroke.class);
        when(keyUp.getKeyType()).thenReturn(KeyType.ArrowUp);

        KeyStroke keyDown = mock(KeyStroke.class);
        when(keyDown.getKeyType()).thenReturn(KeyType.ArrowDown);

        KeyStroke keyEnter = mock(KeyStroke.class);
        when(keyEnter.getKeyType()).thenReturn(KeyType.Enter);

        GameOverMenu menu1 = new GameOverMenu(100);
        menu1.setSelectedRestartButton();
        GameOverMenu menu2 = new GameOverMenu(100);
        menu2.setSelectedExitButton();
        GameOverMenu menu3 = new GameOverMenu(100);
        menu3.setSelectedLeaderboardButton();

        return Stream.of(
            Arguments.of(keyUp, menu1, GameStates.START_MENU, false, 0),
            Arguments.of(keyDown, menu1, GameStates.START_MENU, false, 0),
            Arguments.of(keyEnter, menu1, GameStates.NEW_GAME, true, 1),
            Arguments.of(keyEnter, menu2, GameStates.START_MENU, true, 1),
            Arguments.of(keyEnter, menu3, GameStates.LEADERBOARD, false, 0),
            Arguments.of(null, menu1, GameStates.START_MENU, false, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("testStepParams")
    void testStep(KeyStroke key, GameOverMenu menu, GameStates expectedState,
                 boolean shouldUpdateLeaderboard, int soundEffects) throws Exception {
        GameOverController controller = spy(new GameOverController(menu));
        doNothing().when(controller).updateLeaderboard(anyInt(), anyString());

        controller.step(game, key, 0);

        if (shouldUpdateLeaderboard) {
            verify(controller, times(1)).updateLeaderboard(anyInt(), anyString());
        } else {
            verify(controller, never()).updateLeaderboard(anyInt(), anyString());
        }
        verify(mockSoundManager, times(soundEffects)).playSound(Sound_Options.MENU_SWITCH);
        verify(game, times(expectedState != GameStates.START_MENU ? 1 : 0))
            .setState(expectedState);
    }

    @Test
    void testCharacterInput() throws Exception {
        GameOverMenu menu = new GameOverMenu(100);
        GameOverController controller = new GameOverController(menu);
        KeyStroke key = mock(KeyStroke.class);
        when(key.getKeyType()).thenReturn(KeyType.Character);
        when(key.getCharacter()).thenReturn('A');

        controller.step(game, key, 0);

        assertEquals("A", menu.getUsername());
    }*/
}