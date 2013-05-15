class Box {
  Body body;
  float w;
  float h;
  float initAngle;
  Vec2 pos;
  boolean isFrozen;
  
  Box(float w_, float h_, float initAngle_, boolean frozen) {
    w = w_;
    h = h_;
    initAngle = initAngle_;
    isFrozen = frozen;
    BodyDef bd = new BodyDef();
    if(isFrozen) bd.type = BodyType.STATIC;
    else bd.type = BodyType.DYNAMIC;
    bd.position.set(box2d.coordPixelsToWorld(mouseX,mouseY));
    body = box2d.createBody(bd);
    body.setTransform(bd.position,initAngle);
    

    
    PolygonShape ps = new PolygonShape();
    
    float box2dW = box2d.scalarPixelsToWorld(w/2);
    float box2dH = box2d.scalarPixelsToWorld(h/2);
    
    ps.setAsBox(box2dW, box2dH);
    
    FixtureDef fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 0.3;
    fd.friction = 0.5;
    fd.restitution = 0.3;
    
    body.createFixture(fd);
  }
  void display() {
    pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    pushMatrix();
    translate(pos.x,pos.y);
    rotate(-a);
    if(this.isFrozen)
    fill(0);
    else
    fill(175);
    strokeWeight(2);
    stroke(0);
    rectMode(CENTER);
    rect(0,0,w,h);
    popMatrix();
  }
  void remove(){
    box2d.destroyBody(body); 
  }
  float getY(){
    return pos.y;
  }
  float getX(){
    return pos.x;
  }
  float getWidth(){
   // return body.width;
   return 0;
  }
  float getHeight(){
   // return body.height;
   return 0;
  }
  void applyForce(Vec2 force) {
    Vec2 posi = body.getWorldCenter();
    body.applyForce(force, posi);
  }
}  
