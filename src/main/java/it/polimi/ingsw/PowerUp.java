package it.polimi.ingsw;

public class PowerUp{
    private Color color;
    private PowerUpType type;
    private String name;
    private String description;

    public PowerUp (Color color, PowerUpType type){
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public PowerUpType getType() {
        return type;
    }

    @Override
    public String toString(){
        return "PowerUp: "+type+" | "+color+" ";
    }


}
