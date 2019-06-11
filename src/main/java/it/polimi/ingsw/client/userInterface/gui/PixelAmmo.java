package it.polimi.ingsw.client.userInterface.gui;


import it.polimi.ingsw.client.SquareView;

public class PixelAmmo {

    private int xSquare;
    private int ySquare;
    private Double x;
    private Double y;

    public PixelAmmo(int xSquare, int ySquare, Double x, Double y){
        this.xSquare = xSquare;
        this.ySquare = ySquare;
        this.x = x;
        this.y = y;
    }

    public int getxSquare(){
        return xSquare;
    }

    public int getySquare(){
        return ySquare;
    }

    public Double getX(){
        return x;
    }

    public Double getY(){
        return y;
    }

    public boolean equalsSquare(SquareView squareView){
        if(squareView.getX() == xSquare && squareView.getY() == ySquare){
            return true;
        }
        return false;
    }


    /*public static void main(String[] args){

        List<PixelAmmo> pixel = new LinkedList<>();
        pixel.add(new PixelAmmo(0,2,0.2042, 0.3568));
        pixel.add(new PixelAmmo(0, 0,0.2051, 0.7784));
        pixel.add(new PixelAmmo(1,0,0.3441, 0.7784));
        pixel.add(new PixelAmmo(1,2,0.3540, 0.2452));
        pixel.add(new PixelAmmo(2,0,0.5435, 0.7729));
        pixel.add(new PixelAmmo(3,2,0.7618, 0.3475));
        pixel.add(new PixelAmmo(1,1,0.3396, 0.5238));
        pixel.add(new PixelAmmo(2,1,0.5339, 0.5860));
        pixel.add(new PixelAmmo(3,1,0.6830, 0.5852));

        Gson gson = new Gson();
        System.out.println(gson.toJson(pixel));
    }*/
}

