package ConvexHull;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class ConvexHull extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 3199433190138968968L;
	
	PointHandler p = new PointHandler();
	MouseInput m;
	
	public static void main(String[] args) {
		new ConvexHull();
	}

	private Thread thread;
	private boolean running = false;

	public ConvexHull() {
		new Window(640, 640, "ConvexHull", this);
		m = new MouseInput(p, this);
		this.addMouseListener(m);
	}
	private void animateLine(Point A, Point B, Graphics g, BufferStrategy bs){
		int x1 = A.x;
		int x2 = B.x;
		int y1 = A.y;
		int y2 = B.y;
		int currY;
		int currX;
		ArrayList<ArrayList<Point>> pts = new ArrayList<ArrayList<Point>>();
		double m = (y2-y1)/(x2-x1);
		if(x1>x2){
			for(currX=x1; currX>x2; currX--){
				System.out.println(currX);
				ArrayList<Point> temp = new ArrayList<Point>();
				currY = (int)Math.floor(   (m*currX) - (m*x1) + y1  );
				temp.add(new Point(currX, currY));
				pts.add(temp);
			}
		}
		if(x2>x1){
			for(currX=x1; currX<x2; currX++){
				System.out.println(currX);
				ArrayList<Point> temp = new ArrayList<Point>();
				currY = (int)Math.floor(   (m*currX) - (m*x1) + y1  );
				temp.add(new Point(currX, currY));
				pts.add(temp);
			}
		}
		
		
		/*for(int i=0; i<pts.size(); i++){
			g.setColor(Color.YELLOW);
			g.drawLine( A.x, A.y,pts.get(i).get(0).x, pts.get(i).get(0).y);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bs.show();
		}*/
	}
	public void renderLines(Graphics g, BufferStrategy bs){
		
		for(int i=0; i<p.lines.size(); i++){
			g.setColor(Color.BLUE);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<Point> temp = p.lines.get(i);
			g.drawLine(temp.get(0).x, temp.get(0).y, temp.get(1).x, temp.get(1).y);
			bs.show();
		}
		for(int i=0; i<p.convexPoints.size()-1; i++){
			g.setColor(Color.GREEN);
			Point temp = p.convexPoints.get(i);
			Point temp2 = p.convexPoints.get((i + 1));
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawLine(temp.x, temp.y, temp2.x, temp2.y);
			bs.show();
		}
		g.drawLine(p.convexPoints.get(p.convexPoints.size()-1).x, p.convexPoints.get(p.convexPoints.size()-1).y, p.convexPoints.get(0).x, p.convexPoints.get(0).y);
		animateLine(p.convexPoints.get(p.convexPoints.size()-1), p.convexPoints.get(0), g, bs);
		
		
	}
	
	public void renderLines_noanimation(Graphics g){
		if(p.lines.isEmpty() || p.convexPoints.isEmpty()){
			return;
		}
		for(int i=0; i<p.lines.size(); i++){
			g.setColor(Color.BLUE);
			ArrayList<Point> temp = p.lines.get(i);
			g.drawLine(temp.get(0).x, temp.get(0).y, temp.get(1).x, temp.get(1).y);
		}
		for(int i=0; i<p.convexPoints.size()-1; i++){
			g.setColor(Color.GREEN);
			Point temp = p.convexPoints.get(i);
			Point temp2 = p.convexPoints.get((i + 1));
			
			
			g.drawLine(temp.x, temp.y, temp2.x, temp2.y);
		}
		g.drawLine(p.convexPoints.get(p.convexPoints.size()-1).x, p.convexPoints.get(p.convexPoints.size()-1).y, p.convexPoints.get(0).x, p.convexPoints.get(0).y);
		
	}
	

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		// ALL RENDERING
		g.setColor(Color.black);
		g.fillRect(0, 0, 640, 640);
		g.setColor(Color.green);
		Font font = new Font("Arial", 1, 15);
		g.setFont(font);
		
		g.drawLine(0, 500, 640, 500);
		g.drawString("Execute", 282, 525);
		g.drawRect(250, 500, 120, 40);
		g.drawString("Clear Points", 268, 570);
		g.drawRect(250, 545, 120, 40);
		p.render(g);
		if(p.quickHullPerformed){
			renderLines(g, bs);
			p.quickHullPerformed = false;
			
		}
		renderLines_noanimation(g);
		// END RENDERING
		g.dispose();
		bs.show();
	}
	@Override
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double fps = 30.0;
		double timePerTick = 1000000000 / fps; // fps in nanoseconds
		double delta = 0;
		long timer = 0;
		int frames = 0;
		
		while (running) {
			render();
			/*
			long now = System.nanoTime();
			timer += now - lastTime;
			delta += (now - lastTime) / timePerTick;
			lastTime = now;
			while (delta >= 1) {
				render();
				delta--;
			}
			if (running) {
				
			}

			frames++;

			if (timer >= 1000000000) {
				System.out.println("FPS : " + frames);
				frames = 0;
				timer = 0;
			}
			*/
		}
		stop();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
