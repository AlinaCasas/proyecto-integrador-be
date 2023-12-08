package com.proyecto.integrador.characteristics;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.proyecto.integrador.characteristics.dto.CharacteristicDTO;
import com.proyecto.integrador.characteristics.dto.UpdateCharacteristicDTO;
import com.proyecto.integrador.exceptions.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CharacteristicService {

    @Autowired
    private CharacteristicRepository characteristicRepository;

    @Autowired
    private AmazonS3 amazonS3;

    public List<Characteristic> getAllCharacteristics() {
        return characteristicRepository.findAllByDeletedAtIsNull();
    }

    public List<Characteristic> getAllDeletedCharacteristics() {
        return characteristicRepository.findAllByDeletedAtIsNotNull();
    }

    public Characteristic getCharacteristicByName(String name) {
        return characteristicRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Category not found with name: " + name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Characteristic createCharacteristic(CharacteristicDTO characteristicDTO, MultipartFile image) {
        Characteristic existingCharacteristic = characteristicRepository.findByName(characteristicDTO.getName()).orElse(null);
        if (existingCharacteristic != null) {
            throw new BadRequestException("Characteristic already exists with name: " + characteristicDTO.getName());
        }
        String name = characteristicDTO.getName();
        Characteristic characteristic = new Characteristic();
        characteristic.setName(name);
        try{
            Characteristic savedCharacteristic = characteristicRepository.save(characteristic);
            if (image != null) {
                String imageUrl = uploadImage(image, savedCharacteristic.getName());
                savedCharacteristic.setImage(imageUrl);
                characteristicRepository.save(savedCharacteristic);
            }
            return savedCharacteristic;
        } catch(Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Unexpected error, please contact support.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Characteristic updateCharacteristic(String name, UpdateCharacteristicDTO updateCharacteristicDTO, MultipartFile image){
        if (image == null && updateCharacteristicDTO.getName() == null) throw new BadRequestException("Nothing to update");

        Characteristic existingCharacteristic = characteristicRepository.findByName(name).orElseThrow(() -> new BadRequestException("Category not found with name: " + name));
        existingCharacteristic.setName(updateCharacteristicDTO.getName());

        if (image != null) {
            if (existingCharacteristic.getImage() != null) {
                deleteImage(existingCharacteristic.getImage());
            }
            String imageUrl = uploadImage(image, existingCharacteristic.getName());
            existingCharacteristic.setImage(imageUrl);
        }
        return characteristicRepository.save(existingCharacteristic);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void softDeleteCharacteristic(String name) {
        Characteristic characteristic = characteristicRepository.findByName(name)
                .orElseThrow(() -> new BadRequestException("Characteristic not found with name: " + name));

        // Establecer la fecha de eliminación
        characteristic.setDeletedAt(new Date());

        // Agrega estas líneas para imprimir la fecha antes de guardarla
        System.out.println("Fecha antes de guardar: " + characteristic.getDeletedAt());

        characteristicRepository.save(characteristic);
    }

    private String uploadImage(MultipartFile image, String name) {
        String bucketName = "1023c07-grupo5";
        String folderName = "images/characteristics/" + name + "/";
        String imageName = folderName + generateUniqueFileName(image);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, imageName, image.getInputStream(), metadata);
            amazonS3.putObject(request);

            return amazonS3.getUrl(bucketName, imageName).toString();
        } catch (IOException e) {
            throw new BadRequestException("Error uploading image");
        }
    }

    private boolean deleteImage(String image) {
        try {
            String[] parts = image.split("amazonaws.com/");
            String imageName = parts[parts.length - 1];
            String bucketName = "1023c07-grupo5";
            amazonS3.deleteObject(bucketName, imageName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateUniqueFileName(MultipartFile image) {
        return UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
    }
}
