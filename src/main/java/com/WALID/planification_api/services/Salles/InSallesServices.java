package com.WALID.planification_api.services.Salles;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.WALID.planification_api.playload.DTO.ListAttributAUTO;
import com.WALID.planification_api.playload.DTO.SallesDTO;



public interface InSallesServices {
	public List<SallesDTO> getAllSalles();

	public List<ListAttributAUTO> gettAllSallesApi();

    public SallesDTO getSalleById(Long id);

    public SallesDTO addSalle(SallesDTO sallesDTO);

    public void deleteSalle(Long id);

    public SallesDTO updateSalle(Long id , SallesDTO sallesDTO);

    public List<SallesDTO> availableSalles(Date date , LocalDateTime timeStar , LocalDateTime timeEnd);

    public List<SallesDTO> availableSallesModif(Date date , LocalDateTime timeStar , LocalDateTime timeEnd, Long id);
}
