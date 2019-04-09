package it.polimi.ingsw;

public class AmmoSquare extends Square{
    private AmmoTile ammo;

    public AmmoSquare(int x, int y, Border north, Border east, Border south, Border west, Color color){
        super(x, y, north, east, south, west, color);
        isAmmo = true;
    }

    @Override
    public void collect(Player p, StackManager s){
        p.getWallet().deposit(ammo.getAmmos());
        if (ammo.hasPowerUp()){
            if (p.getPowerUps().size() < 3){
                p.addPowerUp(s.drawPowerUp());
            }
        }
        ammo = null;
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
