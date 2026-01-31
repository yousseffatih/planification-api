package com.WALID.planification_api.services.Cumpus;

import java.util.List;

import com.WALID.planification_api.playload.DTO.CumpusDTO;
import com.WALID.planification_api.playload.DTO.ListAttributAUTO;

public interface InCumpusService {
	public List<CumpusDTO> getAllCumpus();

	public List<ListAttributAUTO> getCumpusListApi();

	public CumpusDTO getCumpusById(Long id);

	public CumpusDTO addCumpus(CumpusDTO cumpusDTO);

	public void deleteCumpus(Long id, String motif);

	public CumpusDTO updateCumpus(Long id, CumpusDTO cumpusDTO);
}
