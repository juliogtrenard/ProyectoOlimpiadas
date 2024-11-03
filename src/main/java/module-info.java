module es.juliogtrenard.proyectoolimpiadas {
    requires javafx.controls;
    requires javafx.fxml;


    opens es.juliogtrenard.proyectoolimpiadas to javafx.fxml;
    exports es.juliogtrenard.proyectoolimpiadas;
    exports es.juliogtrenard.proyectoolimpiadas.controladores;
    opens es.juliogtrenard.proyectoolimpiadas.controladores to javafx.fxml;
}