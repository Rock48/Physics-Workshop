class Ball {
  Body body;
  float r;
  Vec2 pos;
  
  Ball(float radius) {
    BodyDef bd = new BodyDef();
    bd.type = BodyType.DYNAMIC;
    bd.position.set(box2d.coordPixelsToWorld(mouseX,mouseY));
    body = box2d.createBody(bd);
    CircleShape cs = new CircleShape();
    r = radius;
    cs.m_radius = box2d.scalarPixelsToWorld(r);
    FixtureDef fd = new FixtureDef();
    fd.shape = cs;
    fd.density = 0.1;
    fd.friction = 0.3;
    fd.restitution = 0.68;
 
    body.createFixture(fd);


  }
  void display() {
    pos = box2d.getBodyPixelCoord(body);
    // Get its angle of rotation
    float a = body.getAngle();
    pushMatrix();
    translate(pos.x,pos.y);
    rotate(-a);
    fill(175);
    stroke(0);
    strokeWeight(2);
    ellipse(0,0,r*2,r*2);
    line(0,0,r,0);
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
  void applyForce(Vec2 force) {
    Vec2 posi = body.getWorldCenter();
    body.applyForce(force, posi);
  }
  
}
