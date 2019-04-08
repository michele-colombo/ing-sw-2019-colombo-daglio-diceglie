package it.polimi.ingsw;

public class PowerUp implements Card{
    private Color color;

    public PowerUp (Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
