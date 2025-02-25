package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Mew;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0MewRoom extends Level0EnemyRoom {
    /**
     * Default constructor of Level0MewRoom.
     * It adds next all the possible orientations to allow to Mew to shoot everywhere.
     * @param pos (DiscreteCoordinates) : the position of the boss room in the map.
     */
    public Level0MewRoom(DiscreteCoordinates pos) {
        super(pos);

        List<Orientation> orientations = new ArrayList<Orientation>() {{
            add(Orientation.DOWN);
            add(Orientation.LEFT);
            add(Orientation.UP);
            add(Orientation.RIGHT);
        }};

        activeEnemies.add(new Mew(this, orientations));
    }


}
