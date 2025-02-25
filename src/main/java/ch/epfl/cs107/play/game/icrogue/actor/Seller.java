package ch.epfl.cs107.play.game.icrogue.actor;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Dialog;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.io.XMLTexts;


public class Seller extends ICRogueActor{

    private Sprite sprite;
    private static boolean hasDealtWithPlayer;

    private Dialog message;
    private boolean messageCreated;

    /**
     * Default constructor of seller.
     * @param owner (Area) : This is the area where the seller is created.
     * @param orientation (Orientation) : The orientation of the seller.
     * @param position (DiscreteCoordinates) : The position of the seller in the area where she lives.
     */
    public Seller(Area owner, Orientation orientation, DiscreteCoordinates position){
        super(owner, orientation, position);
        setSprite(orientation);
        hasDealtWithPlayer = false;
        messageCreated = false;
    }

    /**
     * Sets the sprite of the seller depending on the orientation of the player.
     * @param orientation (Orientation) : the orientation of the seller.
     */
    public void setSprite(Orientation orientation){
        switch (orientation) {
            case UP -> // haut
                    sprite = new Sprite("joel.fixed", .75f, 1.f, this,
                            new RegionOfInterest(32, 0, 16, 32), new Vector(.15f,
                            -.15f));
            case DOWN -> //bas
                    sprite = new Sprite("joel.fixed", .75f, 1.f, this,
                            new RegionOfInterest(0, 0, 16, 32), new Vector(.15f, -.15f));
            case LEFT -> // gauche
                    sprite = new Sprite("joel.fixed", .75f, 1.f, this,
                            new RegionOfInterest(16, 0, 16, 32), new Vector(.15f,
                            -.15f));
            case RIGHT -> // droite
                    sprite = new Sprite("joel.fixed", .75f, 1.f, this,
                            new RegionOfInterest(48, 0, 16, 32), new Vector(.15f,
                            -.15f));

        }
    }

    /**
     * Draws the sprite of the seller.
     * Also, if an interaction for a dialog is requested it is displaying the message.
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        if(messageCreated){
            message.draw(canvas);
        }
    }

    /**
     * calls the super class function
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
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

    /**
     * The dialog touches to its end and the player wants to move on,
     * it is erasing the displayed message
     */
    public void EraseMessage() {
        messageCreated = false;
    }

    /**
     * @return if the seller has dealt with the player.
     */
    public boolean HasDealtWithPlayer(){
        return hasDealtWithPlayer;
    }

    /**
     * Set the fact that the seller has already dealt with the player
     */
    public void Deal(){
        hasDealtWithPlayer = true;
    }

    /**
     * If the player does not fulfill all the accommodations to trade with the seller,
     * it displays a message
     * stating that the trade is impossible
     */
    public void RejectProposition(){
        message = new Dialog(XMLTexts.getText("Rejet"), "dialog", getOwnerArea());
        messageCreated = true;
        message.push();
    }

    /**
     * If all the accommodations are fulfilled to do the trade,
     * the message displays that the player can deal with the seller
     * and that he is allowed to wear an armour.
     */
    public void AcceptProposition(){
        message = new Dialog(XMLTexts.getText("Question"), "dialog", getOwnerArea());
        messageCreated = true;
        message.push();
    }


    /**
     * @return the fact that the seller is taking the cell.
     * So nothing can enter the cell occupied by the seller.
     */
    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * @return the fact that the seller is accepting view interactions.
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * @return the fact that the seller is refusing cell interactions.
     */
    @Override
    public boolean isCellInteractable() {
        return false;
    }
}