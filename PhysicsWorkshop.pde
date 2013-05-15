import pbox2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;

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

void setup(){
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
void draw() {
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
      boxRot += 0.01;
    }
    if(keyPressed && key == 'e'){
      boxRot -= 0.01;
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
    box2d.setGravity(0,-9.81);
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
void mouseReleased(){
 if(mouseButton == LEFT && itemAdded){
   itemAdded = false;
 }
}
void mousePressed(){
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
void keyPressed(){
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
void stdOut(String par1){
  println("[INFO] [STDOUT]"+par1);
  //pause(500);
}
void stdErr(String par1){
  println("[INFO] [STDERR]"+par1);
}
void toggleMenu(){
  if(gameState == "menu"){
    gameState = gamestateBuffer;
  }else{
    gamestateBuffer = gameState;
    gameState = "menu";
  }
  menuOpen = !menuOpen;
}
void menu(){

  if(mouseX>=width/2-156 && mouseX <= width/2-156+300 && mouseY>=height/3-6.5 && mouseY<=height/3-6.5+40){
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
    translate(width/2-156,height/3-6.5);
    
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
synchronized void menuButtons(){
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
