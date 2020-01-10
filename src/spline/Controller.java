package spline;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * La clase Controller es el controlador que coordina la interfaz gráfica y el
 * propio spline. Se encarga de los eventos generados por los botones y el mouse 
 * de la interfaz gráfica.
 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
 * @version 1.0
 * @subject Programación de aplicaciones interactivas
 * @organization Universidad de La Laguna
 * @since 15-05-2017
 */
public class Controller extends KeyAdapter implements ActionListener, KeyListener {
	private SplineModel model;
	private View view;
	private final int movement = 10;
	
	public Controller() {	
		
	}
	
	/**
	 * Método para controlar las acciones de los botones de la interfaz gráfica.
	 * Generar: Genera números nodos aleatorios para un spline.
	 * Reiniciar: No se dibuja el spline.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {	
		if(e.getActionCommand() == "Generar") {
			view.setDraw(true);
			view.setSelectedNode(0);
			
			int numberNodes = Integer.parseInt(view.getNumberNodesText());
			model.generateRandomNodes(numberNodes, view.getWidthDrawing(), view.getHeightDrawing());		
		}
		else {
			if(e.getActionCommand() == "Reiniciar") { // clear splines?
				view.setDraw(false);
				view.repaint();							
			}
		}
		view.focus();
	}
	
	/**
	 * Método para controlar el spline en la interfáz gráfica, mediante
	 * las teclas A y D se selecciona un nodo. Con las teclas de dirección
	 * se mueve el nodo seleccionado. 
	 */
	@Override
	public void keyPressed(KeyEvent e) {		
		switch(e.getKeyCode()) {
			case KeyEvent.VK_A: // Select left node			
				view.setSelectedNode(view.getSelectedNode() - 1);
				view.repaint();
				break;
			case KeyEvent.VK_D:  // Select right node					
				view.setSelectedNode(view.getSelectedNode() + 1);
				view.repaint();
				break;
			case KeyEvent.VK_UP: // Moves node up			
				model.incrementNode(view.getSelectedNode(), 0, movement);
				break;
			case KeyEvent.VK_DOWN: // Moves node down			
				model.incrementNode(view.getSelectedNode(), 0, -movement);
				break;
			case KeyEvent.VK_LEFT: // Moves node left
				model.incrementNode(view.getSelectedNode(), -movement, 0);
				break;
			case KeyEvent.VK_RIGHT: // Moves node right
				model.incrementNode(view.getSelectedNode(), movement, 0);	
		}		
	}
	
	/**
	 * Método para añadir el modelo al controlador
	 * @param model Objeto que contiene la información del spline
	 */
	public void addModel(SplineModel model) {
		this.model = model;
	}
		
	/**
	 * Método para añadir la vista al controlador
	 * @param view Objeto que va a representar gráficamente el modelo
	 */
	public void addView(View view) {	
		this.view = view;
	}	
}
