package spline;

import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JFrame;


/**
 * La clase Splines es un Applet. Se encarga de poner en funcionamiento el conjunto de clases 
 * modelo-vista-controlador que ejecutarán la representación de un spline a partir de una
 * serie de nodos. 
 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
 * @version 1.0
 * @subject Programación de aplicaciones interactivas
 * @organization Universidad de La Laguna
 * @since 15-05-2017
 */
public class Splines extends JApplet {
	/**
	 * Constructor que crea los componentes del spline representado gráficamente
	 */
	public Splines() {
		SplineModel model = new SplineModel();
		View view = new View();
		Controller controller = new Controller();
		
		model.addObserver(view);
		controller.addModel(model);
		controller.addView(view);
		view.addController(controller);		
			
		add(view);	
	}

	/**
	 * Método pricipal para que el applet sea un programa independiente
	 * @param args Ninguno 
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Interpolacion mediante splines");
		
		Splines program = new Splines();
		frame.add(program);
			
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setVisible(true);	
		
	}

}
