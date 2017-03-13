package infection;

import java.awt.Dimension;
import java.awt.Point;

public class Cell{
	private Dimension size = null;
	private Point point = null;
	private boolean isSelected;
	private boolean isEmpty;
	private boolean isPressed;
	private Team team = null;
	public Cell(){
		team = new Team();
		isSelected = false;
		isEmpty = true;
		isPressed = false;
	}
	public void setTeam (int t) {
		team.setTeam(t);
		isEmpty = false;
	}
	public void setTeam (Team t) {
		team = new Team(t);
		isEmpty = false;
	}
	public Team getTeam(){
		return team;
	}
	public void setSize(Dimension s){
		size = new Dimension(s.width, s.height);
	}
	public void setSize(int w, int h){
		size = new Dimension(w, h);
	}
	public Dimension getSize(){
		return size;
	}
	public void setPoint(Point p){
		point = new Point(p.x, p.y);
		// hello from kimden!
	}
	public void setPoint(int x, int y){
		point = new Point(x, y);
	}
	public Point getPoint(){
		return point;
	}
	public void setSelected(boolean b){
		isSelected = b;
	}
	public boolean isSelected(){
		return isSelected;
	}
	public void setEmpty(boolean b){
		isEmpty = b;
	}
	public boolean isEmpty(){
		return isEmpty;
	}
	public void setPressed(boolean b){
		isPressed = b;
	}
	public boolean getPressed(){
		return isPressed;
	}
}
