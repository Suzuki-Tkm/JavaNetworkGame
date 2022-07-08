
//カウンセラーサーバ　通信処理スレッド
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class KaiwaMThread extends Thread {
	Socket socket = null;

	public KaiwaMThread(Socket s) {
		super("KaiwaMSThread");
		socket = s;

	}

	public void run() {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String fromC, fromUser;
			Counsel c = new Counsel();
			String ip = socket.getRemoteSocketAddress().toString();
			ip = ip.replace("/", "");
			String time = java.time.LocalDateTime.now().toString();
			System.out.println(time + "に" + ip + "と接続しました。");
			synchronized (KaiwaMServer.file) {
				//FileWriter fileout = new FileWriter(KaiwaMServer.file, true);
				KaiwaMServer.fileout.write(time + "に" + ip + "と接続しました。" + "\n");
				KaiwaMServer.fileout.flush();
			}
			out.write("何でも話してください\n");
			out.flush();
			while ((fromUser = in.readLine()) != null) {
				KaiwaMServer.map.merge(ip, 1, (x, y) -> x + y);
				fromC = c.kaiwa(fromUser);
				ip = socket.getRemoteSocketAddress().toString();
				ip = ip.replace("/", "");
				time = java.time.LocalDateTime.now().toString();
				System.out.println(time + " " + ip + ":" + fromUser);
				synchronized (KaiwaMServer.file) {
					//FileWriter fileout = new FileWriter(KaiwaMServer.file, true);
					KaiwaMServer.fileout.write(time + " " + ip + " : " + fromUser + "\n");
					KaiwaMServer.fileout.flush();
				}
				out.write(fromC + "\n");
				out.flush();
				if (fromC.equals("ではまたにしましょう。"))
					break;
				System.out.println(KaiwaMServer.map.get(ip) + "回送信が確認されました。");
			}
			out.write(KaiwaMServer.map.get(ip) + "回送信が確認されました。" + "\n");
			out.flush();
			synchronized (KaiwaMServer.file) {
				FileWriter fileout = new FileWriter(KaiwaMServer.file, true);
				fileout.write(ip + "との接続を終了します。" + "\n");
				fileout.flush();
			}
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("runメソッド実行中例外: " + e);
			System.exit(1);
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
}
