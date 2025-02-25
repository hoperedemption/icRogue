package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0KeyRoom extends Level0ItemRoom {

    /**
     * Default constructor of Level0KeyRoom. It adds a kex in the middle of it
     * @param pos (DiscreteCoordinates) : the position of the room in the map.
     */
    public Level0KeyRoom(DiscreteCoordinates pos, int keyID) {
        super(pos);
        Key key = new Key(this, Orientation.DOWN, new DiscreteCoordinates(5, 5), keyID);
        items.add(key);
    }
}
