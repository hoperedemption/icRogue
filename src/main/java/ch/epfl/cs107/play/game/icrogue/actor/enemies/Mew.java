package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.WaterBall;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Mew extends Enemy {
    private final static float COOLDOWN = 1.0f;
    private final static int MOVE_DURATION = 11;
    private int hp;
    private List<Orientation> directions;
    private float counter;
    private int targetNumber = 0;
    private boolean alive = true;

    private TextGraphics message;

    private DiscreteCoordinates[] targets = {new DiscreteCoordinates(2,2),
            new DiscreteCoordinates(7,2),
            new DiscreteCoordinates(7,7),
            new DiscreteCoordinates(2,7)};


    /**
     * Default constructor of Mew
     * @param area (Area) : the current area where Mew lives
     * @param newDirections (DiscreteCoordinates) : the coordinates used to place Mew
     */
    public Mew(Area area,
                  List<Orientation> newDirections) {
        super(area, Orientation.DOWN, new DiscreteCoordinates(2, 7));

        hp = 30;

        message = new TextGraphics(Integer.toString((int)hp), 0.5f, Color.RED);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));

        setSprite(Orientation.DOWN);
        directions = new ArrayList<>();
        directions.addAll(newDirections);

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
     * Draw Mew on the canvas.
     * @param canvas (Canvas) target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        if(isAlive()) {
            sprite.draw(canvas);
            message.draw(canvas);
        }
    }

    /**
     * Update all the parameters of Mew.
     * Check if Mew is still alive, makes his points of heal display on the screen,
     * checks if the enemy is able to move.
     * Finally, verify if the cooldown allows Mew to attack another time, then reset the cooldown.
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(!isDisplacementOccurs()) {
            move();
        }

        if(alive) {
            message.setText(Integer.toString((int)hp));
        }

        if(alive && !isAlive()) {
            getOwnerArea().unregisterActor(this);
        }

        if(isAlive()) {
            if(counter>=COOLDOWN){
                counter = 0.f;
                attack();
            }
            counter += deltaTime;
        }
    }

    /**
     * Makes Mew attack. He can fire Waterball in the four orientations.
     * These projectiles are created in the area of living of Mew, then move.
     */
    private void attack() {
        for(Orientation direction : directions) {
            WaterBall waterBall = new WaterBall(getOwnerArea(), direction, getCurrentCells().get(0));
            waterBall.enterArea(getOwnerArea(), getCurrentCells().get(0));
            waterBall.move();
        }
    }

    /**
     * Depending on the orientation of Mew, set his Sprite.
     * @param orientation (Orientation) : the current orientation of Mew.
     */
    private void setSprite(Orientation orientation) {
        switch (orientation) {
            case UP -> // haut
                    sprite = new Sprite("mew.fixed", .75f, 1.f, this,
                            new RegionOfInterest(32, 0, 16, 32), new Vector(.15f,
                            -.15f));
            case DOWN -> //bas
                    sprite = new Sprite("mew.fixed", .75f, 1.f, this,
                            new RegionOfInterest(0, 0, 16, 32), new Vector(.15f, -.15f));
            case LEFT -> // gauche
                    sprite = new Sprite("mew.fixed", .75f, 1.f, this,
                            new RegionOfInterest(16, 0, 16, 32), new Vector(.15f,
                            -.15f));
            case RIGHT -> // droite
                    sprite = new Sprite("mew.fixed", .75f, 1.f, this,
                            new RegionOfInterest(48, 0, 16, 32), new Vector(.15f,
                            -.15f));

        }
    }

    /**
     * Makes Mew move. Mew has four target positions.
     * If one target position is reached, he switches his target ot another.
     * If all the last position is reached he resets the loop.
     */
    private void move(){
        switch (targetNumber){
                case 0 -> goDown();
                case 1 -> goRight();
                case 2 -> goUp();
                case 3 -> goLeft();
        }
    }

    /**
     * Mew moves until he reaches the position (2,7).
     * If this position is reached, he switches his orientation towards
     * the next position which (2,2).
     * Then, he set the sprite Right and add a number to the list to switch the target position.
     */
    private void goDown(){
        if(!getCurrentCells().get(0).equals(targets[0])){
            move(MOVE_DURATION);
        }
        else {
            orientate(Orientation.RIGHT);
            setSprite(getOrientation());
            ++targetNumber;

        }
    }

    /**
     * Mew moves until he reaches the position (2,2).
     * If this position is reached, he switches his orientation towards
     * the next position which (7,2).
     * Then, he set the sprite Up and add a number to the list to switch the target position.
     */
    private void goRight(){
        if(!getCurrentCells().get(0).equals(targets[1])){
            move(MOVE_DURATION);
        }else{
            orientate(Orientation.UP);
            setSprite(getOrientation());
            ++targetNumber;
        }
    }

    /**
     * Mew moves until he reaches the position (7,2).
     * If this position is reached, he switches his orientation towards
     * the next position which (7,7).
     * Then, he set the sprite Left and add a number to the list to switch the target position.
     */
    private void goUp(){
        if(!getCurrentCells().get(0).equals(targets[2])){
            move(MOVE_DURATION);
        }else{
            orientate(Orientation.LEFT);
            setSprite(getOrientation());
            ++targetNumber;
        }
    }

    /**
     * Mew moves until he reaches the position (7,7).
     * If this position is reached, he switches his orientation towards
     * the next position which (2,7).
     * Then, he set the sprite Down and reset the loop for the target position.
     */
    private void goLeft(){
        if(!getCurrentCells().get(0).equals(targets[3])){
            move(MOVE_DURATION);
        }else{
            orientate(Orientation.DOWN);
            setSprite(getOrientation());
            targetNumber = 0;
        }
    }

    /**
     * Checks if Mew is Alive.
     * @return boolean that checks if Mew is still alive.
     */
    @Override
    public boolean isAlive() {
        alive = hp >= 0;
        return alive;
    }

    /**
     * If Mew gets attacked he withdraws point oh heal.
     */
    public void getDamage() {
        hp -= 2;
    }

}
