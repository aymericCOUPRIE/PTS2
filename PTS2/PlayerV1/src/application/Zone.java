package application;

import java.sql.SQLException;

import bdd.H2.H2;
import javafx.scene.shape.Polygon;

public class Zone {

	public static Zone zoneEnCours;

	private final static Double OPACITE = 0.5;
	private Double[] coordonn�es;
	private int idZone;

	private Polygon zoneReponse = new Polygon();
	private String coordonn�esZoneX;
	private String coordonn�esZoneY;

	public Zone(int idZone) {
		load(idZone);
		this.idZone = idZone;
		this.coordonn�es = new Double[2 * (coordonn�esZoneX.split(";").length)];
		int j = 0, k = 0;
		for (int i = 0; i < coordonn�es.length; i++) {
			if (i % 2 == 0) {
				System.out.println("coordonneesX : "+ coordonn�esZoneX);
				this.coordonn�es[i] = Double.parseDouble(coordonn�esZoneX.split(";")[j]);
				j++;
			} else {
				System.out.println("coordonneesY : "+ coordonn�esZoneY);
				this.coordonn�es[i] = Double.parseDouble(coordonn�esZoneY.split(";")[k]);
				k++;
			}
		}
		zoneReponse.getPoints().addAll(coordonn�es);
		zoneReponse.setOpacity(OPACITE);
	}

	private void load(int idZone) {
		try {
			coordonn�esZoneX = H2.lire("SELECT Coor_Zone_X FROM COORDONNEE WHERE Id_Zone = '" + idZone + "' "
					+ "AND Id_Question = '" + Question.questionEnCours.getId() + "';");

			coordonn�esZoneY = H2.lire("SELECT Coor_Zone_Y FROM COORDONNEE WHERE Id_Zone = '" + idZone + "' "
					+ "AND Id_Question = '" + Question.questionEnCours.getId() + "';");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// System.out.println("coordonn�esX: " + coordonn�esZoneX);
		// System.out.println("coordonn�esY: " + coordonn�esZoneY);

	}

	public int getIdZone() {
		return idZone;
	}

	public Double[] getCoordonn�es() {
		return coordonn�es;
	}

	public Polygon getZoneReponse() {
		return zoneReponse;
	}

}
