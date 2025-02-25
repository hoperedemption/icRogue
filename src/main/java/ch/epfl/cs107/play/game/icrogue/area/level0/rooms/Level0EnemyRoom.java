package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0EnemyRoom extends Level0Room {
    protected List<Enemy> activeEnemies;

    /**
     * Default constructor of Level0EnemyRoom.
     * @param position (DiscreteCoordinates) : the position of the room in the map
     */
    public Level0EnemyRoom(DiscreteCoordinates position) {
        super(position);
        activeEnemies = new ArrayList<>();
    }


    /**
     * Initializes the list of all the enemies of a room according to the list given in parameter.
     * @param newActiveEnemies (List<Enemy>) : the list of all the enemies that are living in the room
     */
    protected void initializeActiveEnemies(List<Enemy> newActiveEnemies) {
        activeEnemies.addAll(newActiveEnemies);
    }

    /**
     * An enemy room is solved if all the enemies are marked as dead.
     * So the method verifies that. If an enemy is still alive then
     * the room is not solved yet.
     */
    @Override
    public void setResolved() {
        if(isVisited) {
            int i = 0;
            while(i < activeEnemies.size() && !activeEnemies.get(i).isAlive()) {
                ++i;
            }
            if(i >= activeEnemies.size()) {
                isResolved = true;
            }
        }
    }

    /**
     * Registers all the enemies into the room
     */
    @Override
    public void createArea() {
        super.createArea();
        for(Enemy enemy : activeEnemies) {
            registerActor(enemy);
        }
    }
}
