import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    
	public static BigInteger[] publicKey = new BigInteger[2];
	public static BigInteger[] publicKeyServer = new BigInteger[2];
	public static  BigInteger[] privateKey = new BigInteger[2];
    
    //Crear clave publica y manda
    public static void main(String args[]) throws Exception{
		Socket socket=new Socket("127.0.0.1",5000);
		BufferedReader entrada=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream ps=new PrintStream(socket.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		
		RSA.inicio(publicKey, privateKey);
		ps.println(publicKey[0]);
		ps.println(publicKey[1]);

		//Desencriptar llave de Server
		String strKeyS0 = entrada.readLine();
		String strKeyS1 = entrada.readLine();
		publicKeyServer[0] = new BigInteger(RSA.desencriptarLlave(privateKey, strKeyS0, publicKey[1]));
		publicKeyServer[1] = new BigInteger(RSA.desencriptarLlave(privateKey, strKeyS1, publicKey[1]));
		
		while ( true ){
			System.out.print("Cliente: " );
			s=br.readLine();
			s=RSA.encriptar(publicKeyServer, s);
			ps.println(s);
            if ( s.equalsIgnoreCase("Adios") ){
               System.out.println("El cliente termino la conexión");
 			   break;
            }
			
            s = entrada.readLine();
            System.out.print("Server : "+RSA.desencriptar(privateKey, s,publicKey[1])+"\n");
          
  			
		}
		 socket.close();
		 entrada.close();
		 ps.close();
 		br.close();
	}
    
    public void getMSG(String s) {
    	System.out.println(s);
    }
    
}