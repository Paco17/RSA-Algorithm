import java.math.BigInteger;

public class RSA {
	
	
	public static void inicio(BigInteger[] llavePublica, BigInteger[] llavePrivada) {
		//Esté método es para generar las laves tanto la publica y privada
		
		//1.- Conseguir los primos aleatorios
		String pStr = String.valueOf(primesList.primos());
		String qStr =  String.valueOf(primesList.primos());
		BigInteger p = new BigInteger(pStr);
		BigInteger q =  new BigInteger(qStr);
		
		//2.- Conseguir el modulo n
		BigInteger n = p.multiply(q);
		
		//3.-Calcular el phi de euler
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
	
		//4.- Calcular e (exponente de llave publica)
		BigInteger e = MCD_Euclides(BigInteger.TWO, phi);
		
		//5.- Calcular d (exponente de llave privada)
		BigInteger d = e.modInverse(phi);
		
		//6.- Generar las llaves
		generarLlaves(llavePublica, llavePrivada, n, e, d);
	}
	
	private static void generarLlaves(BigInteger[] publica, BigInteger[] privada, BigInteger n, BigInteger e, BigInteger d) {
		//Llave pública (e, n)
		publica[0] = e;
		publica [1] = n; 
		 
		//Llave Privada (d, n)
		privada[0] = d;
		privada[1] = n;
	
	}
	
	private static BigInteger MCD_Euclides(BigInteger number, BigInteger phi) {
		BigInteger res = phi.mod(number);
		while(res.intValue()!=1 && number.compareTo(phi)<0) {
			number = number.add(BigInteger.ONE);
			res = phi.mod(number);
		}return res;
	}
	
	public static String encriptar(BigInteger[] llavePublica, String mensaje) {
		String msg = "";
		char c = ' ';
		
		for(char caracter :mensaje.toCharArray()) {
			
			BigInteger bi =  new BigInteger((int)caracter+"");
			msg += ""+encriptarChar(bi, llavePublica)+" ";
		}
		//System.out.println("Encriptacion"+msg);
		return msg;
	}
	

	public static String desencriptar(BigInteger[] llavePrivada, String mensaje, BigInteger n) {
		String msg = "";
		String c = " ";

		String[] arr = mensaje.split(" ");	
		for(String str : arr) {
			 BigInteger i = new BigInteger(str); 
			msg += ""+(char)desencriptarChar(i, llavePrivada,  n).intValue();
		}return msg;
	}
	
	public static String desencriptarLlave(BigInteger[] llavePrivada, String mensaje, BigInteger n) {
		String msg = "";
		String c = " ";
		String[] arr = mensaje.split(" ");	
		for(String str : arr) {
			 BigInteger i = new BigInteger(str); 
			msg += ""+desencriptarChar(i, llavePrivada,  n);
		}return msg;
	}
	
	private static BigInteger encriptarChar(BigInteger c, BigInteger[] llavePublica) {
		BigInteger n = llavePublica[1]; 
		BigInteger e = llavePublica[0]; 
		return exponenciacionRapida(c, e, n);
	}
	
	private static BigInteger desencriptarChar(BigInteger c, BigInteger[] llavePrivada, BigInteger n) { 
		BigInteger d = llavePrivada[0]; 
		return exponenciacionRapida(c, d, n);
	}
	
	private static BigInteger exponenciacionRapida(BigInteger x, BigInteger exp, BigInteger n) {
		BigInteger r = new BigInteger("1");
		BigInteger xbi = new BigInteger(x+"");
		while(exp.compareTo(BigInteger.ZERO)>0) {
	        if(exp.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0 ) {
	            xbi = (xbi.multiply(xbi)).mod(n);
	            exp = exp.divide(BigInteger.TWO);
	        }
	        else {
	            r = (xbi.multiply(r)).mod(n);
	            exp = exp.subtract(BigInteger.ONE);
	        }
		}
		//System.out.println("R:"+r);
		
	    return r;
	}
	
	public static void main(String[] args) {
		RSA rsa =  new RSA();
	}
	/** 1.- Seleccionar p y q = Apartir de 5 cifras
	 *  2.- N = p * q 
	 *  3.- phiEuler (p-1)(q-1)
	 *  3.1.- e Bucar validacion de mcd[e, phi(n)] = 1 a partir de 2
	 *  4.- Llave publica (e, n)
	 *  4.1 - Llave privada (d)
	 *  4.2.- La exponenciación es para encriptar y desencriptar la cual es un while 
	 * 		 por cada caracter LEER EL GITHUB DE AGUS
	 *  5.- Se crea el mensaje y se manda encriptado
	 *  6.- Cuando desencriptas ocupas llave privada y la n tuya y caracter encriptado (y)
	 * */
	
	
	
	//Cada mensaje que mande el cliente los parámetros será el texto plano
	

}
