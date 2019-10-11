package io.crf.cattlelog.aminranch.service.dto;

import java.io.Serializable;


public class RanchWithAccessDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private Long id;
	private String name;
	private String status;
	private Long consultantUserId;

	public RanchWithAccessDTO() {
	}
	
	public RanchWithAccessDTO(final Long id, final String name, final String status, final Long consultantUserId) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.consultantUserId = consultantUserId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getConsultantUserId() {
		return consultantUserId;
	}

	public void setConsultantUserId(Long consultantUserId) {
		this.consultantUserId = consultantUserId;
	}

}
