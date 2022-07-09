import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import tg.Turtle;
import tg.TurtleFrame;

public class GameClient extends Turtle {
	static TurtleFrame f;
	static Map<String,Turtle> turtles;
	
	double w = 600, h = 600;
	TextArea text;
	TextField field;
	Socket chatS = null;
	BufferedReader in = null;
	PrintStream out = null;

	static String sName; // サーバIP
	static int portN; // ポート番号
	static String uName; // ユーザ名
	
	boolean tcolor = true;

	String userName;

	public void start() {

		initNet(sName, portN, uName);
		// 別スレッド上でサーバと接続，応答を待って，表示
		new Thread(() -> {
			startChat();
		}).start();
		new Thread(() -> {
			try {
				usermessage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void startChat() {
		sendMessage(" 接続しました。");
		String fromServer;
		try {
			while ((fromServer = in.readLine()) != null) {
				// text.setText(text.getText().concat(fromServer + "\n"));
				System.out.println(fromServer);
				String[] newStr = fromServer.split("\\s+");
				try {
					switch (newStr[0]) {
					case "generate":
						createt(newStr[1],Double.parseDouble(newStr[2]),Double.parseDouble(newStr[3]),Double.parseDouble(newStr[4]),Double.parseDouble(newStr[5])); break;
					case "moveto":
						moveto(newStr[1],Double.parseDouble(newStr[2]),Double.parseDouble(newStr[3]),Double.parseDouble(newStr[4]),Double.parseDouble(newStr[5])); break;
					case "remove":
						remove(newStr[1]); break;
					}
				}catch(NumberFormatException e){
					out.println("数値の書式が正しくありません。");
				}
			}
			end();
		} catch (IOException e) {
			System.out.println("チャット中に問題が起こりました。");
			System.exit(1);
		}
	}
	
	synchronized public void createt(String id,double x,double y,double a,double e) {
		Turtle t = new Turtle(x,400-y,90-a);
		f.add(t);
		t.tScale = e/10000;
		if(tcolor) {
			t.setTColor(Color.RED);
			tcolor = false;
		}
		turtles.put(id,t);
	}
	
	
	
	public void moveto(String id,double x,double y,double a,double e) {
		turtles.get(id).moveTo(x,400-y,90-a);
		turtles.get(id).tScale = e/10000;
	}
	
	public void remove(String id) {
		f.remove(turtles.get(id));
	}

	public void sendMessage(String msg) {
		String s = msg;
		// System.out.println("sendMessage " + s);
		out.println(s);
	}

	public void usermessage() throws IOException {

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser = null;
		while (true) {
			fromUser = stdIn.readLine();
			out.println(fromUser);
		}

	}

	// network setup
	public void initNet(String serverName, int port, String uName) {
		userName = uName;
		// create Socket
		try {
			// サーバを別のホストで起動する場合は下の行を有効にする
			// chatS = new Socket(InetAddress.getByName(serverName), port);
			// ローカルホストでテストの場合は上の代わりに下の行を使う
			chatS = new Socket(InetAddress.getLocalHost(), port);
			in = new BufferedReader(new InputStreamReader(chatS.getInputStream()));
			out = new PrintStream(chatS.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("ホストに接続できません。");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("IOコネクションを得られません。");
			System.exit(1);
		}
	}

	public void end() {
		try {
			out.close();
			in.close();
			chatS.close();
		} catch (IOException e) {
			System.out.println("end:" + e);
		}
	}

	public static void main(String... args) {
		if (args.length != 3 && args.length != 4) {
			System.out.println("Usage: java ChatClient サーバのIPアドレス ポート番号 ユーザ名");
			System.out.println("例: java ChatClient 210.0.0.1 50002 ariga");
			System.exit(0);
		}
		// Getting argument.
		sName = args[0];
		portN = Integer.valueOf(args[1]).intValue();
		uName = args[2];
		
		turtles = new HashMap<>();
		f = new TurtleFrame();
		
		System.out.println("serverName = " + sName);
		System.out.println("portNumber = " + portN);
		System.out.println("userName = " + uName);
		new GameClient().start();
	}
}
