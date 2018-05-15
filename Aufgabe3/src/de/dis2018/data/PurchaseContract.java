package de.dis2018.data;

import de.dis2018.util.Helper;

import javax.persistence.*;

/**
 * Purchase Contract-Bean
 */
@Entity
@Table(name = "purchasecontract")
@PrimaryKeyJoinColumn(name = "id")
public class PurchaseContract extends Contract {

	@Column(name = "noOfInstallments")
	private int noOfInstallments;

	@Column(name = "intrestRate")
	private int intrestRate;

	@ManyToOne
	@JoinColumn(name = "houseID", unique = true, insertable = false, updatable = false)
	private House house;
	
	public PurchaseContract() {
		super();
	}
	
	public int getNoOfInstallments() {
		return noOfInstallments;
	}
	public void setNoOfInstallments(int noOfInstallments) {
		this.noOfInstallments = noOfInstallments;
	}
	public int getIntrestRate() {
		return intrestRate;
	}
	public void setIntrestRate(int intrestRate) {
		this.intrestRate = intrestRate;
	}
	
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + getNoOfInstallments();
		result = prime * result + getIntrestRate();
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof PurchaseContract))
			return false;
	
		PurchaseContract other = (PurchaseContract)obj;
	
		if(other.getContractNo() != getContractNo() ||
				!Helper.compareObjects(this.getDate(), other.getDate()) ||
				!Helper.compareObjects(this.getPlace(), other.getPlace()) ||
				other.getNoOfInstallments() != getNoOfInstallments() ||
				other.getIntrestRate() != getIntrestRate())
		{
			return false;
		}
		
		return true;
	}
}
