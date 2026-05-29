package admin_sql_tp2;

import java.sql.*;
import static java.lang.System.out;

public class MyJDBC {


	private String m_dbConnectorDriver;
	private Connection m_dbConnection;
	private String m_serverUrl;
	private Statement m_state;


	//constructor with parameters
	public MyJDBC(String dbConnectorDriver,String serverUrl){
		this.m_dbConnection=null;
		this.m_state=null;
		this.m_dbConnectorDriver=dbConnectorDriver;
		this.m_serverUrl = serverUrl;
	}
	
	public void connect(String userName, String passWord) throws ClassNotFoundException, SQLException {
			Class.forName(this.m_dbConnectorDriver); //verifie que le driver est bien importe dans le projet
			out.println("Le driver est bien present et charge");
			if (this.m_dbConnection==null) {
				this.m_dbConnection=DriverManager.getConnection(this.m_serverUrl,userName, passWord);
				if (this.m_dbConnection==null) 
					out.println("Connexion echouée");
				else { 
					out.println("Connexion effective !");
				    //https://openclassrooms.com/fr/courses/26832-apprenez-a-programmer-en-java/26543-fouiller-dans-sa-base-de-donnees
					this.m_state = this.m_dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				}
			}
			else {
				out.println("Connexion deja active, veuillez fermer la connection avant de vous reconnecter"); 
			}
	}
	public void disconnect() throws SQLException {
		if (this.m_dbConnection==null) {
			out.println("Connexion inactive"); 
		}
		else {
			this.m_state.close();
			this.m_dbConnection.close();
			out.println("Connexion fermee"); 
			this.m_state=null;
			this.m_dbConnection=null;
		}
	}
	

	
	public String executeReadQuery(String query) throws SQLException {
		 String result;
		  if(this.m_dbConnection==null) {
			  result="Veuillez d'abord vous connecter " +
					  "a une base de donnees";
			  return result;
		  }
		  
		  //on execute la requete
	      ResultSet resultSelect = this.m_state.executeQuery(query);
	      ResultSetMetaData resultMeta = resultSelect.getMetaData();
	      
	      //on compte le nombre d'enregistrements
	      int rowNumbers=0;
	      while (resultSelect.next()) {
	    	  ++rowNumbers;
	      }
	      resultSelect.beforeFirst();
	      

		  result="La table contient " + resultMeta.getColumnCount()
				  + " colonne(s) et " + rowNumbers + " ligne(s)";
		  result+="\n";




		  if(resultMeta.getColumnCount()!=0 && rowNumbers!=0) {
		      //On affiche le nom des colonnes
		      for(int i = 1; i <= resultMeta.getColumnCount(); ++i)
				  result+=resultMeta.getColumnName(i).toUpperCase() +
						  "\t|";

			  result+="\n";


		      
		      //affichage le contenu de la table
		      while(resultSelect.next()){         
		        for(int i = 1; i <= resultMeta.getColumnCount(); ++i)
		          result+=resultSelect.getObject(i).toString() + "\t|";

		        result+="\n";
		      }

		      
	      }
	      resultSelect.close();
		return result;
	}

	public String executePreparedReadQuery(String query, Object... parameters) throws SQLException {
		if (this.m_dbConnection==null) {
			return "Veuillez d'abord vous connecter a une base de donnees";
		}

		try (PreparedStatement preparedStatement = this.m_dbConnection.prepareStatement(query,
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			setParameters(preparedStatement, parameters);
			try (ResultSet resultSelect = preparedStatement.executeQuery()) {
				return formatResultSet(resultSelect);
			}
		}
	}

	public String executePreparedWriteQuery(String query, Object... parameters) throws SQLException {
		if (this.m_dbConnection==null) {
			return "Veuillez d'abord vous connecter a une base de donnees";
		}

		try (PreparedStatement preparedStatement = this.m_dbConnection.prepareStatement(query)) {
			setParameters(preparedStatement, parameters);
			int linesNumber = preparedStatement.executeUpdate();
			return "La requete a modifie : " + linesNumber + " ligne(s)";
		}
	}
	
	public String executeWriteQuery(String query) throws SQLException {
		  String result;

		  if(this.m_dbConnection==null) {
			  result="Veuillez d'abord vous connecter a une " +
					  "base de donnees";
			  return result;
		  }
	      int linesNumber = this.m_state.executeUpdate(query);
	      result="La requete a modifie : " + linesNumber + " ligne(s)";

		return result;
	}

	private void setParameters(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
		for (int i = 0; i < parameters.length; i++) {
			preparedStatement.setObject(i + 1, parameters[i]);
		}
	}

	private String formatResultSet(ResultSet resultSelect) throws SQLException {
		ResultSetMetaData resultMeta = resultSelect.getMetaData();
		int rowNumbers = 0;
		while (resultSelect.next()) {
			++rowNumbers;
		}
		resultSelect.beforeFirst();

		String result = "La requete retourne " + resultMeta.getColumnCount()
				+ " colonne(s) et " + rowNumbers + " ligne(s)\n";

		if (resultMeta.getColumnCount() != 0 && rowNumbers != 0) {
			for (int i = 1; i <= resultMeta.getColumnCount(); ++i) {
				result += resultMeta.getColumnName(i).toUpperCase() + "\t|";
			}
			result += "\n";

			while(resultSelect.next()) {
				for (int i = 1; i <= resultMeta.getColumnCount(); ++i) {
					Object value = resultSelect.getObject(i);
					result += (value == null ? "NULL" : value.toString()) + "\t|";
				}
				result += "\n";
			}
		}

		return result;
	}

}
