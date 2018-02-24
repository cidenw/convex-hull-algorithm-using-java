package ConvexHull;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseInput extends MouseAdapter{
	
	
	int mx, my;
	PointHandler p;
	ConvexHull ch;
	public MouseInput(PointHandler p, ConvexHull ch){
		this.ch = ch;
		this.p = p;
	}
	
	
	
	public void mousePressed(MouseEvent e){
		mx = e.getX();
		my = e.getY();
		
		if (mouseOver(mx, my, 250, 500, 120, 40)) {
			p.performQuickHull();
			ch.render();
		}
		
		if (mouseOver(mx, my, 250, 545, 120, 40)) {
			p.clearPoints();
			
		}

		
		if(!p.isQuickHullPerformed()){
			if(my<500){
				p.addPoint(new Point(mx, my));
				ch.render();
			}
		}
		
	}
	
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		if (mx > x && mx < x + width) {
			if (my > y && my < y + height) {
				return true;
			} else
				return false;
		} else
			return false;
	}

}
