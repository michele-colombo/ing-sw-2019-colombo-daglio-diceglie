package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.AmmoTile;

import java.util.List;

/**
 * Represents the decks on client, in order to properly load description in user interface
 */
public class DecksView {
    /**
     * Used to check if a weapon has no name
     */
    private static final String NONAME_WEAPON = "";
    /**
     * Contains WeaponViews of the game
     */
    private List<WeaponView> originalWeaponArsenal;
    /**
     * Contains PowerUpf of the game
     */
    private List<PowerUpView> originalPowerUps;
    /**
     * Contains AmmoTiles of the game
     */
    private List<AmmoTile> originalAmmoTiles;

    /**
     * Creates a DeckView with give weapons arsenal, power ups and ammo tiles
     * @param originalWeaponArsenal Weapon arsenal to be loaded
     * @param originalPowerUps Power ups to be loaded
     * @param originalAmmoTiles Ammo tiles to be loaded
     */
    public DecksView(List<WeaponView> originalWeaponArsenal, List<PowerUpView> originalPowerUps, List<AmmoTile> originalAmmoTiles) {
        this.originalWeaponArsenal = originalWeaponArsenal;
        this.originalPowerUps = originalPowerUps;
        this.originalAmmoTiles = originalAmmoTiles;
    }


    /**
     * Looks for the PowerUpView with a given name
     * @param string The name of the PowerUp to be searched
     * @return The PowerUpView corresponding to the PowerUp name
     */
    public PowerUpView getPowerUpFromString(String string){
        for (PowerUpView po : originalPowerUps){
            if (po.toString().equalsIgnoreCase(string)){
                return po;
            }
        }
        return null;
    }

    /**
     * Looks for the AmmoTileView with given name
     * @param string The name of the AmmoTile to be searched
     * @return The AmmoTileView corresponding to the AmmoTile name
     */
    public AmmoTile getAmmoTileFromString(String string){
        if (string == null ) return null;
        if (string.equals(NONAME_WEAPON)) return null;
        for (AmmoTile at : originalAmmoTiles){
            if (at.toString().equalsIgnoreCase(string)){
                return at;
            }
        }
        return null;
    }

    /**
     * Looks for the WeaponView with given name
     * @param name The name of the Weapon to be searched
     * @return The WeaponView corresponding to the Weapon name
     */
    public WeaponView getWeaponFromName(String name){
        for (WeaponView w : originalWeaponArsenal){
            if (w.getName().equalsIgnoreCase(name)){
                return w;
            }
        }
        return null;
    }
}
