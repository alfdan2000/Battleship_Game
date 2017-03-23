package edu.utep.cs.cs4330.battleship;

//Thomas Payan

public class Ship {

    private String shipName;
    private int shipSize;
    private boolean isplaced;
    private boolean isdestroyed;
    private int place;
    private int health;

    Ship(String name, int length){
        shipName=name;
        shipSize=length;
        isplaced=false;
        isdestroyed=false;
        place=0;
        health=length;
    }
    //getters for variables
    public String getName(){
        return shipName;
    }
    public int getLength(){
        return shipSize;
    }
    public boolean getIsPlaced(){
        return isplaced;
    }
    public boolean getIsDestroyed(){
        return isdestroyed;
    }
    public int getPlace(){return place;}
    public int getHealth(){return health;}

    //setters for variables
    public void shipPlaced(){
        isplaced = true;
    }

    public void setIsPlaced(boolean p){

        this.isplaced=p;
    }

    public void setIsDestroyed(boolean d){

        this.isdestroyed=d;
    }

    public void setyPlace(int p){
        this.place=p;}

    public void setHealth(int h){
        this.health=h;}


}
