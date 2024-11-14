module co.edu.uniquindio.banco.bancouq {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires transitive java.desktop;

    opens co.edu.uniquindio.banco.bancouq to javafx.fxml;
    exports co.edu.uniquindio.banco.bancouq;


}
