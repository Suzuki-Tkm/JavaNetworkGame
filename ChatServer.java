import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
	
	static File file;
	static FileWriter fileout;
	public static Map<String,TurtleD> turtledata;
	
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.out.println("起動方法: java ChatServer ポート番号");
			System.out.println("例: java ChatServer 50002");
			System.exit(1);
		}
		int port = Integer.valueOf(args[0]).intValue();
		ServerSocket serverS = null;
		boolean end = true;
		turtledata = new HashMap<>();
		
		String filename = "/Users/apple/Desktop/" + "ChatSeverLog-" + java.time.LocalDateTime.now().toString()
				+ ".txt";
		file = new File(filename);
		fileout = new FileWriter(ChatServer.file, true);
		
		
		try {
			serverS = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("ポートにアクセスできません。");
			System.exit(-1);
		}
		while(end){
			new ChatMThread(serverS.accept()).start();
		}
		serverS.close();   
	}
	
}


class ChatMThread extends Thread {
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	static ArrayList<ChatMThread> member;
	String id;
	String time;
	static int i = 0;
	
	
	ChatMThread(Socket s) {
		super("ChatMThread");
		socket = s;
		id = socket.getRemoteSocketAddress().toString();
		id = id.replace("/", "");
		ChatServer.turtledata.put(id,new TurtleD(id , i*50.0+100.0 , 200.0 , 90.0 , 10000.0));
		i++;
		if (member == null) {
			member = new ArrayList<ChatMThread>();
		}
		member.add(this);
		//System.out.println("接続");
	}

	public void run() {
		try{
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			
			synchronized (ChatServer.file) {
				ChatServer.fileout.write("generate" + ChatServer.turtledata.get(id)+ "\n");
				ChatServer.fileout.flush();
			}
			
			for(ChatMThread client : member){
				client.out.println("generate" + ChatServer.turtledata.get(id));
			}
			
			String fromClient;
			
			while ((fromClient = in.readLine()) != null) {
				String[] newStr = fromClient.split("\\s+");
				try {
					switch (newStr[0]) {
					case "rotate":
						rotate(Double.parseDouble(newStr[1]));output(); break;
						
					case "walk":
						walk(Double.parseDouble(newStr[1]));output(); break;
					}
				
				}catch(NumberFormatException | InterruptedException e){
					out.println("数値の書式が正しくありません。");
				}
				//Thread.sleep(3000);
			}
		}catch(IOException e){ System.out.println("run:" + e); }
		try {
			end();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void end() throws InterruptedException {
		for(ChatMThread client : member){
			client.out.println("remove " + id);
			Thread.sleep(500);
		}
		synchronized (ChatServer.file) {
			try {
				ChatServer.fileout.write("remove " + id+ "\n");
				ChatServer.fileout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("remove " + id);
		ChatServer.turtledata.remove(id);
		try {
			synchronized (ChatServer.file) {
				ChatServer.fileout.write(id + "との接続を終了します。" + "\n");
				ChatServer.fileout.close();
			}
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) { System.out.println("end:" + e); }
		member.remove(this);
	}
	
	public void rotate(double d) {
		
		if(d > -360 && 360 > d) {
			ChatServer.turtledata.get(id).a = ChatServer.turtledata.get(id).getA() + d;
			if(ChatServer.turtledata.get(id).getA() >= 360) {
				ChatServer.turtledata.get(id).a = ChatServer.turtledata.get(id).getA() - 360;
			}
			else if(ChatServer.turtledata.get(id).getA() < 0) {
				ChatServer.turtledata.get(id).a = 360 + ChatServer.turtledata.get(id).getA();
			}
			ChatServer.turtledata.get(id).e = ChatServer.turtledata.get(id).getE() - Math.abs(d);
		}
		else {
			out.println("数値が正しくありません。");
		}
	}
	
	public void walk(double d) {
		
		if(d >= -100 && 100 >= d) {
			
			double ang = 0;
			if(ChatServer.turtledata.get(id).getA() >= 270) {
				ang = Math.abs(360 - ChatServer.turtledata.get(id).getA());
			}else if(ChatServer.turtledata.get(id).getA() >= 180) {
				ang = Math.abs(ChatServer.turtledata.get(id).getA() - 180);
			}else if(ChatServer.turtledata.get(id).getA() >= 90) {
				ang = Math.abs(180 - ChatServer.turtledata.get(id).getA());
			}else{
				ang = Math.abs(ChatServer.turtledata.get(id).getA());
			}
			
			double x = Math.cos(Math.toRadians(ang)) * d;
			double y = Math.sin(Math.toRadians(ang)) * d;
			
			if(ChatServer.turtledata.get(id).getA() >= 270) {
				ChatServer.turtledata.get(id).x = ChatServer.turtledata.get(id).getX() + x;
				ChatServer.turtledata.get(id).y = ChatServer.turtledata.get(id).getY() - y;
			}else if(ChatServer.turtledata.get(id).getA() >= 180) {
				ChatServer.turtledata.get(id).x = ChatServer.turtledata.get(id).getX() - x;
				ChatServer.turtledata.get(id).y = ChatServer.turtledata.get(id).getY() - y;
			}else if(ChatServer.turtledata.get(id).getA() >= 90) {
				ChatServer.turtledata.get(id).x = ChatServer.turtledata.get(id).getX() - x;
				ChatServer.turtledata.get(id).y = ChatServer.turtledata.get(id).getY() + y;
			}else {
				ChatServer.turtledata.get(id).x = ChatServer.turtledata.get(id).getX() + x;
				ChatServer.turtledata.get(id).y = ChatServer.turtledata.get(id).getY() + y;
			}
			
			ChatServer.turtledata.get(id).e = ChatServer.turtledata.get(id).getE() - Math.abs(d);
		}
		else {
			out.println("数値が正しくありません。");
		}
	}
	
	public void output() throws InterruptedException, IOException {
		for(ChatMThread client : member){
			client.out.println("moveto " + ChatServer.turtledata.get(id));
			Thread.sleep(500);
		}
		synchronized (ChatServer.file) {
			ChatServer.fileout.write("moveto " + ChatServer.turtledata.get(id)+ "\n");
			ChatServer.fileout.flush();
		}
		System.out.println("moveto " + ChatServer.turtledata.get(id));
	}
}

class TurtleD{
	String id;
	public double x;
	public double y;
	public double a;
	public double e;
	
	TurtleD(String id, double x, double y,double a, double e) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.a = a;
		this.e = e;
	}

	@Override
	public String toString() {
		return (id + " " + x + " " + y + " " + a + " " + e);
	}
	
	String getID() {
		return id;
	}

	double getX() {
		return x;
	}

	double getY() {
		return y;
	}
	
	double getA() {
		return a;
	}
	
	double getE() {
		return e;
	}
}


