package com.WALID.planification_api.services.Classes;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.Classes;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.ClassesDTO;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.repositories.Parametrage.ClassesRepository;

@Service
public class ClassesServiceImp implements InClassesServices {

    @Autowired
    private ClassesRepository classesRepository;

    @Override
    public List<ClassesDTO> getAllClasses() {
        List<Classes> classes = classesRepository.findAllWithStatus();
        return classes.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
    }

    @Override
    public List<ListAttributAUTO> getClassesListApi() {
        List<Classes> classes = classesRepository.getClassesListApi();
        return classes.stream().map((c) -> mapToList(c)).collect(Collectors.toList());
    }

    @Override
    public ClassesDTO getClasseById(Long id) {
        Classes classe = classesRepository.findByIdStatut(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", "id", id));
        return mapToDTO(classe);
    }

    @Override
    public ClassesDTO addClasse(ClassesDTO classesDTO) {
        Classes classe = new Classes();

        classe.setDateCreation(new Date());
        classe.setStatut(GlobalConstant.STATUT_ACTIF);
        classe.setNom(GlobalConstant.formatName(classesDTO.getNom()));
        classe.setLibelle(classesDTO.getNom());
        classe.setNomberEff(classesDTO.getNomberEff());
        classe.setAnnuerScolaire(classesDTO.getAnnuerScolaire());
        Classes cls = classesRepository.save(classe);
        return mapToDTO(cls);
    }

    @Override
    public void deleteClasse(Long id) {
        Classes cls = classesRepository.findByIdStatut(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", "id", id));
        cls.setDateDesactivation(new Date());
        cls.setStatut(GlobalConstant.STATUT_DELETE);
        classesRepository.save(cls);
    }

    @Override
    public ClassesDTO updateClasse(Long id, ClassesDTO classesDTO) {
        Classes classe = classesRepository.findByIdStatut(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe", "id", id));

        classe.setLibelle(classesDTO.getNom());
        classe.setNom(GlobalConstant.formatName(classesDTO.getNom()));
        // classe.setStatut(classesDTO.getStatut());
        classe.setDateModification(new Date());
        classe.setNomberEff(classesDTO.getNomberEff());
        classe.setAnnuerScolaire(classesDTO.getAnnuerScolaire());
        classe.setStatut(classesDTO.getStatut());
        Classes cls = classesRepository.save(classe);
        return mapToDTO(cls);
    }

    private ClassesDTO mapToDTO(Classes x) {
        ClassesDTO dto = new ClassesDTO();
        dto.setId(x.getId());
        dto.setLibelle(x.getLibelle());
        dto.setNom(x.getNom());
        dto.setAnnuerScolaire(x.getAnnuerScolaire());
        dto.setNomberEff(x.getNomberEff());

        dto.setStatut(x.getStatut());
        dto.setDateCreation(x.getDateCreation());
        dto.setDateDesactivation(x.getDateDesactivation());
        dto.setDateModification(x.getDateModification());
        return dto;
    }

    private ListAttributAUTO mapToList(Classes x) {
        ListAttributAUTO dto = new ListAttributAUTO();
        dto.setId(x.getId());
        dto.setLibelle(x.getNom());

        return dto;
    }
}
