package com.mygdx.platventure;

import com.badlogic.gdx.physics.box2d.*;

public class EcouteurContacts implements ContactListener {

    private Body joyauSupprime;
    private boolean joyauTouche = false;
    private boolean persoEstMort = false;
    private boolean contactBriqueOuPlateforme = false;
    private boolean sortieDepassee = false;

    @Override
    public void beginContact(Contact contact) {
        //Si le contact est un contact avec le joueur (2 cas selon la position du joueur dans contact)
        if (contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.PERSONNAGE) {
            if (contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.JOYAU) { //Si le joueur à un contact avec un joyau, on détruit ce joyau
                joyauSupprime = contact.getFixtureB().getBody();
                joyauTouche = true;
            } else if (contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.EAU) { // Si le joueur à un contact avec de l'eau, on détruit le joueur
                persoEstMort = true;
            } else if (contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.BRIQUE || contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.PLATEFORME_BORD_DROIT || contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.PLATEFORME_BORD_GAUCHE || contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.PLATEFORME_CENTRALE) {
                if (contact.getFixtureA().getBody().getLinearVelocity().x > 3 || contact.getFixtureA().getBody().getLinearVelocity().y > 3 || contact.getFixtureA().getBody().getLinearVelocity().x < -3 || contact.getFixtureA().getBody().getLinearVelocity().y < -3)
                    contactBriqueOuPlateforme = true;
            }
        } else if(contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.PERSONNAGE) {
            if (contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.JOYAU) { //idem
                joyauSupprime = contact.getFixtureA().getBody();
                joyauTouche = true;
            } else if (contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.EAU) { //idem
                persoEstMort = true;
            } else if (contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.BRIQUE || contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.PLATEFORME_BORD_DROIT || contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.PLATEFORME_BORD_GAUCHE || contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.PLATEFORME_CENTRALE) {
                if (contact.getFixtureB().getBody().getLinearVelocity().x > 3 || contact.getFixtureB().getBody().getLinearVelocity().y > 3 || contact.getFixtureB().getBody().getLinearVelocity().x < -3 || contact.getFixtureB().getBody().getLinearVelocity().y < -3)
                    contactBriqueOuPlateforme = true;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.PERSONNAGE) {
            // Si le joueur à un contact avec la sortie, on détruit le joueur
            // Sinon, on reset la sortie pour pas qu'on puisse aller sur la sortie et repartir puis aller au prchain niveau en se jetant dans le vide
            sortieDepassee = contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.SORTIE;
        } else if(contact.getFixtureB().getBody().getUserData() == EnumDonneesBody.PERSONNAGE) {
            //idem
            //idem
            sortieDepassee = contact.getFixtureA().getBody().getUserData() == EnumDonneesBody.SORTIE;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public Body getJoyauSupprime() {
        return joyauSupprime;
    }

    public boolean isJoyauTouche() {
        return joyauTouche;
    }

    public boolean isPersoEstMort() {
        return persoEstMort;
    }

    public void setJoyauTouche(boolean joyauTouche) {
        this.joyauTouche = joyauTouche;
    }

    public boolean isSortieDepassee() {
        return sortieDepassee;
    }

    public void dispose() {
    }

    public boolean isContactBriqueOuPlateforme() {
        return contactBriqueOuPlateforme;
    }

    public void setContactBriqueOuPlateforme(boolean contactBriqueOuPlateforme) {
        this.contactBriqueOuPlateforme = contactBriqueOuPlateforme;
    }
}
