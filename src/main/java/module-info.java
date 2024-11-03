module es.juliogtrenard.proyectoolimpiadas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens es.juliogtrenard.proyectoolimpiadas to javafx.fxml;
    exports es.juliogtrenard.proyectoolimpiadas;
    exports es.juliogtrenard.proyectoolimpiadas.controladores;
    opens es.juliogtrenard.proyectoolimpiadas.controladores to javafx.fxml;
    exports es.juliogtrenard.proyectoolimpiadas.lenguaje;
    opens es.juliogtrenard.proyectoolimpiadas.lenguaje to javafx.fxml;
    exports es.juliogtrenard.proyectoolimpiadas.modelos;
    exports es.juliogtrenard.proyectoolimpiadas.dao;
}