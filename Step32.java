import tg.Turtle;
import tg.TurtleFrame;

public class Step32 extends Turtle {

	public static void main(String[] args) {
		double d = 100, x, y, a;
		TurtleFrame f = new TurtleFrame();
		Step32 t = new Step32(200, 300, 0);
		f.add(t);
		System.out.println(t.toString());
		t.fd(d);
		System.out.println(t.toString());
		x = t.getX();
		y = t.getY();
		a = t.getAngle() - 45;
		Step32 t1 = new Step32(x, y, a);
		f.add(t1);
		System.out.println(t1.toString());
		t1.fd(d);
		System.out.println(t1.toString());
		Turtle t2 = t.clone();
		f.add(t2);
		System.out.println(t2.toString());
		t.rt(45);
		System.out.println(t.toString());
		t.fd(d);
		System.out.println(t.toString());
		System.out.println("tとt1 "+t.equals(t1));
		System.out.println("t1とt2 "+t1.equals(t2));
		System.out.println("tとt2 "+t.equals(t2));
	}

	public String toString() {
		return "[" + getX() + " , " + getY() + " , " + getAngle() + " , " + isDown() + "]";
	}
	public Step32(double x, double y , double angle) {	
		super(x , y ,angle);	
	}
}
