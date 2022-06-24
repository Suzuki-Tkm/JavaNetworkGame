// File
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
//network
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//チャットクライアント
import javafx.application.Application;
//Scene
import javafx.scene.Scene;
// Button
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
// VBox
import javafx.scene.layout.VBox;
//Stage, FileChooser
import javafx.stage.Stage;

public class ChatClient extends Application {
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
	@Override public void start(Stage stage) {
		//メニュー
		MenuBar bar = new MenuBar();
		Menu m = new Menu("終了");
		MenuItem menuExit = new MenuItem("終了");
		m.getItems().add(menuExit);
		bar.getMenus().add(m);
		//下部ボタンコンテナ
		Button buttonSay = new Button("発言");
		field = new TextField();
		VBox lowerPane = new VBox();
		//lowerPane.setAlignment(Pos.CENTER);
		lowerPane.getChildren().addAll(field, buttonSay);
		//lowerPane.setPadding(new Insets(15, 10, 15, 10));
		//lowerPane.setSpacing(20);
		//上部コンテナ
		ScrollPane upperPane = new ScrollPane();
		upperPane.setPrefSize(w, h);
		upperPane.setFitToHeight(true); //ScrollPaneの高さにノードのサイズを変更
		upperPane.setFitToWidth(true); //ScrollPaneの幅にノードのサイズを変更
		text = new TextArea();
		upperPane.setContent(text);

		VBox root = new VBox();
		root.getChildren().addAll(bar, upperPane, lowerPane);

		Scene scene = new Scene(root);
		stage.setTitle("Chat Client");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();

		menuExit.setOnAction((event)-> {
			System.exit(0);
		});
		buttonSay.setOnAction((event)-> {
			sendMessage(field.getText());
			field.setText("");
			buttonSay.requestFocus();
		});

		initNet(sName, portN, uName);
		// 別スレッド上でサーバと接続，応答を待って，表示
		new Thread(() -> {startChat();}).start();
	}

	public void startChat() {
		sendMessage(" 接続しました。");
		String fromServer;
		try{
			while ((fromServer = in.readLine()) != null) {
				text.setText(text.getText().concat(fromServer + "\n"));
			}
			end();
		}catch (IOException e){
			System.out.println("チャット中に問題が起こりました。");
			System.exit(1);
		}
	}

	public void sendMessage(String msg) {
		String s = userName + ":" + msg;
		System.out.println("sendMessage  " + s);
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
		launch(args);
	}
}


