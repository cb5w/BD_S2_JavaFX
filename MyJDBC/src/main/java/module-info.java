module admin_sql_tp2.myjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens admin_sql_tp2 to javafx.fxml;
    exports admin_sql_tp2;
}