package com.enedkiu.cursos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.enedkiu.cursos.models.NotaModel;
import com.enedkiu.cursos.models.TareaModel;
import com.enedkiu.cursos.services.TareaService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
    @Autowired
    TareaService tareaService;

    @GetMapping("/tareas")
    public Iterable<TareaModel> getAllTareas() {
        return tareaService.getAllTareas();
    }

    @GetMapping("/{tareaId}")
    public TareaModel getOneTarea(@PathVariable Long tareaId) {
        return tareaService.getOneTarea(tareaId);
    }

    @PostMapping()
    public TareaModel saveTarea(@RequestBody TareaModel tarea) {
        return tareaService.saveTarea(tarea);
    }

    @PutMapping("/{tareaId}")
    public TareaModel updateTarea(@PathVariable Long tareaId, @RequestBody TareaModel tarea) {
        return tareaService.updateTarea(tareaId, tarea);
    }

    @DeleteMapping("/{tareaId}")
    public void deleteTarea(@PathVariable Long tareaId) {
        tareaService.deleteTarea(tareaId);
    }

    // Notas de una tarea
    @GetMapping("/{tareaId}/notas")
    public Iterable<NotaModel> getTareas(@PathVariable Long tareaId) {
        return tareaService.getOneTarea(tareaId).getNotas();
    }
    
}
