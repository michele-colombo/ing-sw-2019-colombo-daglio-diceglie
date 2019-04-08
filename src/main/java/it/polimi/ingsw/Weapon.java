package it.polimi.ingsw;

public class Weapon implements Card{
    private boolean isLoaded;

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }
}
