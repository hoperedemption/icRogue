package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile {
    private final ICRogueArrowInterractionHandler handler;

    /**
     * Default constructor of arrow
     * @param owner (Area) : the area where the arrow evolves.
     * @param orientation (Orientation) : the orientation of the arrow
     * @param coordinates (DiscreteCoordinates) : the coordinates of the arrow used to place it on the area
     */
    public Arrow(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        handler = new ICRogueArrowInterractionHandler();
    }

    /**
     * Makes the arrow move according to a certain move duration.
     */
    public void move() {
        move(DEFAULT_MOVE_DURATION);
    }

    /**
     * Updates all the parameters of arrow. Also it makes it move
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        move();
        super.update(deltaTime);
    }

    /**
     * If the arrow is not yet consumed, it draws it on the canvas. Otherwise, it is not.
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if(!isConsumed()) {
            super.draw(canvas);
        }
    }

    /**
     * Makes the visitor v handle the interaction between the two entities.
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean) : tells if the interaction is a CellInteraction or not.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }

    /**
     * States that the arrow can interact with the other interactable that is passed in parameter.
     * @param other (Interactable). Not null
     * @param isCellInteraction True if this is a cell interaction
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }


    private class ICRogueArrowInterractionHandler implements ICRogueInteractionHandler {
        /**
         * If the interaction is a view interaction and if the arrow is not yet consumed, it is destroying the arrow
         * if it hits a wall.
         * @param cell (ICRogueBehaviour.ICRogueCell)
         * @param isCellInteraction (boolean) : states if the interaction is a cell interaction or not
         */
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if (!isCellInteraction) {
                if(!isConsumed()) {
                    if (cell.getType().equals(ICRogueBehavior.ICRogueCellType.WALL)) {
                        consume();
                    }
                }
            }
        }
    }

    /**
     * Set the sprite of the arrow.
     */
    @Override
    public void setSprite() {
        sprite = new Sprite("zelda/arrow", 1f, 1f, this,
                new RegionOfInterest(32*getOrientation().ordinal(), 0, 32, 32),
                new Vector(0, 0));
    }

    /**
     * destroys the arrow.
     */
    @Override
    public void consume() {
        super.consume();
        leaveArea();
    }

    /**
     * @return the damages that the arrow is inflicting.
     */
    public int getDamage() {
        return damage;
    }
}
