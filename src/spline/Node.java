package spline;

/**
 * La clase Node representa una coordenada
 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
 * @version 1.0
 * @subject Programación de aplicaciones interactivas
 * @organization Universidad de La Laguna
 * @since 15-05-2017
 */
public class Node {
	private double positionX;
	private double positionY;
	
	public Node(double x, double y) {
		positionX = x;
		positionY = y;
	}
	
	public double getX() {
		return positionX;
	}
	
	public double getY() {
		return positionY;
	}
		
	public Node getPoint() {
		return new Node(positionX, positionY);
	}
	
	public void setX(int positionX) {
		this.positionX = positionX;
	}
	
	public void setY(int positionY) {
		this.positionY = positionY;
	}
		
	public String toString() {
		return "( " + positionX + " " + positionY + " )";
	}
}
