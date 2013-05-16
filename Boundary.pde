class Boundary {
  float x,y;
  float w,h;
  
  Body b;
  
  Boundary(float x_, float y_, float w_, float h_){
    x = x_;
    y = y_;
    w = w_;
    h = h_;
    
    BodyDef bd = new BodyDef();
    bd.position.set(box2d.coordPixelsToWorld(x,y));
    bd.type = BodyType.STATIC;
    b = box2d.createBody(bd);
    float box2dW = box2d.scalarPixelsToWorld(w/2);
    float box2dH = box2d.scalarPixelsToWorld(h/2);
    PolygonShape ps = new PolygonShape();
    ps.setAsBox(box2dW, box2dH);
    
    b.createFixture(ps, 1);
  }
  void display(){
    fill(0);
    stroke(0);
    rectMode(CENTER);
    rect(x,y,w,h);
  }
}

class Surface {
  ArrayList<Vec2> surface;
  
  Surface(){
    PhysicsWorkshop.stdOut("Generating terrain");
    surface = new ArrayList<Vec2>();
    float y=0;
    for(int i=0; i<width;i++){
      surface.add(new Vec2(i,map(noise(y/2.7),0,1,0,height)));
      y+=0.01;
    }
    
    ChainShape chain  = new ChainShape();
    
    Vec2[] vertices = new Vec2[surface.size()];
    
    for(int i=0; i<vertices.length;i++){
      vertices[i] = box2d.coordPixelsToWorld(surface.get(i));
    }
    
    chain.createChain(vertices, vertices.length);
    
    BodyDef bd = new BodyDef();
    Body body = box2d.world.createBody(bd);
    
    body.createFixture(chain, 1);
    }
  void display(){
    translate(0,0);
    strokeWeight(5);
    stroke(0);
    noFill();
    beginShape();
    for(Vec2 v: surface){
      vertex(v.x,v.y);
    }
    endShape();
  }
}
