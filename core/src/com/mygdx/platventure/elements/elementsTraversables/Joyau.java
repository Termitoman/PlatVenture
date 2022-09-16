package com.mygdx.platventure.elements.elementsTraversables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.platventure.EnumDonneesBody;
import com.mygdx.platventure.elements.ElementGraphique;

public class Joyau extends ElementGraphique {

    private final CircleShape forme;
    private final int num_joyau;
    private final Animation animation;


    public Joyau(Vector2 vecteur, Texture texture, int num_joyau) {
        super(vecteur, texture);
        this.num_joyau = num_joyau;

        //On définit la forme du joyau par son centre et son rayon (point = vector2)
        forme = new CircleShape();
        this.forme.setPosition(new Vector2(0.5f,0.5f));
        this.forme.setRadius(0.25f);

        //On découpe le sprite pour l'animation
        TextureRegion[][] textureRegions = TextureRegion.split(texture, 56, 56);
        TextureRegion[] sousRegion = new TextureRegion[textureRegions.length];
        for (int i = 0; i < textureRegions.length; i++)
            sousRegion[i] = textureRegions[i][0];
        animation = new Animation<>(0.2f, sousRegion);
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
        body.setUserData(EnumDonneesBody.JOYAU);
    }

    public int getScore() {
        return num_joyau;
    }

    @Override
    public float getLargeur() {
        return 0.5f;
    }

    @Override
    public float getHauteur() {
        return 0.5f;
    }

    @Override
    public void dispose() {
    }

    public Animation getAnimation() {
        return animation;
    }

}
