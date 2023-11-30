package com.proyecto.integrador.characteristics;

import com.proyecto.integrador.characteristics.dto.CharacteristicDTO;
import com.proyecto.integrador.characteristics.dto.UpdateCharacteristicDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/characteristic")
public class CharacteristicController {

    @Autowired
    private CharacteristicService characteristicService;

    @GetMapping
    public List<Characteristic> getAllCharacteristics(){
        return characteristicService.getAllCharacteristics();
    }

    @GetMapping("/{id}")
    public Characteristic getCharacteristicById(@PathVariable(value = "name", required = true) String name){
        return characteristicService.getCharacteristicByName(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Characteristic createCharacteristic(@RequestBody @Valid CharacteristicDTO characteristicDTO){
        return characteristicService.createCharacteristic(characteristicDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{name}")
    public Characteristic updateCharacteristic (@PathVariable String name, @RequestBody @Valid UpdateCharacteristicDTO updateCharacteristicDTO){
        return characteristicService.updateCharacteristic(name, updateCharacteristicDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{name}")
    public void softDeleteCharacteristic(@PathVariable String name) {
        characteristicService.softDeleteCharacteristic(name);
    }
}
