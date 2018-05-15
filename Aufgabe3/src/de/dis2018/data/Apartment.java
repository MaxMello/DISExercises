package de.dis2018.data;

import de.dis2018.util.Helper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * Apartment Bean
 */
@Entity
@Table(name = "apartment")
@PrimaryKeyJoinColumn(name = "id")
public class Apartment extends Estate {

	@Column(name = "floor")
	private int floor;

	@Column(name = "rent")
	private int rent;

	@Column(name = "rooms")
	private int rooms;

	@Column(name = "balcony")
	private boolean balcony;

	@Column(name = "kitchen")
	private boolean kitchen;
	
	public Apartment() {
		super();
	}
	
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public int getRent() {
		return rent;
	}
	public void setRent(int rent) {
		this.rent = rent;
	}
	public int getRooms() {
		return rooms;
	}
	public void setRooms(int rooms) {
		this.rooms = rooms;
	}
	public boolean isBalcony() {
		return balcony;
	}
	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}
	public boolean isKitchen() {
		return kitchen;
	}
	public void setKitchen(boolean kitchen) {
		this.kitchen = kitchen;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + getFloor();
		result = prime * result + getRent();
		result = prime * result + getRooms();
		result = prime * result + ((isBalcony()) ? 1 : 0);
		result = prime * result + ((isKitchen()) ? 1 : 0);
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof Apartment))
			return false;
	
		Apartment other = (Apartment)obj;
	
		if(other.getId() != getId() ||
				other.getPostalcode() != getPostalcode() ||
				other.getSquareArea() != getSquareArea() ||
				!Helper.compareObjects(this.getCity(), other.getCity()) ||
				!Helper.compareObjects(this.getStreet(), other.getStreet()) ||
				!Helper.compareObjects(this.getStreetnumber(), other.getStreetnumber()) ||
				getFloor() != other.getFloor() ||
				getRent() != other.getRent() ||
				getRooms() != other.getRooms() ||
				isBalcony() != other.isBalcony() ||
				isKitchen() != other.isKitchen())
		{
			return false;
		}
		
		return true;
	}
}
