package edu.fje.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Puzzle {
    final int Esquerra = 0;
    final int Adalt = 1;
    final int Dreta = 2;
    final int Abaix = 3;

    final int[] X = {0, 1, 2, 3, 4};
    final int[] Y = {0 ,1, 2, 3, 4};

    private int[] fitxes;
    private int handleLocation;
    private Random random = new Random();
    private int amplada;
    private int alçada;

    public void init(int width, int height) {
        this.amplada = width;
        this.alçada = height;
        fitxes = new int[width * height];

        for(int i = 0; i < fitxes.length; i++) {
            fitxes[i] = i;
        }

        handleLocation = fitxes.length - 1;
    }

    public void setFitxes(int[] fitxes) {
        this.fitxes = fitxes;
        for (int i = 0; i < fitxes.length; i++){
            if (fitxes[i] == fitxes.length - 1) {
                handleLocation = i;
                break;
            }
        }
    }

    public int[] getFitxes() { return fitxes; }

    public int getColumna(int location) { return location % amplada; }

    public int getFila(int location) { return location / amplada; }

    public int getAmplada() { return amplada; }

    public int getAlçada() { return alçada; }

    public int Distancia() {
        int dist = 0;

        for(int i = 0; i < fitxes.length; i++) {
            dist += Math.abs(i - fitxes[i]);
        }

        return dist;
    }
    public int getPosMov() {
        int x = getColumna(handleLocation);
        int y = getFila(handleLocation);

        boolean left = x > 0;
        boolean right = x < amplada - 1;
        boolean up = y > 0;
        boolean down = y < alçada - 1;

        return  (left ? 1 << Esquerra : 0) |
                (right ? 1 << Dreta: 0) |
                (up ? 1 << Adalt: 0) |
                (down ? 1 << Abaix: 0);
    }

    private int fitxaBuida(int exclude) {
        List<Integer> moviments = new ArrayList<Integer>(4);
        int posMovs = getPosMov() & ~exclude;

        if((posMovs & (1 << Esquerra)) > 0) { moviments.add(Esquerra); }
        if((posMovs & (1 << Adalt)) > 0)    { moviments.add(Adalt); }
        if((posMovs & (1 << Dreta)) > 0)    { moviments.add(Dreta); }
        if((posMovs & (1 << Abaix)) > 0)    { moviments.add(Abaix); }

        return moviments.get(random.nextInt(moviments.size()));
    }

    private int movInvert(int move) {
        if(move == 0)             { return 0; }
        if(move == 1 << Esquerra) { return 1 << Dreta; }
        if(move == 1 << Adalt)    { return 1 << Abaix; }
        if(move == 1 << Dreta)    { return 1 << Esquerra; }
        if(move == 1 << Abaix)    { return 1 << Adalt; }

        return 0;
    }

    public boolean movFitxa(int direccio, int count){
        boolean match = false;

        for (int i = 0; i < count; i++) {
            int targetLocation = handleLocation + X[direccio] + Y[direccio] * amplada;
            fitxes[handleLocation] = fitxes[targetLocation];
            match |= fitxes[handleLocation] == handleLocation;
            fitxes[targetLocation] = fitxes.length - 1; // handle tile
            handleLocation = targetLocation;
        }
        return match;
    }

    public void Barrejar(){
        if (amplada < 2 || alçada < 2) {
            return;
        }

        int limit = amplada * alçada * Math.max(amplada, alçada);
        int move = 0;

        while(Distancia() < limit){
            move = fitxaBuida(movInvert(move));
            movFitxa(move, 1);
        }
    }

    public int getDirection(int location) {
        int delta = location - handleLocation;

        if (delta % amplada == 0) { return delta < 0 ? Adalt : Abaix; }
        else if (handleLocation / amplada == (handleLocation + delta) / amplada) {
            return delta < 0 ? Esquerra : Dreta;
        }
        else { return -1; }
    }

    public int getHandleLocation() {
        return handleLocation;
    }
}
