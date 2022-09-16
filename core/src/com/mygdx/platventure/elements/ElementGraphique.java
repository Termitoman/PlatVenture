package com.mygdx.platventure.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class ElementGraphique {
    protected Vector2 pos;
    protected Body body;
    protected BodyDef bodyDef;
    protected Texture texture;

    public ElementGraphique(Vector2 vecteur, Texture texture) {
        this.pos = vecteur;
        this.texture = texture;
    }

    public abstract void setBodyDef();

    public abstract void setFixture();

    public void createBody(World world) {
        this.body = world.createBody(this.bodyDef);
        setUserData();
    }

    protected abstract void setUserData();

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public abstract void dispose();

    public Texture getTexture() {
        return texture;   
    }

    public abstract float getLargeur();

    public abstract float getHauteur();
}
