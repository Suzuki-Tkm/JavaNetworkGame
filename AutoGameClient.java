import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AutoGameClient {
	
	double w = 600, h = 600;
	TextArea text;
	TextField field;
	Socket chatS = null;
	BufferedReader in = null;
	PrintStream out = null;

	static String sName;   //サーバIP
	static int portN;      //ポート番号
	static String uName;   //ユーザ名

	String userName;
	
	public void start() {

		initNet(sName, portN, uName);
		// 別スレッド上でサーバと接続，応答を待って，表示
		new Thread(() -> {startChat();}).start();
		
		Timer timer = new Timer(false);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				int ram = (int)(Math.random()*3);
				if(ram == 0) {
					rotate();
				}else if(ram == 1) {
					walk();
				}	
				else {
					attack();
				}
			}
		};
		
		timer.schedule(task, 0, 10000);
	}
	
	public void rotate() {
		double d = 0;
		double e = (Math.random()*10000)*2;
		d = 10 - e / 1000;
		System.out.println("rotate "+ d);
		out.println("rotate "+ d);
	}
	
	public void walk() {
		double d = 0;
		double e = (Math.random()*10000)*2;
		d = 10 - e / 1000;
		System.out.println("walk "+ d);
		out.println("walk "+ d);
	}
	
	public void attack() {
		double d = 0;
		double e = (Math.random()*10000)*2;
		d = e / 1000;
		System.out.println("attack "+ d);
		out.println("attack "+ d);
	}

	public void startChat() {
		sendMessage(" 接続しました。");
		out.println("connect");
		String fromServer;
		try{
			while ((fromServer = in.readLine()) != null) {
				//text.setText(text.getText().concat(fromServer + "\n"));
				System.out.println(fromServer);
			}
			end();
		}catch (IOException e){
			System.out.println("チャット中に問題が起こりました。");
			System.exit(1);
		}
	}
	
	

	public void sendMessage(String msg) {
		String s = msg;
		//System.out.println("sendMessage  " + s);
		out.println(s);
	}

	//network setup
	public void initNet(String serverName, int port, String uName) {
		userName = uName;
		// create Socket
		try {
			//サーバを別のホストで起動する場合は下の行を有効にする
			//chatS = new Socket(InetAddress.getByName(serverName), port);
			//ローカルホストでテストの場合は上の代わりに下の行を使う
			chatS = new Socket(InetAddress.getLocalHost(), port);
			in = new BufferedReader(
					new InputStreamReader(chatS.getInputStream()));
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
		} catch (IOException e) { System.out.println("end:" + e); }
	}

	public static void main(String... args) {
		if (args.length != 3 && args.length != 4) {
			System.out.println(
					"Usage: java ChatClient サーバのIPアドレス ポート番号 ユーザ名");
			System.out.println("例: java ChatClient 210.0.0.1 50002 ariga");
			System.exit(0);
		}
		// Getting argument.
		sName = args[0];
		portN = Integer.valueOf(args[1]).intValue();
		uName = args[2];
		System.out.println("serverName = " + sName);
		System.out.println("portNumber = " + portN);
		System.out.println("userName = " + uName);
		new AutoGameClient().start();
	}

}
