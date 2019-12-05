package application;

import java.sql.SQLException;

import application.view.JeuControllers;
import bdd.H2.H2;

public class Score {

	private String pseudo;
	private int score;
	

	public Score() {
		this.score = 0;
	}

	public void save(String pseudo) {
		try {
			H2.modifier("INSERT INTO SCORE VALUES('" + pseudo + "'," + JeuControllers.getScoreDeLaPartie() + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String getPseudo() {
		return pseudo;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

}
