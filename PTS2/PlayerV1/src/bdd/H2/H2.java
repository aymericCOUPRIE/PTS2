package bdd.H2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class H2 {
	static Connection con = null;

	public static void Connexion() throws SQLException {
		Properties props = new Properties();
		props.setProperty("user", "root");
		props.setProperty("password", "root");
		// Url = jdbc:h2:(chemin jusqu'a la BDD)
		con = DriverManager.getConnection(
				"jdbc:h2:..\\BaseDeDonnees\\Bdd\\EditeurDB.db", props);
	}

	public static void modifier(String requete) throws SQLException {
		Connexion();
		try {
			Statement state = con.createStatement();
			// Syntaxe pour MODIFIER la BDD (Pas de doublon dans la BDD donc si tu executes
			// deux fois change de nom)
			state.execute(requete);
		} catch (Exception PSQLException) {
			System.out.println(PSQLException);
		}
	}

	public static String lire(String requete) throws SQLException {
		Connexion();
		String rep = "";
		try {
			Statement state = con.createStatement();
			ResultSet rs = state.executeQuery(requete);
			ResultSetMetaData resultMeta = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= resultMeta.getColumnCount(); i++) {
					rep = rep + rs.getObject(i).toString() + ";";
					// System.out.println(rep);
					System.out.print(rs.getObject(i).toString() + "\t");
				}
				System.out.println("");
			}
			System.out.println(rep);
			rs.close();
			state.close();
		} catch (Exception PSQLException) {
			System.out.println(PSQLException);
		}
		return rep;
	}
	
	public static String nonApos(String sansApos) {
		return sansApos.replaceAll("~", "'");
	}
}
