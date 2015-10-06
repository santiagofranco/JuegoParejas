package com.santiagofranco.pairsgame;

public class Carta {

    private int posX;
    private int posY;
    private int value;

    public Carta(int posX, int posY, int value) {
        this.posX = posX;
        this.posY = posY;
        this.value = value;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Carta{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", value=" + value +
                '}';
    }
}
