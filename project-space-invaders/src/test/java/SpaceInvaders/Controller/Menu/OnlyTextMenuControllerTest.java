package SpaceInvaders.Controller.Menu;

import SpaceInvaders.Game;
import SpaceInvaders.Model.Menu.OnlyTextMenu;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OnlyTextMenuControllerTest {

    @Mock
    private OnlyTextMenu menu;
    @Mock
    private Game game;

    private OnlyTextMenuController controller;

    private static Stream<Arguments> valuesTestStep() {
        KeyStroke key1 = mock(KeyStroke.class);
        when(key1.getKeyType()).thenReturn(KeyType.Escape);

        KeyStroke key2 = mock(KeyStroke.class);
        when(key2.getKeyType()).thenReturn(KeyType.ArrowUp);

        return Stream.of(
                Arguments.of(key1, 1),
                Arguments.of(key2, 0),
                Arguments.of(null, 0)
        );
    }

    @BeforeEach
    void setUp() {
        controller = new OnlyTextMenuController(menu);
    }

    @ParameterizedTest
    @MethodSource("valuesTestStep")
    void testStep(KeyStroke key, int expected) throws IOException, URISyntaxException {
        assertDoesNotThrow(() -> controller.step(game, key, 0));

        verify(game, times(expected)).setPrevState();
    }
}