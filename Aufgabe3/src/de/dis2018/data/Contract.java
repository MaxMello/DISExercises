package de.dis2018.data;

import java.util.Date;

import de.dis2018.util.Helper;

import javax.persistence.*;

/**
 * Contract-Bean
 */
@Entity
@Table(name = "contract")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "contractNo")
	private int contractNo;

	@Column(name = "date")
	private Date date;

	@Column(name = "place")
	private String place;

	@OneToOne
	Person contractPartner;
	
	public Contract() {

	}
	
	public int getContractNo() {
		return contractNo;
	}
	public void setContractNo(int contractNo) {
		this.contractNo = contractNo;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person getContractPartner() {
		return contractPartner;
	}

	public void setContractPartner(Person contractPartner) {
		this.contractPartner = contractPartner;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
		result = prime * result + ((getPlace() == null) ? 0 : getPlace().hashCode());
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof Contract))
			return false;
	
		Contract other = (Contract)obj;
	
		if(other.getContractNo() != getContractNo() ||
				!Helper.compareObjects(this.getDate(), other.getDate()) ||
				!Helper.compareObjects(this.getPlace(), other.getPlace()))
		{
			return false;
		}
		
		return true;
	}
}
