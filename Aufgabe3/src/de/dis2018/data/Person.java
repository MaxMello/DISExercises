package de.dis2018.data;

import de.dis2018.util.Helper;

import javax.persistence.*;
import java.util.Set;

/**
 * Person-Bean
 */
@Entity
@Table(name = "person")
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contractPartner")
    private Set<Contract> contracts;
	
	public Person() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((getFirstname() == null) ? 0 : getFirstname().hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof Person))
			return false;
	
		Person other = (Person)obj;
	
		if(other.getId() != getId() ||
				!Helper.compareObjects(this.getFirstname(), other.getFirstname()) ||
				!Helper.compareObjects(this.getName(), other.getName()) ||
				!Helper.compareObjects(this.getAddress(), other.getAddress()))
		{
			return false;
		}
		
		return true;
	}
}
