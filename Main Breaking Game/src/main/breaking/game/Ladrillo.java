package main.breaking.game;

import java.awt.Rectangle;
public class Ladrillo extends Base{
    
        /**
	 * Metodo constructor que hereda los atributos de la clase <code>Universo</code>.
	 * @param posX es la <code>posiscion en x</code> del objeto Planeta.
	 * @param posY es el <code>posiscion en y</code> del objeto Planeta.
	 * @param image es la <code>imagen</code> del objeto Planeta.
	 */
    
    public Ladrillo(int posX,int posY,Animacion image){
		super(posX,posY,image);	
	}
    
    public Rectangle arriba() {
        return (new Rectangle(this.getPosX() + 19, this.getPosY(),this.getAncho() - 19, 20));
    }
    
    public Rectangle abajo() {
        return (new Rectangle(this.getPosX() + 19, this.getPosY() + this.getAlto() - 20, this.getAncho() - 19, 20));
    }

}
