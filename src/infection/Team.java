package infection;

import java.awt.Color;
import java.awt.Image;

public class Team {
	private int team = 0;
	private Color colorTeam = null;
	private Image img = null;
	public Team(){
		colorTeam = new Color(255, 255, 255);
	}
	public Team(int t){
		setTeam(t);
	}
	public Team(Team t){
		team = t.team;
		colorTeam = t.colorTeam;
	}
	public int getTeam(){
		return team;
	}
	public void setTeam(int t){
		team = t;
		if (team == 0){
			colorTeam = new Color(255, 255, 255);
		}
		else if (team == 1){
			colorTeam = new Color(0, 255, 0);
		}
		else if (team == 2){
			colorTeam = new Color(255, 0, 0);
		}
		else if (team == 3){
			colorTeam = new Color(0, 255, 255);
		}
		else if (team == 4){
			colorTeam = new Color(255, 255, 0);
		}
	}
	public Color getColor(){
		return colorTeam;
	}
	public Image getImage(){
		return img;
	}
}
