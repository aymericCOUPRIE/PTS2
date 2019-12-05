package editeur;

import java.sql.SQLException;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Zone {

	private Polygon laZone;
	private int idZone;

	public Zone() {
		laZone = new Polygon();
		laZone.setFill(Color.TRANSPARENT);
		laZone.setStrokeWidth(2);
		laZone.setStroke(Color.BLACK);
	}
	
	public Zone(int idZone) {
		this();
		this.idZone = idZone;
	}

	public Zone(double[] coor) {
		laZone = new Polygon();
		for (int i = 0; i < coor.length; i++)
			laZone.getPoints().add(coor[i]);
	}

	public void save(int idQuestion) { // sauvegarde de la zone en bdd
		try {
			H2.connexion();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int idZone = 0;
		if (H2.separer(H2.lire("SELECT MAX(Id_Zone) FROM COORDONNEE WHERE Id_Question=" + idQuestion + ";"))[0] == "") {
			idZone = 1;
		} else
			idZone = Integer.parseInt(
					H2.separer(H2.lire("SELECT MAX(Id_Zone) FROM COORDONNEE WHERE Id_Question=" + idQuestion + ";"))[0])
					+ 1;
		this.idZone = idZone;
		for (int i = 0; i < laZone.getPoints().size(); i = i + 2) {
			try {
				H2.modifier("INSERT INTO COORDONNEE(Id_Zone,Coor_Zone_X,Coor_Zone_Y,Id_Question) VALUES (" + idZone
						+ "," + laZone.getPoints().get(i) + "," + laZone.getPoints().get(i + 1) + "," + idQuestion
						+ ");");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void ajouterPoint(double pointX, double pointY) {
		laZone.getPoints().add(pointX);
		laZone.getPoints().add(pointY);
	}

	public void remove(String intQuestion) { //supprimer de la bdd
		int idQuestion = Integer.parseInt(H2.separer(H2.lire("SELECT Id_Question FROM QUESTION WHERE Int_Question='"
				+ H2.apos(intQuestion) + "' AND Id_THEME=" + Theme.getModifTheme() + ";"))[0]);
		int idZone = Integer.parseInt(H2.separer(H2.lire("SELECT Id_Zone FROM COORDONNEE WHERE Id_Question=" + idQuestion + ";"))[0]);
		try {
			H2.modifier("DELETE COORDONNEE WHERE Id_Zone=" + idZone + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getIdZone() {
		return idZone;
	}

	public Polygon getLaZone() {
		return laZone;
	}
}
