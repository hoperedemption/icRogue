package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

public interface Consumable {
    /**
     * Makes the projectile destroys itself.
     */
    void consume();

    /**
     * @return if the boolean is consumed or not
     */
    boolean isConsumed();
}
