import java.io.*;
import java.net.*;

public class Client{
	static String IDENTIFY_NUMBER;
	static int CONNECT_PORT;
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String str;
	private String FILE_TO_RECEIVED;
	private String STORED_FILE; 
	public static void main(String[] args) {
		IDENTIFY_NUMBER = args[0];
		CONNECT_PORT = Integer.valueOf(args[1]);
		mkDir(IDENTIFY_NUMBER);
		new Client().begin();
	}
	void begin() {
		try{
			socket = new Socket("localhost",CONNECT_PORT);
			//initialize inputStream and outputStream
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Please enter command as a string ");
			Server ser = new Server();
			new Thread(ser).start();
			while(true){
				str = bufferedReader.readLine();//read a sentence from the standard input
				sendMessage(str);//Send the sentence to the server
				System.out.println("Meassage sent");
				System.out.println("Please enter command as a string ");
			}
		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{ //Close connections
			try{
				in.close();
				out.close();
				socket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	private class Server implements Runnable {
		private boolean bconnected = false;
		public void run() { //receive message from server;
			try {
				in = new ObjectInputStream(socket.getInputStream());
				bconnected = true;
				while (bconnected) {
					String inStr = (String)in.readObject();
					if(!inStr.equals("file")) {
						System.out.println(inStr);
					}else {
						String senderNumber=(String)in.readObject();
						STORED_FILE = (String)in.readObject();
						//create file to received path;
						FILE_TO_RECEIVED = IDENTIFY_NUMBER + "/" + STORED_FILE;	
				        byte[] contents = new byte[10000];
				        //Initialize the FileOutputStream to the output file's full path.
				        FileOutputStream fos = new FileOutputStream(FILE_TO_RECEIVED);
				        BufferedOutputStream bos = new BufferedOutputStream(fos); 
				        InputStream is = socket.getInputStream();
				        String inLengthStr = (String)in.readObject();
				        int fileLength= Integer.valueOf(inLengthStr); 
				        int bytesRead = 0;
				        int[] total=new int[1];
				       while((bytesRead=is.read(contents))!=-1){
				    	     total[0]= total[0]+bytesRead;
				    	     bos.write(contents, 0, bytesRead);
				    	     if(total[0]==fileLength){
				    	    	 break;
				    	     }
				        }
				       bos.flush(); 
				       System.out.println("File: "+STORED_FILE+" was sent by client"+senderNumber);
					}		        
				}
			} catch (SocketException e1) {
				System.out.println("Bye!");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
 
		}
	}
	//send a message to the output stream
	void sendMessage(String msg)
	{
		try{
			//stream write the message
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	/*Create directory if new client is created*/
	private static boolean mkDir(String name) {
		File dir = new File(name);
		if(dir.exists()) {
			return false;
		}
		if(dir.mkdir()) {
			return true;
		} else {
			return false;
		}
	}
}
