package com.enedkiu.cursos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Ya no necesitamos la anotación Transactional aquí ni los repositorios.

@Component
public class DataLoader implements CommandLineRunner {

    // Inyectamos el componente que maneja la lógica de carga transaccional
    // Esta inyección es crucial para que Spring intercepte la llamada.
    @Autowired
    private DataInitializer dataInitializer;
    
    // NOTE: Los repositorios y todos los métodos load*Data han sido movidos a DataInitializer.java.
    
    @Override
    public void run(String... args) throws Exception {
        // Delegamos la carga de datos al método transaccional en DataInitializer
        this.dataInitializer.initializeData();
    }
    
    // Se elimina el método generateRandomStudentIds de aquí ya que reside ahora en DataInitializer.java
}
