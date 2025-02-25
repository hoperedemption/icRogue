package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0StaffRoom extends Level0ItemRoom {
    /**
     * Default constructor of Level0StaffRoom. It adds a staff in the middle of it
     * @param pos (DiscreteCoordinates) : the position of the room in the map.
     */
    public Level0StaffRoom(DiscreteCoordinates pos) {
        super(pos);
        Staff staff = new Staff(this, Orientation.DOWN, new DiscreteCoordinates(5, 5));
        items.add(staff);
    }
}
