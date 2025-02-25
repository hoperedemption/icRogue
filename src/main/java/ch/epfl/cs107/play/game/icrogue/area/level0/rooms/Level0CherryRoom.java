package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;


import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0CherryRoom extends Level0ItemRoom{
    /**
     * Default constructor of Level0CherryRoom. It adds a cherry in the middle of it
     * @param position (DiscreteCoordinates) : the position of the room in the map.
     */
    public Level0CherryRoom(DiscreteCoordinates position){
        super(position);
        items.add(new Cherry(this, Orientation.DOWN, new DiscreteCoordinates(5,5)));
    }
}

