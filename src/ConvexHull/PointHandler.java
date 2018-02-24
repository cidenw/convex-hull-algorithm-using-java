package ConvexHull;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class PointHandler {
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Point> convexPoints = new ArrayList<Point>();
	boolean quickHullPerformed = false;
	Graphics g;
	
	public boolean isQuickHullPerformed() {
		return quickHullPerformed;
	}

	public void render(Graphics g) {
		this.g = g;
		g.setColor(Color.white);
		for (int i = 0; i < points.size(); i++) {
			g.fillRect(points.get(i).x - 3, points.get(i).y - 3, 6, 6);
			Font font = new Font("Arial", 1, 10);
			g.setFont(font);
			g.drawString("(" + Integer.toString(points.get(i).x) + "," + Integer.toString(points.get(i).y) + ")",
					points.get(i).x - 20, points.get(i).y - 10);
		}
		
		
		if (quickHullPerformed) {
			/**
			g.setColor(Color.green);
			if(points.isEmpty()){
				clearPoints();
				return;
			}
			/**
			for (int i = 0; i < convexPoints.size() - 1; i++) {
				g.drawLine(convexPoints.get(i).x, convexPoints.get(i).y, convexPoints.get(i + 1).x,
						convexPoints.get(i + 1).y);
			}
			g.drawLine(convexPoints.get(0).x, convexPoints.get(0).y, convexPoints.get(convexPoints.size() - 1).x,
					convexPoints.get(convexPoints.size() - 1).y);
		/**/
			//
			//renderLines(g);
		}
		

	}


	public ArrayList<ArrayList<Point>> lines = new ArrayList<ArrayList<Point>>();
	
	
	
	public void performQuickHull() {
		convexPoints = quickHull(new ArrayList<Point>(points));
		quickHullPerformed = true;
	}

	public ArrayList<Point> quickHull(ArrayList<Point> points) {
		ArrayList<Point> convexHull = new ArrayList<Point>();
		if (points.size() < 3)
			return points;

		int minPoint = -1, maxPoint = -1;
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).x < minX) {
				minX = points.get(i).x;
				minPoint = i;
			}
			if (points.get(i).x > maxX) {
				maxX = points.get(i).x;
				maxPoint = i;
			}
		}
		Point A = points.get(minPoint);
		Point B = points.get(maxPoint);
		g.setColor(Color.white);
		ArrayList<Point> temp = new ArrayList<Point>();
		temp.add(A);
		temp.add(B);
		lines.add(temp);
		convexHull.add(A);
		convexHull.add(B);
		

		ArrayList<Point> leftSet = new ArrayList<Point>();
		ArrayList<Point> rightSet = new ArrayList<Point>();

		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (pointLocation(A, B, p) == -1)
				leftSet.add(p);
			else if (pointLocation(A, B, p) == 1)
				rightSet.add(p);
		}
		hullSet(A, B, rightSet, convexHull);
		hullSet(B, A, leftSet, convexHull);
		return convexHull;
	}

	public int distance(Point A, Point B, Point C) {
		int ABx = B.x - A.x;
		int ABy = B.y - A.y;
		int num = ABx * (A.y - C.y) - ABy * (A.x - C.x);
		if (num < 0)
			num = -num;
		return num;
	}

	public void hullSet(Point A, Point B, ArrayList<Point> set, ArrayList<Point> hull) {
		int insertPosition = hull.indexOf(B);
		if (set.size() == 0)
			return;
		if (set.size() == 1) {
			Point p = set.get(0);
			set.remove(p);
			hull.add(insertPosition, p);
			ArrayList<Point> temp = new ArrayList<Point>();
			temp.add(A);
			temp.add(p);
			lines.add(temp);
			temp = new ArrayList<Point>();
			temp.add(p);
			temp.add(B);
			lines.add(temp);
			return;
		}
		int dist = Integer.MIN_VALUE;
		int furthestPoint = -1;
		for (int i = 0; i < set.size(); i++) {
			Point p = set.get(i);
			int distance = distance(A, B, p);
			if (distance > dist) {
				dist = distance;
				furthestPoint = i;
			}
		}
		Point P = set.get(furthestPoint);
		set.remove(furthestPoint);
		hull.add(insertPosition, P);
		ArrayList<Point> temp = new ArrayList<Point>();
		temp.add(A);
		temp.add(P);
		lines.add(temp);
		temp = new ArrayList<Point>();
		temp.add(P);
		temp.add(B);
		lines.add(temp);
		// Determine who's to the left of AP
		ArrayList<Point> leftSetAP = new ArrayList<Point>();
		for (int i = 0; i < set.size(); i++) {
			Point M = set.get(i);
			if (pointLocation(A, P, M) == 1) {
				leftSetAP.add(M);
			}
		}

		// Determine who's to the left of PB
		ArrayList<Point> leftSetPB = new ArrayList<Point>();
		for (int i = 0; i < set.size(); i++) {
			Point M = set.get(i);
			if (pointLocation(P, B, M) == 1) {
				leftSetPB.add(M);
			}
		}
		hullSet(A, P, leftSetAP, hull);
		hullSet(P, B, leftSetPB, hull);

	}

	public void clearPoints(){
		quickHullPerformed = false;
		points.clear();
		lines.clear();
	}
	
	public int pointLocation(Point A, Point B, Point P) {
		int cp1 = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
		if (cp1 > 0)
			return 1;
		else if (cp1 == 0)
			return 0;
		else
			return -1;
	}

	public void addPoint(Point p) {
		this.points.add(p);
	}
}
