package de.dis2018.data;

import de.dis2018.util.Helper;

import javax.persistence.*;
import java.util.List;

/**
 * House-Bean
 */
@Entity
@Table(name = "house")
@PrimaryKeyJoinColumn(name = "id")
public class House extends Estate {

	@Column(name = "floors")
	private int floors;

	@Column(name = "price")
	private int price;

	@Column(name = "garden")
	private boolean garden;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "house")
    private List<PurchaseContract> contracts;
	
	public House() {
		super();
	}
	
	public int getFloors() {
		return floors;
	}
	public void setFloors(int floors) {
		this.floors = floors;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isGarden() {
		return garden;
	}
	public void setGarden(boolean garden) {
		this.garden = garden;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + getFloors();
		result = prime * result + getPrice();
		result = prime * result + ((isGarden()) ? 1 : 0);
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof House))
			return false;
	
		House other = (House)obj;
	
		if(other.getId() != getId() ||
				other.getPostalcode() != getPostalcode() ||
				other.getSquareArea() != getSquareArea() ||
				!Helper.compareObjects(this.getCity(), other.getCity()) ||
				!Helper.compareObjects(this.getStreet(), other.getStreet()) ||
				!Helper.compareObjects(this.getStreetnumber(), other.getStreetnumber()) ||
				getFloors() != other.getFloors() ||
				getPrice() != other.getPrice() ||
				isGarden() != other.isGarden())
		{
			return false;
		}
		
		return true;
	}
}
