package application;

import java.sql.SQLException;

import application.view.JeuControllers;
import bdd.H2.H2;

public class Question {

	public static Question questionEnCours;

	private int id;
	private int numero;
	private String intitule;
	private Zone zoneCorrecte;
	private int nbZone;

	private String idQuestion;
	private String idZoneQuestion;
	private String intituleQuestion;
	private Zone zoneCorrecteQuestion;

	private int idZone;
	private Integer[] listeIdZone;

	public Question(int numQuestion) {
		load(numQuestion);
		this.listeIdZone = new Integer[idZoneQuestion.split(";").length];
		this.nbZone = getlisteIdZone().length;
		for (int i = 0; i < nbZone; i++) {
			this.listeIdZone[i] = Integer.parseInt(idZoneQuestion.split(";")[i]);
		}

		for (int i = 0; i < listeIdZone.length; i++) {
			listeIdZone[i] = Integer.parseInt(idZoneQuestion.split(";")[i]);
			System.out.println("taille" + listeIdZone.length);
			System.out.println("[i]" + listeIdZone[i]);
		}
		this.id = Integer.parseInt(idQuestion.replace(";", ""));
		this.numero = 1;
		this.intitule = intituleQuestion;
		this.zoneCorrecte = zoneCorrecteQuestion;
	}

	public void load(int numQuestion) {
		try {
			intituleQuestion = H2.nonApos(H2.lire("SELECT Int_Question FROM QUESTION WHERE Num_Question = "
					+ numQuestion + " AND Id_Theme = " + Theme.themeEnCours.getId() + ";"));

			idZoneQuestion = H2
					.lire("SELECT DISTINCT Id_Zone FROM QUESTION NATURAL JOIN COORDONNEE WHERE Num_Question ="
							+ JeuControllers.getComptQuestion() + " AND Id_Theme =" + Theme.themeEnCours.getId()
							+ ";");

			idQuestion = H2.lire("SELECT Id_Question FROM QUESTION WHERE Id_Theme =" + Theme.themeEnCours.getId()
					+ " AND Num_Question =" + numQuestion + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public int getId() {

		return id;
	}

	public int getNumero() {
		return numero;
	}

	public String getIntitule() {
		return intitule;
	}

	public int getNbZone() {
		return nbZone;
	}

	public Integer[] getlisteIdZone() {
		return listeIdZone;
	}

	public int getIdZone() {
		return idZone;
	}

	public Zone getZoneCorrecte() {
		return zoneCorrecte;
	}

	public String getIntituleQuestion() {
		return intituleQuestion;
	}

}
