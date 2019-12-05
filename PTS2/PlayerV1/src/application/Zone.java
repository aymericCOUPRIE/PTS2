package application;

import java.sql.SQLException;

import bdd.H2.H2;
import javafx.scene.shape.Polygon;

public class Zone {

	public static Zone zoneEnCours;

	private final static Double OPACITE = 0.5;
	private Double[] coordonnées;
	private int idZone;

	private Polygon zoneReponse = new Polygon();
	private String coordonnéesZoneX;
	private String coordonnéesZoneY;

	public Zone(int idZone) {
		load(idZone);
		this.idZone = idZone;
		this.coordonnées = new Double[2 * (coordonnéesZoneX.split(";").length)];
		int j = 0, k = 0;
		for (int i = 0; i < coordonnées.length; i++) {
			if (i % 2 == 0) {
				System.out.println("coordonneesX : "+ coordonnéesZoneX);
				this.coordonnées[i] = Double.parseDouble(coordonnéesZoneX.split(";")[j]);
				j++;
			} else {
				System.out.println("coordonneesY : "+ coordonnéesZoneY);
				this.coordonnées[i] = Double.parseDouble(coordonnéesZoneY.split(";")[k]);
				k++;
			}
		}
		zoneReponse.getPoints().addAll(coordonnées);
		zoneReponse.setOpacity(OPACITE);
	}

	private void load(int idZone) {
		try {
			coordonnéesZoneX = H2.lire("SELECT Coor_Zone_X FROM COORDONNEE WHERE Id_Zone = '" + idZone + "' "
					+ "AND Id_Question = '" + Question.questionEnCours.getId() + "';");

			coordonnéesZoneY = H2.lire("SELECT Coor_Zone_Y FROM COORDONNEE WHERE Id_Zone = '" + idZone + "' "
					+ "AND Id_Question = '" + Question.questionEnCours.getId() + "';");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// System.out.println("coordonnéesX: " + coordonnéesZoneX);
		// System.out.println("coordonnéesY: " + coordonnéesZoneY);

	}

	public int getIdZone() {
		return idZone;
	}

	public Double[] getCoordonnées() {
		return coordonnées;
	}

	public Polygon getZoneReponse() {
		return zoneReponse;
	}

}
