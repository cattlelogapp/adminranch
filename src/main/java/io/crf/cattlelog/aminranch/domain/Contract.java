package io.crf.cattlelog.aminranch.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@IdClass(ContractId.class)
@Table(name = "consultant_ranch")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
//	private Long ranchId;
	@ManyToOne
    @JsonIgnoreProperties("contracts")
    private Ranch ranch;
	
	@Id
//	private Long consultantId;
	@ManyToOne
    @JsonIgnoreProperties("contracts")
    private Consultant consultant;
	
	@Column
	private String status;

//	public Long getRanchId() {
//		return ranchId;
//	}
//
//	public void setRanchId(Long ranchId) {
//		this.ranchId = ranchId;
//	}
//
//	public Long getConsultantId() {
//		return consultantId;
//	}
//
//	public void setConsultantId(Long consultantId) {
//		this.consultantId = consultantId;
//	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Ranch getRanch() {
		return ranch;
	}

	public void setRanch(Ranch ranch) {
		this.ranch = ranch;
	}

	public Consultant getConsultant() {
		return consultant;
	}

	public void setConsultant(Consultant consultant) {
		this.consultant = consultant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((consultant == null) ? 0 : consultant.hashCode());
		result = prime * result + ((ranch == null) ? 0 : ranch.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		if (consultant == null) {
			if (other.consultant != null)
				return false;
		} else if (!consultant.equals(other.consultant))
			return false;
		if (ranch == null) {
			if (other.ranch != null)
				return false;
		} else if (!ranch.equals(other.ranch))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Contract [ranch=" + ranch + ", consultant=" + consultant + ", status=" + status + "]";
	}

	
	
	
}
