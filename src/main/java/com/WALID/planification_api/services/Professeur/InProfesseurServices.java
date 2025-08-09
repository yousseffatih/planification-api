package com.WALID.planification_api.services.Professeur;
import java.util.List;

import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.ProfesseurDTO;



public interface  InProfesseurServices {
    public List<ProfesseurDTO>AllProfesseur();

    public ProfesseurDTO getProfesseurById(Long id);

    public ProfesseurDTO addProfesseur(ProfesseurDTO classesDTO);

    public void deleteProfesseur(Long id);

    public ProfesseurDTO updateProfesseur(Long idLong , ProfesseurDTO classesDTO);
    
	public List<ListAttributAUTO> gettAllSallesApi();
}
