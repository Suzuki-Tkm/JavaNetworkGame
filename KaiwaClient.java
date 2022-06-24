
//カウンセラークライアント
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class KaiwaClient {
	public static void main(String[] args) throws IOException {
		Socket kaiwaS = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		String file;
		boolean keyorfile = false;
		if(args.length == 1) {
			file = "/Users/apple/Desktop/" + String.valueOf(args[0]);
			keyorfile = true;
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
		String fromUser;
		while ((fromServer = in.readLine()) != null ) {
			System.out.println("カウンセラー: " + fromServer);
			if (fromServer.equals("ではまたにしましょう。"))
				break;
			System.out.print("あなた: ");
			if(keyorfile == false)fromUser = stdIn.readLine();
			else if(keyorfile == true)
			out.write(fromUser + "\n");
			out.flush();
		}
		System.out.println("Server: " + in.readLine());

		out.close();
		in.close();
		stdIn.close();
		kaiwaS.close();
	}
}
