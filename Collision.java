class Collision{
	
	Collision<T0><T1>(T0 a, T1 b){
		
	}
	
	//boolean CollisionMouseAABB(AAB box)
	
	boolean CollisionAABB(AABB a, AABB b){//AABB/AABB
		return !((b.x-b.border>a.x+a.w+a.border) || (b.x+b.w+b.border<a.x-a.border) || (b.y-b.border>a.y+a.h+a.border) || (b.y+b.h+b.border<a.y-a.border));
	}
	
	boolean CollisionAABBCirc(AABB a, Circ b){//AABB/Circle
		return !((b.x-b.radius-b.border>a.x+a.w+a.border) || (b.x+b.radius+b.border<a.x-a.border) || (b.y-b.radius-b.border>a.y+a.h+a.border) || (b.y+b.radius<a.y-a.border));
	}
	
	boolean CollisionPtCirc(Pt p, Circ c){//Point/Circle
		return Math.pow(p.x-c.x-c.border, 2)+Math.pow(p.y-c.y-c.border, 2) <= Math.pow(c.r+c.border, 2);
	}
	
	boolean CollisionCirc(Circ a, Circ b){//Circle/Circle
		return Math.pow(a.x+a.border-b.x-b.border, 2)+Math.pow(a.y+a.border-b.y-b.border, 2) <= Math.pow(a.r+b.r+a.border+b.border, 2);
	}
	
	boolean CollisionLineCirc(Line l, Circ c){//Line/Circle
		Vector u = new Vector(l.e.x-l.s.x+l.border, l.e.y-l.s.y+l.border), v = new Vector(c.x-c.border-l.s.x-l.border, c.y-c.border-l.s.y-l.border);
		return (Math.abs(u.cross(v))/u.length())<(c.r+c.border);
	}
	
	boolean CollisionSegCirc(Pt a, Pt b, Circ c){//Segment/Circle
		Vector u = new Vector(b.x-a.x, b.y-a.y), v = new Vector(c.x-a.x, c.y-a.y), w = new Vector(c.x-b.x, c.y-b.y);
		return CollisionLineCirc(a, b, c) || (u.dot(v)>=0 && -u.x*w.x-u.y*w.y>=0) || CollisionPtCirc(a, c) || CollisionPtCirc(b, c);
	}
	
	boolean CollisionLineSeg(Pt a, Pt b, Pt o, Pt p){//Line/Segment
		Vector u = new Vector(b.x-a.x, b.y-a.y), v = new Vector(p.x-a.x, p.y-a.y), w = new Vector(o.x-a.x, o.y-a.y);
		return u.cross(v)*u.cross(w)<0;
	}
	
	boolean CollisionSeg(Pt a, Pt b, Pt o, Pt p){//Segment/Segment
		Vector u = new Vector(b.x-a.x, b.y-a.y), v = new Vector(p.x-a.x, p.y-a.y), w = new Vector(o.x-a.x, o.y-a.y);
		return CollisionLineSeg(a, b, o, p);
	}
	
	public String toString(){
		return "Collision()";
	}
}

class AABB{//Axe Aligned Bounding Box
	int x, y, w, h, border = 1;
	Vector vel = new Vector(0, 0), norm = new Vector(0, 0);
	
	AABB(Pt s, int[] size){
		x = s.x;
		y = s.y;
		w = size[0];
		h = size.length>1? size[1]: size[0];
		border = 1;
		vel = new Vector(0, 0);
		norm = vel.getNormal();
	}
	
	AABB(int px, int py, int pw, int ph, int b){
		x = px;
		y = py;
		w = pw;
		h = ph;
		border = b;
		vel = new Vector(0, 0);
		norm = vel.getNormal();
	}
	
	AABB(int px, int py, int pw, int ph, int b, Vector v){
		x = px;
		y = py;
		w = pw;
		h = ph;
		border = b;
		vel = v;
		norm = vel.getNormal();
	}
	
	public void update(){
		x += vel.x;
		y += vel.y;
		norm = vel.getNormal();
	}
	
	public void stop(){
		vel = norm = new Vector(0, 0);
	}
	
	public boolean equals(AABB a){
		return x==a.x && y==a.y && w==a.w && h==a.h && border==a.border && vel.equals(a.vel);
	}
	
	public String toString(){
		return "AAB(x="+x+", y="+y+", width="+w+", height="+h+", velocity="+vel.toString()+")";
	}
	
	public getEdge(String s){
		return s=='l'?  x-1-border: (s=='r'? x+1+border: (s=='u'? y-1-border: y+1+border));
	}
	
	public boolean hit<T>(T obj, String side){
		return s=='l'?  obj.offset('l')<=this.getEdge('r'): (s=='r'? obj.offset('r')>=this.getEdge('l'): (s=='u'? obj.offset('u')<=this.getEdge('d'): (s=='d'? obj.offset('d')>=this.getEdge('u'): (this.hit<T>(obj, 'l')||this.hit<T>(obj, 'r')||this.hit<T>(obj, 'u')||this.hit<T>(obj, 'd')))));
	}
	
	public void bounce(Vector n){
		vel.reflect(n);
	}
	
	public AABB copy(){
		return new ABB(x, y, w, h, vel);
	}
	
	public AABB mult(int k){
		x *= k;
		y *= k;
		return this;
	}
	
	public AABB div(int k){
		x /= k;
		y /= k;
		return this;
	}
	
	public AABB add(Vector v){
		x += v.x;
		y += v.y;
		return this;
	}
	
	public AABB sub(Vector v){
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	public AABB concat(AABB a){
		w = a.x-x-w;//or w+a.x+a.w
		h = a.y-y-h;//or h+a.y+a.h
	}
	
	public AABB deconcat(AABB a){
		w = (a.x-x)/2;//(a.x+a.w)/2
		h = (a.y-y)/2;//(a.y+a.h)/2
	}
	
	public double getSpeed(){
		return Math.sqrt(Math.pow(vel.x, 2)+Math.pow(vel.y, 2));
	}
}

class Circ{//Circle
	int x, y, r, border;//r=radius
	Vector vel = new Vector(0, 0), norm;//velocity, normal
	
	Circ(Vector p, int pr, Vector v){
		x = p.x;
		y = p.y;
		r = pr;
		vel = v;
		border = 1;
		norm = vel.getNormal();
	}
	
	Circ(Vector p, int pr, Vector v, int b){
		x = p.x;
		y = p.y;
		r = pr;
		vel = v;
		border = b;
		norm = vel.getNormal();
	}
	
	Circ(int px, int py, int pr, int b){
		x = px;
		y = py;
		r = pr;
		border = b;
		vel = Vector(0, 0);
		norm = vel.getNormal();
	}
	
	public void update(){
		x += vel.x;
		y += vel.y;
		norm = vel.getNormal();
	}
	
	public void stop(){
		vel = norm = new Vector(0, 0);
	}
	
	public int offset(char s){
		return s=='l'?  x-r: (s=='r'? x+r: (s=='u'? y-r: y+r));
	}
	
	public void bounce(Vector n){
		vel.reflect(n);
	}
	
	public boolean equals(Circ a){
		return x==a.x && y==a.y && r==a.r && border==a.border && vel.equals(a.vel);
	}
	
	public String toString(){
		return "Circ(x="+x+", y="+y+", radius="+r+", velocity="+vel.toString()+")";
	}
	
	public boolean hit<T>(T obj, String s){//more like a getHit(obj) but for also circle/circle situations
		if(obj.hit<T>(this, '')){
			this.bounce(obj.norm);
			this.update();
			return true;
		}
		return false;
	}
	
	public double getSpeed(){
		return Math.sqrt(Math.pow(vel.x, 2)+Math.pow(vel.y, 2));
	}
}

class Pt{//Point
	int x, y;
	Vector vel;
	Pt(int px, int py){
		x = px;
		y = py;
		vel = Vector(0, 0);
	}
	
	Pt(int p){
		x = y = p;
		vel = Vector(0, 0);
	}
	
	public boolean equals(Pt p){
		return x==p.x && y==p.y;
	}
	
	public String toString(){
		return "Pt(x="+x+", y="+y+")";
	}
	
	public double getSpeed(){
		return Math.sqrt(Math.pow(vel.x, 2)+Math.pow(vel.y, 2));
	}
}

class Line{
	Pt s, e;
	int border;
	
	Line(Pt a, Pt b){
		s = a;
		e = b;
		border = 1;
	}
	
	Line(Pt a, Pt b, int b){
		s = a;
		e = b;
		border = b;
	}
	
	Line(Pt c){
		s = Pt(0, 0);
		e = c;
		border = 1;
	}
	
	Line(Pt a, Vector c){
		s = a;
		e = Pt(s.x+c.x, s.y+c.y);
	}
	
	public boolean equals(Line l){
		return s==l.s && e==l.e;
	}
	
	public String toString(){
		return "Line(start="+s.toString()+", end="+e.toString()+")";
	}
}

class Vector{
	int x, y;
	
	Vector(int px, int py){
		x = px;
		y = py;
	}
	
	Vector(){
		x = 0;
		y = 0;
	}
	
	public String toString(){
		return "Vector(x="+x+", y="+y+")";
	}
	
	public boolean equals(Vector v){
		return x==v.x && y==v.y;
	}
	
	public Vector copy(){
		return new Vector(x, y);
	}
	
	public Vector mult(int k){
		x *= k;
		y *= k;
		return this;
	}
	
	public Vector div(int k){
		x /= k;
		y /= k;
		return this;
	}
	
	public Vector add(Vector v){
		x += v.x;
		y += v.y;
		return this;
	}
	
	public Vector sub(Vector v){
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	public void normalise(){
		double v = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
		x /= v;
        y /= v;
	}
	
	public String getNormal(){
		return new Vector(x/Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)), y/Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)));
	}
	
	public Vector zero(){
		x = y = 0;
		return this;
	}
	
	public Vector neg(){
		x = -x;
		y = -y;
		return this;
	}
	
	public int dot(Vector v){
		return x*v.x+y*v.y;
	}
	
	public int cross(Vector v){
		return x*v.y-y*v.x;
	}
	
	public double lenSq(){
		return Math.pow(x, 2)+Math.pow(y, 2);
	}
	
	public double length(){
		return Math.sqrt(this.lenSq());
	}
	
	public reflect(Vector normal){//.. on a normal
		Vector n = normal.copy();
		n.mult(2*this.dot(normal));
		this.sub(n);
		return this;
	}
	
	public double angle(Vector v){
		return Math.acos((x*v.x+y*v.y)/(this.length()*v.length()))
	}
}
