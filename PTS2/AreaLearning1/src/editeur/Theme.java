package editeur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Theme {

	private String titre, description, srcLogo, srcImage;
	private int idTheme, nbQuestion;
	private boolean actif;
	private static int idModifTheme;
	private List<Question> listeQuestion;

	public Theme() {
		load();
		loadListeQuestion();
	}

	public Theme(int idTheme, String titre, String description, int nbQuestion, String srcImage, String srcLogo,
			boolean actif) {
		this.titre = titre;
		this.description = description;
		this.srcLogo = srcLogo;
		this.srcImage = srcImage;
		this.idTheme = idTheme;
		this.nbQuestion = nbQuestion;
		this.actif = actif;
		listeQuestion = new ArrayList<Question>();
	}

	public static int getModifTheme() {
		return idModifTheme;
	}

	public static void setModifTheme(int modifTheme) {
		idModifTheme = modifTheme;
	}

	public int getIdTheme() {
		return idTheme;
	}

	public void setIdTheme(int idTheme) {
		this.idTheme = idTheme;
	}

	public List<Question> getListeQuestion() {
		return listeQuestion;
	}

	public void setListeQuestion(List<Question> listeQuestion) {
		this.listeQuestion = listeQuestion;
	}

	public int getNbQuestion() {
		return nbQuestion;
	}

	public void setNbQuestion(int nbQuestion) {
		this.nbQuestion = nbQuestion;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSrcLogo() {
		return srcLogo;
	}

	public void setSrcLogo(String srcLogo) {
		this.srcLogo = srcLogo;
	}

	public String getSrcImage() {
		return srcImage;
	}

	public void setSrcImage(String srcImage) {
		this.srcImage = srcImage;
	}

	public void load() {
		try {
			H2.connexion();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] loading = H2.separer(H2.lire("SELECT * FROM THEME WHERE Id_Theme=" + idModifTheme + ";"));
		this.idTheme = Integer.parseInt(loading[0]);
		titre = H2.nonApos(loading[1]);
		description = H2.nonApos(loading[2]);
		nbQuestion = Integer.parseInt(loading[3]);
		srcImage = loading[4];
		srcLogo = loading[5];
		actif = Boolean.parseBoolean(loading[6]);
	}

	public void loadListeQuestion() {
		try {
			H2.connexion();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listeQuestion = new ArrayList<Question>();
		String elements[] = H2.separer(
				H2.lire("SELECT * FROM QUESTION WHERE Id_Theme=" + idModifTheme + ";"));
		for (int i = 0; i < elements.length-1; i = i + 4) {
			listeQuestion.add(new Question(Integer.parseInt(elements[i]),Integer.parseInt(elements[i+1]),H2.nonApos(elements[i+2]),Integer.parseInt(elements[i+3])));
		}
	}

	public void save() throws SQLException {
		H2.connexion();
		H2.modifier("UPDATE THEME SET Nom_Theme ='" + H2.apos(titre) + "', Desc_Theme='" + H2.apos(description) + "', Nb_Question="
				+ nbQuestion + ", Adr_Image ='" + srcImage + "', Adr_Logo ='" + srcLogo + "', Actif_Theme = " + actif
				+ " WHERE Id_Theme =" + idTheme + ";");
	}

	
}
