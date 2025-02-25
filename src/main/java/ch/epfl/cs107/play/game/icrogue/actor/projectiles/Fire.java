package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Seller;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Mew;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Fire extends Projectile {

    private final ICRogueFireInteractionHandler handler;

    /**
     * Default constructor of Fire
     * @param owner (Area) : the current area where the fires is moving
     * @param orientation (Orientation) : the orientation of te fire.
     * @param coordinates (DiscreteCoordinates) : the coordinates that helps us to positionate the fire on the area.
     */
    public Fire(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates, 5, 1);
        handler = new ICRogueFireInteractionHandler();
    }

    /**
     * Sets the sprite of the fire.
     */
    @Override
    public void setSprite() {
        sprite = new Sprite("zelda/fire", 1f, 1f, this,
                new RegionOfInterest(0, 0, 16, 16), new
                Vector(0, 0));
    }

    /**
     * consume the fire and makes him leave the area
     */
    @Override
    public void consume() {
        super.consume();
        leaveArea();
    }

    /**
     * Makes the fire move according to a certain move duration
     */
    public void move() {
        move(DEFAULT_MOVE_DURATION);
    }

    /**
     * Updates all the parameters of fire. Also, it makes it move.
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        move();
        super.update(deltaTime);
    }

    /**
     * Draws the fire in the current canvas if the fire is not consumed.
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
     * If the arrow hits the player, it is inflicting him damages then the arrow is destroyed
     * @param other (Interactable) : is not null
     * @param isCellInteraction (boolean) : states if the interaction is a cell interaction or not
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }


    private class ICRogueFireInteractionHandler implements ICRogueInteractionHandler {
        /**
         * If the fire is not yet consumed, and it is going on a wall or on a hole it
         * will be consumed
         * @param cell (ICRogueBehaviour.ICRogueCell) : the cell that is interacting with the fire.
         * @param isCellInteraction (boolean) : states if the interaction is a cell interaction or not.
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
         * If the fire is interacting with the turret (and this one is not yet consumed),
         * it kills the turret. This action consumes the fire and the turret.
         * @param turret (Turret) : the turret that is interacting with the fire
         * @param isCellInteraction (boolean) : states if the interaction is a cell interaction
         */
        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if(isCellInteraction) {
                if(!isConsumed()) {
                    turret.kill();
                    consume();
                }
            }
        }

        /**
         * If both of the projectiles are not consumed, they interact with each other to destroy themselves during
         * their interaction.
         * @param arrow (Arrow) : the arrow that is interacting with the fire
         * @param isCellInteraction (boolean) : states if the interaction that is occuring is a cell interaction or not
         */
        @Override
        public void interactWith(Arrow arrow, boolean isCellInteraction){
            if(isCellInteraction && !isConsumed && !arrow.isConsumed){
                consume();
                arrow.consume();
            }
        }

        /**
         * If both of the projectiles are not consumed, they interact with each other to destroy themselves during
         * their interaction.
         * @param waterBall (WaterBall) : the waterball that is interacting with the fire
         * @param isCellInteraction (boolean) : states if the interaction that is occurring is a cell interaction or not
         */
        @Override
        public void interactWith(WaterBall waterBall, boolean isCellInteraction) {
            if(isCellInteraction) {
                if(!isConsumed() && !waterBall.isConsumed()) {
                    waterBall.consume();
                    consume();
                }
            }
        }

        /**
         * If a fire is not yet consumed, it is inflicting damages to the boss, Mew.
         * @param mew (Mew) : the boss that is interacting with the fireball
         * @param isCellInteraction (boolean) : states if the interaction is a cell interaction or not
         */
        @Override
        public void interactWith(Mew mew, boolean isCellInteraction) {
            if(isCellInteraction) {
                if(!isConsumed()) {
                    mew.getDamage();
                    consume();
                }
            }
        }

        /**
         * As the seller cannot take damage, she is immune against the fireball. So, the fire is just leaving the area
         * @param seller (Seller) : the seller that is interacting with the fireball
         * @param isCellInteraction (boolean) : states if the interaction is a cell interaction or not
         */
        @Override
        public void interactWith(Seller seller, boolean isCellInteraction) {
            if(!isCellInteraction){
                consume();
            }
        }
    }


}
