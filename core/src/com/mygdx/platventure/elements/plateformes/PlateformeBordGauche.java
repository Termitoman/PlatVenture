package com.mygdx.platventure.elements.plateformes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.platventure.EnumDonneesBody;
import com.mygdx.platventure.elements.ElementGraphique;

public class PlateformeBordGauche extends ElementGraphique {

    private final PolygonShape forme;
    private final float densite;
    private final float restitution;
    private final float friction;


    public PlateformeBordGauche(Vector2 vecteur, Texture texture) {
        super(vecteur, texture);
        //On initialise les attributs de plateforme comme donné dans le sujet
        densite = 1f;
        restitution = 0.1f;
        friction = 0.25f;

        //On définit la forme de la plateforme par 5 points (vector2)
        forme = new PolygonShape();
        Vector2[] vecteurs = new Vector2[5];
        vecteurs[0] = new Vector2(0, 0.75f/2f);
        vecteurs[1] = new Vector2(0, 0.75f);
        vecteurs[2] = new Vector2(1, 0.75f);
        vecteurs[3] = new Vector2(1, 0);
        vecteurs[4] = new Vector2(0.5f, 0);
        this.forme.set(vecteurs);
    }

    @Override
    public void setBodyDef() {
        //On définit le type de l'objet et sa position à travers un BodyDef
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(pos);
    }

    @Override
    public void setFixture() {
        //On attribue les caractéristiques physiques de l'objet à son Body à travers un FixtureDef
        if (bodyDef != null && body != null) {
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = forme;
            fixtureDef.density = densite;
            fixtureDef.restitution = restitution;
            fixtureDef.friction = friction;
            body.createFixture(fixtureDef);
        }
        forme.dispose();
    }

    @Override
    protected void setUserData() {
        body.setUserData(EnumDonneesBody.PLATEFORME_BORD_GAUCHE);
    }

    @Override
    public float getLargeur() {
        return 1;
    }

    @Override
    public float getHauteur() {
        return 0.75f;
    }

    @Override
    public void dispose() {
    }
}
