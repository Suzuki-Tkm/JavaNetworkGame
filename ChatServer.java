import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.out.println("起動方法: java ChatServer ポート番号");
			System.out.println("例: java ChatServer 50002");
			System.exit(1);
		}
		int port = Integer.valueOf(args[0]).intValue();
		ServerSocket serverS = null;
		boolean end = true;
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
	ChatMThread(Socket s) {
		super("ChatMThread");
		socket = s;

		if (member == null) {
			member = new ArrayList<ChatMThread>();
		}
		member.add(this);
	}

	public void run() {
		try{
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			String fromClient;
			while ((fromClient = in.readLine()) != null) {
				for(ChatMThread client : member){
					client.out.println(fromClient);
				}
			}
		}catch(IOException e){ System.out.println("run:" + e); }
		end();
	}

	public void end() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) { System.out.println("end:" + e); }
		member.remove(this);
	}
}


