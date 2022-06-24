
//カウンセラーサーバ　マルチクライアントサポート
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class KaiwaMServer {

	static public File file;
	static public Map<String, Integer> map;

	public static void main(String[] args) throws IOException {
		ServerSocket serverS = null;
		boolean end = true;
		try {
			serverS = new ServerSocket(50001);
		} catch (IOException e) {
			System.out.println("ポートにアクセスできません。");
			System.exit(1);
		}
		System.out.println("KaiwaMServer起動");
		String filename = "/Users/apple/Desktop/" + "KaiwaMSeverLog-" + java.time.LocalDateTime.now().toString()
				+ ".txt";
		file = new File(filename);
		map = new HashMap<>();
		while (end) {
			new KaiwaMThread(serverS.accept()).start();
		}
		serverS.close();
	}

}
