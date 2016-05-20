/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Usuario9
 */
public class Tablero extends JPanel implements ActionListener{
    private Timer timer; 
    private ArrayList<Circulo> circulo;
    private Carro personajePrincipal1, personajePrincipal2;
    private  ServerSocket serverSocket ;
    private Socket cliente1, cliente2 ;
    
    private int puntaje = 0;
    public Tablero(){
        try {
            this.personajePrincipal1 = new Carro(100,200);
            this.personajePrincipal2 = new Carro(100,200);
            this.serverSocket = new ServerSocket(8000);
            this.circulo = new ArrayList<Circulo>();
            this.circulo.add(new Circulo(20,20));
            this.circulo.add(new Circulo(100,80));
            this.circulo.add(new Circulo(80,120));
            
            cliente1 = this.serverSocket.accept();
            cliente2 = this.serverSocket.accept();
            System.out.println("Acepte dos clientes");
            Thread proceso1 = new Thread(new Hilo(cliente1, cliente2, personajePrincipal1));
            proceso1.start();
            Thread proceso2 = new Thread(new Hilo(cliente2, cliente1, personajePrincipal2));
            proceso2.start();
            this.timer = new Timer(50, this);
            this.timer.start();
        } catch (IOException ex) {
            Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    protected void paintComponent(Graphics g) {
        System.out.println("DibujANDO...");
         super.paintComponent(g);
         for(Circulo c: this.circulo)
            c.dibujar(g,this);
         
         this.personajePrincipal1.dibujar(g,this);
         g.drawString("Puntaje " + puntaje, 40, 40);
         
         this.personajePrincipal2.dibujar(g,this);
         g.drawString("Puntaje " + puntaje, 40, 60);
         
         
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        validarColisiones(personajePrincipal1);
        //System.out.println("nulo: "+(personajePrincipal2==null));
        validarColisiones(personajePrincipal2);
         for(Circulo c: this.circulo)
            c.mover();
            repaint();
        
    }
     
    
    public void validarColisiones(Carro personaje){
        Rectangle recPersonaje = personaje.obtenerRectangulo();
        ArrayList<Circulo> copia = (ArrayList<Circulo>) this.circulo.clone();
        for(Circulo c : circulo){
           Rectangle RecCir = c.obtenerRectangulo();
           if(recPersonaje.intersects(RecCir)){
               copia.remove(c);
               this.puntaje++;
           }
           this.circulo=copia;   
           
        }
    }

}
