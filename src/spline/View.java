package spline;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.*;
import javax.swing.*;

/**
 * La clase View es la vista que representa gráficamente un spline.
 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
 * @version 1.0
 * @subject Programación de aplicaciones interactivas
 * @organization Universidad de La Laguna
 * @since 15-05-2017
 */
public class View extends JPanel implements Observer {
	private ArrayList<Polynomial> splines; // Spline to draw
	private ArrayList<Node> nodes; // Nodes to draw
	private int selectedNode;
	private boolean draw; 
	private DrawingPanel drawing = new DrawingPanel(); // Panel to draw spline
	private JButton generateButton = new JButton("Generar");
	private JButton restartButton = new JButton("Reiniciar");
	private JTextField numberNodesField = new JTextField(5);
	
	/**
	 * Método constructor 
	 */
	public View() {
		setLayout(new BorderLayout());
		setFocusable(true);
		setDraw(false);
		requestFocusInWindow();	
		// Image
		ImageIcon icon = new ImageIcon(this.getClass().getResource("informationImage.png"));		
		JLabel image = new JLabel(icon);
		image.addMouseListener(new InformationListener());		
		// Actions
		JPanel actionPanel = new JPanel();
		numberNodesField.setText("3");
		actionPanel.setBackground(Color.WHITE);		
		actionPanel.add(restartButton);
		actionPanel.add(generateButton);
		actionPanel.add(new JLabel("Número de nodos: "));
		actionPanel.add(numberNodesField);
		actionPanel.add(image);
		
		add(drawing, BorderLayout.CENTER);
		add(actionPanel, BorderLayout.SOUTH);		
		this.addMouseListener(new FocusListener()); // To be able to get focus in order to listen keyboard
	}
		
	/**
	 * Método que mantiene la vista actualizada respecto al modelo
	 * @param obs Modelo
	 * @param obj Objeto que contiene los datos del modelo
	 */
	@Override
	public void update(Observable obs, Object obj) {
		ArrayList<Object> data = (ArrayList<Object>)obj;
		nodes = (ArrayList<Node>)(data.get(0));
		splines = (ArrayList<Polynomial>)(data.get(1));
		drawing.repaint();
	}
	
	/**
	 * Método para anadir un controlador a la vista
	 * @param controller Controlador
	 */
	public void addController(ActionListener controller) {
		generateButton.addActionListener(controller);
		restartButton.addActionListener(controller);
		addKeyListener((KeyListener)controller);
	}
	
	/**
	 * Método para obtener el número de nodos del campo de texto.
	 * @return String Número de nodos
	 */
	public String getNumberNodesText() {
		return numberNodesField.getText();
	}
	
	public boolean getDraw() {
		return draw;
	}
	
	public void setDraw(boolean draw) {
		this.draw = draw;
	}
	
	/**
	 * Método para obtener el ancho del panel donde se dibuja el spline.
	 * @return int Ancho del panel del spline
	 */
	public int getWidthDrawing() {
		return drawing.getWidth();
	}
	
	/**
	 * Método para devolver el alto del panel donde se dibuja el spline.
	 * @return int Alto del panel del spline
	 */
	public int getHeightDrawing() {
		return drawing.getHeight();
	}
		
	/**
	 * Método para cambiar el nodo seleccionado con comprobación de no
	 * misma cordenada X.
	 * @param selected Índice del nodo seleccionado
	 */
	public void setSelectedNode(int selected) {		
		try {		
			if(selected < 0) {
				selectedNode = 0;
			}
			else {
				if(selected >= (nodes.size() - 1)) {
					selectedNode = nodes.size() - 1;
				}
				else {
					selectedNode = selected;
				}
			}		
		}
		catch(Exception e) {		
			selectedNode = 0;
		}
	}
	
	public int getSelectedNode() {
		return selectedNode;
	}
	
	/**
	 * Método para que el panel vuelva a estar activo.
	 */
	public void focus() {
		requestFocusInWindow();
	}
	
	/**
	 * La clase DrawingPanel es un panel donde se dibuja el spline y sus nodos.
	 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
	 * @version 1.0
	 * @subject Programación de aplicaciones interactivas
	 * @organization Universidad de La Laguna
	 * @since 15-05-2017
	 */
	class DrawingPanel extends JPanel {
		private static final int RADIO_NODE = 8;
		private static final int RADIO_SELECTED_NODE = 20;
		
		/**
		 * Método constructor que permite que el panel reciba eventos del teclado
		 */
		public DrawingPanel() {
			setBackground(Color.CYAN);	
			setFocusable(true);
		}	
		
		/**
		 * Método que dibuja el spline y sus nodos
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);		
			Graphics2D g2D = (Graphics2D)g;		
								
			if(!getDraw()) {
				return;
			}
						
			// Drawing Splines		
			ArrayList<Node> traceNodes = new ArrayList<Node>();
			int positionX = 0, positionY = 0;
			
			for(int i = 0; i < splines.size(); i++) {				
				int minLimit = (int)(nodes.get(i).getX());
				int maxLimit = (int)(nodes.get(i + 1).getX());
			
				for(int value = minLimit; value < maxLimit; value++) {
					positionX = value;
					positionY = (int)((getHeight() - splines.get(i).getY(value)));	// Obtaining value Y with spline								
					traceNodes.add(new Node(positionX, positionY));
				}
			}
			positionX = (int)(nodes.get(nodes.size() - 1).getX());
			positionY = (int)(getHeight() - nodes.get(nodes.size() - 1).getY());
			traceNodes.add(new Node(positionX, positionY)); // Last trace node
			
			Stroke original = g2D.getStroke();
			g2D.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));			
			for(int i = 0; i < (traceNodes.size() - 1); i++) { // Drawing lines between trace nodes
				g2D.draw(new Line2D.Double((int)(traceNodes.get(i).getX()), (int)(traceNodes.get(i).getY()), 
						(int)(traceNodes.get(i+1).getX()), (int)(traceNodes.get(i+1).getY())));
			}
			g2D.setStroke(original);
			
			// Nodes
			g2D.setColor(Color.RED);			
			
			positionX = (int)(nodes.get(0).getX());
			positionY = (int)((getHeight() - splines.get(0).getY(positionX))); 
			g2D.fillOval(positionX - RADIO_NODE, positionY - RADIO_NODE, RADIO_NODE * 2, RADIO_NODE * 2); // First one
			for(int i = 0; i < nodes.size() - 1; i++) { // Rest
				positionX = (int)(nodes.get(i + 1).getX());
				positionY = (int)(getHeight() - nodes.get(i + 1).getY()); 				
				g2D.fillOval(positionX - RADIO_NODE, positionY - RADIO_NODE, RADIO_NODE * 2, RADIO_NODE * 2);						
			}
			
			// Selected a node					
			positionX = (int)(nodes.get(getSelectedNode()).getX());
			positionY = (int)((getHeight() - nodes.get(getSelectedNode()).getY())); 
			g2D.drawOval(positionX - RADIO_SELECTED_NODE, positionY - RADIO_SELECTED_NODE, 
					RADIO_SELECTED_NODE * 2, RADIO_SELECTED_NODE * 2);					
		}		
	}
	
	/**
	 * La clase InformationListener permite que la imagen de información 
	 * pueda generar una nueva ventana con información .
	 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
	 * @version 1.0
	 * @subject Programación de aplicaciones interactivas
	 * @organization Universidad de La Laguna
	 * @since 15-05-2017
	 */
	class InformationListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e)  {			
			JFrame frame = new JFrame();
			
			JPanel informationPanel = new JPanel();
			informationPanel.setBackground(Color.WHITE);
			informationPanel.setLayout(new GridLayout(0, 1, 10, 10));
			
			informationPanel.add(new JLabel("Asignatura: Programación de aplicaciones interactivas"));
			informationPanel.add(new JLabel("Práctica: Interpolacion mediante Splines"));
			informationPanel.add(new JLabel("Descripción: Interaccion grafica con un spline y sus nodos"));
			informationPanel.add(new JLabel("Alumno: Oscar Catari Gutierrez"));
			informationPanel.add(new JLabel("Contacto: oscarcatari@outlook.es"));
			informationPanel.add(new JLabel("Universidad: Universidad de La Laguna"));
			informationPanel.add(new JLabel("Facultad: ETSSI"));
			
			frame.add(informationPanel, BorderLayout.CENTER);
			frame.setTitle("Splines");
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(600, 400);
			frame.setVisible(true);
		}
	}
	
	/**
	 * La clase FocusListener permite que la vista vuelva a estar activa con
	 * ayuda del ratón.
	 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
	 * @version 1.0
	 * @subject Programación de aplicaciones interactivas
	 * @organization Universidad de La Laguna
	 * @since 15-05-2017
	 */
	class FocusListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e)  {					
			focus();
		}
	}

}
