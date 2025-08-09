package com.WALID.planification_api.playload.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableResponseDTO {

	private List<?> content;

	private int pageNo;

	private int pageSize;

	private long totalElement;

	private int totlaPages;

	private Boolean last;
}
