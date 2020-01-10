package spline;

import java.util.ArrayList;

/**
 * La clase Determinant representa un determinante y puede realizar algunas
 * operaciónes como calculas su cofactor y su valor.
 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
 * @version 1.0
 * @subject Programación de aplicaciones interactivas
 * @organization Universidad de La Laguna
 * @since 15-05-2017
 */
public class Determinant {
	private ArrayList<ArrayList<Double>> coef;
	
	public Determinant() {
		coef = new ArrayList<ArrayList<Double>>();
	}
	
	/** 
	 * Método que actualiza los coeficientes del determinante.
	 * @param coef Coeficientes
	 */
	public void setCoef(ArrayList<ArrayList<Double>> coef) {
		this.coef.clear();
		
		for(int i = 0; i < coef.size(); i++) {			
			this.coef.add(new ArrayList<Double>());
			for(int j = 0; j < coef.get(i).size(); j++) {
				this.coef.get(i).add(coef.get(i).get(j));
			}
		}		
	}
	
	/**
	 * Método que calcula el valor del determinante. La suma de
	 * los elementos de la primera columna multiplicados el valor
	 * de su cofactor. El signo positivo para posiciones pares y 
	 * negativo para los demás.
	 * @return
	 */
	public double getValue() {
		double value = 0.0;
		
		if(coef.size() == 1) {	// Determinant size = 1		
			return coef.get(0).get(0);
		}
		
		for(int i = 0; i < coef.size(); i++) { //Rows
			if(coef.get(i).get(0) != 0.0) {
				if((i % 2) == 0) {
					value = value + coef.get(i).get(0) * (getCofactor(i, 0).getValue());
				}
				else {
					value = value - coef.get(i).get(0) * (getCofactor(i, 0).getValue());
				}		
			}
		}		
		
		return value;	
	}
	
	@Override
	public String toString() {
		String aux = "";
		
		for(int i = 0; i < coef.size(); i++) {				
			for(int j = 0; j < coef.get(i).size(); j++) {
				aux += coef.get(i).get(j) + " ";
			}
			if(i != (coef.size() - 1))
				aux += "\n";
		}
		return aux;
	}
	
	/**
	 * Método que obtiene el cofactor del determinante actual
	 * seǵun uno de los elementos.
	 * @param row Número de fila
	 * @param column Número de columna
	 * @return Determinant Determinante cofactor
	 */
	public Determinant getCofactor(int row, int column) {
		Determinant cofactor = new Determinant();		
		ArrayList<ArrayList<Double>> subCoef = new ArrayList<ArrayList<Double>>();
		
		for(int i = 0; i < coef.size(); i++) {			
			subCoef.add(new ArrayList<Double>());
			
			for(int j = 0; j < coef.get(i).size(); j++) {
				if((i != row) && (j != column)) {
					subCoef.get(i).add(coef.get(i).get(j));
				}
			}			
		}
		subCoef.remove(row);
		cofactor.setCoef(subCoef);	
		return cofactor;
	}
	
	/**
	 * Método que modifica una columna del determinante actual.
	 * @param column Número de columna
	 * @param values Valores de la nueva columna
	 */
	public void setColumn(int column, ArrayList<Double> values) {
		for(int i = 0; i < coef.size(); i++) {					
			for(int j = 0; j < coef.get(i).size(); j++) {
				if(j == column) {
					coef.get(i).set(j, values.get(i));
				}
			}			
		}
	}
}
