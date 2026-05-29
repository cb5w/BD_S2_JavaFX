package admin_sql_tp2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;
public class MyController {

    @FXML
    private Button myConnexionButton;

    @FXML
    private Label myConnexionLabel;

    @FXML
    private TextField myIdentityTextField;

    @FXML
    private TextField myPasswordTextField;

    @FXML
    private Button myQueryButton;

    @FXML
    private TextField myQueryTextField;

    @FXML
    private Label myResultLabel;

    // MSSQL
    private MyJDBC myJDBC=new MyJDBC("com.microsoft.sqlserver.jdbc.SQLServerDriver",
               "jdbc:sqlserver://localhost:1433;Database=live;trustServerCertificate=true");
               //"jdbc:sqlserver://172.30.4.243\\LHAMON;databaseName=testaccountDB");

    // MYSQL
    //private MyJDBC myJDBC=new MyJDBC("com.mysql.cj.jdbc.Driver",
    //           "jdbc:mysql://localhost:3306/live");

    private boolean connected=false;

    @FXML
    void connexionPressEvent(ActionEvent event)
            throws ClassNotFoundException, SQLException {
            if(!connected){
                myJDBC.connect( myIdentityTextField.getText(),
                                myPasswordTextField.getText());
                myConnexionLabel.setText("Connecté");
                connected=true;
            }
            else{
                myJDBC.disconnect();
                myConnexionLabel.setText("Pas connecté");
                connected=false;
            }
    }

    @FXML
    void executionPressEvent(ActionEvent event)
            throws SQLException {
        String result=myJDBC.executeReadQuery(myQueryTextField.getText());
        myResultLabel.setText(result);
    }

}
