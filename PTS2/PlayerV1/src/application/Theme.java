package application;

import java.sql.SQLException;

import bdd.H2.H2;

public class Theme {

	public static Theme themeEnCours;

	private int id;
	private String nom;
	private String description;
	private int nbQuestion;
	private String urlImage;
	private String urlLogo;
	private String difficulté;
	
	private String nomTheme;
	private String descriptionTheme;
	private String nbQuestionTheme;
	private String urlImageTheme;
	private String urlLogoTheme;
	

	public Theme(int idTheme, String difficulté) {
		load(idTheme);
		this.id = idTheme;
		this.nom = nomTheme.replace(";", "");
		this.description = descriptionTheme.replace(";", "");
		this.nbQuestion = Integer.parseInt(nbQuestionTheme.replace(";", ""));
		this.urlImage = urlImageTheme.replace(";", "");
		this.urlLogo = urlLogoTheme.replace(";", "");
		this.difficulté = difficulté;
	}

	public void load(int idTheme) {
		try {

			nomTheme = H2.nonApos(H2.lire("SELECT Nom_Theme FROM THEME WHERE Id_Theme = '" + idTheme + "';"));

			descriptionTheme = H2.nonApos(H2.lire("SELECT Desc_Theme FROM THEME WHERE Id_Theme = '" + idTheme + "';"));

			nbQuestionTheme = H2.lire("SELECT Nb_Question FROM THEME WHERE Id_Theme = '" + idTheme + "';");

			urlImageTheme = H2.nonApos(H2.lire("SELECT Adr_Image FROM THEME WHERE Id_Theme = '" + idTheme + "';"));

			urlLogoTheme = H2.nonApos(H2.lire("SELECT Adr_Logo FROM THEME WHERE Id_Theme = '" + idTheme + "';"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void info() {
		System.out.println("nom:" + nom + "\ndescription:" + description + "\nnbquestion:" + nbQuestion + "\nimage:"
				+ urlImage + "\nlogo:" + urlLogo);
	}

	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public String getDescription() {
		return description;
	}

	public int getNbQuestion() {
		return nbQuestion;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public String getUrlLogo() {
		return urlLogo;
	}
	
	public String getDifficulté() {
		return difficulté;
	}

}
