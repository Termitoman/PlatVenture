package com.mygdx.platventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.platventure.elements.*;
import com.mygdx.platventure.elements.elementsTraversables.Eau;
import com.mygdx.platventure.elements.elementsTraversables.Joyau;
import com.mygdx.platventure.elements.elementsTraversables.Sortie;
import com.mygdx.platventure.elements.plateformes.PlateformeBordDroit;
import com.mygdx.platventure.elements.plateformes.PlateformeBordGauche;
import com.mygdx.platventure.elements.plateformes.PlateformeCentrale;

import java.util.ArrayList;

public class Monde {
    private ArrayList<ElementGraphique> elements;
    private World world;
    private Personnage personnage;
    private EcouteurContacts ecouteurContacts;
    private int score_joueur;
    private int largeur_niveau;
    private int hauteur_niveau;
    private Niveau niveau;
    private Timer timer;
    private int numNiveau;
    private final int[] timer_limite = new int[1];
    private final boolean[] pause = new boolean[1];
    private Vector2 posPersoInit;
    private boolean gagne;
    private boolean perdu;
    private Timer timerAlerte;
    private boolean niveau3 = false;
    private final Sound son_joyau = Gdx.audio.newSound(Gdx.files.internal("sounds/gem.ogg"));
    private final Sound son_eau = Gdx.audio.newSound(Gdx.files.internal("sounds/plouf.ogg"));
    private final Sound son_danger = Gdx.audio.newSound(Gdx.files.internal("sounds/alert.ogg"));
    private final Sound son_collision = Gdx.audio.newSound(Gdx.files.internal("sounds/collision.ogg"));
    private final Sound son_perdu = Gdx.audio.newSound(Gdx.files.internal("sounds/loose.ogg"));
    private final Sound son_gagne = Gdx.audio.newSound(Gdx.files.internal("sounds/win.ogg"));


    public Monde() {
        numNiveau = 1;
        score_joueur = 0;
        creerMonde();
    }

    private void creerMonde() {
        //Initialisation du niveau
        niveau = new Niveau("levels/level_00" + numNiveau + ".txt");

        gagne = false;
        perdu = false;

        this.largeur_niveau = niveau.getLarg();
        this.hauteur_niveau = niveau.getHaut();
        char[][] representationNiveau = niveau.getTab_char();

        world = new World(new Vector2(0, -10f), true);
        elements = new ArrayList<>();
        for (int i = 0; i < representationNiveau.length; i++)
            for (int j = 0; j < representationNiveau[i].length; j++)
                elements.add(creerElementDansMonde(representationNiveau[i][j], i, j, representationNiveau[i].length));


        ecouteurContacts = new EcouteurContacts();
        world.setContactListener(this.ecouteurContacts);

        //On démarre le timer du niveau
        timer_limite[0] = niveau.getTemps(); //On utilise un tableau pour pouvoir initialiser la variable en dehors de la Task mais quand même pouvoir changer sa valeur à l'intérieur
        pause[0] = false; //idem
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!pause[0])
                    timer_limite[0]--;
            }
        }, 0, 1);

        //On initialise le timer de l'alerte
        timerAlerte = new Timer();

        //On précise si on est au niveau 3 pour gérer la caméra
        niveau3 = numNiveau == 3;
    }

    //Procédure crée pour alléger le constructeur
    private ElementGraphique creerElementDansMonde(char elem, int i, int j, int secondTabSize) {
        ElementGraphique elementResultat = null;
        switch (elem) {
            case '1':
                elementResultat = new Joyau(new Vector2(i, secondTabSize - j -1), new Texture("images/Gem_1.png"), 1); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                break;
            case '2':
                elementResultat = new Joyau(new Vector2(i, secondTabSize - j -1), new Texture("images/Gem_2.png") , 2); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                break;
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
                elementResultat = new Brique(new Vector2(i, secondTabSize - j -1), new Texture("images/Brick_" + elem + ".png")); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                break;
            case 'J':
                elementResultat = new PlateformeBordGauche(new Vector2(i, secondTabSize - j - 1), new Texture("images/Platform_J.png")); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                break;
            case 'K':
                elementResultat = new PlateformeCentrale(new Vector2(i, secondTabSize - j - 1), new Texture("images/Platform_K.png")); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                break;
            case 'L' :
                elementResultat = new PlateformeBordDroit(new Vector2(i, secondTabSize - j - 1), new Texture("images/Platform_L.png")); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                break;
            case 'W' :
                elementResultat = new Eau(new Vector2(i, secondTabSize - j - 1), new Texture("images/Water.png")); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                break;
            case 'Z' :
                elementResultat = new Sortie(new Vector2(i, secondTabSize - j - 1), new Texture("images/Exit_Z.png")); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                break;
            case 'P' :
                elementResultat = new Personnage(new Vector2(i, secondTabSize - j - 1), new Texture("images/Idle__000.png")); //On positionne l'élément selon le positionnement dans le tableau de caractères et le positionnement voulu dans le monde
                personnage = (Personnage) elementResultat;
                posPersoInit = personnage.getPos();
                break;
        }
        if (elementResultat != null) {
            elementResultat.setBodyDef();
            elementResultat.createBody(world);
            elementResultat.setFixture();
        }
        return elementResultat;
    }

    public World getWorld() {
        return world;
    }

    public Personnage getPersonnage() {
        return personnage;
    }

    public void update() {
        // On met à jour la position des éléments dans le monde
        for (ElementGraphique e : elements)
            if (e != null)
                e.setPos(e.getBody().getPosition());

        //Si le joueur à touché un joyau
        if (ecouteurContacts.isJoyauTouche()) {
            //Recherche de l'élément
            ElementGraphique elemTemp = null;
            for (ElementGraphique e :elements)
                if (e != null && ecouteurContacts.getJoyauSupprime() == e.getBody())
                    elemTemp = e;

            //Ajout du score du joyau au joueur
            Joyau joyauTemp = (Joyau) elemTemp;
            assert joyauTemp != null;
            score_joueur += joyauTemp.getScore();
            son_joyau.play();

            //Suppression de l'élément de la liste
            elements.remove(elemTemp);
            //On supprime définitivement l'élément
            world.destroyBody(elemTemp.getBody());

            ecouteurContacts.setJoyauTouche(false);

        } else if (ecouteurContacts.isPersoEstMort()) { // Si le joueur à touché de l'eau
            timerAlerte.clear();
            son_eau.play();
            perdu = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    relancerMonde(1.5f);
                }
            }, 0.5f);
        } else if ((personnage.getBody().getPosition().x < -1) || (personnage.getBody().getPosition().x > largeur_niveau)) {
            if (ecouteurContacts.isSortieDepassee()) {
                lancerProchainMonde();
            } else {
                relancerMonde(2);
            }
        }

        //Si le timer est terminé, le joueur à perdu
        if (timer_limite[0] < 0) {
            relancerMonde(2);
        }

        //Si le personnage touche une brique ou une plateforme
        if (ecouteurContacts.isContactBriqueOuPlateforme()) {
            son_collision.play();
            ecouteurContacts.setContactBriqueOuPlateforme(false);
        }

        //Si le temps restant est critique et que l'on a pas déjà lancé l'alerte
        if (timer_limite[0] <= niveau.getTemps()/5 && timerAlerte.isEmpty()) {
            timerAlerte.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    son_danger.play();
                }
            }, 0, 1);
        }
    }

    private void relancerMonde(float temps_attente) {
        timerAlerte.clear();
        son_perdu.play();
        perdu = true;
        score_joueur = 0;
        timer.clear();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                creerMonde();
            }
        }, temps_attente);
    }

    private  void lancerProchainMonde() {
        timerAlerte.clear();
        son_gagne.play();
        gagne = true;
        if (numNiveau < 3)
            numNiveau++;
        else
            numNiveau = 1;
        timer.clear();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                creerMonde();
            }
        }, 2);
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void dispose() {
        for (ElementGraphique e : elements)
            if (e != null)
                e.dispose();
        elements.clear();
        elements = null;
        world.dispose();
        world = null;
        personnage.dispose();
        personnage = null;
        ecouteurContacts.dispose();
        ecouteurContacts = null;
        largeur_niveau = 0;
        hauteur_niveau = 0;
        niveau.dispose();
        niveau = null;
        timer.clear();
        timer = null;
        timer_limite[0] = 0;
        timerAlerte.clear();
    }

    public Vector2 getPosPersoInit() {
        return posPersoInit;
    }

    public int getScore_joueur() {
        return score_joueur;
    }

    public int getTimer_limite() {
        return timer_limite[0];
    }

    public boolean isGagne() {
        return gagne;
    }

    public boolean isPerdu() {
        return perdu;
    }

    public ArrayList<ElementGraphique> getElements() {
        return elements;
    }

    public void ajoutTimer() {
        timer_limite[0] += 10;
    }

    public void changerPause(boolean pause) {
        this.pause[0] = pause;
    }

    public boolean isNiveau3() {
        return niveau3;
    }
}
