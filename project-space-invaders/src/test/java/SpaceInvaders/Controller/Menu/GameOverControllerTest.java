package SpaceInvaders.Controller.Menu;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Game;
import SpaceInvaders.Model.Menu.GameOverMenu;
import SpaceInvaders.Model.Sound.Sound_Options;
import SpaceInvaders.State.GameStates;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameOverControllerTest {
    private static SoundManager mockSoundManager;
    private static MockedStatic<SoundManager> mockedStatic;
    @Mock
    private Game game;
    @Mock
    private BufferedWriter mockBuffer;
    @Mock
    private File mockFile;

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

    private static Stream<Arguments> valuesTestStep() {
        GameOverMenu menu1 = mock(GameOverMenu.class);
        when(menu1.isSelectedRestart()).thenReturn(true);
        when(menu1.isSelectedLeaderboard()).thenReturn(false);
        when(menu1.isSelectedExit()).thenReturn(false);
        when(menu1.getScore()).thenReturn(0);
        when(menu1.getUsername()).thenReturn("test");

        GameOverMenu menu2 = mock(GameOverMenu.class);
        when(menu2.isSelectedRestart()).thenReturn(false);
        when(menu2.isSelectedLeaderboard()).thenReturn(true);
        when(menu2.isSelectedExit()).thenReturn(false);
        when(menu2.getScore()).thenReturn(0);
        when(menu2.getUsername()).thenReturn("test");

        GameOverMenu menu3 = mock(GameOverMenu.class);
        when(menu3.isSelectedRestart()).thenReturn(false);
        when(menu3.isSelectedLeaderboard()).thenReturn(false);
        when(menu3.isSelectedExit()).thenReturn(true);
        when(menu3.getScore()).thenReturn(0);
        when(menu3.getUsername()).thenReturn("test");

        GameOverMenu menu4 = mock(GameOverMenu.class);
        when(menu4.isSelectedRestart()).thenReturn(false);
        when(menu4.isSelectedLeaderboard()).thenReturn(false);
        when(menu4.isSelectedExit()).thenReturn(false);
        when(menu4.getScore()).thenReturn(0);
        when(menu4.getUsername()).thenReturn("test");


        KeyStroke key1 = mock(KeyStroke.class);
        when(key1.getKeyType()).thenReturn(KeyType.ArrowUp);

        KeyStroke key2 = mock(KeyStroke.class);
        when(key2.getKeyType()).thenReturn(KeyType.ArrowDown);

        KeyStroke key3 = mock(KeyStroke.class);
        when(key3.getKeyType()).thenReturn(KeyType.Enter);

        KeyStroke key4 = mock(KeyStroke.class);
        when(key4.getKeyType()).thenReturn(KeyType.ArrowRight);

        KeyStroke key5 = mock(KeyStroke.class);
        when(key5.getKeyType()).thenReturn(KeyType.Character);
        when(key5.getCharacter()).thenReturn('a');

        KeyStroke key6 = mock(KeyStroke.class);
        when(key6.getKeyType()).thenReturn(KeyType.Backspace);

        return Stream.of(
                Arguments.of(key1, 1, 0, 1, menu4, GameStates.START_MENU, 0, 0, 0),
                Arguments.of(key2, 1, 1, 0, menu4, GameStates.START_MENU, 0, 0, 0),
                Arguments.of(key3, 0, 0, 0, menu1, GameStates.NEW_GAME, 1, 0, 0),
                Arguments.of(key3, 0, 0, 0, menu2, GameStates.LEADERBOARD, 1, 0, 0),
                Arguments.of(key3, 0, 0, 0, menu3, GameStates.START_MENU, 1, 0, 0),
                Arguments.of(key3, 0, 0, 0, menu4, GameStates.QUIT_GAME, 0, 0, 0),
                Arguments.of(key3, 0, 0, 0, menu4, GameStates.QUIT_GAME, 0, 0, 0),
                Arguments.of(null, 0, 0, 0, menu4, GameStates.START_MENU, 0, 0, 0),
                Arguments.of(key4, 0, 0, 0, menu4, GameStates.START_MENU, 0, 0, 0),
                Arguments.of(key5, 0, 0, 0, menu4, GameStates.START_MENU, 0, 1, 0),
                Arguments.of(key6, 0, 0, 0, menu4, GameStates.START_MENU, 0, 0, 1)
        );
    }

    private static Stream<Arguments> testUpdateLeaderboardValues() {
        return Stream.of(
                Arguments.of("test", 0, "test 0\n"),
                Arguments.of("", 100, "Unknow 100\n")
        );
    }

    @ParameterizedTest
    @MethodSource("valuesTestStep")
    void testStep(KeyStroke key, int numSoundSwitch, int nextOption, int previousOption, GameOverMenu menu, GameStates state, int numState, int numAddLetter, int numRemoveLetter) {
        GameOverController controller = new GameOverController(menu);
        clearInvocations(menu, game, mockSoundManager);

        assertDoesNotThrow(() -> controller.step(game, key, 0));

        verify(mockSoundManager, times(numSoundSwitch)).playSound(Sound_Options.MENU_SWITCH);
        verify(menu, times(nextOption)).nextOption();
        verify(menu, times(previousOption)).previousOption();
        verify(menu, times(numAddLetter)).addLetter(any());
        verify(menu, times(numRemoveLetter)).removeLetter();
        assertDoesNotThrow(() -> verify(game, times(numState)).setState(any()));
        assertDoesNotThrow(() -> verify(game, times(numState)).setState(state));
    }

    @ParameterizedTest
    @MethodSource("testUpdateLeaderboardValues")
    void testUpdateLeaderboard(String userName, int score, String expectedLine) throws IOException {
        GameOverController controller = spy(new GameOverController(new GameOverMenu(100)));
        doReturn(mockBuffer).when(controller).createBuffer(any());

        controller.updateLeaderboard(score, userName);

        verify(mockBuffer).write(expectedLine);
        verify(mockBuffer).flush();
        verify(mockBuffer).close();
    }
}