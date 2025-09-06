package com.WALID.planification_api.services.Villes;

import java.util.List;

import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.VillesDTO;

public interface InVillesServices {
	
	    public List<VillesDTO> getAllVilles();
	    
	    public List<ListAttributAUTO> getVillesListApi();

	    public VillesDTO getVilleById(Long id);

	    public VillesDTO addVille(VillesDTO classesDTO);

	    public void deleteVille(Long id);

	    public VillesDTO updateVille(Long idLong , VillesDTO classesDTO);
}
