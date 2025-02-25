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

public class WaterBall extends Projectile{
    private final static int damage = 4;
    private final WaterBall.ICRogueWaterInteractionHandler handler;

    /**
     * Default constructor of Waterball
     * @param owner (Area) : the area where the waterball evolves
     * @param orientation (Orientation) : the orientation of the waterball
     * @param coordinates (DiscreteCoordinates) : the coordinates used to place the waterball on the area.
     */
    public WaterBall(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates, 5, 1);
        handler = new WaterBall.ICRogueWaterInteractionHandler();
    }

    /**
     * Sets the sprite of the waterball.
     */
    @Override
    public void setSprite() {
        sprite = new Sprite("zelda/magicWaterProjectile", 0.8f, 0.8f, this,
                new RegionOfInterest(0, 0, 32, 32), new
                Vector(0, 0));
    }

    /**
     * Destroys the waterball and makes it leave the area.
     */
    @Override
    public void consume() {
        super.consume();
        leaveArea();
    }

    /**
     * Makes the waterball move over the area
     */
    public void move() {
        move(8);
    }

    /**
     * Updates all the parameters of the waterball (i.e. its position).
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        move();
        super.update(deltaTime);
    }

    /**
     * Draws the waterball on the canvas if it is not consumed
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
     * If the waterball is interacting with another interactable, some actions are done.
     * @param other (Interactable) : is not null
     * @param isCellInteraction (boolean) : states if the interaction is a cell interaction or not
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class ICRogueWaterInteractionHandler implements ICRogueInteractionHandler {
        /**
         * If the waterBall goes on a wall or hole, and if the waterball is not yet consumed, it will destroy itself.
         * @param cell (ICRogueBehaviour.ICRogueCell) : The cell that is interacting with the waterball
         * @param isCellInteraction (boolean) : states if the interaction that is occurring is a cell interaction or not
         */
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(!isCellInteraction) {
                if(!isConsumed()) {
                    if(cell.getType().equals(ICRogueBehavior.ICRogueCellType.WALL)
                            || cell.getType().equals(ICRogueBehavior.ICRogueCellType.HOLE)) {
                        consume();
                    }
                }
            }
        }

        /**
         * If both projectiles are not consumed, they will have an interaction in which they will mutually cancel their action.
         * Thus, they both destroy themselves
         * @param fire (Fire) : the fireball that is having an interaction with the waterball.
         * @param isCellInteraction (boolean) : states if the occurring interaction is a cell interaction or not
         */
        @Override
        public void interactWith(Fire fire, boolean isCellInteraction) {
            if(isCellInteraction) {
                if(!isConsumed() && !fire.isConsumed()) {
                    fire.consume();
                    consume();
                }
            }
        }
    }

    /**
     * @return the damages that are inflicted by the waterball.
     */
    @Override
    public int getDamage() {
        return damage;
    }
}
