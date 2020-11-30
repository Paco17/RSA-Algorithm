
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Francisco Javier Ramos
 * @matricula A01636425
 */

public class Server {
	
	public static BigInteger[] publicKey = new BigInteger[2];
	public static BigInteger[] publicKeyServer = new BigInteger[2];
	public static  BigInteger[] privateKey = new BigInteger[2];
    static int port;
    ServerSocket serverS=null;
    Socket cliente=null;
    ExecutorService pool = null;
    int clientes=0;
    
    public static void main(String[] args) throws IOException {
        Server server=new Server(5000);
        server.iniciar();
    }
    
    Server(int port){
        this.port=port;
        pool = Executors.newFixedThreadPool(5);
        RSA.inicio(publicKeyServer, privateKey);
    }

    public void iniciar() throws IOException {
        
        serverS=new ServerSocket(5000);
        
        while(true)
        {
            cliente=serverS.accept();
          
            clientes++;
            HiloServer runnable= new HiloServer(cliente,clientes,this);
            pool.execute(runnable);
        }
        
    }

    
    
    private static class HiloServer implements Runnable {
        
        Server server=null;
        Socket cliente=null;
        BufferedReader br;
        PrintStream ps;
        Scanner scanner=new Scanner(System.in);
        int id;
        String s;
        BufferedReader brLocal = new BufferedReader(new InputStreamReader(System.in));
        
        HiloServer(Socket client, int count , Server server ) throws IOException {
        	this.cliente=client;
            this.server=server;
            this.id=count;
          
            
            br=new BufferedReader(new InputStreamReader(client.getInputStream()));
            ps=new PrintStream(client.getOutputStream());
            
           
            
        }

        @Override
        public void run() {
        	 int x=1;
	         try{
	        	 publicKey[0] = new BigInteger(br.readLine());
	        	 publicKey[1] = new BigInteger(br.readLine());
	        	 ps.println(RSA.encriptar(publicKey, publicKeyServer[0]+""));
	        	 ps.println(RSA.encriptar(publicKey, publicKeyServer[1]+""));
		         while(true){
		        	 s=br.readLine();
		        	 String tmp = s;
		        	 s = RSA.desencriptar(privateKey, s, publicKeyServer[1]);
		        	 System.out.print("Client("+id+") :"+tmp+"\n");
		        	 System.out.print ("Server: ");
		        	
		        	 if (s.equalsIgnoreCase("Adios")){
		        		 ps.println("Adiós");
		        		 x=0;
		        		 System.out.println("Conexión terminada");
		        		 break;
		        	}
		      
		        	s = scanner.nextLine();
		 			s=RSA.encriptar(publicKey, s);
		 			ps.println(s);
		        	 
				}
		        
		        br.close();
		        cliente.close();
				ps.close();
				
				if(x==0)
					System.exit(0);
		         
	         }catch(IOException ex){
	        	 System.out.println("Error : "+ex);
	         }
        }
    }
 }