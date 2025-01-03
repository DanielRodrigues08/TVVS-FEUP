package SpaceInvaders.Controller.Menu;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Game;
import SpaceInvaders.Model.Menu.StartMenu;
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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StartMenuControllerTest {

    private static SoundManager mockSoundManager;
    private static MockedStatic<SoundManager> mockedStatic;
    @Mock
    private Game game;

    private static Stream<Arguments> valuesTestStep() {
        StartMenu menu1 = mock(StartMenu.class);
        when(menu1.isSelectedPlay()).thenReturn(true);
        when(menu1.isSelectedLeaderboard()).thenReturn(false);
        when(menu1.isSelectedInstructions()).thenReturn(false);
        when(menu1.isSelectedExit()).thenReturn(false);

        StartMenu menu2 = mock(StartMenu.class);
        when(menu2.isSelectedPlay()).thenReturn(false);
        when(menu2.isSelectedLeaderboard()).thenReturn(true);
        when(menu2.isSelectedInstructions()).thenReturn(false);
        when(menu2.isSelectedExit()).thenReturn(false);

        StartMenu menu3 = mock(StartMenu.class);
        when(menu3.isSelectedPlay()).thenReturn(false);
        when(menu3.isSelectedLeaderboard()).thenReturn(false);
        when(menu3.isSelectedInstructions()).thenReturn(true);
        when(menu3.isSelectedExit()).thenReturn(false);

        StartMenu menu4 = mock(StartMenu.class);
        when(menu4.isSelectedPlay()).thenReturn(false);
        when(menu4.isSelectedLeaderboard()).thenReturn(false);
        when(menu4.isSelectedInstructions()).thenReturn(false);
        when(menu4.isSelectedExit()).thenReturn(true);

        StartMenu menu5 = mock(StartMenu.class);
        when(menu5.isSelectedPlay()).thenReturn(false);
        when(menu5.isSelectedLeaderboard()).thenReturn(false);
        when(menu5.isSelectedInstructions()).thenReturn(false);
        when(menu5.isSelectedExit()).thenReturn(false);

        KeyStroke key1 = mock(KeyStroke.class);
        when(key1.getKeyType()).thenReturn(KeyType.ArrowUp);

        KeyStroke key2 = mock(KeyStroke.class);
        when(key2.getKeyType()).thenReturn(KeyType.ArrowDown);

        KeyStroke key3 = mock(KeyStroke.class);
        when(key3.getKeyType()).thenReturn(KeyType.Enter);

        KeyStroke key4 = mock(KeyStroke.class);
        when(key4.getKeyType()).thenReturn(KeyType.ArrowRight);  // Fixed: changed key3 to key4

        return Stream.of(
                Arguments.of(key1, 1, 0, 1, menu5, GameStates.START_MENU, 0),
                Arguments.of(key2, 1, 1, 0, menu5, GameStates.START_MENU, 0),
                Arguments.of(key3, 0, 0, 0, menu1, GameStates.NEW_GAME, 1),
                Arguments.of(key3, 0, 0, 0, menu2, GameStates.LEADERBOARD, 1),
                Arguments.of(key3, 0, 0, 0, menu3, GameStates.INSTRUCTIONS, 1),
                Arguments.of(key3, 0, 0, 0, menu4, GameStates.QUIT_GAME, 1),
                Arguments.of(key3, 0, 0, 0, menu5, GameStates.QUIT_GAME, 0),
                Arguments.of(null, 0, 0, 0, menu5, GameStates.START_MENU, 0),  // Fixed: changed key3 to null for START_MENU case
                Arguments.of(key4, 0, 0, 0, menu5, GameStates.START_MENU, 0)   // Fixed: changed menu4 to menu5 for consistency
        );
    }

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

    @ParameterizedTest
    @MethodSource("valuesTestStep")
    void testStep(KeyStroke key, int numSoundSwitch, int nextOption, int previousOption, StartMenu menu, GameStates state, int numState) {
        StartMenuController controller = new StartMenuController(menu);
        clearInvocations(menu, game, mockSoundManager);

        assertDoesNotThrow(() -> controller.step(game, key, 0));

        verify(mockSoundManager, times(numSoundSwitch)).playSound(Sound_Options.MENU_SWITCH);
        verify(menu, times(nextOption)).nextOption();
        verify(menu, times(previousOption)).previousOption();
        assertDoesNotThrow(() -> verify(game, times(numState)).setState(any()));
        assertDoesNotThrow(() -> verify(game, times(numState)).setState(state));
    }
}