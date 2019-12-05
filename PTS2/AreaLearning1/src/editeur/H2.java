package editeur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class H2 {
	static Connection con = null;

	public static void connexion() throws SQLException {
		Properties props = new Properties();
		props.setProperty("user", "root");
		props.setProperty("password", "root");
		String url = "jdbc:h2:..\\BaseDeDonnees\\Bdd\\EditeurDB.db";
		con = DriverManager.getConnection(url, props);
		}

	public static void modifier(String requete) throws SQLException {
		try {
			Statement state = con.createStatement();
			// Syntaxe pour MODIFIER la BDD (Pas de doublon dans la BDD donc si tu executes
			// deux fois change de nom)
			state.execute(requete);
		} catch (Exception PSQLException) {
			System.out.println(PSQLException);
		}
	}

	public static String lire(String requete) {
		String rep = "";
		try {
			Statement state = con.createStatement();
			ResultSet rs = state.executeQuery(requete);
			ResultSetMetaData resultMeta = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= resultMeta.getColumnCount(); i++) {
					rep = rep + rs.getObject(i).toString() + ";";
				}
			}
			rs.close();
			state.close();
		} catch (Exception PSQLException) {
			System.out.println(PSQLException);
		}
		return rep;
	}
	
	public static String[] separer(String chaineASeparer) {
		return chaineASeparer.split(";");
	}
	
	public static String apos(String chaineAvecApos) {
		String res = chaineAvecApos.replace("'", "~");
		return res;
	}
	
	public static String nonApos(String chaineSansApos) {
		String res = chaineSansApos.replace("~", "'");
		return res;
	}
}
