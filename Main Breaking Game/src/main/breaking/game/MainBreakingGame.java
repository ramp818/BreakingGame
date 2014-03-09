/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.breaking.game;

import javax.swing.JFrame;

/**
 *
 * @author Pache
 */
public class MainBreakingGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BreakingGame juego = new BreakingGame();
    	juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	juego.setVisible(true);
    }
    
}
