
//カウンセラークライアント
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class KaiwaClient {
	public static void main(String[] args) throws IOException, InterruptedException {
		Socket kaiwaS = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		String file = null;
		String textwords[] = new String[11];
		boolean keyorfile = false;
		int i = 0;
		if(args.length == 1) {
			file = String.valueOf(args[0]);
			keyorfile = true;
			System.out.println("ファイルが認識されました。");
			try(BufferedReader din = new BufferedReader(new FileReader(file));){
				String s;
				while((s = din.readLine())!=null) {
					textwords[i] = s;
					i++;
				}
			}catch(IOException e) {}
		}
		
		try {
			kaiwaS = new Socket(InetAddress.getLocalHost(), 50001);
			in = new BufferedReader(new InputStreamReader(kaiwaS.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(kaiwaS.getOutputStream()));
		} catch (UnknownHostException e) {
			System.out.println("ホストに接続できません。");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("IOコネクションを得られません。");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer;
		String fromUser = null;
		i = 0;
		while ((fromServer = in.readLine()) != null ) {
			System.out.println("カウンセラー: " + fromServer);
			if (fromServer.equals("ではまたにしましょう。"))
				break;
			System.out.print("あなた: ");
			if(keyorfile == false)fromUser = stdIn.readLine();
			else if(keyorfile == true) {
					System.out.println(textwords[i]);
					fromUser = textwords[i];
					Thread.sleep(1000);

			}
			out.write(fromUser + "\n");
			out.flush();
			i++;
		}
		System.out.println("Server: " + in.readLine());

		out.close();
		in.close();
		stdIn.close();
		kaiwaS.close();
	}
}
