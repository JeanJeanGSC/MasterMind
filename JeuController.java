/*
AUTEUR: Jean COUTURIER
CRÉER LE:               DD/MM/YYYY
                        11/05/2024
DERNIÈRE MIS À JOUR:    
                        15/05/2024 by Jean COUTURIER
*/

package mastermind;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class JeuController implements Initializable {
    
    //Déclaration des deux tableaux de 32 pions avec leur index respectif
    static Circle[] listePion       = new Circle [32];
    static Circle[] listeClue       = new Circle [32];
    static int indexPion = 0, indexClue = 0;
    
    //Déclaration des deux tableau qui seron manipuler a chaque comparaison
    static Circle[] secretCombine   = new Circle [4];
    static Circle[] serieEnCour     = new Circle [4];
    
    //Variable pour setter une couleur en fonction d'un chiffre random
    static int color1, color2, color3, color4;
    
    //Message afficher en cas de victoire ou de défaite + animation de révélation de code secret
    Alert message = new Alert(Alert.AlertType.CONFIRMATION);
    static FadeTransition fadeOut;
    
    //Pane qui cache le code secret
    @FXML private Pane cacheCombine;
    @FXML private Circle
            //Pion pour chaque ligne d'essaie
            p11, p12, p13, p14,
            p21, p22, p23, p24,
            p31, p32, p33 ,p34,
            p41, p42, p43, p44,
            p51, p52, p53, p54,
            p61, p62, p63, p64,
            p71, p72, p73, p74,
            p81, p82, p83, p84,
            
            //Pion "Clue" pour chaque ligne d'essai
            c11, c12, c13, c14,
            c21, c22, c23, c24,
            c31, c32, c33, c34,
            c41, c42, c43, c44,
            c51, c52, c53, c54,
            c61, c62, c63, c64,
            c71, c72, c73, c74,
            c81, c82, c83, c84,
            
            //Combinaison secrète
            rep1, rep2, rep3, rep4;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //\/\/\/ décommenté ligne 71 pour voir la combinaison \/\/\/
        //cacheCombine.setStyle("-fx-background-color: #6A353460;");
        fadeOut = new FadeTransition(Duration.seconds(3), cacheCombine);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        instancier();
        assignerTab();
        secretCombine();
    }

    @FXML void clickBlue(MouseEvent event) {
        listePion[indexPion++].setFill(Color.web("#1711ad"));
        Comparaison();
    }

    @FXML void clickGreen(MouseEvent event) {
        listePion[indexPion++].setFill(Color.web("#328a10"));
        Comparaison();
    }

    @FXML void clickOrange(MouseEvent event) {
        listePion[indexPion++].setFill(Color.web("#ce732e"));
        Comparaison();
    }

    @FXML void clickRed(MouseEvent event) {
        listePion[indexPion++].setFill(Color.web("#c01215"));
        Comparaison();
    }

    @FXML void clickYellow(MouseEvent event) {
        listePion[indexPion++].setFill(Color.web("#efe62d"));
        Comparaison();
    }
    
    /**
     * Procédure de comparaison des couleur et position de chaque serie.
     */
    private void Comparaison(){
        //Déclaration des variable utile au comptage des indices de jeu
        int clueNoir = 0, clueBlanc = 0;
        //Condition pour débuter la procédure de comparaison (activé tous les 4 pion joué).
        if (indexPion%4==0){
            //Chargement de la derniere de 4 pion joué
            chargementColorSerie();
            
            //Comparaison COULEUR et POSITION
            for (int i = 0; i < serieEnCour.length; i++) {
                if (serieEnCour[i].getFill().equals(secretCombine[i].getFill())) {
                    clueNoir++;
                    secretCombine[i].setFill(Color.WHITE);
                    serieEnCour[i].setFill(Color.BLACK);
                }
            }
            //Comparaison COULEUR seulement
            if(clueNoir != 4){
                for (int i = 0; i < serieEnCour.length; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (serieEnCour[i].getFill().equals(secretCombine[j].getFill())) {
                            clueBlanc++;
                            secretCombine[j].setFill(Color.WHITE);
                            serieEnCour[i].setFill(Color.BLACK);
                            break;
                        }
                    }
                }
            }
            
            //On rempli le tableau d'indice avec autant de couleur que clueNoir ou clueBlanc compté
            for (int i = 0; i < clueNoir; i++) {
                listeClue[indexClue++].setFill(Color.BLACK);
            }
            for (int i = 0; i < clueBlanc; i++) {
                listeClue[indexClue++].setFill(Color.WHITE);
            }
            
            messageVictoire_Echec(clueNoir);
            
            //Incrémentation de l'index du tableau des pions Noir et Balnc
            //Pour concorder avec prochaine serie testé
            if(clueNoir == 0 && clueBlanc == 0){
                indexClue += 4;
            } else {
                while (indexClue % 4 != 0){
                    indexClue++;
                }
            }
            //On replace les couleur original du code secret pour la prochaine comparaison.
            chargementColorCombine();
        }
    }
    
    /**
     * Initialisation du code secret en fonction d'un chiffre random.
     */
    private void secretCombine(){
        Random r = new Random();
        color1 = r.nextInt(5); color2 = r.nextInt(5);
        color3 = r.nextInt(5); color4 = r.nextInt(5);
        rempliRep(color1, rep1);
        rempliRep(color2, rep2);
        rempliRep(color3, rep3);
        rempliRep(color4, rep4);
        
        chargementColorCombine();
    }
    
    /**
     * placement de couleur dans un tableau de Circle
     * pour la manipulation et la comparaison.
     */
    private void chargementColorCombine() {
        secretCombine[0].setFill(Color.web(rep1.getFill().toString()));
        secretCombine[1].setFill(Color.web(rep2.getFill().toString()));
        secretCombine[2].setFill(Color.web(rep3.getFill().toString()));
        secretCombine[3].setFill(Color.web(rep4.getFill().toString()));
    }
    /**
     * placement de couleur dans un tableau de Circle
     * pour la manipulation et la comparaison
     * en partant par le dernier pion saisi par l'utilisateur.
     */
    private void chargementColorSerie() {
        serieEnCour[3].setFill(Color.web(listePion[indexPion - 1].getFill().toString()));
        serieEnCour[2].setFill(Color.web(listePion[indexPion - 2].getFill().toString()));
        serieEnCour[1].setFill(Color.web(listePion[indexPion - 3].getFill().toString()));
        serieEnCour[0].setFill(Color.web(listePion[indexPion - 4].getFill().toString()));
    }
    
    /**
     * Procédure de remplissage de chaque pion du code secret
     * en fonctions d'un chiffre random entre 0 et 5(exclu).
     * @param color
     * @param rep 
     */
    private void rempliRep(int color, Circle rep) {
        switch (color) {
            case 0:
                rep.setFill(Color.web("#c01215"));
                break;
            case 1:
                rep.setFill(Color.web("#328a10"));
                break;
            case 2:
                rep.setFill(Color.web("#ce732e"));
                break;
            case 3:
                rep.setFill(Color.web("#1711ad"));
                break;
            case 4:
                rep.setFill(Color.web("#efe62d"));
                break;
            default:
                break;
        }
    }
    
    /**
     * Affichage d'un message type Alert sous certaine condition
     * Message de Victoire ou Game Over.
     * @param clueNoir 
     */
    private void messageVictoire_Echec(int clueNoir) {
        if(clueNoir == 4){
            fadeOut.play();
            message.setTitle("Victoire");
            message.setHeaderText("Bravo !!!");
            message.setContentText("Vous avez trouvé la bonne combinaison !!!");
            message.showAndWait();
            System.exit(0);
        }
        else if (indexPion == 32 && clueNoir != 4){
            fadeOut.play();
            message.setTitle("Echec");
            message.setHeaderText("GAME OVER");
            message.setContentText("Vous n'avez pas trouvé la bonne combinaison !!!");
            message.showAndWait();
            System.exit(0);
        }
    }
    
    /**
     * Assignation de chaque pion FXML a un index de tableau
     * -un tableau pour les 32 pion du joueur
     * -un tableau pour les 32 potentiel pion d'indice NOIR et BLANC.
     */
    private void assignerTab(){
        listePion[0] = p11; listePion[1] = p12; listePion[2] = p13; listePion[3] = p14;
        listePion[4] = p21; listePion[5] = p22; listePion[6] = p23; listePion[7] = p24;
        listePion[8] = p31; listePion[9] = p32; listePion[10] = p33; listePion[11] = p34;
        listePion[12] = p41; listePion[13] = p42; listePion[14] = p43; listePion[15] = p44;
        listePion[16] = p51; listePion[17] = p52; listePion[18] = p53; listePion[19] = p54;
        listePion[20] = p61; listePion[21] = p62; listePion[22] = p63; listePion[23] = p64;
        listePion[24] = p71; listePion[25] = p72; listePion[26] = p73; listePion[27] = p74;
        listePion[28] = p81; listePion[29] = p82; listePion[30] = p83; listePion[31] = p84;
        
        listeClue[0] = c11; listeClue[1] = c12; listeClue[2] = c13; listeClue[3] = c14;
        listeClue[4] = c21; listeClue[5] = c22; listeClue[6] = c23; listeClue[7] = c24;
        listeClue[8] = c31; listeClue[9] = c32; listeClue[10] = c33; listeClue[11] = c34;
        listeClue[12] = c41; listeClue[13] = c42; listeClue[14] = c43; listeClue[15] = c44;
        listeClue[16] = c51; listeClue[17] = c52; listeClue[18] = c53; listeClue[19] = c54;
        listeClue[20] = c61; listeClue[21] = c62; listeClue[22] = c63; listeClue[23] = c64;
        listeClue[24] = c71; listeClue[25] = c72; listeClue[26] = c73; listeClue[27] = c74;
        listeClue[28] = c81; listeClue[29] = c82; listeClue[30] = c83; listeClue[31] = c84;
    }
    
    /**
     * Procédure d'instanciation pour les deux tableaux
     * utilisé pour les comparaison de couleur.
     */
    private void instancier() {
        for (int i = 0; i < 4; i++) {
            serieEnCour[i] = new Circle();
            secretCombine[i] = new Circle();
        }
    }
}