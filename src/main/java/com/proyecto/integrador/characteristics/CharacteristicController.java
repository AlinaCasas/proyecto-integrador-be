package com.proyecto.integrador.characteristics;

import com.proyecto.integrador.characteristics.dto.CharacteristicDTO;
import com.proyecto.integrador.characteristics.dto.UpdateCharacteristicDTO;
import com.proyecto.integrador.exceptions.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/characteristic")
public class CharacteristicController {

    @Autowired
    private CharacteristicService characteristicService;

    @GetMapping
    public List<Characteristic> getAllCharacteristics(){
        return characteristicService.getAllCharacteristics();
    }

    @GetMapping("/{name}")
    public Characteristic getCharacteristicByName(@PathVariable(value = "name", required = true) String name){
        return characteristicService.getCharacteristicByName(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Characteristic createCharacteristic(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @Valid @ModelAttribute CharacteristicDTO characteristicDTO)
    {
        if (image != null) {
            if (!Objects.equals(image.getContentType(), "image/jpeg") &&
                    !Objects.equals(image.getContentType(), "image/png") &&
                    !Objects.equals(image.getContentType(), "image/webp")) {

                throw new BadRequestException("Only JPG, PNG and WEBP images are allowed");
            }

            if (image.getSize() > 1048576) throw new BadRequestException("Maximum image size is 1MB");
        }
        return characteristicService.createCharacteristic(characteristicDTO, image);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{name}")
    public Characteristic updateCharacteristic (
            @PathVariable String name,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @Valid @ModelAttribute UpdateCharacteristicDTO updateCharacteristicDTO){
        if (image != null) {
            if (!Objects.equals(image.getContentType(), "image/jpeg") &&
                    !Objects.equals(image.getContentType(), "image/png") &&
                    !Objects.equals(image.getContentType(), "image/webp")) {

                throw new BadRequestException("Only JPG, PNG and WEBP images are allowed");
            }

            if (image.getSize() > 1048576) throw new BadRequestException("Maximum image size is 1MB");

        }
        return characteristicService.updateCharacteristic(name, updateCharacteristicDTO, image);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{name}")
    public void softDeleteCharacteristic(@PathVariable String name) {
        characteristicService.softDeleteCharacteristic(name);
    }
}