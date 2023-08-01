import tg.Turtle;
import tg.TurtleFrame;

public class Step2 extends Turtle{

	public static void main(String[] args) {
		Step2 t = new Step2();
		TurtleFrame f = new TurtleFrame();
		f.add(t);
		System.out.println(t.toString());
		for(int i = 1 ; i <= 10 ; i++)t.move(t, i);
	}
	
	public void move(Step2 t , int i) {
		
		double x = 200.0 + 100.0 * Math.sin(i * 1.5);
		double y = 200.0 - i * 10.0;
		double a = (90 * i) % 360;
		if(t.getX() < x) t.up();
		else t.down();
		t.moveTo(x , y , a);
		System.out.println(t.toString());
		if(t.isDown())t.lt(35);
		else t.rt(50);
		System.out.println(t.toString());
		System.out.println();
	}
	
	public String toString(){
		return "[" + getX() + " , " + getY() + " , " +getAngle() + " , " + isDown() + "]";
	}

}
