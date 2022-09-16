package com.mygdx.platventure.ecrans;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.platventure.PlatVenture;
import com.mygdx.platventure.ecrans.EcranJeu;

public class EcranPresentation extends ScreenAdapter {
    private final PlatVenture platVenture;
    private final Texture fond;

    public EcranPresentation(PlatVenture platVenture) {
        this.platVenture = platVenture;
        fond = new Texture("images/Intro.png");
    }

    @Override
    public void show() {
        //Lancement de la musique
        Music musique_intro = Gdx.audio.newMusic(Gdx.files.internal("sounds/win.ogg"));
        musique_intro.play();

        //Lancement du timer pour l'écran de jeu
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                platVenture.setScreen(new EcranJeu(platVenture));
            }
        }, 3);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,0);
        platVenture.getBatch().begin();
        platVenture.getBatch().draw(fond, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        platVenture.getBatch().end();
    }

    @Override
    public void hide() {
        super.hide();
        fond.dispose();
    }
}
