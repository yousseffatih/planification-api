package com.WALID.planification_api.services.Professeur;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.ListAttribut;
import com.WALID.planification_api.entities.Professeur;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.ProfesseurDTO;
import com.WALID.planification_api.repositories.Parametrage.ListAttributRepository;
import com.WALID.planification_api.repositories.Parametrage.ProfesseurRepository;

@Service
public class ProfesseurServiceImp implements InProfesseurServices {

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private ListAttributRepository listAttributRepository;

    @Override
    public List<ProfesseurDTO> AllProfesseur() {
        List<Professeur> profs = professeurRepository.findAllWithStatus();
        return profs.stream().map((c) -> mapToDTO(c)).collect(Collectors.toList());
    }

    @Override
    public ProfesseurDTO getProfesseurById(Long id) {
        Professeur prof = professeurRepository.findByIdStatut(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur", "id", id));
        return mapToDTO(prof);
    }

    @Override
    public List<ListAttributAUTO> gettAllSallesApi() {
        List<Professeur> salles = professeurRepository.findAllWithStatus();
        return salles.stream().map((c) -> mapToList(c)).collect(Collectors.toList());
    }

    @Override
    public ProfesseurDTO addProfesseur(ProfesseurDTO professeurDTO) {
        Professeur prof = new Professeur();
        ListAttribut typeProfAttribut = listAttributRepository
                .findByIdAndStatut(professeurDTO.getIdTypeProf(), GlobalConstant.STATUT_ACTIF)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Type de professeur", "id", professeurDTO.getIdTypeProf()));

        prof.setDateCreation(new Date());
        prof.setStatut(GlobalConstant.STATUT_ACTIF);
        prof.setNumeroTele(professeurDTO.getNumeroTele());
        prof.setEmail(professeurDTO.getEmail());
        // prof.setStatut(professeurDTO.getStatut());
        prof.setNom(professeurDTO.getNom());
        prof.setPrenom(professeurDTO.getPrenom());
        prof.setTypeProf(typeProfAttribut);

        return mapToDTO(professeurRepository.save(prof));
    }

    @Override
    public void deleteProfesseur(Long id, String motif) {
        Professeur cls = professeurRepository.findByIdStatut(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur", "id", id));
        cls.setDateDesactivation(new Date());
        cls.setStatut(GlobalConstant.STATUT_DELETE);
        cls.setMotif(motif);
        professeurRepository.save(cls);
    }

    @Override
    public ProfesseurDTO updateProfesseur(Long id, ProfesseurDTO professeurDTO) {
        Professeur professeur = professeurRepository.findByIdStatut(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur", "id", id));
        ListAttribut typeProfAttribut = listAttributRepository
                .findByIdAndStatut(professeurDTO.getIdTypeProf(), GlobalConstant.STATUT_ACTIF)
                .orElseThrow(() -> new ResourceNotFoundException("Type de salle", "id", professeurDTO.getIdTypeProf()));

        professeur.setNom(professeurDTO.getNom());
        professeur.setPrenom(professeurDTO.getPrenom());
        professeur.setNumeroTele(professeurDTO.getNumeroTele());
        professeur.setEmail(professeurDTO.getEmail());
        professeur.setStatut(professeurDTO.getStatut());
        professeur.setDateModification(new Date());
        professeur.setTypeProf(typeProfAttribut);
        Professeur cls = professeurRepository.save(professeur);
        return mapToDTO(cls);
    }

    private ProfesseurDTO mapToDTO(Professeur x) {
        ProfesseurDTO dto = new ProfesseurDTO();
        dto.setId(x.getId());
        dto.setNom(x.getNom());
        dto.setPrenom(x.getPrenom());
        dto.setEmail(x.getEmail());
        dto.setNumeroTele(x.getNumeroTele());
        dto.setIdTypeProf(x.getTypeProf().getId());
        dto.setLibelleTypeProf(x.getTypeProf().getLibelle());

        dto.setStatut(x.getStatut());
        dto.setDateCreation(x.getDateCreation());
        dto.setDateDesactivation(x.getDateDesactivation());
        dto.setDateModification(x.getDateModification());
        return dto;
    }

    private ListAttributAUTO mapToList(Professeur x) {
        ListAttributAUTO dto = new ListAttributAUTO();
        dto.setId(x.getId());
        dto.setLibelle(x.getNom() + " " + x.getPrenom());

        return dto;
    }

}
