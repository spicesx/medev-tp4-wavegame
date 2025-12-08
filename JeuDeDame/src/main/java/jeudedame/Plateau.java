/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jeudedame;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author rapha
 */
public class Plateau {
    public static final int Taille=10;
    private Piece[][] grille;
    
    
    
    public void init_plateau(){
        for(int i=0;i<Taille;i++){
            for(int j=0;j<Taille;j++){
                if((i+j)%2!=0){
                    if(i<4){
                        //grille[i][j]=new Piece(Point2D());
                    }
                    else if(i>5){
                        //grille[i][j]=new Piece();
                    }
                }
            }
        }
        
    }
    
    public void affiche(){
        for(int i=0; i<grille.length; i++){
            for(int j=0; j<grille[0].length ;j++){
                grille[i][j].toString();
               sdsgsdgsgsd
                       gfgfs
        }
            
        }
    }
    
}
