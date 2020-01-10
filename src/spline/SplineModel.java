package spline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

/**
 * La clase SplineModel contiene los datos de un spline y lo calcula.
 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
 * @version 1.0
 * @subject Programación de aplicaciones interactivas
 * @organization Universidad de La Laguna
 * @since 15-05-2017
 */
public class SplineModel extends Observable{
	private ArrayList<Node> nodes;
	private ArrayList<Polynomial> splines;	
	
	public SplineModel() {
		splines = new ArrayList<Polynomial>();
	}
	
	/**
	 * Método para actualizar el conjunto de nodos e inmediatamente
	 * después, calcular el nuevo spline.
	 * @param newNodes Nuevo conjunto de nodos
	 */
	public void setNodes(ArrayList<Node> newNodes) {
		nodes = newNodes;		
		calculateSpline();
		
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(nodes);
		data.add(splines);
		
		setChanged();
		notifyObservers(data);
	}
	
	/**
	 * Método para obtener el número de splines
	 * @return int Número de splines
	 */
	public int getSize() {
		return getNodes().size() - 1;
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public ArrayList<Polynomial> getSplines() {
		return splines;
	}
	
	/**
	 * Método que añade un polinomia al conjunto de splines
	 * @param polyn Polinomio
	 */
	void add(Polynomial polyn) {
		splines.add(polyn);
	}
	
	/**
	 * Método que calcula el valor Y de un spline en un punto X.
	 * @param numberSpline Índice del spline
	 * @param value Valor X
	 * @return int Valor Y
	 */
	public int getSplineY(int numberSpline, double value) {
		return (int)(splines.get(numberSpline).getY(value));
	}
	
	/**
	 * Método que calcula el conjunto de splines con los nodos actuales.
	 */
	public void calculateSpline() {
		splines.clear();
		// Calculating k constants	
		ArrayList<Double> kConstants = calculateKConstants();	
		// Calculating polynomials		
		Polynomial polynomialSpline = new Polynomial();
		Polynomial auxPolyn = new Polynomial();
		Polynomial constant1 = new Polynomial(0, 0, 0, 1);		
		
		for(int i = 0; i < getSize(); i++) { // Times: Number of nodes - 1 
			double leftPositionX = nodes.get(i).getX(); // x0
			double leftPositionY = nodes.get(i).getY(); // y0
			double rightPositionX = nodes.get(i + 1).getX(); // x1
			double rightPositionY = nodes.get(i + 1).getY(); // y1
			
			Polynomial leftPositionYPolyn = new Polynomial(0, 0, 0, leftPositionY); // y0
			Polynomial rightPositionYPolyn = new Polynomial(0, 0, 0, rightPositionY); // y1
			Polynomial t = new Polynomial(0, 0, 1.0 / (rightPositionX - leftPositionX),
					-leftPositionX / (rightPositionX - leftPositionX));
			Polynomial oneMinusT = constant1.subtract(t); // 1 - t
			
			double a = kConstants.get(i) * (rightPositionX - leftPositionX) - (rightPositionY - leftPositionY);
			double b = -kConstants.get(i + 1) * (rightPositionX - leftPositionX) + (rightPositionY - leftPositionY);
			Polynomial aPolyn = new Polynomial(0, 0, 0, a);
			Polynomial bPolyn = new Polynomial(0, 0, 0, b);
			
			//Constructing spline
			polynomialSpline = oneMinusT.multiply(leftPositionYPolyn); // (1 - t)*y0
			polynomialSpline = polynomialSpline.sum(t.multiply(rightPositionYPolyn)); // + t*y1
			// t*(1 -t)*(a*(1 - t) + b * t)
			auxPolyn = t.multiply(oneMinusT).multiply(aPolyn.multiply(oneMinusT).sum(bPolyn.multiply(t)));
			polynomialSpline = polynomialSpline.sum(auxPolyn); 	
							
			add(polynomialSpline);			
		}		
	}
	
	/**
	 * Método que calcula las constantes k del conjunto de nodos actual.
	 * @return
	 */
	public ArrayList<Double> calculateKConstants() {
		ArrayList<Double> kConstants = new ArrayList<Double>();		
		ArrayList<ArrayList<Double>> coef = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> constants = new ArrayList<Double>();
		
		// Constructing coefficients
		for(int i = 0; i < (getSize() + 1); i++) { // Number of splines + 1 = number of nodes
			coef.add(new ArrayList<Double>());
			for(int j = 0; j < (getSize() + 1); j++) {
				if ((i == 0) && (j == i)) { // First
					double element = 0.0;
					element = 2.0 / (nodes.get(i + 1).getX() - nodes.get(i).getX());					
					coef.get(i).add(element);
					j++;
					element = 1.0 / (nodes.get(i + 1).getX() - nodes.get(i).getX());					
					coef.get(i).add(element);
				}
				else {
					if ((i == (getSize() + 1 - 1)) && (j == i)) { // Last					
						coef.get(i).remove(coef.get(i).size() - 1);
						double element = 0.0;
						element = 1.0 / (nodes.get(i).getX() - nodes.get(i - 1).getX());					
						coef.get(i).add(element);
						j++;
						element = 2.0 / (nodes.get(i).getX() - nodes.get(i - 1).getX());					
						coef.get(i).add(element);				
					}
					else {
						if(i == j) { // Between
							coef.get(i).remove(coef.get(i).size() - 1);
							j--;
							double element = 0.0;
							double previous = 1.0 / (nodes.get(i).getX() - nodes.get(i - 1).getX());					
							double next = 1.0 / (nodes.get(i + 1).getX() - nodes.get(i).getX());															
							element = 2 * (previous + next);
							coef.get(i).add(previous);
							j++;
							coef.get(i).add(element);
							j++;									
							coef.get(i).add(next);
						}
						else {
							coef.get(i).add(0.0);
						}
					}
				}
			}
		}
				
		//Constructing constants
		for(int i = 0; i < (getSize() + 1); i++) { // Number of splines + 1 = number of nodes
			if (i == 0) { // First
				double element = 0.0;				
				double componentY = nodes.get(i + 1).getY() - nodes.get(i).getY();
				double componentX = Math.pow(nodes.get(i + 1).getX() - nodes.get(i).getX(), 2);
				element = 3 * (componentY / componentX);					
				constants.add(element);
			}
			else {
				if (i == (getSize() + 1 - 1)) { // Last
					double element = 0.0;					
					double componentY = nodes.get(i).getY() - nodes.get(i - 1).getY();
					double componentX = Math.pow(nodes.get(i).getX() - nodes.get(i - 1).getX(), 2);
					element = 3 * (componentY / componentX);					
					constants.add(element);
				}
				else { // Between						
					double element = 0.0, componentY = 0.0, componentX = 0.0;					
					componentY = nodes.get(i).getY() - nodes.get(i - 1).getY();
					componentX = Math.pow(nodes.get(i).getX() - nodes.get(i - 1).getX(), 2);
					double left = componentY / componentX;
					componentY = nodes.get(i + 1).getY() - nodes.get(i).getY();
					componentX = Math.pow(nodes.get(i + 1).getX() - nodes.get(i).getX(), 2);
					double right = componentY / componentX;
																				
					element = 3 * (left + right);									
					constants.add(element);				
				}
			}
		}		
		
		kConstants =  resolveSystemEcuation(coef, constants);	
		return kConstants;
	}
	
	/**
	 * Método que resulve un sistema de ecuaciones(polinomios) por el método de Cramer.
	 * @param coef Coeficientes
	 * @param constants Constantes
	 * @return ArrayList<Double> Valores de las variables
	 */
	public ArrayList<Double> resolveSystemEcuation(ArrayList<ArrayList<Double>> coef, ArrayList<Double> constants) {
		ArrayList<Double> kConstants = new ArrayList<Double>();		
		Determinant comunDeterm = new Determinant();		
		comunDeterm.setCoef(coef);
		double comunValue = comunDeterm.getValue();
		
		for (int i = 0; i < coef.size(); i ++) {			
			// 
			Determinant determinant = new Determinant();
			determinant.setCoef(coef);
			determinant.setColumn(i, constants);			
					
			kConstants.add(determinant.getValue() / comunValue);
		}
	
		return kConstants;
	}
	
	/**
	 * Método para generar nodos sin que estos tengas la misma 
	 * coordenada X.
	 * @param numberNodes Número de nodos a generar
	 * @param xLimit Límite máximo del valor x
	 * @param yLimit Límite máximo del valor y
	 */
	public void generateRandomNodes(int numberNodes, int xLimit, int yLimit) {
		ArrayList<Node> randomNodes = new ArrayList<Node>();
		ArrayList<Integer> randomX = new ArrayList<Integer>();		
		int random = 0;
		boolean sameX = true;
		
		while(sameX) {
			randomX.clear();
			sameX = false;
			// Generating  multiples random x
			for(int i = 0; i < numberNodes; i++) {
				random = (int)(Math.random() * xLimit);
				randomX.add(random);
			}
			
			Collections.sort(randomX);			
			// Checking if two X are the same
			for(int i = 0; i < randomX.size(); i++) {
				int value = randomX.get(i);
				for(int j = 0; j < randomX.size(); j++) {
					if((i != j) && (value == randomX.get(j))) {
						sameX = true;					
					}
				}
			}
		}
		// Generating multiples random  y, and creating nodes
		for(int i = 0; i < numberNodes; i++) {
			random = (int)(Math.random() * yLimit);
			randomNodes.add(new Node(randomX.get(i), random));
		}
		
		setNodes(randomNodes);
	}
	
	/**
	 * Método que modifica la posición de un nodo del conjunto actual de nodos.
	 * Inmediatamente después se calcula el nuevo spline.
	 * @param node Índice del nodo a modificar
	 * @param positionX Incremento de la coordenada X
	 * @param positionY Incremento de la coordenada Y
	 */
	public void incrementNode(int node, int positionX, int positionY) {
		int x = positionX + (int)(nodes.get(node).getX());
		int y = positionY + (int)(nodes.get(node).getY());
		
		// Checking if new position is valid. Nodes order should be maintained
		if(node == 0) {
			if(x >= nodes.get(node + 1).getX()) // First node
				return;		
		}
		else {
			if(node == (nodes.size() - 1)) {
				if(nodes.get(node - 1).getX() >= x) // Last node
					return;
			}
			else { // Central nodes
				if((nodes.get(node - 1).getX() >= x) || (x >= nodes.get(node + 1).getX())) {
					return;
				}
			}
		}
		
		nodes.get(node).setX(x);
		nodes.get(node).setY(y);
		
		calculateSpline();
		
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(nodes);
		data.add(splines);		
		setChanged();
		notifyObservers(data);
	}
	
}
