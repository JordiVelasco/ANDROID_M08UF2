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

    private int[] fiches;
    private int handleLocation;
    private Random random = new Random();
    private int width;
    private int height;

    public void init(int width, int height) {
        this.width = width;
        this.height = height;
        fiches = new int[width * height];

        for(int i = 0; i < fiches.length; i++)
        {
            fiches[i] = i;
        }

        handleLocation = fiches.length - 1;
    }

    public void setFiches(int[] fiches) {
        this.fiches = fiches;
        for (int i = 0; i < fiches.length; i++){
            if (fiches[i] == fiches.length - 1){ handleLocation = i;
                break;
            }
        }
    }

    public int[] getFiches() { return fiches; }

    public int getColumna(int location) { return location % width; }

    public int getFila(int location) { return location / width; }

    public int getAmplada() { return width; }

    public int getAlçada() { return height; }

    public int Distancia() {
        int dist = 0;

        for(int i = 0; i < fiches.length; i++) {
            dist += Math.abs(i - fiches[i]);
        }

        return dist;
    }
    public int getPosMov() {
        int x = getColumna(handleLocation);
        int y = getFila(handleLocation);

        boolean left = x > 0;
        boolean right = x < width - 1;
        boolean up = y > 0;
        boolean down = y < height - 1;

        return  (left ? 1 << Esquerra : 0) |
                (right ? 1 << Dreta: 0) |
                (up ? 1 << Adalt: 0) |
                (down ? 1 << Abaix: 0);
    }

    private int fichaBuida(int exclude) {
        List<Integer> moviments = new ArrayList<Integer>(4);
        int posMovs = getPosMov() & ~exclude;

        if((posMovs & (1 << Esquerra)) > 0) { moviments.add(Esquerra); }
        if((posMovs & (1 << Adalt)) > 0) { moviments.add(Adalt); }
        if((posMovs & (1 << Dreta)) > 0) { moviments.add(Dreta); }
        if((posMovs & (1 << Abaix)) > 0) { moviments.add(Abaix); }

        return moviments.get(random.nextInt(moviments.size()));
    }

    private int invertMove(int move) {
        if(move == 0) { return 0; }
        if(move == 1 << Esquerra) { return 1 << Dreta; }
        if(move == 1 << Adalt) { return 1 << Abaix; }
        if(move == 1 << Dreta) { return 1 << Esquerra; }
        if(move == 1 << Abaix) { return 1 << Adalt; }

        return 0;
    }
}
