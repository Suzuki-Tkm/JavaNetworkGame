import tg.Turtle;

public class HTurtle extends Turtle {
	
	public String name = "";
	
	public HTurtle(double x, double y , double angle) {
		
		super(x , y ,angle);
		
	}
	
	public HTurtle() {
		
	}
	
	public HTurtle(double x, double y , double angle,String name) {
		super(x , y ,angle);
		this.name = name;
	}
	
	public synchronized double polygon(int n, double s){
		double a = 360.0/n;
		for(int j = 0; j < n; j++){
			fd(s);
			rt(a);	
		}
		return n * s;      
		}
	
	public synchronized void house(double s){
		polygon(4, s);
		fd(s);
		rt(30);
		polygon(3, s);
		lt(30); 
		bk(s);	
	}
	
	public String toString(){
		return "\n" + "名前 = " + this.name +
				",オブジェクトのクラス名 = " + getClass().getName() +
				",x = " +  getX() +
				",y = " + getY() + 
				",angle = " + getAngle() + "\n";
	}
}
