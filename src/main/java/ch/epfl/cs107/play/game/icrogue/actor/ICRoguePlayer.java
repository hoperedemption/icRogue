package ch.epfl.cs107.play.game.icrogue.actor;


import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Mew;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Coin;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.WaterBall;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {

    private Sprite sprite;
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 6;

    private List<Key> collectedKeys = new ArrayList<>();
    private boolean isGoingThrough = false;

    private final ICRogueInteractionHandler handler;

    private String destination;
    private DiscreteCoordinates positionDestination;

    private boolean collectedStaff = false;

    private int hp;

    private TextGraphics message;
    private boolean isDead = false;

    private List<Cherry> healObjects = new ArrayList<>();

    private int positionFisrtNonEatenCherry;

    private int Wealthness;

    private int armorHp;
    private boolean isProtected;
    private boolean isMessageDisplayed = false;

    /**
     * Constructor ICRoguePlayer Default
     * @param (owner) the given area
     * @param (orientation) the given orientation
     * @param (coordinates) the given coordinates
     */
    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        setSprite(orientation);
        handler = new ICRoguePlayerInteractionHandler();
        hp = 20;
        Wealthness = 0;
        isProtected = false;

        message = new TextGraphics(Integer.toString((int)hp), 0.5f, Color.WHITE);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
        positionFisrtNonEatenCherry = 0;

        resetMotion();
    }


    /**
     * Update
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     * Gets the keyboard of the area, moves depending on the given button that is pressed by the user.
     * If is the player is not dead:
     *    -> if he is protected by the armor, we will print the armorHp on to the screen
     *    -> if he is not protected by the armor, we will print the Hp on to the screen
     *
     * If armor hp is down to a non-positive number:
     *                 we will change the message to the normal hp version
     *                 doesn't have protected attribute, hence is not protected by the armor anymore
     *
     * Checks if firePressed, then spawns the fire and moves
     * Checks if healIfPressed, then heals the player with the cherry he collected previous
     *
     * If he is dead, prints the message that he lost the game and unregister him.
     * Unregister the actor, if he lost.
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        if(!isMessageDisplayed){
            move(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            move(Orientation.UP, keyboard.get(Keyboard.UP));
            move(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            move(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }

        if(!isDead) {
            if(isProtected) {
                message.setText(Integer.toString(armorHp));
            } else {
                message.setText(Integer.toString(hp));
            }
        }

        if(armorHp <= 0) {
            isProtected = false;

            message = new TextGraphics(Integer.toString(hp), 0.5f, Color.WHITE);
            message.setParent(this);
            message.setAnchor(new Vector(-0.3f, 0.1f));
        }

        firePressed(keyboard.get(Keyboard.X));
        healIfPressed(keyboard.get(Keyboard.H));

        if(isDead()) {
            System.out.println("Loser");
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "You lost! Try another time");
            getOwnerArea().unregisterActor(this);
        }


        super.update(deltaTime);
    }


    /**
     * Moves the player in the given orientation, all changing the sprite depending on
     * the given orientation and calls move from the class above
     * @param (orientation) the orientation that the player will move to
     * @param (b) the button that is relevant for the given move
     */
    private void move(Orientation orientation, Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                setSprite(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     Set the status to going the through the connector
     */
    public void setGoingThrough() {
        isGoingThrough = false;
    }

    /**
     * @return (boolean) indicates if the player is going through the door
     */
    public boolean isGoingThrough(){
        return isGoingThrough;
    }

    /**
     * @return (String) corresponds to the area destination
     */
    public String getDestination(){
        return destination;
    }

    /**
     * @return (DiscreteCoordinates) the coordinates of the player destination
     */
    public DiscreteCoordinates getCoordDestination(){
        return positionDestination;
    }

    /**
     * Set the sprite depending on the protected status that he has (hence if he has armor or not)
     * and depending on the given orientation
     * @param (orientation) the given orientation to the player
     */
    private void setSprite(Orientation orientation) {
        if(!isProtected){
            switch (orientation) {
                case UP -> // haut
                        sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                                new RegionOfInterest(0, 64, 16, 32), new Vector(.15f,
                                -.15f));
                case DOWN -> //bas
                        sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                                new RegionOfInterest(0, 0, 16, 32), new Vector(.15f, -.15f));
                case LEFT -> // gauche
                        sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                                new RegionOfInterest(0, 96, 16, 32), new Vector(.15f,
                                -.15f));
                case RIGHT -> // droite
                        sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                                new RegionOfInterest(0, 32, 16, 32), new Vector(.15f,
                                -.15f));

            }
        }else{
            switch (orientation) {
                case UP -> // haut
                        sprite = new Sprite("zelda/darkLord", 1.25f, 1.5f, this,
                                new RegionOfInterest(0, 0, 32, 32), new Vector(.15f,
                                -.15f));
                case DOWN -> //bas
                        sprite = new Sprite("zelda/darkLord", 1.25f, 1.5f, this,
                                new RegionOfInterest(0, 64, 32, 32), new Vector(.15f, -.15f));
                case LEFT -> // gauche
                        sprite = new Sprite("zelda/darkLord", 1.25f, 1.5f, this,
                                new RegionOfInterest(0, 32, 32, 32), new Vector(.15f,
                                -.15f));
                case RIGHT -> // droite
                        sprite = new Sprite("zelda/darkLord", 1.25f, 1.5f, this,
                                new RegionOfInterest(0, 96, 32, 32), new Vector(.15f,
                                -.15f));

            }
        }

    }

    /**
     * Heals the player with a cherry he has collected previously
     * and deletes it from the collected cherries, hence he eats a cherry previously
     * collected, heals himself and then the cherry disappears
     * positionFirstNonEatenCherry corresponds to first non eaten cherry in the array
     * @param (b) Button that we get from update, that is always H
     */
    private void healIfPressed(Button b){
        if(b.isPressed() && stillHealItems()){
            hp+=5;
            healObjects.get(positionFisrtNonEatenCherry).eat();
            ++positionFisrtNonEatenCherry;
        }
    }

    /**
     * Spawns a new fire entity, with the player's orientation, in the player's are,
     * all with his current position.
     * Then the fire moves in the given direction.
     * @param (b) Button that we get from update, that is always X
     */
    private void firePressed(Button b) {
        if(b.isPressed() && collectedStaff) {
            Fire fire = new Fire(getOwnerArea(), getOrientation(),
                    getCurrentCells().get(0));
            fire.enterArea(getOwnerArea(), getCurrentCells().get(0));
            fire.move();
        }
    }

    /**
     * @return true if the player has still cherries to eat
     */
    private boolean stillHealItems(){
        if(!healObjects.isEmpty() && positionFisrtNonEatenCherry<healObjects.size()){
            for(Cherry cherry : healObjects){
                if(!cherry.isEaten()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * draws the player and the associated hp message,
     * thus the one corresponding to his armor status
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if(!isDead()) {
            sprite.draw(canvas);
            message.draw(canvas);
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
     * Gets the field of view cells, hence the cells that are in the player's field of view
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /**
     * @return true, because the player by default always wants a view interraction
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * @return if he wants view interaction or not depending if W is pressed
     * hence if W is pressed then he wants a view interaction
     * Is useful for example to opening locked doors
     */
    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(Keyboard.W).isPressed();
    }

    /**
     * accept interaction, with the interactable with whom he wants to interract
     * @param other (Interactable). Not null
     * @param isCellInteraction True if this is a cell interaction
     */
    public void interactWith(Interactable other,boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * The inner class ICRoguePlayerInteractionHandler implements the ICRogueInteractionHandler interface
     * Hence it redefines the potential interactions with all other game entities
     */
    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler {

        /**
         * If it is a cell interaction and the cherry is not yet collected
         * then he adds the cherry to the heal objects array and then collects the cherry
         * @param (cherry) the cherry with which he wants to interact
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(Cherry cherry, boolean isCellInteraction) {
            if (isCellInteraction) {
                if (!cherry.isCollected()) {
                    healObjects.add(cherry);
                    cherry.collect();
                }
            }
        }

        /**
         * If it is not a cell interaction, and the staff is not yet collected, then
         * the staff will be collected by the player
         * @param (staff) the cherry with which he wants to interact
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(Staff staff, boolean isCellInteraction) {
            if (!isCellInteraction) {
                if (!staff.isCollected()) {
                    collectedStaff = true;
                    staff.collect();
                }
            }
        }

        /**
         * If it is a cell interaction then if the key is not collected the key will be collected
         * and added to the array of collected keys
         * @param (key) the key with which he wants to interact
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(Key key, boolean isCellInteraction) {
            if (isCellInteraction) {
                if (!key.isCollected()) {
                    collectedKeys.add(key);
                    key.collect();
                }
            }
        }


        /**
         * If it is not a cell interaction and the connector is locked then
         * if the player has the key to open it, the connector will open
         * @param (connector) the connector with which he wants to interact
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(Connector connector, boolean isCellInteraction) {
            if(!isCellInteraction && connector.isLocked()){
                if(hasKey(connector)){
                    connector.setState(Connector.State.OPEN);
                }
            }

            if(connector.getState()== Connector.State.OPEN && !isDisplacementOccurs() && isCellInteraction){
                positionDestination = connector.getPositionAreaDestination();
                destination = connector.getAreaDestination();
                isGoingThrough = true;
            }

        }

        /**
         * @param (connector) the connector with which the player interacts
         * @return true if has the key to open the given connector
         */
        private boolean hasKey(Connector connector) {
            int i = 0;
            while(i<collectedKeys.size() && collectedKeys.get(i).getIdentifiant() != connector.getKeyID()){
                ++i;
            }
            return i<collectedKeys.size();
        }

        /**
         * if it is a cell interaction (hence the arrow goes in the player)
         * then if the arrow is not yet consumed then :
         *  --> if the player is not protected the damage will be inflicted to his hp
         *  --> if the player is protected the damage will be inflicted to hos amor hp
         * then the arrow shall be consumed
         * @param (arrow) arrow with which the player interacts
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(Arrow arrow, boolean isCellInteraction) {
            if(isCellInteraction) {
                if(!arrow.isConsumed()) {
                    if(!isProtected) {
                        hp -= arrow.getDamage();

                    } else {
                        armorHp -= arrow.getDamage();
                    }
                    arrow.consume();
                }
            }
        }

        /**
         * If it is a cell interaction, hence the player goes into the cell of the turret,
         * then the turret shall be killed
         * @param (turret) turret with which the player interacts
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if(isCellInteraction) {
                turret.kill();
            }
        }

        /**
         * If there is a cell interaction, then if the water ball is not yet consumed it
         * will inflict the appropriate damage depending if he is protected or not :
         * hence to the armor or to his hp directly
         * It then shall be consumed
         * @param (waterBall) waterBall with which the player interacts
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(WaterBall waterBall, boolean isCellInteraction) {
            if(isCellInteraction) {
                if(!waterBall.isConsumed()) {
                    if(!isProtected) {
                        hp -= waterBall.getDamage();
                    } else {
                        armorHp -= waterBall.getDamage();
                    }
                    waterBall.consume();
                }
            }
        }

        /**
         * if it is a cell interaction, then mew will inflict damage to the player depending
         * on the fact if he is protected by the armor or not.
         * @param (mew) mew with which the player interacts
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(Mew mew, boolean isCellInteraction) {
            if(isCellInteraction) {
                if(!isProtected) {
                    hp -= 5;
                } else {
                    armorHp -= 2;
                }

            }
        }

        /**
         * if it is not a cell interaction, the player speaks to the seller, not taking
         * his cell, then the seller set turns to the player's direction.
         * if he is wealthy enough, has enough coins the seller accepts the
         * player's deal and gets him a new and fancy armour. We change the appropriate message
         * , we also change the player's sprite, for he is already armored.
         * Otherwise, the seller gives the player the appropriate message.
         * @param (seller) seller with which the player interacts
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        @Override
        public void interactWith(Seller seller, boolean isCellInteraction) {
            if(!isCellInteraction && !seller.HasDealtWithPlayer()){
                seller.setSprite(getOrientation().opposite());
                if(Wealthness < 2){
                    seller.RejectProposition();
                    isMessageDisplayed = true;
                }else{
                    seller.AcceptProposition();
                    seller.Deal();
                    isMessageDisplayed = true;
                    isProtected = true;
                    armorHp = 10;

                    message = new TextGraphics(Integer.toString((int)armorHp), 0.5f, Color.ORANGE);
                    message.setParent(ICRoguePlayer.this);
                    message.setAnchor(new Vector(0.3f, 0.1f));

                    setSprite(getOrientation());
                }
            }
        }

        /**
         * Collects the given coin and increases the player's wealthiness
         * @param (coin) the coin with which the player interacts
         * @param (isCellInteraction) indicates if it is a cell interaction or not
         */
        public void interactWith(Coin coin, boolean isCellInteraction){
            if(isCellInteraction && !coin.isCollected()){
                Wealthness += 2;
                coin.collect();
            }
        }
    }

    /**
     * @return true, if the player is dead, basically when he has non-positive hp's
     */
    private boolean isDead() {
        isDead = hp <= 0;
        return isDead;
    }

    /**
     * sets the message displayed status to false
     */
    public void setMessageDisplayed() {
        isMessageDisplayed = false;
    }

}

