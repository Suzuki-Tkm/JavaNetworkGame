import tg.Turtle;
import tg.TurtleFrame;

public class Step31 extends Turtle{

	public static void main(String[] args) {
		Step31 t = new Step31();
		TurtleFrame f = new TurtleFrame();
		f.add(t);
		System.out.println(t.toString());
		for(int i = 1 ; i <= 10 ; i++) {
			t.fd(i * 10);
			System.out.println(t.toString());
			t.rt(72);
			System.out.println(t.toString());
			System.out.println();
		}
	}
	
	public String toString(){
		return "[" + getX() + " , " + getY() + " , " +getAngle() + " , " + isDown() + "]";
	}

}
