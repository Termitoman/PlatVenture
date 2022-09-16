package com.mygdx.platventure.elements.elementsTraversables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.platventure.EnumDonneesBody;
import com.mygdx.platventure.elements.ElementGraphique;

public class Eau extends ElementGraphique {

    private final PolygonShape forme;


    public Eau(Vector2 vecteur, Texture texture) {
        super(vecteur, texture);

        //On définit la forme de l'eau par 4 points (vector2)
        forme = new PolygonShape();
        Vector2[] vecteurs = new Vector2[4];
        vecteurs[0] = new Vector2(0, 0);
        vecteurs[1] = new Vector2(0, 0.75f);
        vecteurs[2] = new Vector2(1, 0.75f);
        vecteurs[3] = new Vector2(1, 0);
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
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
        }
        forme.dispose();
    }

    @Override
    protected void setUserData() {
        body.setUserData(EnumDonneesBody.EAU);
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
