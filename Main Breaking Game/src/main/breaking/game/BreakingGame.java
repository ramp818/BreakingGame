package main.breaking.game;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;

public class BreakingGame extends JFrame implements Runnable, KeyListener
{
    
        private static final long serialVersionUID = 1L;
	// Se declaran las variables.
	private int direccion;    // Direccion
        private int posXPelota;
        private int posYPelota;
        private int puntos;
	private final int MIN = 3;    //Minimo al generar un numero al azar.
	private final int MAX = 6;    //Maximo al generar un numero al azar.
	private Image dbImage;	// Imagen a proyectar
        private Image gameover;
	private Graphics dbg;	// Objeto grafico
	//private SoundClip explosion;    // Objeto AudioClip
        //private SoundClip beep;
	private Pelota pelota;    // Objeto de la clase Lanzado
	private Barra policia; //Objeto de la clase Atrapador
        private Ladrillo bloque;
	private boolean gameOver;
        private boolean colision;
        private boolean pausa;
        private boolean inicio;
        private boolean movido;
        private int velocidad;
        private int gravedad;
        private int pelotaX;
        private int pelotaY;
        private boolean click;
        private boolean sonido;
        private boolean movimiento;
        private Animacion animFB;
        private Animacion animBM;
        private Animacion animBloque1;
        private Animacion animBloque2;
        private Animacion animBloque3;
        private Animacion animBarra;
        private long tiempoActual;
	private long tiempoInicial;
        private int ultDireccion;
        private int vidas;
        private int move=0;
        private int bolaPerdida;
        private String nombreArchivo;
        private LinkedList bloques;
        private boolean coliX=false;
        private boolean coliY=false;
        private Image fondo1;
        private Image fondo2;
        private Image fondo3;
        private Image fondo4;
        private Image fondoGO;
        private int score;
        
        public BreakingGame(){
            
                direccion=0;
                vidas=5;
                bolaPerdida=0;
                pausa=false;
                movimiento=false;
                inicio=false;
                gameOver=false;
                nombreArchivo="Juego.txt";
                posXPelota=50;
                posYPelota=450;
                score=0;

                sonido=true;
                movido=false;
                bloques=new LinkedList();
                
                fondo1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/desierto.jpg"));
                fondo2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/PH.jpg"));
                fondo3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/VP.jpg"));
                fondo4 =  Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/heisenberg.jpg"));
                fondoGO = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/intro.png"));
                
                Image fb1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/fb1.gif"));
                Image fb2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/fb2.gif"));
                Image fb3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/fb3.gif"));
                Image fb4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/fb4.gif"));
           
                animFB= new Animacion();
                animFB.sumaCuadro(fb1,100);
                animFB.sumaCuadro(fb2,100);
                animFB.sumaCuadro(fb3,100);
                animFB.sumaCuadro(fb4,100);
                
                pelota= new Pelota(550,550,animFB);
                
                Image bm1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/bm1.gif"));
                Image bm2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/bm2.gif"));
                Image bm3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/bm3.gif"));
                Image bm4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/bm4.gif"));
                
                animBM= new Animacion();
                animBM.sumaCuadro(bm1,100);
                animBM.sumaCuadro(bm2,100);
                animBM.sumaCuadro(bm3,100);
                animBM.sumaCuadro(bm4,100);
                
                Image Barra1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/barra.png"));
                
                animBarra=new Animacion();
                animBarra.sumaCuadro(Barra1,100);
                policia=new Barra(100,550,animBarra);
                
                //beep = new SoundClip("sonidos/beep.wav");
                //explosion = new SoundClip("sonidos/explosion.wav");
                Image Bloques1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/RV.png"));
                Image Bloques2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/PHlogo.png"));
                Image Bloques3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/VPlogo.png"));
                animBloque1= new Animacion();
                animBloque1.sumaCuadro(Bloques1,100);
                animBloque2=new Animacion();
                animBloque2.sumaCuadro(Bloques2,100);
                animBloque3=new Animacion();
                animBloque3.sumaCuadro(Bloques3,100);
                
                for (int i = 150; i < 336; i += 59){
                    for (int j = 15; j < 1130; j += 150){
                        if(i==150){
                            bloques.add(new Ladrillo(j, i, animBM));
                            bloques.add(new Ladrillo(j+40, i, animBM));
                            bloques.add(new Ladrillo(j+80, i, animBM));
                        }else if(i==209){
                            bloques.add(new Ladrillo(j, i, animBloque3));
                        }else if(i==268){
                            bloques.add(new Ladrillo(j, i, animBloque2));
                        }else if (i==327){
                            bloques.add(new Ladrillo(j, i, animBloque1));
                        }
                        
                    }
                }
            
                setBackground(Color.white);
                setSize(1200,600);
                addKeyListener(this);
                
            
                Thread th = new Thread (this);
		// Empieza el hilo
		th.start ();
        }
        
        
        /** 
	 * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se incrementa
     * la posicion en x o y dependiendo de la direccion, finalmente 
     * se repinta el <code>Applet</code> y luego manda a dormir el hilo.
     * 
     */
        public void run() {
            tiempoActual = System.currentTimeMillis();
            while (true) {
                if (!pausa && !gameOver) {
                    checaColision();
                    actualiza();
            }
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    System.out.println("Error en " + ex.toString());
                }   
            }
        }

        
        /**
	 * Metodo usado para actualizar la posicion de objetos planeta y meteorito.
	 * 
	 */
        
        public void actualiza() {  
          if(!pausa){  
                 //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
                long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
         
                 //Guarda el tiempo actual
                tiempoActual += tiempoTranscurrido;
                 //Actualiza la animación con base en el tiempo transcurrido para cada malo
                
                animFB.actualiza(tiempoTranscurrido);
                animBM.actualiza(tiempoTranscurrido);
                tiempoActual += tiempoTranscurrido;
                animBarra.actualiza(tiempoTranscurrido);
                //animPelota.actualiza(tiempoTranscurrido);
                //Dependiendo de la direccion del bueno es hacia donde se mueve.
                switch(direccion){
                    case 1: {
                        policia.setPosX(policia.getPosX() + 15);
                        break;
                    }
                    case 2: {
                        policia.setPosX(policia.getPosX() - 15);
                        break;
                    }
                }
                }
                pelota.setPosX(pelota.getPosX() - 3);
                pelota.setPosY(pelota.getPosY() - 3);
                if(coliX){
                   pelota.setPosX(pelota.getPosX() - 3);
                   pelota.setPosY(pelota.getPosY() + 3);
                }
                if(coliY){
                    pelota.setPosX(pelota.getPosX() + 3);
                    pelota.setPosY(pelota.getPosY() - 3);
                }
                
        }
        
        /**
	 * Metodo usado para checar las colisiones del objeto planeta y meteorito
	 * con las orillas del <code>JFrame</code>.
	 */
        public void checaColision(){
            
            if (policia.getPosX() + policia.getAncho() > getWidth()) {
                     policia.setPosX(getWidth()-policia.getAncho());
                }
                 
                if (policia.getPosX() < 0) {
			policia.setPosX(0);
		}    
           
                //Colision entre objetos policia-pelota
                if (policia.intersecta(pelota)) {
                        
                        //pelota.setVelocidadY(vY);
                        puntos += 2; 
                        movido = true;
                        coliY=!coliY;
                }
                
                //Colision de la pelota con el applet
                if (pelota.getPosY() + pelota.getAlto() > getHeight()) {
                    coliY=!coliY;
                    movido = true;
                }
                if (pelota.getPosX() + pelota.getAncho() > getWidth()) {
                     coliX=!coliX;
                } 
                if (pelota.getPosX() < 0) {
			coliX=!coliX;
		}   
                if (pelota.getPosY() < 0) {
			coliY=!coliY;
		}   
                
                
                
                
                if (pelota.getPosY() + pelota.getAlto() - 35 > this.getHeight()) {
                        gameOver = true; // Acabo el juego
                }


                // Colision de la pelota con la meth
                for (int i = 0; i < bloques.size(); i++) {
                        bloque = (Ladrillo) (bloques.get(i));
                    if (pelota.intersecta(bloque)) {
                        coliY=!coliY;
                    if (bloque.arriba().intersects(pelota.getPerimetro()) || bloque.abajo().intersects(pelota.getPerimetro())) {
                            pelotaY *= -1;
                    } else {
                        pelotaX *= -1;
                    }

                    score++;

                    bloques.remove(i);
                    }
                }   
        }
      
        /**
	 * Metodo <I>paint</I> sobrescrito de la clase <code>JFrame</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo lo que hace es actualizar el contenedor
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint(Graphics g){
		// Inicializan el DoubleBuffer
		if (dbImage == null){
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}

		// Actualiza la imagen de fondo.
		dbg.setColor(getBackground ());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

		// Actualiza el Foreground.
		dbg.setColor(getForeground());
		paint1(dbg);

		// Dibuja la imagen actualizada
		g.drawImage (dbImage, 0, 0, this);
	}
        
        /**
	 * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo se dibuja la imagen con la posicion actualizada,
	 * ademas que cuando la imagen es cargada te despliega una advertencia.
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint1(Graphics g) {
                    
            if(score<=8)g.drawImage(fondo1, 0, 0, this);
            if(score>8 && score<=16)g.drawImage(fondo2, 0, 0, this);
            if(score>16 && score<=24)g.drawImage(fondo3, 0, 0, this);
            if(score>24 && score<=50)g.drawImage(fondo4, 0, 0, this);
            if (policia.getAnimacion() != null) {
                g.drawImage(policia.getAnimacion().getImagen(), policia.getPosX(), policia.getPosY(), this);
            }
            if (pelota.getAnimacion() != null) {
                g.drawImage(pelota.getAnimacion().getImagen(), pelota.getPosX(), pelota.getPosY(), this);
            }

            for (int i = 0; i < bloques.size(); i++) {
                bloque = (Ladrillo) (bloques.get(i));
                    g.drawImage(bloque.getAnimacion().getImagen(), bloque.getPosX(), bloque.getPosY(), this);
            }
        
            if (gameOver){
            g.drawImage(fondoGO, 0 ,0, this);
            }
            
        } 
      /**
     * Metodo <I>keyPressed</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar cualquier la
     * tecla.
     *
     * @param e es el <code>evento</code> generado al presionar las teclas.
     */
    public void keyPressed(KeyEvent e) {
        
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {    //Presiono flecha derecha
                
                    direccion = 1;
                    movimiento=true;
                } 
                else if (e.getKeyCode() == KeyEvent.VK_LEFT) {    //Presiono tecla A izquierda
                
                    direccion = 2;
                    movimiento=true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_P){
                
                    pausa=!pausa;
                }
    }

    /**
     * Metodo <I>keyTyped</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una tecla que
     * no es de accion.
     *
     * @param e es el <code>evento</code> que se genera en al presionar las
     * teclas.
     */
    public void keyTyped(KeyEvent e) {
       
    }

    /**
     * Metodo <I>keyReleased</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla
     * presionada.
     *
     * @param e es el <code>evento</code> que se genera en al soltar las teclas.
     */
    
    public void keyReleased(KeyEvent e) {
        direccion=0;
        movimiento=false;
    }
}
