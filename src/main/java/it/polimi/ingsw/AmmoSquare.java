package it.polimi.ingsw;

public class AmmoSquare extends Square{
    private AmmoTile ammo;

    public AmmoSquare(int x, int y, Border north, Border east, Border south, Border west, Color color){
        super(x, y, north, east, south, west, color);
        isAmmo = true;
        ammo = null;
    }

    @Override
    public boolean collect(Player p, Match m){
        p.getWallet().deposit(ammo.getAmmos());
        if (ammo.hasPowerUp()){
            if (p.getPowerUps().size() < 3){
                p.addPowerUp(m.getStackManager().drawPowerUp());
            }
        }
        ammo = null;
        return true;
    }

    @Override
    public boolean isEmpty(){
        return (ammo == null);
    }

    @Override
    public void refill(StackManager s){
        if (isEmpty()){
            ammo = s.drawAmmoTile();
        }
    }
}
