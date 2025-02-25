package ch.epfl.cs107.play.game.icrogue.handler;

import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.window.Button;

public interface ICRogueDialogueHandler {

    default void GestionDialogue(ICRoguePlayer player, Button button){}
}