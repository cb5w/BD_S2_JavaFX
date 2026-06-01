package admin_sql_tp2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class MyController {

    @FXML
    private Label myConnexionLabel;

    @FXML
    private TextField myIdentityTextField;

    @FXML
    private TextField myPasswordTextField;

    @FXML
    private TextField myIneTextField;

    @FXML
    private TextField mySeanceTextField;

    @FXML
    private TextField myEtatPresenceTextField;

    @FXML
    private TextField myEnseignantTextField;

    @FXML
    private TextField myGroupeTextField;

    @FXML
    private TextField myNomTextField;

    @FXML
    private TextField myPrenomTextField;

    @FXML
    private TextField myEmailTextField;

    @FXML
    private TextField myTelephoneTextField;

    @FXML
    private Label myResultLabel;

    //private MyJDBC myJDBC = new MyJDBC("org.h2.Driver",
    //      "jdbc:h2:./data/database/sae204;MODE=MSSQLServer;INIT=RUNSCRIPT FROM './data/script/sae204_H2.sql'");

    // Configuration SQL Server pour les postes de l'IUT :
    private MyJDBC myJDBC = new MyJDBC("com.microsoft.sqlserver.jdbc.SQLServerDriver",
             "jdbc:sqlserver://localhost:1433;Database=SAE204;trustServerCertificate=true");

    private boolean connected = false;

    @FXML
    void connexionPressEvent(ActionEvent event) {
        try {
            if (!connected) {
                myJDBC.connect(myIdentityTextField.getText(), myPasswordTextField.getText());
                myConnexionLabel.setText("Connecte a SAE204");
                connected = true;
            } else {
                myJDBC.disconnect();
                myConnexionLabel.setText("Pas connecte");
                connected = false;
            }
        } catch (ClassNotFoundException | SQLException exception) {
            showError(exception);
        }
    }

    @FXML
    void allAbsencesPressEvent(ActionEvent event) {
        executeRead("SELECT * FROM vue_absences WHERE Etat_Presence LIKE 'abs%' OR Etat_Presence = 'en retard' ORDER BY Date_Seance, Heure_Seance");
    }

    @FXML
    void exportViewPressEvent(ActionEvent event) {
        executeRead("SELECT * FROM vue_absences ORDER BY Date_Seance, Heure_Seance, Nom_Etudiant");
    }

    @FXML
    void studentGroupsPressEvent(ActionEvent event) {
        executePreparedRead("""
                SELECT e.Nom_Etudiant, e.Prenom_Etudiant, g.Code_Groupe, g.Libelle_Groupe
                FROM ETUDIANT e
                JOIN APPARTENIR a ON e.INE_Etudiant = a.INE_Etudiant
                JOIN GROUPE g ON a.ID_Groupe = g.ID_Groupe
                WHERE e.INE_Etudiant = ?
                """, myIneTextField.getText());
    }

    @FXML
    void seancePresencePressEvent(ActionEvent event) {
        executePreparedRead("""
                SELECT e.Nom_Etudiant, e.Prenom_Etudiant, p.Etat_Presence
                FROM PARTICIPER p
                JOIN ETUDIANT e ON p.INE_Etudiant = e.INE_Etudiant
                WHERE p.ID_Seance = ?
                ORDER BY e.Nom_Etudiant, e.Prenom_Etudiant
                """, parseInteger(mySeanceTextField));
    }

    @FXML
    void teacherSeancesPressEvent(ActionEvent event) {
        executePreparedRead("""
                SELECT s.ID_Seance, s.Date_Seance, s.Heure_Seance, s.Type_Cours, g.Code_Groupe
                FROM SEANCE s
                JOIN GROUPE g ON s.ID_Groupe = g.ID_Groupe
                WHERE s.ID_Enseignant = ?
                ORDER BY s.Date_Seance, s.Heure_Seance
                """, parseInteger(myEnseignantTextField));
    }

    @FXML
    void addPresencePressEvent(ActionEvent event) {
        executePreparedWrite("""
                INSERT INTO PARTICIPER (INE_Etudiant, ID_Seance, Etat_Presence)
                VALUES (?, ?, ?)
                """, myIneTextField.getText(), parseInteger(mySeanceTextField), myEtatPresenceTextField.getText());
    }

    @FXML
    void updatePresencePressEvent(ActionEvent event) {
        executePreparedWrite("""
                UPDATE PARTICIPER
                SET Etat_Presence = ?
                WHERE INE_Etudiant = ? AND ID_Seance = ?
                """, myEtatPresenceTextField.getText(), myIneTextField.getText(), parseInteger(mySeanceTextField));
    }

    @FXML
    void addStudentPressEvent(ActionEvent event) {
        executePreparedWrite("""
                INSERT INTO ETUDIANT (INE_Etudiant, Nom_Etudiant, Prenom_Etudiant, Email_Etudiant, Tel_Etudiant)
                VALUES (?, ?, ?, ?, ?)
                """,
                myIneTextField.getText(),
                myNomTextField.getText(),
                myPrenomTextField.getText(),
                myEmailTextField.getText(),
                myTelephoneTextField.getText());
    }

    @FXML
    void addStudentGroupPressEvent(ActionEvent event) {
        executePreparedWrite("""
                INSERT INTO APPARTENIR (ID_Groupe, INE_Etudiant)
                VALUES (?, ?)
                """, parseInteger(myGroupeTextField), myIneTextField.getText());
    }

    private void executeRead(String query) {
        try {
            myResultLabel.setText(myJDBC.executeReadQuery(query));
        } catch (SQLException exception) {
            showError(exception);
        }
    }

    private void executePreparedRead(String query, Object... parameters) {
        try {
            myResultLabel.setText(myJDBC.executePreparedReadQuery(query, parameters));
        } catch (SQLException | NumberFormatException exception) {
            showError(exception);
        }
    }

    private void executePreparedWrite(String query, Object... parameters) {
        try {
            myResultLabel.setText(myJDBC.executePreparedWriteQuery(query, parameters));
        } catch (SQLException | NumberFormatException exception) {
            showError(exception);
        }
    }

    private int parseInteger(TextField textField) {
        return Integer.parseInt(textField.getText());
    }

    private void showError(Exception exception) {
        myResultLabel.setText("Erreur : " + exception.getMessage());
    }
}
