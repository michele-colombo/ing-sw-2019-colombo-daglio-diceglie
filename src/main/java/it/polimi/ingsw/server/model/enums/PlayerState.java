package it.polimi.ingsw.server.model.enums;

/**
 * Possible states of a player.
 * The game is represented by a finite state machine with these states.
 */
public enum PlayerState {
    IDLE,
    CHOOSE_ACTION,
    MOVE_THERE,
    GRAB_THERE,
    GRAB_WEAPON,
    DISCARD_WEAPON,
    RELOAD,
    PAYING,
    PAYING_ANY,
    SHOOT_WEAPON,
    CHOOSE_MODE,
    SHOOT_TARGET,
    SPAWN,
    USE_POWERUP;
}
