package com.mygdx.platventure.ecrans;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.platventure.*;
import com.mygdx.platventure.elements.ElementGraphique;
import com.mygdx.platventure.elements.Personnage;
import com.mygdx.platventure.elements.elementsTraversables.Joyau;

public class EcranJeu extends ScreenAdapter {
    private final PlatVenture platVenture;
    private final Texture fond;
    private final OrthographicCamera camera;
    private final OrthographicCamera cameraTexte;
    private final FitViewport vp;
    private final Monde monde;
    //private Box2DDebugRenderer debugRenderer;
    private final EcouteurUtilisateur ecouteurUtilisateur = new EcouteurUtilisateur();
    private BitmapFont font;
    private int indexAnimationJoyau = 0;
    private float tempsDepuisDerniereAnimationJoyau = 0;



    public EcranJeu(PlatVenture platVenture) {
        this.platVenture = platVenture;

        //Initialisation du monde
        monde = new Monde();

        //Mode débuggage
        //debugRenderer = new Box2DDebugRenderer();

        fond = new Texture("images/" + monde.getNiveau().getNom_fond() + "");
        //Initialisation de la caméra
        camera = new OrthographicCamera();
        vp = new FitViewport(16f, (16f * Gdx.graphics.getHeight()) / Gdx.graphics.getWidth(), camera);
        vp.apply();

        cameraTexte = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        chargerFont();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.ecouteurUtilisateur);
    }

    @Override
    public void render(float delta) {
        //On gère la pause
        monde.changerPause(ecouteurUtilisateur.isMettreEnPause());
        if (!ecouteurUtilisateur.isMettreEnPause()) {
            //On clear l'écran
            ScreenUtils.clear(0, 0, 0, 0);

            if (!monde.isGagne() && !monde.isPerdu()) {
                //Si le temps doit s'incrémenter
                if (ecouteurUtilisateur.isAjoutTimer()) {
                    monde.ajoutTimer();
                    ecouteurUtilisateur.setAjoutTimer(false);
                }

                monde.getPersonnage().appliquerMouvement(ecouteurUtilisateur.getMouvement());

                //On applique les changements dans le monde
                monde.update();

                //Définition du step du monde
                monde.getWorld().step(Gdx.graphics.getDeltaTime(), 6, 2);

                //On mets à jour la camera
                if (monde.isNiveau3())
                    ajusterCameraNiveau3();
                else
                    ajusterCameraSurPersonnage();

            }
            camera.update();
            platVenture.getBatch().setProjectionMatrix(camera.combined);

            //On affiche à nouveau les objets
            platVenture.getBatch().begin();
            platVenture.getBatch().draw(fond, 0, 0, monde.getNiveau().getLarg(), monde.getNiveau().getHaut());
            for (ElementGraphique e : monde.getElements())
                if (e != null) {
                    if (e.getBody().getUserData() == EnumDonneesBody.PERSONNAGE) {
                        //Ensuite on regarde si on affiche le personnage vers la droite ou la gauche
                        if (e.getBody().getLinearVelocity().x < 0) //Si le personnage va vers la gauche
                            platVenture.getBatch().draw(e.getTexture(), e.getPos().x + 0.25f + e.getLargeur(), e.getPos().y, -e.getLargeur(), e.getHauteur());
                        else //Si le personnage va vers la droite ou ne bouge pas
                            platVenture.getBatch().draw(e.getTexture(), e.getPos().x + 0.25f, e.getPos().y, e.getLargeur(), e.getHauteur());
                    } else if (e.getBody().getUserData() == EnumDonneesBody.JOYAU) {
                        tempsDepuisDerniereAnimationJoyau += Gdx.graphics.getDeltaTime();
                        if (tempsDepuisDerniereAnimationJoyau >= 1.5) {
                            if (indexAnimationJoyau < 5)
                                indexAnimationJoyau++;
                            else
                                indexAnimationJoyau = 0;
                            tempsDepuisDerniereAnimationJoyau = 0;
                        }
                        TextureRegion textureRegion = new TextureRegion((TextureRegion) (((Joyau) e).getAnimation().getKeyFrame(indexAnimationJoyau, true)));
                        platVenture.getBatch().draw(textureRegion, e.getPos().x + 0.25f, e.getPos().y + 0.25f, 0.5f, 0.5f);
                    } else if (e.getBody().getUserData() == EnumDonneesBody.SORTIE && e.getPos().x < 2)
                        platVenture.getBatch().draw(e.getTexture(), e.getPos().x + e.getLargeur(), e.getPos().y - 0.25f, -e.getLargeur(), e.getHauteur() + 0.25f);
                    else
                        platVenture.getBatch().draw(e.getTexture(), e.getPos().x, e.getPos().y, e.getLargeur(), e.getHauteur());
                }
            //debugRenderer.render(monde.getWorld(), camera.combined);
            platVenture.getBatch().end();

            platVenture.getBatch().setProjectionMatrix(cameraTexte.combined);
            platVenture.getBatch().begin();
            //On affiche le texte sur la camera
            float splitEcran = cameraTexte.viewportWidth / 90;
            font.draw(platVenture.getBatch(), "Score : " + monde.getScore_joueur(), camera.position.x + cameraTexte.viewportWidth / 2 - 7 - (int) (splitEcran * monde.getScore_joueur() / 10f), camera.position.y + cameraTexte.viewportHeight / 2 - 7, 0, 0, false);
            font.draw(platVenture.getBatch(), "" + monde.getTimer_limite(), camera.position.x, camera.position.y + cameraTexte.viewportHeight / 2 - 7, 0, 0, false);
            if (monde.isGagne()) {
                font.draw(platVenture.getBatch(), "Bravo :-)", camera.position.x + (9 * splitEcran), camera.position.y - camera.viewportWidth / 2, 0, 0, false);
            } else if (monde.isPerdu()) {
                font.draw(platVenture.getBatch(), "Dommage :-/", camera.position.x + (11 * splitEcran), camera.position.y - camera.viewportWidth / 2, 0, 0, false);
            }
            platVenture.getBatch().end();
        }
    }

    @Override
    public void resize(int width, int height) {
        //Création de la camera
        vp.update(width, height);
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
        camera.update();
        platVenture.getBatch().setProjectionMatrix(camera.combined);
    }

    public void ajusterCameraSurPersonnage() {
        float pos_joueur_x = monde.getPersonnage().getPos().x;
        float pos_joueur_y = monde.getPersonnage().getPos().y;

        if (monde.getPersonnage().getPos().epsilonEquals(monde.getPosPersoInit()))
            camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        else {
            if (((pos_joueur_x < camera.position.x) && (camera.position.x - camera.viewportWidth / 2 > 0)) || ((pos_joueur_x > camera.position.x) && (camera.position.x + camera.viewportWidth / 2 < monde.getNiveau().getLarg()))) {
                camera.position.set(pos_joueur_x, camera.position.y, 0);
            }
            if (((pos_joueur_y < camera.position.y) && (camera.position.y - camera.viewportHeight / 2 > 0)) || ((pos_joueur_y > camera.position.y) && (camera.position.y + camera.viewportHeight / 2 < monde.getNiveau().getHaut()))) {
                camera.position.set(camera.position.x, pos_joueur_y, 0);
            }
        }
    }

    public void ajusterCameraNiveau3() {
        camera.position.set(monde.getNiveau().getLarg()/2f, monde.getNiveau().getHaut()/2f, 0);
    }

    private void chargerFont() {
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comic_Sans_MS_Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        freeTypeFontParameter.size = (int) ((60f * Gdx.graphics.getWidth()) / 1024f);
        freeTypeFontParameter.color = Color.YELLOW;
        freeTypeFontParameter.borderColor= Color.BLACK;
        freeTypeFontParameter.borderWidth = ((3f * Gdx.graphics.getWidth()) / 1024f);

        font = freeTypeFontGenerator.generateFont(freeTypeFontParameter);

        freeTypeFontGenerator.dispose();
    }
}
