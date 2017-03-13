package infection;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class Pole extends JFrame{
	private Vector<Vector<Cell>> pole = null;
	private final int XX = 140;
	private final int YY = 125;
	private final int sizeCell = 50;
	private int w;
	private int h;
	private int teamNumb = 4;
	private int teamGo = 1;
	private Vector<Integer> teams = new Vector<>();
	private Point selected = null;
	private Point prevSelected = null;
	private JPanel map = new JPanel();
	private boolean spacePressed = false;
	private boolean end = false;
	private int turn = 0;
	private Vector<Color> colors = null;
	public Pole(int ww, int hh, int n){
		super("Инфекция");
		colors = new Vector<>();
		colors.add(new Color(0, 255, 0));
		colors.add(new Color(255, 0, 0));
		colors.add(new Color(0, 255, 255));
		colors.add(new Color(255, 255, 0));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("images/infection.png"));
		teamNumb = n;
		w = ww;
		h = hh;
		pole = new Vector<>();
		map.setBackground(new Color(255, 167, 167));
		add(map);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setCell();
		setMenu();
		setSize(
				2*XX+w*sizeCell,
				2*YY+h*sizeCell
				);
		setTeams();
		setKey();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	private void teamWin(){
		int a = 0;
		for(int i=0; i<teams.size(); i++){
			a+=teams.get(i);
		}
		if (a == pole.size()*pole.get(0).size()){
			end = true;
			return;
		}
		a=0;
		for (int i=0; i<teams.size(); i++){
			if (teams.get(i) == 0){
				a++;
			}
		}
		if (a == teams.size()-1){
			end = true;
			for (int i=0; i<teams.size(); i++){
				if (teams.get(i)!=0){
					teams.set(i, pole.size()*pole.get(0).size());
					break;
				}
			}
		}
		repaint();
	}
	private void nextTeam(){
		setTeams();
		teamWin();
		if (end){
			endGame();
		}
		int k = teamGo;
		int t = turn;
		if (teamNumb == 2){
			if (teamGo == 1){
				if (!teamBlock(2)){
					teamGo = 2;
				}
				else{
					turn = t;
					theLast(k);
				}
			}
			else if (teamGo == 2){
				if (!teamBlock(1)){
					teamGo = 1;
					turn++;
				}
				else{
					turn = t;
					theLast(k);
				}
			}
		}
		else if (teamNumb == 3){
			if (teamGo == 1){
				if (teams.get(1)!=0 && !teamBlock(2)){
					teamGo = 2;
				}
				else{
					if (!teamBlock(3)){
						teamGo = 3;
					}
					else{
						turn = t;
						theLast(k);
					}
				}
			}
			else if (teamGo == 2){
				if (teams.get(2)!=0  && !teamBlock(3)){
					teamGo=3;
				}
				else{
					if (!teamBlock(1)){
						teamGo = 1;
					}
					else{
						turn = t;
						theLast(k);
					}
				}
			}
			else if (teamGo == 3){
				turn++;
				if (teams.get(0) != 0 && !teamBlock(1)){
					teamGo = 1;
				}
				else{
					if (!teamBlock(2)){
						teamGo = 2;
					}
					else{
						turn = t;
						theLast(k);
					}
				}
			}
		}
		else if (teamNumb == 4){
			if (teamGo == 1){
				turn++;
				if (teams.get(1)!=0 && !teamBlock(2)){
					teamGo = 2;
				}
				else if (teams.get(2) !=0 && !teamBlock(3)){
					teamGo = 3;
				}
				else {
					if (!teamBlock(4)){
						teamGo = 4;
					}
					else{
						turn = t;
						theLast(k);
					}
				}
			}
			else if (teamGo == 2){
				if (teams.get(2)!=0  && !teamBlock(3)){
					teamGo=3;
				}
				else if (teams.get(3)!=0 && !teamBlock(4)){
					teamGo = 4;
				}
				else{ 
					if (!teamBlock(4)){
						teamGo = 4;
					}
					else{
						turn = t;
						theLast(k);
					}
				}
			}
			else if (teamGo == 3){
				if (teams.get(3) != 0 && !teamBlock(4)){
					teamGo = 4;
				}
				else if(teams.get(0) != 0 && !teamBlock(1)){
					teamGo = 1;
				}
				else{
					if(!teamBlock(2)){
						teamGo = 2;
					}
					else{
						turn = t;
						theLast(k);
					}
				}
			}
			else if (teamGo == 4){
				turn++;
				if (teams.get(0) != 0 && !teamBlock(1)){
					teamGo = 1;
				}
				else if(teams.get(1) != 0 && !teamBlock(2)){
					teamGo = 2;
				}
				else {
					if(!teamBlock(3)){
						teamGo = 3;
					}
					else{
						turn = t;
						theLast(k);
					}
				}
				
			}
		}
	}
	private void theLast(int t){
		for (int i=0; i<pole.size(); i++){
			for (int j=0; j<pole.get(0).size(); j++){
				if (pole.get(i).get(j).isEmpty()){
					teams.set(t-1, teams.get(t-1)+1);
				}
			}
		}
		repaint();
		endGame();
	}
	private boolean teamBlock(int t){
		for (int i=0; i<pole.size(); i++){
			for (int j=0; j<pole.get(0).size(); j++){
				if (pole.get(i).get(j).getTeam().getTeam() == t){
					for (int k=1; k<3; k++){
						if (i-k>=0){
							if (j-k>=0 && pole.get(i-k).get(j-k).isEmpty()){
								return false;
							}
							if (j+k<pole.get(0).size() && pole.get(i-k).get(j+k).isEmpty()){
								return false;
							}
							if (pole.get(i-k).get(j).isEmpty()){
								return false;
							}
						}
						if (i+k<pole.size()){
							if (j-k>=0 && pole.get(i+k).get(j-k).isEmpty()){
								return false;
							}
							if (j+k<pole.get(0).size() && pole.get(i+k).get(j+k).isEmpty()){
								return false;
							}
							if (pole.get(i+k).get(j).isEmpty()){
								return false;
							}
						}
						if (j-k>=0 && pole.get(i).get(j-k).isEmpty()){
							return false;
						}
						if (j+k<pole.get(0).size() && pole.get(i).get(j+k).isEmpty()){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	private void endGame(){
		teamGo = 0;
		JFrame jf = new JFrame("Результаты");
		jf.setIconImage(Toolkit.getDefaultToolkit().getImage("images/res.png"));
		jf.setSize(300, 300);
		Vector<Vector<String>>res = new Vector<>();
		for (int i=0; i<teamNumb; i++){
			Vector<String> p = new Vector<>();
			p.add("Team: " + (i+1));
			p.add(""+teams.get(i));
			res.add(p);
		}
		Vector<String> headers = new Vector<>();
		headers.add("Teams");
		headers.add("Score");
		JTable tabl = new JTable(res, headers);
		tabl.getTableHeader().setEnabled(false);
		JButton ng = new JButton("Новая игра");
		ng.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				jf.dispose();
				newGame();
				repaint();
			}
			
		});
		res.sort(new Comp());
		tabl.setEnabled(false);
		JScrollPane jscrlp = new JScrollPane(tabl);
	    tabl.setPreferredScrollableViewportSize(new Dimension(250, 100));
	    jf.getContentPane().add(jscrlp);
		jf.add(ng, BorderLayout.SOUTH);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
	private void setKey(){
		map.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) { // Кликнули	
				for (int i=0; i<pole.size(); i++){
					for (int j=pole.get(0).size()-1; j>-1; j--){
						if (
								arg0.getX() > XX + 1 + i*sizeCell && 
								arg0.getX() < XX - 1 + sizeCell*(i+1) &&
								arg0.getY() > YY + 1 + (pole.get(0).size() - j-1)*sizeCell - 62 &&
								arg0.getY() < YY - 1 + sizeCell*(pole.get(0).size() - j) - 62
							){
							if (selected.x == i && selected.y == pole.get(0).size()-j-1){
								clicked();
							}
							else{
								pole.get(selected.x).get(selected.y).setSelected(false);
								selected = new Point(i, pole.get(0).size() - j - 1);
								pole.get(i).get(pole.get(0).size() - j - 1).setSelected(true);
							}
							validate();
							repaint();
							break;
						}
					}
				}
			}
		});
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent key) {
				if (key.getKeyCode() == KeyEvent.VK_LEFT){
					if (selected.x > 0){
						//pole.get(selected.x).get(selected.y).setPressed(false);
						pole.get(selected.x).get(selected.y).setSelected(false);
						selected.x--;
						pole.get(selected.x).get(selected.y).setSelected(true);
					}
				}
				else if (key.getKeyCode() == KeyEvent.VK_RIGHT){
					if (selected.x < pole.size()-1){
						//pole.get(selected.x).get(selected.y).setPressed(false);
						pole.get(selected.x).get(selected.y).setSelected(false);
						selected.x++;
						pole.get(selected.x).get(selected.y).setSelected(true);
					}
				}
				else if (key.getKeyCode() == KeyEvent.VK_UP){
					if (selected.y > 0){
						//pole.get(selected.x).get(selected.y).setPressed(false);
						pole.get(selected.x).get(selected.y).setSelected(false);
						selected.y--;
						pole.get(selected.x).get(selected.y).setSelected(true);
					}
				}
				else if (key.getKeyCode() == KeyEvent.VK_DOWN){
					if (selected.y < pole.get(0).size()-1){
						//pole.get(selected.x).get(selected.y).setPressed(false);
						pole.get(selected.x).get(selected.y).setSelected(false);
						selected.y++;
						pole.get(selected.x).get(selected.y).setSelected(true);
					}
				}
				else if (key.getKeyCode() == KeyEvent.VK_SPACE){
					clicked();
					
				}
				else if (key.getKeyCode()==KeyEvent.VK_F2){
					newGame();
				}
				setTeams();
				repaint();
			}
		});
		
	}
	private void setActive(int x, int y){
		pole.get(x).get(y).setPressed(true);
		spacePressed = true;
		prevSelected = new Point(x, y);
	}
	private void setUnActive(int x, int y){
		pole.get(prevSelected.x).get(prevSelected.y).setPressed(false);
		spacePressed = false;
	}
	private void setTeams(){
		teams.removeAllElements();
		for (int i=0; i<teamNumb; i++){
			teams.add(0);
		}
		for (int i=0; i<pole.size(); i++){
			for (int j=0; j<pole.get(0).size(); j++){
				int a = pole.get(i).get(j).getTeam().getTeam();
				if (a <= teamNumb && a > 0){
					teams.set(a-1, teams.get(a-1)+1);
				}
			}
		}
	}
	public void newGame(){
		turn = 0;
		selected = new Point(0, 0);
		prevSelected = new Point(0, 0);
		pole.removeAllElements();
		setCell();
		setTeams();
		setSize(2*XX+sizeCell*pole.size(), 2*YY+sizeCell*pole.get(0).size());
		setLocationRelativeTo(null);
		spacePressed = false;
		teamGo = 1;
		end = false;
	}
	private void setMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu game = new JMenu("Игра");
		JMenuItem newGame = new JMenuItem("Новая игра");
		JMenuItem options = new JMenuItem("Параметры");
		JMenuItem exit = new JMenuItem("Выход");
		JMenu help = new JMenu("Справка");
		JMenuItem howPlay = new JMenuItem("Как играть?");
		JMenuItem helper = new JMenuItem("О программе");
		newGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
				repaint();
			}
		});
		options.addActionListener(new ActionListener(){
			class Options{
				JButton ok = new JButton("Ок");
				public Options(){
					JFrame jf = new JFrame("Параметры");
					jf.setIconImage(Toolkit.getDefaultToolkit().getImage("images/param.png"));
					Vector<Integer>v = new Vector<>();
					Vector<Integer> t = new Vector<>();
					for (int i=6; i<16; i++){
						v.add(i);
					}
					for (int i=2; i<5; i++){
						t.add(i);
					}
					JComboBox<Integer> ww = new JComboBox<>(v);
					JComboBox<Integer> hh = new JComboBox<>(v);
					JComboBox<Integer> teams = new JComboBox<>(t);
					jf.setLayout(new GridLayout(5, 2, 50, 5));
					jf.add(new JLabel("Введите количество столбцов:"));
					jf.add(new JLabel("Введите количество строк:"));
					jf.add(ww);
					jf.add(hh);
					
					ok.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent arg0) {
							w = v.get(ww.getSelectedIndex());
							h = v.get(hh.getSelectedIndex());
							teamNumb = t.get(teams.getSelectedIndex());
							newGame();
							repaint();
							jf.dispose();
						}
						
					});
					jf.setSize(450, 200);
					jf.add(new JLabel("Введите количество команд:"));
					jf.add(new JPanel());
					jf.add(teams);
					jf.add(ok);
					jf.setLocationRelativeTo(null);
					jf.setResizable(false);
					jf.setVisible(true);
				}
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				new Options();
				repaint();
				
			}
		});
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		howPlay.addActionListener(new ActionListener(){
			class HowPlay{
				JFrame jf = new JFrame("Как играть?");
				public HowPlay(){
					StringBuilder st = new StringBuilder(new String());
					st.append("<html><p>Инфекция - это игра для нескольких человек (от 2 до 4).</p>");
					st.append("<p>Игроки ходят по очереди. Цель каждого игрока - захватить поле шарами "
							+ "своего цвета.</p>");
					st.append("<p>Нажмите <b color=\"red\">стрелку влево</b>, чтобы двигать указатель влево,"
							+ " <b color=\"red\">стрелку вправо</b>, чтобы двигать указатель вправо, "
							+ "<b color=\"red\">стрелку вверх</b>, чтобы двигать указатель вверх, "
							+ "и <b color=\"red\">стрелку вниз</b>, чтобы двигать указатель вниз."
							+ " Чтобы выделить ячейку достаточно также <b color=\"blue\">кликнуть"
							+ " по ней мышью.</b></p>");
					st.append("<p>Для выбора шарика нажмите <b color=\"red\">пробел</b> либо <b color=\"blue\">кликните по выделенной клетке мышью</b>.</p>");
					st.append("<p>После того, как Вы выбрали шарик, его можно переместить на другую позицию, "
							+ "при этом:</p>");
					st.append("<p>1. При выборе соседней по горизонтали, вертикали либо диагонали клетке шарик "
							+ "размножается, то есть на выбранной клетке создается еще один той команды, "
							+ "которая делала ход.</p>");
					st.append("<p>2. Шарик может перепрыгнуть через 1 клетку по горизонтали, вертикали либо"
							+ " диагонали, либо пойти аналогично ходу коня в шахматах, однако при "
							+ "этом размножение происходить не будет - шарик только переместится.</p>");
					st.append("<p>3. Переместившись, шарик обращает все шарики другого цвета вокруг себя,"
							+ " то есть все шарики другого цвета на 1 клетку по горизонтали,"
							+ " вертикали либо диагонали становятся шариками той команды,"
							+ " которая совершила ход.</p>");
					st.append("<p>Игра заканчивается в трех случаях:</p>");
					st.append("<p>1. Все клетки поля заняты шариками.</p>");
					st.append("<p>2. На поле остались шарики только одного игрока.</p>");
					st.append("<p>3. Походить может только один игрок.</p>");
					st.append("<p>Выигрывает тот, у кого в конце игры осталось больше шариков. Удачной игры!</p></html>");
					jf.setSize(500, 420);
					jf.setIconImage(Toolkit.getDefaultToolkit().getImage("images/help.png"));
					jf.add(new JLabel(st.toString()), BorderLayout.NORTH);
					jf.setLocationRelativeTo(null);
					jf.setResizable(false);
					jf.setVisible(true);
				}
				
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				new HowPlay();
				repaint();
			}
		});
		helper.addActionListener(new ActionListener(){
			class Help{
				JFrame jf = new JFrame("О программе");
				public Help(){
					ImageIcon im = new ImageIcon("images/logo.png");
					jf.setIconImage(Toolkit.getDefaultToolkit().getImage("images/about.png"));
					jf.add(new JLabel(im), BorderLayout.WEST);
					jf.add(new JLabel("<html><p>Игра сделана с помощью Eclipse Neon.2.<p><p>E-mail разработчика:</p><p>evgeniy.skorohodov@gmail.com</p></html>"), BorderLayout.EAST);
					jf.setSize(520, 300);
					jf.setLocationRelativeTo(null);
					jf.setResizable(false);
					jf.setVisible(true);
				}
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				new Help();
				repaint();
			}
		});
		game.add(newGame);
		game.addSeparator();
		game.add(options);
		game.addSeparator();
		game.add(exit);
		help.add(howPlay);
		help.addSeparator();
		help.add(helper);
		menuBar.add(game);
		menuBar.add(help);
		setJMenuBar(menuBar);
	}
	private void clicked(){
		if (pole.get(selected.x).get(selected.y).getTeam().getTeam() == teamGo || pole.get(prevSelected.x).get(prevSelected.y).getPressed()){
			if (
					pole.get(prevSelected.x).get(prevSelected.y).getPressed() == false &&
					pole.get(selected.x).get(selected.y).getPressed() == false &&
					pole.get(selected.x).get(selected.y).isEmpty() == false
					){
				setActive(selected.x, selected.y);
			}
			else if (
					pole.get(prevSelected.x).get(prevSelected.y).getPressed() &&
					pole.get(selected.x).get(selected.y).isEmpty() &&
					Math.abs(selected.x-prevSelected.x) +
						Math.abs(selected.y-prevSelected.y) <= 4 &&
					Math.abs(selected.x-prevSelected.x)<=2 &&
					Math.abs(selected.y-prevSelected.y)<=2
					){
				pole.get(selected.x).get(selected.y).setEmpty(false);
				pole.get(selected.x).get(selected.y).setTeam(
						pole.get(prevSelected.x).get(prevSelected.y).getTeam()
						);
				if (
						(Math.abs(selected.x-prevSelected.x) == 2 &&
						Math.abs(selected.y-prevSelected.y) == 0) ||
						(Math.abs(selected.x-prevSelected.x) == 0 &&
						Math.abs(selected.y-prevSelected.y) == 2) ||
						(Math.abs(selected.x-prevSelected.x) == 1 &&
						Math.abs(selected.y-prevSelected.y) == 2) ||
						(Math.abs(selected.x-prevSelected.x) == 2 &&
						Math.abs(selected.y-prevSelected.y) == 1) ||
						(Math.abs(selected.x-prevSelected.x) == 2 &&
						Math.abs(selected.y-prevSelected.y) == 2)
						){
					pole.get(prevSelected.x).get(prevSelected.y).setTeam(0);
					pole.get(prevSelected.x).get(prevSelected.y).setEmpty(true);
				}
				//----------------------------------------------------
				if (selected.x-1>=0){
					if (selected.y-1>=0 && pole.get(selected.x-1).get(selected.y-1).isEmpty() == false){
						pole.get(selected.x-1).get(selected.y-1).setTeam(
								pole.get(selected.x).get(selected.y).getTeam()
								);
						pole.get(selected.x-1).get(selected.y-1).setEmpty(false);
					}
					if (selected.y+1<pole.get(0).size() && pole.get(selected.x-1).get(selected.y+1).isEmpty() == false){
						pole.get(selected.x-1).get(selected.y+1).setTeam(
								pole.get(selected.x).get(selected.y).getTeam()
								);
						pole.get(selected.x-1).get(selected.y+1).setEmpty(false);
					}
					if (pole.get(selected.x-1).get(selected.y).isEmpty() == false){
						pole.get(selected.x-1).get(selected.y).setTeam(
								pole.get(selected.x).get(selected.y).getTeam()
								);
						pole.get(selected.x-1).get(selected.y).setEmpty(false);
					}
				}
				if (selected.x+1 < pole.size()){
					if (selected.y-1>=0 && pole.get(selected.x+1).get(selected.y-1).isEmpty() == false){
						pole.get(selected.x+1).get(selected.y-1).setTeam(
								pole.get(selected.x).get(selected.y).getTeam()
								);
						pole.get(selected.x+1).get(selected.y-1).setEmpty(false);
					}
					if (selected.y+1<pole.get(0).size() && pole.get(selected.x+1).get(selected.y+1).isEmpty() == false){
						pole.get(selected.x+1).get(selected.y+1).setTeam(
								pole.get(selected.x).get(selected.y).getTeam()
								);
						pole.get(selected.x+1).get(selected.y+1).setEmpty(false);
					}
					if (pole.get(selected.x+1).get(selected.y).isEmpty() == false){
						pole.get(selected.x+1).get(selected.y).setTeam(
								pole.get(selected.x).get(selected.y).getTeam()
								);
						pole.get(selected.x+1).get(selected.y).setEmpty(false);
					}
				}
				if (selected.y-1>=0 && pole.get(selected.x).get(selected.y-1).isEmpty() == false){
					pole.get(selected.x).get(selected.y-1).setTeam(
							pole.get(selected.x).get(selected.y).getTeam()
							);
					pole.get(selected.x).get(selected.y-1).setEmpty(false);
				}
				if (selected.y+1<pole.get(0).size() && pole.get(selected.x).get(selected.y+1).isEmpty() == false){
					pole.get(selected.x).get(selected.y+1).setTeam(
							pole.get(selected.x).get(selected.y).getTeam()
							);
					pole.get(selected.x).get(selected.y+1).setEmpty(false);
				}
				nextTeam();
				setUnActive(selected.x, selected.y);
			}
			else if (
				pole.get(selected.x).get(selected.y).isEmpty() == false ||
				Math.abs(selected.x-prevSelected.x) > 2 ||
				Math.abs(selected.y-prevSelected.y) > 2){
				setUnActive(selected.x, selected.y);
			}
			
		}
	}
	private void setCell(){
		for (int i=0; i<w; i++){
			Vector<Cell> f = new Vector<>();
			for (int j=0; j<h; j++){
				f.add(new Cell());
			}
			pole.add(f);
		}
		for (int i=0; i<pole.size(); i++){
			for (int j=0; j<pole.get(i).size(); j++){
				pole.get(i).get(j).setPoint(new Point(XX+sizeCell*i, YY+sizeCell*j));
				pole.get(i).get(j).setSize(new Dimension(sizeCell, sizeCell));
			}
		}
		if (teamNumb == 2){
			pole.get(0).get(0).setTeam(1);
			pole.get(pole.size()-1).get(0).setTeam(2);
			pole.get(0).get(pole.get(0).size()-1).setTeam(2);
			pole.get(pole.size()-1).get(pole.get(0).size()-1).setTeam(1);
		}
		else if (teamNumb == 3){
			pole.get(0).get(0).setTeam(1);
			pole.get(pole.size()-1).get(0).setTeam(2);
			pole.get(0).get(pole.get(0).size()-1).setTeam(3);
		}
		else if (teamNumb == 4){
			pole.get(0).get(0).setTeam(1);
			pole.get(pole.size()-1).get(0).setTeam(2);
			pole.get(0).get(pole.get(0).size()-1).setTeam(3);
			pole.get(pole.size()-1).get(pole.get(0).size()-1).setTeam(4);
		}
		pole.get(0).get(0).setSelected(true);
		selected = new Point(0, 0);
		prevSelected = new Point(0, 0);
	}
	@Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		drawConst(g2d);
		if (spacePressed){
			g2d.setColor(Color.RED);
		}
		else{
			g2d.setColor(Color.BLACK);
		}
		g2d.drawRect(
				pole.get(selected.x).get(selected.y).getPoint().x, 
				pole.get(selected.x).get(selected.y).getPoint().y, 
				pole.get(selected.x).get(selected.y).getSize().width, 
				pole.get(selected.x).get(selected.y).getSize().height
				);
		g2d.setFont(new Font("TimesNewRoman", Font.BOLD, 15));
		for (int i=0; i<teamNumb; i++){
			g2d.setColor(colors.get(i));
			g2d.fillOval(XX+100, YY*11/10 + YY*i/5 + sizeCell*pole.get(0).size(), 15, 15);
			g2d.setColor(Color.black);
			g2d.drawString("Team " + (i+1) +":   "+ teams.get(i), XX, YY+YY*(i+1)/5 + sizeCell*pole.get(0).size());
			g2d.drawOval(XX+100, YY*11/10 + YY*i/5 + sizeCell*pole.get(0).size(), 15, 15);
		}
		g2d.drawString("Ходов сделано:   " + turn, XX+sizeCell*pole.size()/2, YY*6/5+sizeCell*pole.get(0).size());
		if (teamGo != 0){
			g2d.drawString("Ходит: ", XX+sizeCell*pole.size()/2, YY*7/5+sizeCell*pole.get(0).size());
			g2d.setColor(colors.get(teamGo-1));
			g2d.fillOval(XX+sizeCell*pole.size()/2 + 75, YY*13/10 + sizeCell*pole.get(0).size(), 15, 15);
			g2d.setColor(Color.BLACK);
			g2d.drawOval(XX+sizeCell*pole.size()/2 + 75, YY*13/10 + sizeCell*pole.get(0).size(), 15, 15);
		}
		else{
			g2d.drawString("Игра окончена!", XX+sizeCell*pole.size()/2, YY*7/5+sizeCell*pole.get(0).size());
		}
	}
	private void drawConst(Graphics2D g2d){
		for (int i=0; i<pole.size(); i++){
			for (int j=0; j<pole.get(i).size(); j++){
				g2d.setStroke(new BasicStroke(1));
				g2d.drawRect(
						pole.get(i).get(j).getPoint().x, 
						pole.get(i).get(j).getPoint().y, 
						pole.get(i).get(j).getSize().width, 
						pole.get(i).get(j).getSize().height
						);

				g2d.setStroke(new BasicStroke(3));
				if (pole.get(i).get(j).isEmpty() == false){
					g2d.setColor(pole.get(i).get(j).getTeam().getColor());
					g2d.fillOval(
							pole.get(i).get(j).getPoint().x+sizeCell/4, 
							pole.get(i).get(j).getPoint().y+sizeCell/4,
							pole.get(i).get(j).getSize().width/2, 
							pole.get(i).get(j).getSize().height/2
							);
					g2d.setColor(Color.BLACK);
					g2d.drawOval(
							pole.get(i).get(j).getPoint().x+sizeCell/4, 
							pole.get(i).get(j).getPoint().y+sizeCell/4,
							pole.get(i).get(j).getSize().width/2, 
							pole.get(i).get(j).getSize().height/2
							);
				}
			}
		}
	}
}