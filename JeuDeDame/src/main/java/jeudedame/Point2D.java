/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jeudedame;

import java.util.ArrayList;

/**
 * Abscisse x et ordonnée y d'un point dans un plan cartésien
 * @author vdufo
 */
public class Point2D {

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
     * 
     * @param a
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
     * Méthode qui vérifie si 2 points sont égaux
     * @param A
     * @return
     */
    public boolean equals(Point2D A){
        return A.getX()== this.getX() && A.getY()==this.getY();
    }
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        String aff = "["+x+";"+y+"]";
        return aff;
    }
    
    
    /**
     * Calcule la distance entre 2 points
     * @param p
     * @return
     */
    public double distance(Point2D p){
        return Math.sqrt(p.x*p.x+p.y*p.y);
    } 
}
