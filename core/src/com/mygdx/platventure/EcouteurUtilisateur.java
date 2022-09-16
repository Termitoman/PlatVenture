package com.mygdx.platventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class EcouteurUtilisateur implements InputProcessor {
    private final Vector2 mouvement;
    private boolean ajoutTimer = false;
    private boolean mettreEnPause = false;


    public EcouteurUtilisateur() {
        mouvement = new Vector2(0f, 0f);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {

            //Le personnage saute
            case Keys.W: //On prends en compte les claviers azerty et qwerty
            case Keys.DPAD_UP: //On prends en compte les flèches directionnelles
            case Keys.Z:
                mouvement.set(mouvement.x, 40f);
                break;

            //Le personnage se déplace vers la gauche
            case Keys.A: //On prends en compte les claviers azerty et qwerty
            case Keys.DPAD_LEFT: //On prends en compte les flèches directionnelles
            case Keys.Q:
                mouvement.set(-1f, mouvement.y);
                break;

            //Le personnage se déplace vers la droite
            case Keys.DPAD_RIGHT: //On prends en compte les flèches directionnelles
            case Keys.D:
                mouvement.set(1f, mouvement.y);
                break;

            //On quitte l'application quand on appuie sur echap
            case Keys.ESCAPE:
                Gdx.app.exit();
                break;

            //On remet du temps au timer quand on appuie sur t
            case Keys.T:
                ajoutTimer = true;
                break;

            //On met le jeu en pause quand on appuie sur p
            case Keys.P:
                mettreEnPause = !mettreEnPause;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {

            //Le personnage finit son saut
            case Keys.W: //On prends en compte les claviers azerty et qwerty
            case Keys.DPAD_UP: //On prends en compte les flèches directionnelles
            case Keys.Z:
                mouvement.set(mouvement.x, 0f);
                break;

            //Le personnage finit son déplacement horizontal
            case Keys.A: //On prends en compte les claviers azerty et qwerty
            case Keys.DPAD_LEFT: //On prends en compte les flèches directionnelles
            case Keys.Q:
            case Keys.DPAD_RIGHT: //On prends en compte les flèches directionnelles
            case Keys.D:
                mouvement.set(0f, mouvement.y);
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer == 1) {
            //Le personnage saute
            mouvement.set(0f, 40f);
        } else if (pointer == 0) {
            if (screenX <= Gdx.graphics.getWidth()/2) {
                //Le personnage se déplace vers la gauche
                mouvement.set(-1f, 0f);
            } else {
                //Le personnage se déplace vers la droite
                mouvement.set(1f, 0f);
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == 1) {
            //Le personnage finit de sauter
            mouvement.set(mouvement.x, 0f);
        } else if(pointer == 0) {
            //Le personnage finit de se déplacer
            mouvement.set(0f, mouvement.y);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public Vector2 getMouvement() {
        return mouvement;
    }

    public boolean isAjoutTimer() {
        return ajoutTimer;
    }

    public void setAjoutTimer(boolean ajoutTimer) {
        this.ajoutTimer = ajoutTimer;
    }

    public boolean isMettreEnPause() {
        return mettreEnPause;
    }
}
