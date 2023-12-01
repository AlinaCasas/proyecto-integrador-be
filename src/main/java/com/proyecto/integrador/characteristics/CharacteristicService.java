package com.proyecto.integrador.characteristics;

import com.proyecto.integrador.characteristics.dto.CharacteristicDTO;
import com.proyecto.integrador.characteristics.dto.UpdateCharacteristicDTO;
import com.proyecto.integrador.exceptions.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CharacteristicService {

    @Autowired
    private CharacteristicRepository characteristicRepository;

    public List<Characteristic> getAllCharacteristics() {
        return characteristicRepository.findAllByDeletedAtIsNull();
    }

    public Characteristic getCharacteristicByName(String name) {
        return characteristicRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Category not found with name: " + name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Characteristic createCharacteristic(CharacteristicDTO characteristicDTO){
        Characteristic existingCharacteristic = characteristicRepository.findByName(characteristicDTO.getName()).orElse(null);
        if (existingCharacteristic != null) {
            throw new BadRequestException("Characteristic already exists with name: " + characteristicDTO.getName());
        }
        String name = characteristicDTO.getName();
        Characteristic characteristic = new Characteristic();
        characteristic.setName(name);
        try{
            return characteristicRepository.save(characteristic);
        } catch(Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Unexpected error, please contact support.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Characteristic updateCharacteristic(String name, UpdateCharacteristicDTO updateCharacteristicDTO){
        Characteristic existingCharacteristic = characteristicRepository.findByName(name).orElseThrow(() -> new BadRequestException("Category not found with name: " + name));
        existingCharacteristic.setName(updateCharacteristicDTO.getName());
        return characteristicRepository.save(existingCharacteristic);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void softDeleteCharacteristic(String name){
        Characteristic characteristic = characteristicRepository.findByName(name).orElseThrow(() -> new BadRequestException("Characteristic not found with name: "));
    }
}
