package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;


import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Coin;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0CoinRoom extends Level0ItemRoom{
    /**
     * Default constructor of Level0CoinRoom. It adds a coin in the middle of it
     * @param position (DiscreteCoordinates) : the position of the room in the map.
     */
    public Level0CoinRoom(DiscreteCoordinates position){
        super(position);
        items.add(new Coin(this, Orientation.DOWN, new DiscreteCoordinates(2,7)));
        items.add(new Coin(this,Orientation.DOWN, new DiscreteCoordinates(7,2)));
    }
}