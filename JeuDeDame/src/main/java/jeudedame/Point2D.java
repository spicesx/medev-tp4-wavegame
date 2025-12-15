/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jeudedame;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abscisse x et ordonnée y d'un point dans un plan cartésien
 * @author vdufo
 */
public class Point2D implements Serializable {

    /**
     * Abscisse x
     */
    public int x;

    /**
     * Ordonnée y
     */
    public int y;  

    // Constructeurs et Méthodes
    /**
     * Constructeur d'un point à partir de x et y
     * @param x
     * @param y
     */
    public Point2D(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructeur d'un point à partir d'un autre point
     * @param p
     */
    public Point2D(Point2D p){
        this.x = p.x;
        this.y = p.y;
    }
    
    /**
     * Constructeur par défaut d'un point
     */
    public Point2D(){
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Setter x
     * @param x
     */
    public void setX(int x){
        this.x = x;
    }
    
    /**
     * Setter y
     * @param y
     */
    public void setY(int y){
        this.y = y;
    }
    
    /**
     * Getter x
     * @return
     */
    public int getX(){
        return this.x;
    }
    
    /**
     * Getter y 
     * @return
     */
    public int getY(){
        return this.y;
    }
    
    /**
     * * @param a
     * @param b
     */
    public void setPosition(int a, int b){
        x = a;
        y = b;
    }
    
    /**
     * Méthode qui déplace un point de x et y vers x+dx et y+dy
     * @param dx
     * @param dy
     */
    public void translate(int dx, int dy){
        x += dx;
        y += dy;
    }
    
    /**
     * Méthode standard pour vérifier l'égalité entre deux objets.
     * Corrige l'erreur Sonar "Override Object.equals".
     * @param obj L'objet à comparer
     * @return true si les coordonnées sont identiques
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Point2D other = (Point2D) obj;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Nécessaire quand on surcharge equals.
     * @return le hashcode du point
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return "[" + x + ";" + y + "]";
    }
    
    
    /**
     * Calcule la distance entre 2 points.
     * Corrige l'erreur Sonar sur le cast double.
     * @param p
     * @return
     */
    public double distance(Point2D p){
        if (p == null) {
            return -1;
        } else {
            // Conversion explicite en double pour éviter le calcul entier avant la puissance
            return Math.sqrt(Math.pow((double)this.x - p.x, 2) + Math.pow((double)p.y - this.y, 2));
        }
    } 
}
