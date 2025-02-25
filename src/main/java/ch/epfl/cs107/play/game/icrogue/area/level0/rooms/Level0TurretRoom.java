package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Level0TurretRoom extends Level0EnemyRoom {

    /**
     * Default constructor of Level0TurretRoom. It adds a turret that can shoot downwards and to the right, at the up left
     * corner of the room. It also adds a turret that is shooting upwards and to the left, in the bottom right corner. After
     * adding them, it initializes the list of enemies.
     * @param pos (DiscreteCoordinates) : the position of the room in the map
     */
    public Level0TurretRoom(DiscreteCoordinates pos) {
        super(pos);

        List<Enemy> enemies = new ArrayList<>();

        List<Orientation> firstTurretOrientations = Arrays.asList(Orientation.DOWN, Orientation.RIGHT);
        enemies.add(new Turret(this, Orientation.UP, new DiscreteCoordinates(1, 8),
                firstTurretOrientations));

        List<Orientation> secondTurretOrientations = Arrays.asList(Orientation.UP, Orientation.LEFT);
        enemies.add(new Turret(this, Orientation.UP, new DiscreteCoordinates(8, 1),
                secondTurretOrientations));

        initializeActiveEnemies(enemies);
    }
}
