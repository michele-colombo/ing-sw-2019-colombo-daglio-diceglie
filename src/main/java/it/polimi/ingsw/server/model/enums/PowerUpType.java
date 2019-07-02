package it.polimi.ingsw.server.model.enums;

/**
 * Different types of powerups.
 * In the original game powerups do not have types, but they are in fact rather different one from each other,
 * because they have completely different moment of application. That's why we introduced powerup types.
 * What a powerup does is implemented through Effects, though, and has nothing to do with this type.
 */
public enum PowerUpType {
    TARGETING_SCOPE, TAGBACK_GRENADE, ACTION_POWERUP
}
