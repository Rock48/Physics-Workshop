import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import pbox2d.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PhysicsWorkshop extends PApplet {






ArrayList<Box> boxes;
ArrayList<Ball> balls;
PBox2D box2d;
Surface surface;
Boundary b1;
Boundary b2;
Boundary b3;
Boundary b4;
float boxWidth;
boolean itemAdded;
boolean checkGravity;
boolean finalGravity;
String gameState;
Vec2 force;
float ballRad;
String itemSelected;
float boxRot;
boolean firstBall;
PFont font;
boolean frozenEnabled;
int screwsRemaining;
boolean menuOpen;
String gamestateBuffer;

int b1r;
int b2r;

int b1g;
int b2g;

int b1b;
int b2b;

boolean button1;
boolean button2;

public void setup(){
  b1r = 255;
  b2r = 255;
  b1g = 255;
  b2g = 255;
  b1b = 255;
  b2b = 255;
  button1 = false;
  button2 = false;
  menuOpen = false;
  screwsRemaining = 3;
  frozenEnabled = false;
  stdOut("Creating Fonts");
  font = createFont("Comic Sans MS", 12);
  textAlign(CENTER);
  stdOut("Setting up variables");
  firstBall = true;
  gameState = "planning";
  noCursor();
  force = new Vec2();
  boxWidth = 25;
  itemAdded = false;
  size(1280,720);
  boxes = new ArrayList<Box>(); 
  balls = new ArrayList<Ball>();
  box2d = new PBox2D(this);
  stdOut("Initializing World");
  box2d.createWorld();
  surface = new Surface();
  box2d.setGravity(0,0);
  itemSelected = "box";
  ballRad = random(8,16);
  boxRot = 0;
  b1 = new Boundary(640,10,1280,20);
  b2 = new Boundary(10,360,20,720);
  b3 = new Boundary(1270,360,20,720);
  b4 = new Boundary(640,710,1280,20);
  stdOut("Game Starting");
  
}
public void draw() {
  background(255);

  if(gameState!="menu"){
    box2d.step();
  }
  
  if(itemSelected == "box"){
    if(keyPressed && key == 'd' && boxWidth < 250){
      boxWidth += 1;
    }
    if(keyPressed && key == 'a' && boxWidth > 16){
      boxWidth -= 1;
    }
    if(keyPressed && key == 'q'){
      boxRot += 0.01f;
    }
    if(keyPressed && key == 'e'){
      boxRot -= 0.01f;
    }
  }
  
  for (Box b: boxes) {
    b.display();
  }
  for (Ball ba: balls) {
    ba.display();
    if(ba.getX()>1000 && firstBall){
      gameState="won";
      firstBall = false;
    }

  }
  if(gameState == "menu"){
    cursor();
  }
  if(gameState == "preGame"){
    noCursor();
  }
  if(gameState == "won"){
    noCursor();
  }
  if(gameState == "planning"){
    noCursor();
  }
  if(gameState == "game"){
    cursor();
  }
  surface.display();
  b1.display();
  b2.display();
  b3.display();
  b4.display();
  if(menuOpen){
    menu();
  }
  //Display Goal Area
  pushMatrix();
    fill(50,50,150,100);
    strokeWeight(2);
    stroke(0,0,0,100);
    rectMode(CORNER);
    rect( 1000,20,260,680, 50);
   popMatrix();
   pushMatrix();
    fill(50,200,50);
    textFont(font);
    textSize(35);
    translate(1140,325);
    strokeWeight(1);
    textAlign(CENTER);
    text("Goal Area",0,0);
   popMatrix();
  
  if(gameState=="preGame"){
    pushMatrix();
    fill(50,200,50,100);
    strokeWeight(2);
    stroke(0,0,0,100);
    rectMode(CORNER);
    rect(20,20,280,680, 50);
    popMatrix();
    pushMatrix();
    fill(50,50,200);
    textFont(font);
    textSize(35);
    text("Place the\n ball in\n this area",150,330);
    popMatrix();
  }
  if(itemSelected=="box" && gameState == "planning"){
    pushMatrix();
      translate(mouseX,mouseY);
      rotate(-boxRot);
      fill(175, 175, 175, 100);
      strokeWeight(2);
      stroke(0,0,0,100);
      rectMode(CENTER);
      rect(0,0,boxWidth,16);
      popMatrix();
      if(frozenEnabled){
        pushMatrix();
        fill(0);
        translate(25,60);
        textMode(NORMAL);
        textAlign(LEFT);
        text("Screw enabled!", 0, 0);
        translate(0,0);
        popMatrix();
      }
      if(screwsRemaining <= 0){
        frozenEnabled = false;
        fill(0);
        pushMatrix();
        translate(25,60);
        textAlign(LEFT);
        text("Screws Depleated!", 0, 0);
        translate(0,0);
        popMatrix();
       }
        pushMatrix();
        fill(0);
        translate(25,670);
        textAlign(LEFT);
        text("Screws Remaining: "+screwsRemaining, 0, 0);
        translate(0,0);
        popMatrix();
  }
  if(itemSelected=="ball" && gameState == "preGame"){
    pushMatrix();
    translate(mouseX,mouseY);
    fill(175,175,175,100);
    stroke(0,0,0,100);
    strokeWeight(2);
    ellipse(0,0,ballRad*2,ballRad*2);
    line(0,0,ballRad,0);
    popMatrix();
  }
  /*if(itemSelected=="screw"){
    pushMatrix();
    translate(mouseX,mouseY);
    text("screw not yet implimented",0,-6);
    popMatrix();
  }*/
  if(checkGravity && !finalGravity){
    box2d.setGravity(0,-9.81f);
    for(Box b: boxes) {

      b.applyForce(force);
      
    }
    for(Ball ba: balls) {
      ba.applyForce(force);
    }
  }
  if(gameState == "won"){
    noCursor();
    pushMatrix();
   translate(mouseX,mouseY);
    textSize(50);
    text("YOU WIN!!!!!!",0,25);
    popMatrix();
  }
}
public void mouseReleased(){
 if(mouseButton == LEFT && itemAdded){
   itemAdded = false;
 }
}
public void mousePressed(){
  if (mouseButton == LEFT && !itemAdded && itemSelected == "box" && gameState== "planning") {
    if(screwsRemaining > 0){
      Box p = new Box(boxWidth, 16, boxRot, frozenEnabled);
          boxes.add(p);
    }else{
      Box p = new Box(boxWidth, 16, boxRot, false);
          boxes.add(p);
    }
    if(frozenEnabled)
    screwsRemaining--;

    itemAdded = true;
  }
  if (mouseButton == LEFT && !itemAdded && itemSelected == "ball" && mouseX < 300 && gameState == "preGame") {
    Ball q = new Ball(ballRad);
    balls.add(q);
    ballRad = random(8,16);
    itemAdded = true;
    gameState = "game";
    checkGravity = true; 
    itemSelected = null;
    cursor();
  }
  if (mouseButton == LEFT){
    menuButtons();

    
  }
}
public void keyPressed(){
  if(key==' '){
    if(gameState == "planning"){
      gameState = "preGame";
      itemSelected = "ball";
    }
    else if(gameState == "preGame"){
  
    }
  }
  if(key=='b' && gameState == "planning"){
    frozenEnabled = !frozenEnabled;
  }
  if(key==ESC){
        toggleMenu();
    key = 0;

  }
}
public void stdOut(String par1){
  println("[INFO] [STDOUT]"+par1);
  //pause(500);
}
public void stdErr(String par1){
  println("[INFO] [STDERR]"+par1);
}
public void toggleMenu(){
  if(gameState == "menu"){
    gameState = gamestateBuffer;
  }else{
    gamestateBuffer = gameState;
    gameState = "menu";
  }
  menuOpen = !menuOpen;
}
public void menu(){

  if(mouseX>=width/2-156 && mouseX <= width/2-156+300 && mouseY>=height/3-6.5f && mouseY<=height/3-6.5f+40){
    button1 = true;
    b1r = 50;
    b1g = 80;
    b1b = 185;
  }else{
    button1 = false;
    b1r = 255;
    b1g = 255;
    b1b = 255;
  }
  if(mouseX>=width/2-156 && mouseX <= width/2-156+300 && mouseY>=height/2+2 && mouseY<=height/2+42){
    button2 = true;
    b2r = 200;
    b2g = 70;
    b2b = 50;
  }else{
    button2 = false;
    b2r = 255;
    b2g = 255;
    b2b = 255;
  }
  pushMatrix();
    translate(width/2-156,height/3-6.5f);
    
    fill(b1r,b1g,b1b);
    strokeWeight(2);
    stroke(0);
    rectMode(NORMAL);
    rect(0,0,300,40);
  popMatrix();
  
  pushMatrix();
    translate(width/2-156,height/2+2);
    fill(b2r,b2g,b2b);
    strokeWeight(2);
    stroke(0);
    rectMode(NORMAL);
    rect(0,0,300,40);
  popMatrix();
 
  //menu text
    fill(0);
  pushMatrix();
  translate(width/2-10,height/3+25);
  textAlign(CENTER);
  text("Return to Game \n\n Exit", 0, 0);
  translate(0,0);
  popMatrix(); 
}
public synchronized void menuButtons(){
      if(button1){
      toggleMenu();
      button1 = false;
    }
    if(button2){
      try{
        wait(100);
      }catch(Exception e){
        print(e);
      }
      exit();
    }
}
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
    fd.density = 0.1f;
    fd.friction = 0.3f;
    fd.restitution = 0.68f;
 
    body.createFixture(fd);


  }
  public void display() {
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
  public void remove(){
    box2d.destroyBody(body); 
  }
  public float getY(){
    return pos.y;
  }
  public float getX(){
    return pos.x;
  }
  public void applyForce(Vec2 force) {
    Vec2 posi = body.getWorldCenter();
    body.applyForce(force, posi);
  }
  
}
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
  public void display(){
    fill(0);
    stroke(0);
    rectMode(CENTER);
    rect(x,y,w,h);
  }
}

class Surface {
  ArrayList<Vec2> surface;
  
  Surface(){
    println("[STDOUT]Generating Terrain");
    surface = new ArrayList<Vec2>();
    float y=0;
    for(int i=0; i<width;i++){
      surface.add(new Vec2(i,map(noise(y/2.7f),0,1,0,height)));
      y+=0.01f;
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
  public void display(){
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
    fd.density = 0.3f;
    fd.friction = 0.5f;
    fd.restitution = 0.3f;
    
    body.createFixture(fd);
  }
  public void display() {
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
  public void remove(){
    box2d.destroyBody(body); 
  }
  public float getY(){
    return pos.y;
  }
  public float getX(){
    return pos.x;
  }
  public float getWidth(){
   // return body.width;
   return 0;
  }
  public float getHeight(){
   // return body.height;
   return 0;
  }
  public void applyForce(Vec2 force) {
    Vec2 posi = body.getWorldCenter();
    body.applyForce(force, posi);
  }
}  
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PhysicsWorkshop" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
