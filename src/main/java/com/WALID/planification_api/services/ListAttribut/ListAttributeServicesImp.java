package com.WALID.planification_api.services.ListAttribut;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WALID.planification_api.constants.GlobalConstant;
import com.WALID.planification_api.entities.ListAttribut;
import com.WALID.planification_api.playload.ResourceNotFoundException;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.ListAttributDTO;
import com.WALID.planification_api.repositories.Parametrage.ListAttributRepository;

@Service
public class ListAttributeServicesImp implements InListAttributeServices{

	@Autowired
	private ListAttributRepository listAttributRepository;

	@Override
	public ListAttributDTO addListAttribute(ListAttributDTO listAttributDTO) {
		ListAttribut listAttribut = new ListAttribut();
		listAttribut.setDateCreation(new Date());
		listAttribut.setStatut(GlobalConstant.STATUT_ACTIF);

		listAttribut.setLibelle(listAttributDTO.getLibelle());
		listAttribut.setValue(listAttributDTO.getValue());
		listAttribut.setListNameApi(listAttributDTO.getListNameApi());
		return mapToDto(listAttributRepository.save(listAttribut));
	}
	
	@Override
	public void deleteAttributDTO(Long id) {
		ListAttribut listAttribut = listAttributRepository.findByIdAndStatut(id, GlobalConstant.STATUT_ACTIF).orElseThrow(() -> new ResourceNotFoundException("Attribute","id",id));
		
		listAttribut.setStatut(GlobalConstant.STATUT_DELETE);
		listAttributRepository.save(listAttribut);
	}

	private ListAttributDTO mapToDto(ListAttribut x)
	{
		ListAttributDTO dto = new ListAttributDTO();
		dto.setId(x.getId());
		dto.setLibelle(x.getLibelle());
        dto.setValue(x.getValue());
        dto.setListNameApi(x.getListNameApi());

        dto.setStatut(x.getStatut());
        dto.setDateCreation(x.getDateCreation());
        dto.setDateDesactivation(x.getDateDesactivation());
        dto.setDateModification(x.getDateModification());
        return dto;
    }

	@Override
	public List<ListAttributAUTO> getListAttributes(String listNameApi) {
		List<ListAttribut> list = listAttributRepository.findAllByListNameApiAndStatut(listNameApi, GlobalConstant.STATUT_ACTIF);
		return list.stream().map((e) -> mapToDtoAuto(e)).collect(Collectors.toList());
	}
	private ListAttributAUTO mapToDtoAuto(ListAttribut x)
	{
		ListAttributAUTO dto = new ListAttributAUTO();
		dto.setId(x.getId());
		dto.setLibelle(x.getLibelle());
		dto.setValue(x.getValue());
        return dto;
    }

}
