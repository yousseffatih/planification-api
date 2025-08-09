package com.WALID.planification_api.services.Classes;

import java.util.List;

import com.WALID.planification_api.playload.DTO.ClassesDTO;


public interface  InClassesServices {
    public List<ClassesDTO> getAllClasses();

    public ClassesDTO getClasseById(Long id);

    public ClassesDTO addClasse(ClassesDTO classesDTO);

    public void deleteClasse(Long id);

    public ClassesDTO updateClasse(Long idLong , ClassesDTO classesDTO);
}
