package editeur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Question {

	private String intitule;
	private int idQuestion, idTheme, NumeroQuestion;
	private List<Zone> listeZone;
	
	public Question(int idQuestion, int NumeroQuestion, String intitule, int idTheme) {
		this.idQuestion = idQuestion;
		this.NumeroQuestion = NumeroQuestion;
		this.intitule = intitule;
		this.idTheme = idTheme;
		listeZone = new ArrayList<Zone>();
	}
	
	public List<Zone> getListeZone() {
		return listeZone;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public int getNumeroQuestion() {
		return NumeroQuestion;
	}

	public void setNumeroQuestion(int numeroQuestion) {
		NumeroQuestion = numeroQuestion;
	}

	public int getIdQuestion() {
		return idQuestion;
	}

	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}

	public int getIdTheme() {
		return idTheme;
	}

	public void setIdTheme(int idTheme) {
		this.idTheme = idTheme;
	}

	public void load() {
		try {
			H2.connexion();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] loading = H2.separer(H2.lire("SELECT * FROM QUESTION WHERE Id_Theme="+idTheme+ " AND Id_Question=" + idQuestion +";"));
		idQuestion =  Integer.parseInt(loading[0]);
		intitule =  H2.nonApos(loading[2]);
		idTheme =  Integer.parseInt(loading[3]);
	}
	
	public void save() throws SQLException {
		H2.connexion();
		H2.modifier("UPDATE QUESTION SET Int_Question ='" + H2.apos(intitule) +" WHERE Id_Theme =" + idTheme + " AND Id_Question=" + idQuestion +";");			
	}
}
