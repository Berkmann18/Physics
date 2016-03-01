//boolean CollisionMouseAABB(AAB box)
	
function CollisionAABB(a, b){//AABB/AABB
	return !((b.x-b.border>a.x+a.w+a.border) || (b.x+b.w+b.border<a.x-a.border) || (b.y-b.border>a.y+a.h+a.border) || (b.y+b.h+b.border<a.y-a.border));
}

function CollisionAABBCirc(a, b){//AABB/Circle
	return !((b.x-b.radius-b.border>a.x+a.w+a.border) || (b.x+b.radius+b.border<a.x-a.border) || (b.y-b.radius-b.border>a.y+a.h+a.border) || (b.y+b.radius<a.y-a.border));
}

function CollisionPtCirc(p, c){//Point/Circle
	return Math.pow(p.x-c.x-c.border, 2)+Math.pow(p.y-c.y-c.border, 2) <= Math.pow(c.r+c.border, 2);
}

function CollisionCirc(a, b){//Circle/Circle
	return Math.pow(a.x+a.border-b.x-b.border, 2)+Math.pow(a.y+a.border-b.y-b.border, 2) <= Math.pow(a.r+b.r+a.border+b.border, 2);
}

function CollisionLineCirc(l, c){//Line/Circle
	var u = new Vector(l.e.x-l.s.x+l.border, l.e.y-l.s.y+l.border), v = new Vector(c.x-c.border-l.s.x-l.border, c.y-c.border-l.s.y-l.border);
	return (Math.abs(u.cross(v))/u.length())<(c.r+c.border);
}

function CollisionSegCirc(a, b, c){//Segment/Circle
	var u = new Vector(b.x-a.x, b.y-a.y), v = new Vector(c.x-a.x, c.y-a.y), w = new Vector(c.x-b.x, c.y-b.y);
	return CollisionLineCirc(a, b, c) || (u.dot(v)>=0 && -u.x*w.x-u.y*w.y>=0) || CollisionPtCirc(a, c) || CollisionPtCirc(b, c);
}

function CollisionLineSeg(a, b, o, p){//Line/Segment
	var u = new Vector(b.x-a.x, b.y-a.y), v = new Vector(p.x-a.x, p.y-a.y), w = new Vector(o.x-a.x, o.y-a.y);
	return u.cross(v)*u.cross(w)<0;
}
	
function CollisionSeg(a, b, o, p){//Segment/Segment
	var u = new Vector(b.x-a.x, b.y-a.y), v = new Vector(p.x-a.x, p.y-a.y), w = new Vector(o.x-a.x, o.y-a.y);
	return CollisionLineSeg(a, b, o, p);
}

function AABB(px, py, pw, ph, b){//Axe Aligned Bounding Box
	this.x = px;
	this.y = py;
	this.w = pw;
	this.h = ph;
	this.border = b||1;
	this.vel = new Vector(0, 0);
	this.norm = this.vel.getNormal();
			
	this.update = function(){
		this.x += this.vel.x;
		this.y += this.vel.y;
		this.norm = this.vel.getNormal();
	}
	
	this.stop = function(){
		this.vel = this.norm = new Vector(0, 0);
	}
	
	this.equals = function(a){
		return this.x==a.x && this.y==a.y && this.w==a.w && this.h==a.h && this.border==a.border && this.vel.equals(a.vel);
	}
	
	this.toString = function(){
		return "AAB(x="+this.x+", y="+this.y+", width="+this.w+", height="+this.h+", velocity="+this.vel.toString()+")";
	}
	
	this.getEdge = function(s){
		return s=='l'?  x-1-border: (s=='r'? x+1+border: (s=='u'? y-1-border: y+1+border));
	}
	
	this.hit = function(obj, side){
		return s=='l'?  obj.offset('l')<=this.getEdge('r'): (s=='r'? obj.offset('r')>=this.getEdge('l'): (s=='u'? obj.offset('u')<=this.getEdge('d'): (s=='d'? obj.offset('d')>=this.getEdge('u'): (this.hit(obj, 'l')||this.hit(obj, 'r')||this.hit(obj, 'u')||this.hit(obj, 'd')))));
	}
	
	this.bounce = function(n){
		this.vel.reflect(n);
	}
	
	this.copy = function(){
		return new ABB(this.x, this.y, this.w, this.h, this.vel);
	}
	
	this.mult(k){
		this.x *= k;
		this.y *= k;
		return this;
	}
	
	this.div = function(k){
		this.x /= k;
		this.y /= k;
		retun this;
	}
	
	this.add = function(v){
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	this.sub = function(v){
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	this.concat(a){
		this.w = a.x-this.x-this.w;//or w+a.x+a.w
		this.h = a.y-this.y-this.h;//or h+a.y+a.h
	}
	
	this.deconcat(a){
		this.w = (a.x-this.x)/2;//(a.x+a.w)/2
		this.h = (a.y-this.y)/2;//(a.y+a.h)/2
	}
	
	this.getSpeed = function(){
		return Math.sqrt(Math.pow(this.vel.x, 2)+Math.pow(this.vel.y, 2));
	}
}

function Circ(px, py, pr, b){//Circle
	this.x = px;
	this.y = py;
	this.r = pr;
	this.border = b||1;
	this.vel = new Vector(0, 0);
	this.norm = vel.getNormal();
	
	this.update = function(){
		this.x += this.vel.x;
		this.y += this.vel.y;
		this.norm = this.vel.getNormal();
	}
	
	public void stop(){
		this.vel = this.norm = new Vector(0, 0);
	}
	
	this.offset = function(s){
		return s=='l'?  this.x-this.r: (s=='r'? this.x+this.r: (s=='u'? this.y-this.r: this.y+this.r));
	}
	
	this.bounce = function(n){
		this.vel.reflect(n);
	}
	
	this.equals = function(a){
		return this.x==a.x && this.y==a.y && this.r==a.r && this.border==a.border && this.vel.equals(a.vel);
	}
	
	this.toString = function(){
		return "Circ(x="+this.x+", y="+this.y+", radius="+this.r+", velocity="+this.vel.toString()+")";
	}
	
	this.hit = function(obj, s){//more like a getHit(obj) but for also circle/circle situations
		if(obj.hit(this, '')){
			this.bounce(obj.norm);
			this.update();
			return true;
		}
		return false;
	}
	
	this.getSpeed = function(){
		return Math.sqrt(Math.pow(this.vel.x, 2)+Math.pow(this.vel.y, 2));
	}
}

function Pt(px, py){//Point
	this.x = px;
	this.y = py||px;
	this.vel = new Vector(0, 0);
	
	this.equals = function(p){
		return this.x==p.x && this.y==p.y;
	}
	
	this.toString = function(){
		return "Pt(x="+this.x+", y="+this.y+")";
	}
	
	this.getSpeed = function(){
		return Math.sqrt(Math.pow(this.vel.x, 2)+Math.pow(this.vel.y, 2));
	}
}

function Line(a, b){
	this.s = a;
	this.e = b;
	
	this.equals = function(l){
		return this.s==l.s && this.e==l.e;
	}
	
	this.toString = function(){
		return "Line(start="+this.s.toString()+", end="+e.toString()+")";
	}
}

function Vector(px, py){
	this.x = px;
	this.y = py||px;
	
	this.toString = function(){
		return "Vector(x="+this.x+", y="+this.y+")";
	}
	
	this.equals = function(v){
		return this.x==v.x && this.y==v.y;
	}
	
	this.copy = function(){
		return new Vector(this.x, this.y);
	}
	
	this.mult = function(k){
		this.x *= k;
		this.y *= k;
		return this;
	}
	
	this.div = function(k){
		this.x /= k;
		this.y /= k;
		retun this;
	}
	
	this.add = function(v){
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	this.sub(v){
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	this.normalise(){
		var v = Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y, 2));
		this.x /= v;
        this.y /= v;
	}
	
	this.getNormal = function(){
		return new Vector(this.x/Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y, 2)), this.y/Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y, 2)));
	}
	
	this.zero = function(){
		this.x = this.y = 0;
		return this;
	}
	
	this.neg = function(){
		this.x = -x;
		this.y = -y;
		return this;
	}
	
	this.dot = function(v){
		return this.x*v.x+this.y*v.y;
	}
	
	this.cross = function(v){
		return this.x*v.y-this.y*v.x;
	}
	
	this.lenSq = function(){
		return Math.pow(this.x, 2)+Math.pow(this.y, 2);
	}
	
	this.length = function(){
		return Math.sqrt(this.lenSq());
	}
	
	this.reflect = function(normal){//.. on a normal
		Vector n = this.normal.copy();
		n.mult(2*this.dot(this.normal));
		this.sub(n);
		return this;
	}
	
	this.angle = function(v){
		return Math.acos((this.x*v.x+this.y*v.y)/(this.length()*v.length()))
	}
}
