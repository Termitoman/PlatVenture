package com.mygdx.platventure.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.platventure.EnumDonneesBody;

public class Personnage extends ElementGraphique {

    private final PolygonShape formeHaut;
    private final CircleShape formeBas;
    private final float densite;
    private final float restitution;
    private final float friction;
    private boolean saute = false;


    public Personnage(Vector2 vecteur, Texture texture) {
        super(vecteur, texture);
        //On initialise les attributs du joueur comme donné dans le sujet
        densite = 0.5f;
        restitution = 0.1f;
        friction = 0.5f;

        //On définit la forme du haut du joueur par 4 points (vector2) en utilisant la hauteur relative à la largeur et aux dimensions de la texture (0.5*500)/290 = 0.8620689655172 .
        formeHaut = new PolygonShape();
        Vector2[] vecteurs = new Vector2[4];
        vecteurs[0] = new Vector2(0.25f, 0.4310344827586f);
        vecteurs[1] = new Vector2(0.5f, 0.8620689655172f);
        vecteurs[2] = new Vector2(0.75f, 0.4310344827586f);
        vecteurs[3] = new Vector2(0.50f, 0.2155172413793f);
        this.formeHaut.set(vecteurs);

        //On définit la forme du bas du joueur par son centre et son rayon (vector2)
        formeBas = new CircleShape();
        formeBas.setPosition(new Vector2(0.50f, 0.1077586206896f));
        formeBas.setRadius(0.1077586206896f);
    }

    @Override
    public void setBodyDef() {
        //On définit le type de l'objet et sa position à travers un BodyDef
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(pos);
    }

    @Override
    public void setFixture() {
        //On attribue les caractéristiques physiques de l'objet à son Body à travers un FixtureDef
        if (bodyDef != null && body != null) {
            //On crée la texte du haut du personnage
            FixtureDef fixtureDefHaut = new FixtureDef();
            fixtureDefHaut.shape = formeHaut;
            fixtureDefHaut.density = densite;
            fixtureDefHaut.restitution = restitution;
            fixtureDefHaut.friction = friction;
            body.createFixture(fixtureDefHaut);
            //On crée la fixture du bas du personnage
            FixtureDef fixtureDefBas = new FixtureDef();
            fixtureDefBas.shape = formeBas;
            fixtureDefBas.density = densite;
            fixtureDefBas.restitution = restitution;
            fixtureDefBas.friction = friction;
            body.createFixture(fixtureDefBas);
        }
        formeHaut.dispose();
        formeBas.dispose();
    }

    @Override
    protected void setUserData() {
        body.setUserData(EnumDonneesBody.PERSONNAGE);
    }

    public void appliquerMouvement(Vector2 mouvement) {

        //On applique la bonne texture sur le personnage selon son mouvement
        if (saute) //Si le personnage est en plein saut
            setTextureSaut();
        else if (mouvement.x != 0) //Si le personnage se déplace
            setTextureCourse();
        else
            setTextureIdle();

        //On empèche le joueur de sauter et bouger en plein saut
        if (getBody().getLinearVelocity().y < 0.00001 && getBody().getLinearVelocity().y > -0.00001) {
            getBody().applyForceToCenter(mouvement, true);
            saute = mouvement.y != 0; //Si le joueur saute on lance la texture du saut
        }
    }

    @Override
    public float getLargeur() {
        return 0.5f;
    }

    @Override
    public float getHauteur() {
        return 0.8620689655172f;
    }

    @Override
    public void dispose() {

    }

    public void setTextureCourse() {
        texture = new Texture("images/Run__003.png");
    }

    public void setTextureSaut() {
        texture = new Texture("images/Jump__002.png");
    }

    public void setTextureIdle() {
        texture = new Texture("images/Idle__000.png");
    }
}
