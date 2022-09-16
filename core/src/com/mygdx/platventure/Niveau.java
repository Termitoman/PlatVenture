package com.mygdx.platventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Niveau {
    private final int larg;
    private final int haut;
    private final int temps;
    private final char[][] tab_char;
    private final String nom_fond;

    public Niveau(String niveau) {
        FileHandle file = Gdx.files.internal(niveau);
        String[] tabLignes = file.readString().split("\n");
        String[] tabNombres = tabLignes[0].split(" ");
        larg = Integer.parseInt(tabNombres[0]);
        haut = Integer.parseInt(tabNombres[1]);
        temps = Integer.parseInt(tabNombres[2]);
        tab_char = new char[larg][haut];
        for(int i = 1; i< haut +1; i++){
            for(int j = 0; j< larg; j++){
                tab_char[j][i-1] = tabLignes[i].charAt(j);
            }
        }
        nom_fond = tabLignes[tabLignes.length-1];
    }

    public int getLarg() {
        return larg;
    }

    public int getHaut() {
        return haut;
    }

    public int getTemps() {
        return temps;
    }

    public char[][] getTab_char() {
        return tab_char;
    }

    public String getNom_fond() {
        return nom_fond;
    }

    public void dispose() {
    }
}