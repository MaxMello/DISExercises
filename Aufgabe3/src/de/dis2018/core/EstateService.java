package de.dis2018.core;

import de.dis2018.data.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *  Class for managing all database entities.
 */
public class EstateService {
	//Hibernate Session
	private SessionFactory sessionFactory;
	
	public EstateService() {
		sessionFactory = new Configuration()
                .addAnnotatedClass(Apartment.class)
                .addAnnotatedClass(Contract.class)
                .addAnnotatedClass(Estate.class)
                .addAnnotatedClass(EstateAgent.class)
                .addAnnotatedClass(House.class)
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(PurchaseContract.class)
                .addAnnotatedClass(TenancyContract.class)
                .configure().buildSessionFactory();
	}
	
	/**
	 * Find an estate agent with the given id
	 * @param id The ID of the agent
	 * @return Agent with ID or null
	 */
	public EstateAgent getEstateAgentByID(int id) {
        return readFromDB(s -> s.get(EstateAgent.class, id));
	}
	
	/**
	 * Find estate agent with the given login.
	 * @param login The login of the estate agent
	 * @return Estate agent with the given ID or null
	 */
	public EstateAgent getEstateAgentByLogin(String login) {
        return readFromDB(s -> s.createQuery("from EstateAgent where login = :login", EstateAgent.class)
                .setParameter("login", login)
                .getSingleResult());
	}
	
	/**
	 * Returns all estateAgents
	 */
	public Set<EstateAgent> getAllEstateAgents() {
        return readFromDB(s -> new HashSet<>(s.createQuery("from EstateAgent", EstateAgent.class)
                .getResultList()));
	}
	
	/**
	 * Find an person with the given id
	 * @param id The ID of the person
	 * @return Person with ID or null
	 */
	public Person getPersonById(int id) {
        return readFromDB(s -> s.get(Person.class, id));
	}
	
	/**
	 * Adds an estate agent
	 * @param ea The estate agent
	 */
	public void addEstateAgent(final EstateAgent ea) {
        writeToDB(s -> s.saveOrUpdate(ea));
	}
	
	/**
	 * Deletes an estate agent
	 * @param ea The estate agent
	 */
	public void deleteEstateAgent(final EstateAgent ea) {
        writeToDB(s -> s.delete(ea));
	}
	
	/**
	 * Adds a person
	 * @param p The person
	 */
	public void addPerson(final Person p) {
        writeToDB((s -> s.saveOrUpdate(p)));
	}

	/**
	 * Returns all persons
	 */
	public Set<Person> getAllPersons() {
	    return readFromDB(s -> new HashSet<>(s.createQuery("from Person", Person.class)
                .getResultList()));
	}
	
	/**
	 * Deletes a person
	 * @param p The person
	 */
	public void deletePerson(final Person p) {
        writeToDB(s -> s.delete(p));
	}
	
	/**
	 * Adds a house
	 * @param h The house
	 */
	public void addHouse(final House h) {
		writeToDB(s -> s.saveOrUpdate(h));
	}
	
	/**
	 * Returns all houses of an estate agent
	 * @param ea the estate agent
	 * @return All houses managed by the estate agent
	 */
	public Set<House> getAllHousesForEstateAgent(EstateAgent ea) {
	    return readFromDB(s -> new HashSet<>(s.createQuery("from House where managerID = :managerID", House.class)
                .setParameter("managerID", ea.getId())
                .getResultList()));
	}
	
	/**
	 * Find a house with a given ID
	 * @param  id the house id
	 * @return The house or null if not found
	 */
	public House getHouseById(int id) {
        return readFromDB(s -> s.get(House.class, id));
	}
	
	/**
	 * Deletes a house
	 * @param h The house
	 */
	public void deleteHouse(final House h) {
        writeToDB(s -> s.delete(h));
	}
	
	/**
	 * Adds an apartment
	 * @param w the aparment
	 */
	public void addApartment(final Apartment w) {
		writeToDB(s -> s.saveOrUpdate(w));
	}
	
	/**
	 * Returns all apartments of an estate agent
	 * @param ea The estate agent
	 * @return All apartments managed by the estate agent
	 */
	public Set<Apartment> getAllApartmentsForEstateAgent(EstateAgent ea) {
        return readFromDB(s -> new HashSet<>(s.createQuery("from Apartment where managerID = :managerID", Apartment.class)
                .setParameter("managerID", ea.getId())
                .getResultList()));
	}
	
	/**
	 * Find an apartment with given ID
	 * @param id The ID
	 * @return The apartment or zero, if not found
	 */
	public Apartment getApartmentByID(final int id) {
		return readFromDB(s -> s.get(Apartment.class, id));
	}
	
	/**
	 * Deletes an apartment
	 * @param p The apartment
	 */
	public void deleteApartment(final Apartment w) {
		writeToDB(s -> s.delete(w));
	}
	
	
	/**
	 * Adds a tenancy contract
	 * @param t The tenancy contract
	 */
	public void addTenancyContract(final TenancyContract t) {
		writeToDB(s -> s.saveOrUpdate(t));
	}
	
	/**
	 * Adds a purchase contract
	 * @param p The purchase contract
	 */
	public void addPurchaseContract(final PurchaseContract p) {
		writeToDB(s -> s.saveOrUpdate(p));
	}
	
	/**
	 * Finds a tenancy contract with a given ID
	 * @param id Die ID
	 * @return The tenancy contract or zero if not found
	 */
	public TenancyContract getTenancyContractByID(int id) {
        return readFromDB(s -> s.get(TenancyContract.class, id));
	}
	
	/**
	 * Finds a purchase contract with a given ID
	 * @param id The id of the purchase contract
	 * @return The purchase contract or null if not found
	 */
	public PurchaseContract getPurchaseContractById(int id) {
        return readFromDB(s -> s.get(PurchaseContract.class, id));
	}
	
	/**
	 * Returns all tenancy contracts for apartments of the given estate agent
	 * @param m The estate agent
	 * @return All contracts belonging to apartments managed by the estate agent
	 */
	public Set<TenancyContract> getAllTenancyContractsForEstateAgent(EstateAgent ea) {
        return readFromDB(s -> new HashSet<>(s.createQuery("from TenancyContract where apartment.manager.id = :managerID", TenancyContract.class)
                .setParameter("managerID", ea.getId())
                .getResultList()));
	}
	
	/**
	 * Returns all purchase contracts for houses of the given estate agent
	 * @param m The estate agent
	 * @return All purchase contracts belonging to houses managed by the given estate agent
	 */
	public Set<PurchaseContract> getAllPurchaseContractsForEstateAgent(EstateAgent ea) {
        return readFromDB(s -> new HashSet<>(s.createQuery("from PurchaseContract where house.manager.id = :managerID", PurchaseContract.class)
                .setParameter("managerID", ea.getId())
                .getResultList()));
	}

	
	/**
	 * Adds some test data
	 */
	public void addTestData() {
		EstateAgent m = new EstateAgent();
		m.setName("Max Mustermann");
		m.setAddress("Am Informatikum 9");
		m.setLogin("max");
		m.setPassword("max");
		writeToDB(s -> s.saveOrUpdate(m));
		
		Person p1 = new Person();
		p1.setAddress("Informatikum");
		p1.setName("Mustermann");
		p1.setFirstname("Erika");
		writeToDB(s -> s.saveOrUpdate(p1));
		
		
		Person p2 = new Person();
		p2.setAddress("Reeperbahn 9");
		p2.setName("Albers");
		p2.setFirstname("Hans");
		writeToDB(s -> s.saveOrUpdate(p2));
		
		House h = new House();
		h.setCity("Hamburg");
		h.setPostalcode(22527);
		h.setStreet("Vogt-Kölln-Street");
		h.setStreetnumber("2a");
		h.setSquareArea(384);
		h.setFloors(5);
		h.setPrice(10000000);
		h.setGarden(true);
		h.setManager(m);
		writeToDB(s -> s.saveOrUpdate(h));

		
		final Apartment w = new Apartment();
		w.setCity("Hamburg");
		w.setPostalcode(22527);
		w.setStreet("Vogt-Kölln-Street");
		w.setStreetnumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setKitchen(true);
		w.setBalcony(false);
		w.setManager(m);
		writeToDB(s -> s.saveOrUpdate(w));


		final Apartment a = new Apartment();
		a.setCity("Berlin");
		a.setPostalcode(22527);
		a.setStreet("Vogt-Kölln-Street");
		a.setStreetnumber("3");
		a.setSquareArea(120);
		a.setFloor(4);
		a.setRent(790);
		a.setKitchen(true);
		a.setBalcony(false);
		a.setManager(m);

        writeToDB(s -> s.saveOrUpdate(a));

		PurchaseContract pc = new PurchaseContract();
		pc.setHouse(h);
		pc.setContractPartner(p1);
		pc.setContractNo(9234);
		pc.setDate(new Date(System.currentTimeMillis()));
		pc.setPlace("Hamburg");
		pc.setNoOfInstallments(5);
		pc.setIntrestRate(4);

        writeToDB(s -> s.saveOrUpdate(pc));


        TenancyContract tc = new TenancyContract();
		tc.setApartment(w);
		tc.setContractPartner(p2);
		tc.setContractNo(23112);
		tc.setDate(new Date(System.currentTimeMillis()-1000000000));
		tc.setPlace("Berlin");
		tc.setStartDate(new Date(System.currentTimeMillis()));
		tc.setAdditionalCosts(65);
		tc.setDuration(36);

		writeToDB(s -> s.saveOrUpdate(tc));
    }

    private void writeToDB(Consumer<Session> consumer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            consumer.accept(session);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> T readFromDB(Function<Session, T> f) {
        try (Session session = sessionFactory.openSession()) {
            return f.apply(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
