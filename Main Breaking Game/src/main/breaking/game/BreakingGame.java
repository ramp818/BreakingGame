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
        private int vX;
        private int vY;
        private boolean click;
        private boolean sonido;
        private boolean movimiento;
        private Animacion animPelota;
        private Animacion animMario;
        private Animacion animBloque;
        private long tiempoActual;
	private long tiempoInicial;
        private int ultDireccion;
        private int vidas;
        private int move=0;
        private int bolaPerdida;
        private String nombreArchivo;
        private LinkedList bloques;
        
        public BreakingGame(){
            
                direccion=0;
                vidas=5;
                bolaPerdida=0;
                pausa=false;
                movimiento=false;
                inicio=false;
                nombreArchivo="Juego.txt";
                posXPelota=50;
                posYPelota=450;
                gravedad = 1;
                vX = (int)(Math.random() * 5 + 13); // posiciones de velocidad x
                vY = (int)(Math.random() * 12 + 15); //posiciones de velocidad y
                sonido=true;
                movido=false;
                bloques=new LinkedList();
                Image pelota0 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Bola1.png"));
                Image pelota1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Bola2.png"));
                Image pelota2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Bola3.png"));
                Image pelota3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Bola4.png"));
            
                animPelota= new Animacion();
                animPelota.sumaCuadro(pelota0,100);
                animPelota.sumaCuadro(pelota1,100);
                animPelota.sumaCuadro(pelota2,100);
                animPelota.sumaCuadro(pelota3,100);
            
                Image mario0 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario0.png"));
                Image mario1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario1.png"));
                Image mario2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario2.png"));
                Image mario3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario3.png"));
                Image mario4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario4.png"));
                Image mario5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario5.png"));
                Image mario6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario6.png"));
            
                animMario= new Animacion();
                animMario.sumaCuadro(mario0,100);
                animMario.sumaCuadro(mario1,100);
                animMario.sumaCuadro(mario2,100);
                animMario.sumaCuadro(mario3,100);
                animMario.sumaCuadro(mario4,100);
                animMario.sumaCuadro(mario5,100);
                animMario.sumaCuadro(mario6,100);
                
                policia=new Barra(400,500,animMario);
                pelota= new Pelota(posXPelota,posYPelota,animPelota);
                
                //beep = new SoundClip("sonidos/beep.wav");
                //explosion = new SoundClip("sonidos/explosion.wav");
                for (int i = 50; i < 236; i += 59){
                    for (int j = 45; j < 1130; j += 150){
                        bloques.add(new Ladrillo(j, i, animBloque));
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
        public void run () {
		
            tiempoActual = System.currentTimeMillis();
            while (true) {
		if(!pausa && inicio && !gameOver){
                        actualiza();
			checaColision();
                        }
			repaint();    // Se actualiza el <code>Applet</code> repintando el contenido.
			try	{
				// El thread se duerme.
				Thread.sleep (20);
			}
			catch (InterruptedException ex)	{
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
            if(vidas>0){
                 //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
                long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
         
                 //Guarda el tiempo actual
                tiempoActual += tiempoTranscurrido;
                 //Actualiza la animación con base en el tiempo transcurrido para cada malo
                 if (movimiento) {
                     animPelota.actualiza(tiempoTranscurrido);
                 }
                tiempoActual += tiempoTranscurrido;
                animMario.actualiza(tiempoTranscurrido);
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
            if (movimiento) { // si movimiento es true se mueve
                
                pelota.setPosX(pelota.getPosX() + 1);
                pelota.setPosY(pelota.getPosY() + 1);
         }
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
                 
                if (policia.getPosX() < getWidth()) {
			policia.setPosX(getWidth());
		}
                
		if (policia.getPosY() + policia.getAlto() > getHeight()) {
			policia.setPosY(getHeight()-policia.getAlto());
		}
                
                if (policia.getPosY() < 0) {
                     policia.setPosY(0);  
                }
                
                
                //Colision entre objetos
                if (policia.intersecta(pelota)) {
                        vX = (int)(Math.random() * 5 + 13); 
                        vY = (int)(Math.random() * 12 + 15); 
                        pelota.setPosX(50);
                        pelota.setPosY(450);
                        //pelota.setVelocidadY(vY);
                        puntos += 2; 
                        click = false;
                        movido = true;
                }
                //Colision de la pelota con el applet
                if (pelota.getPosY() + pelota.getAlto() > getHeight()) {
                    vX = (int)(Math.random() * 5 + 13);
                    vY = (int)(Math.random() * 12 + 15);
                    pelota.setPosX(50);
                    pelota.setPosY(450);
                    //pelota.setVelocidadY(vY);
                    bolaPerdida++;
                    click = false;
                    movido = true;
                    if(bolaPerdida==3){
                    vidas-=1;
                    bolaPerdida=0;
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
            //g.drawImage(imagenGO, 0 ,0, this);
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
            else if(e.getKeyCode() == KeyEvent.VK_ENTER){
                
                inicio=true;
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
