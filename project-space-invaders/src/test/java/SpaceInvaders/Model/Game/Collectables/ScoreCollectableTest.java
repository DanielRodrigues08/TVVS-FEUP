package SpaceInvaders.Model.Game.Collectables;

import SpaceInvaders.Model.Game.RegularGameElements.Alien;
import SpaceInvaders.Model.Game.RegularGameElements.AlienMode;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreCollectableTest {

    private ScoreCollectable scoreCollectable;

    @Mock
    private Position position;

    @Mock
    private Alien alien;


    @ParameterizedTest
    @CsvSource({
            "2, SCORE_2X",
            "3, SCORE_3X",
            "4, SCORE_4X",
            "5, SCORE_5X",
            "10, SCORE_10X",
            "1, NORMAL_MODE"
    })
    void testExecute(int multiplier, AlienMode expectedMode) {
        scoreCollectable = new ScoreCollectable(position, List.of(alien), multiplier);
        scoreCollectable.execute();

        verify(alien).setAlienMode(expectedMode);
    }
}