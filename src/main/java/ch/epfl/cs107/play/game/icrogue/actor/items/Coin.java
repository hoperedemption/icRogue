package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Coin extends Item{
    /**
     * Default Coin Constructor
     * @param area (area) The coin's area
     * @param orientation (orientation) Coin's orientation
     * @param position (position) The coin's position in the given area
     */
    public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        sprite = new Sprite("zelda/coin", 0.6f, 0.6f, this);
    }

    /**
     * Makes the visitor v handle the interaction between the two entities.
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean) : tells if the interaction is a CellInteraction or not.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }
}