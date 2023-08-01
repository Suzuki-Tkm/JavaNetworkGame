
//カウンセラーサーバ
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class KaiwaServer {
	public static void main(String[] args) throws IOException {
		ServerSocket serverS = null;
		Socket clientS = null;
		try {
			serverS = new ServerSocket(50001);
		} catch (IOException e) {
			System.out.println("ポートにアクセスできません。");
			System.exit(1);
		}
		System.out.println("KaiwaServer起動");
		String file = "/Users/apple/Desktop/" + java.time.LocalDateTime.now().toString() + ".txt";
		FileWriter fileout = new FileWriter(file);
		try {
			clientS = serverS.accept();
			String ip = clientS.getRemoteSocketAddress().toString();
			ip = ip.replace("/", "");
			String time = java.time.LocalDateTime.now().toString();
			System.out.println(time + "に" + ip + "と接続しました。");
			fileout.write(time + "に" + ip + "と接続しました。");
		} catch (IOException e) {
			System.out.println("Acceptに失敗しました。");
			System.exit(1);
		}
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientS.getOutputStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(clientS.getInputStream()));
		String fromC, fromUser;
		Counsel c = new Counsel();
		out.write("何でも話してください。\n");
		out.flush();
		while ((fromUser = in.readLine()) != null) {
			fromC = c.kaiwa(fromUser);
			String ip = clientS.getRemoteSocketAddress().toString();
			ip = ip.replace("/", "");
			String time = java.time.LocalDateTime.now().toString();
			System.out.println(time + " " + ip + " : " + fromUser);
			fileout.write(time + " " + ip + " : " + fromUser);
			out.write(fromC + "\n");
			out.flush();
			if (fromC.equals("ではまたにしましょう。"))
				break;
		}
		fileout.close();
		in.close();
		out.close();
		clientS.close();
		serverS.close();
	}
}

class Counsel {
	int i, n = 0;
	String[] henji = { "興味がありますね。", "ほう、そうですか。", "もっと聞かせてください。", "なるほど。", "それで?" };

	String kaiwa(String user) {
		i = (int) (Math.random() * henji.length);
		n++;
		if (n > 10)
			return "ではまたにしましょう。";
		else if (i == 0)
			return (user + "とは" + henji[i]);
		else
			return henji[i];
	}
}
