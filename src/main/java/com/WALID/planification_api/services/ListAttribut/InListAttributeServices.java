package com.WALID.planification_api.services.ListAttribut;

import java.util.List;

import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.ListAttributDTO;


public interface InListAttributeServices {

	public ListAttributDTO addListAttribute(ListAttributDTO listAttributDTO);
	
	public void deleteAttributDTO(Long id);

	public List<ListAttributAUTO> getListAttributes(String listNameApi);
}
