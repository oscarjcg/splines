package spline;

/**
 * La clase Polynomial representa un polinomio de grado 3.
 * La posición 0 se correcponde con la constante, mientras que la 
 * posición 3 es el valor de x^3. 
 * @author Oscar Catari Gutiérrez - E-mail: oscarcatari@outlook.es
 * @version 1.0
 * @subject Programación de aplicaciones interactivas
 * @organization Universidad de La Laguna
 * @since 15-05-2017
 */
public class Polynomial {
	private double[] polynomial; //Position:  3->x^3, 2 ->x^2, 1->x^1, 0-> constant
	
	/**
	 * Método constructor que inicia el polinomio a cero
	 */
	public Polynomial() {
		polynomial = new double[4];
		
		for(int i = 0; i < getSize(); i++) {
			polynomial[i] = 0;
		}
	}
	
	/**
	 * Método constructor que inicializa el polinomio a unos
	 * valores definidos.
	 * @param x3 Valor de x^3
	 * @param x2 Valor de x^2
	 * @param x Valor de x
	 * @param constant Valor de la constante
	 */
	public Polynomial(double x3, double x2, double x, double constant) {
		polynomial = new double[4];
		polynomial[3] = x3;
		polynomial[2] = x2;
		polynomial[1] = x;
		polynomial[0] = constant;		
	}
	
	public void setPolynomial(double x3, double x2, double x, double constant) {		
		polynomial[3] = x3;
		polynomial[2] = x2;
		polynomial[1] = x;
		polynomial[0] = constant;
	}
	
	public int getSize() {
		return polynomial.length;
	}
	
	public double getX3() {
		return polynomial[3];
	}
	
	public double getX2() {
		return polynomial[2];
	}
	
	public double getX() {
		return polynomial[1];
	}
	
	public double getConst() {
		return polynomial[0];
	}
	
	public double getElement(int element) {
		return polynomial[element];
	}
	
	/**
	 * Método que suma el polinomio actual con otro.
	 * @param otherPolynomial Polinomia con el que sumar
	 * @return Polynomial Polinomio resultado
	 */
	public Polynomial sum(Polynomial otherPolynomial) {		
		double x3 = getX3() + otherPolynomial.getX3();
		double x2 = getX2() + otherPolynomial.getX2();
		double x = getX() + otherPolynomial.getX();
		double constant = getConst() + otherPolynomial.getConst();
				
		return new Polynomial(x3, x2, x, constant);
	}
	
	/**
	 * Método que resta el polinomio actual con otro.
	 * @param otherPolynomial Polinomia con el que restar
	 * @return Polynomial Polinomio resultado
	 */
	public Polynomial subtract(Polynomial otherPolynomial) {		
		double x3 = getX3() - otherPolynomial.getX3();
		double x2 = getX2() - otherPolynomial.getX2();
		double x = getX() - otherPolynomial.getX();
		double constant = getConst() - otherPolynomial.getConst();
				
		return new Polynomial(x3, x2, x, constant);
	}
	
	/**
	 * Método que multiplica el polinomio actual con otro.
	 * @param otherPolynomial Polinomia con el que multiplicar
	 * @return Polynomial Polinomio resultado
	 */
	public Polynomial multiply(Polynomial otherPolynomial) {
		
		double x3 = 0.0;
		double x2 = 0.0;
		double x = 0.0;
		double constant = 0.0;
		Polynomial resultPolynomial = new Polynomial(x3, x2, x, constant);
	
		x3 = getConst() * otherPolynomial.getX3();
		x2 = getConst() * otherPolynomial.getX2();
		x = getConst() * otherPolynomial.getX();
		constant = getConst() * otherPolynomial.getConst();
		resultPolynomial = resultPolynomial.sum(new Polynomial(x3, x2, x, constant));
						
		//double x4 = getX() * otherPolynomial.getX3();
		x3 = getX() * otherPolynomial.getX2();
		x2 = getX() * otherPolynomial.getX();
		x = getX() * otherPolynomial.getConst();
		constant = 0.0;
		resultPolynomial = resultPolynomial.sum(new Polynomial(x3, x2, x, constant));
		
		//double x4 = getX2() * otherPolynomial.getX2();
		x3 = getX2() * otherPolynomial.getX();
		x2 = getX2() * otherPolynomial.getConst();
		x = 0.0;
		constant = 0.0;
		resultPolynomial = resultPolynomial.sum(new Polynomial(x3, x2, x, constant));
			
		//double x4 = getX3() * otherPolynomial.getX();	
		x3 = getX3() * otherPolynomial.getConst();
		x2 = 0.0;
		x = 0.0;
		constant = 0.0;
		resultPolynomial = resultPolynomial.sum(new Polynomial(x3, x2, x, constant));
		
		return resultPolynomial;
	}
	
	/**
	 * Método que devuelve el valor Y del polinomio evaluado
	 * en el punto X.
	 * @param x Valor X
	 * @return double Valor Y
	 */
	public double getY(double x) {
		return getX3() * Math.pow(x, 3) + getX2() * Math.pow(x, 2) + getX() * x + getConst();
	}
	
	public String toString() {
		return getX3() + "*X3 " + getX2() + "*X2 " + getX() + "*X " + getConst();
	}
}
