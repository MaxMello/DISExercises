package de.dis2011;

import de.dis2011.data.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	private static void showMainMenu() {
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int QUIT = 1;
		final int MENU_ESTATE = 2;
		final int MENU_CONTRACT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
        mainMenu.addEntry("Estate-Verwaltung", MENU_ESTATE);
        mainMenu.addEntry("Vertrags-Verwaltung", MENU_CONTRACT);
        mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_MAKLER:
					showMaklerMenu();
					break;
                case MENU_ESTATE:
                    showEstateMenu();
                    break;
                case MENU_CONTRACT:
                    showContractMenu();
                    break;
				case QUIT:
					return;
			}
		}
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	private static void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int BACK = 1;
		final int CHANGE_MAKLER = 2;
		final int DELETE_MAKLER = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
        maklerMenu.addEntry("Makler bearbeiten", CHANGE_MAKLER);
        maklerMenu.addEntry("Makler löschen", DELETE_MAKLER);
        maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

        if("geheim".equals(FormUtil.readString("Bitte Passwort eingeben"))) {
            //Verarbeite Eingabe
            while (true) {
                int response = maklerMenu.show();

                switch (response) {
                    case NEW_MAKLER:
                        newMakler();
                        break;
                    case CHANGE_MAKLER:
                        changeMakler();
                        break;
                    case DELETE_MAKLER:
                        deleteMakler();
                        break;
                    case BACK:
                        return;
                }
            }
        } else {
            System.out.println("Falsches Passwort");
        }
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	private static void newMakler() {
		Makler m = new Makler();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
	}

    /**
     * Legt einen neuen Makler an, nachdem der Benutzer
     * die entprechenden Daten eingegeben hat.
     */
    private static void changeMakler() {
        System.out.println("Liste aller Makler: ");
        for(Makler makler : Makler.getAll()) {
            System.out.println("Makler ID=" + makler.getId() + ", Name=" + makler.getName());
        }
        System.out.println("Bitte ID eingeben, um Makler zu ändern");

        Integer id = FormUtil.readInt("ID");

        Makler m = Makler.load(id);
        if(m != null) {
            m.setName(FormUtil.readString("Name"));
            m.setAddress(FormUtil.readString("Adresse"));
            m.setLogin(FormUtil.readString("Login"));
            m.setPassword(FormUtil.readString("Passwort"));
            m.save();
            System.out.println("Makler erfolgreich geändert");
        } else {
            System.out.println("Makler exisitert nicht");
        }
    }

    /**
     * Legt einen neuen Makler an, nachdem der Benutzer
     * die entprechenden Daten eingegeben hat.
     */
    private static void deleteMakler() {
        System.out.println("Liste aller Makler: ");
        for(Makler makler : Makler.getAll()) {
            System.out.println("Makler ID= " + makler.getId() + ", Name=" + makler.getName());
        }
        System.out.println("Bitte ID eingeben, um Makler zu löschen");

        Integer id = FormUtil.readInt("ID");

        boolean deleted = Makler.delete(id);
        if(deleted) {
            System.out.println("Makler erfolgreich gelöscht");
        } else {
            System.out.println("Makler exisitert nicht");
        }
    }

    private static void showEstateMenu() {
        //Menüoptionen
        final int NEW_ESTATE = 0;
        final int BACK = 1;
        final int CHANGE_ESTATE = 2;
        final int DELETE_ESTATE = 3;

        //Maklerverwaltungsmenü
        Menu estateMenu = new Menu("Estate-Verwaltung");
        estateMenu.addEntry("Neues Estate", NEW_ESTATE);
        estateMenu.addEntry("Estate bearbeiten", CHANGE_ESTATE);
        estateMenu.addEntry("Estate löschen", DELETE_ESTATE);
        estateMenu.addEntry("Zurück zum Hauptmenü", BACK);


        final String username = FormUtil.readString("Bitte Nutzernamen eingeben");
        final String password = FormUtil.readString("Passwort");
        if(Makler.login(username, password)) {
            //Verarbeite Eingabe
            while (true) {
                int response = estateMenu.show();

                switch (response) {
                    case NEW_ESTATE:
                        newEstate();
                        break;
                    case CHANGE_ESTATE:
                        changeEstate();
                        break;
                    case DELETE_ESTATE:
                        deleteEstate();
                        break;
                    case BACK:
                        return;
                }
            }
        } else {
            System.out.println("Nutzername und/oder Passwort falsch");
        }
    }

    private static void newEstate() {
        final String answer = FormUtil.readString("Was möchten Sie erstellen ('Haus'/'Apartment')");
        if("Haus".equalsIgnoreCase(answer)){
            final House house = new House();
            house.setAddress(FormUtil.readString("Addresse"));
            house.setArea(FormUtil.readInt("Fläche"));
            house.setPrice(FormUtil.readInt("Preis"));
            house.setGarden("ja".equalsIgnoreCase(FormUtil.readString("Garten (ja/nein)")));
            house.setFloors(FormUtil.readInt("Stockwerke"));
            house.save();
        } else if ("Apartment".equalsIgnoreCase(answer)) {
            final Apartment apartment = new Apartment();
            apartment.setAddress(FormUtil.readString("Addresse"));
            apartment.setArea(FormUtil.readInt("Fläche"));
            apartment.setRent(FormUtil.readInt("Miete"));
            apartment.setRooms(FormUtil.readInt("Räume"));
            apartment.setFloor(FormUtil.readInt("Stockwerk"));
            apartment.setKitchen("ja".equalsIgnoreCase(FormUtil.readString("Einbauküche (ja/nein)")));
            apartment.setBalcony("ja".equalsIgnoreCase(FormUtil.readString("Balkon (ja/nein)")));
            apartment.save();
        } else {
            System.out.print("Unbekannte Auswahl");
        }
    }

    /**
     * Legt einen neuen Makler an, nachdem der Benutzer
     * die entprechenden Daten eingegeben hat.
     */
    private static void changeEstate() {
        final String answer = FormUtil.readString("Was möchten Sie bearbeiten ('Haus'/'Apartment')");
        if("Haus".equalsIgnoreCase(answer)){
            System.out.println("Liste aller Häuser: ");
            for(House house : House.getAll()) {
                System.out.println("Haus ID=" + house.getId() + ", Adresse=" + house.getAddress());
            }
            System.out.println("Bitte ID eingeben, um Haus zu ändern");
            Integer id = FormUtil.readInt("ID");
            final House house = House.load(id);
            if(house != null) {
                house.setId(id);
                house.setAddress(FormUtil.readString("Addresse"));
                house.setArea(FormUtil.readInt("Fläche"));
                house.setPrice(FormUtil.readInt("Preis"));
                house.setGarden("ja".equalsIgnoreCase(FormUtil.readString("Garten (ja/nein)")));
                house.setFloors(FormUtil.readInt("Stockwerke"));
                house.save();
                System.out.println("Haus erfolgreich geändert");
            } else {
                System.out.println("Haus existiert nicht");
            }
        } else if ("Apartment".equalsIgnoreCase(answer)) {
            System.out.println("Liste aller Apartments: ");
            for(Apartment apartment : Apartment.getAll()) {
                System.out.println("Apartment ID=" + apartment.getId() + ", Adresse=" + apartment.getAddress());
            }
            System.out.println("Bitte ID eingeben, um Apartment zu ändern");
            Integer id = FormUtil.readInt("ID");
            final Apartment apartment = Apartment.load(id);
            if(apartment != null) {
                apartment.setAddress(FormUtil.readString("Addresse"));
                apartment.setArea(FormUtil.readInt("Fläche"));
                apartment.setRent(FormUtil.readInt("Miete"));
                apartment.setRooms(FormUtil.readInt("Räume"));
                apartment.setFloor(FormUtil.readInt("Stockwerk"));
                apartment.setKitchen("ja".equalsIgnoreCase(FormUtil.readString("Einbauküche (ja/nein)")));
                apartment.setBalcony("ja".equalsIgnoreCase(FormUtil.readString("Balkon (ja/nein)")));
                apartment.save();
                System.out.println("Apartment erfolgreich geändert");
            } else {
                System.out.println("Apartment existiert nicht");
            }
        } else {
            System.out.print("Unbekannte Auswahl");
        }
    }

    /**
     * Legt einen neuen Makler an, nachdem der Benutzer
     * die entprechenden Daten eingegeben hat.
     */
    private static void deleteEstate() {
        System.out.println("Liste aller Apartments: ");
        for(Apartment apartment : Apartment.getAll()) {
            System.out.println("Apartment ID=" + apartment.getId() + ", Adresse=" + apartment.getAddress());
        }
        System.out.println("Liste aller Häuser: ");
        for(House house : House.getAll()) {
            System.out.println("Haus ID=" + house.getId() + ", Adresse=" + house.getAddress());
        }
        System.out.println("Bitte ID eingeben, um Estate zu löschen");

        Integer id = FormUtil.readInt("ID");

        boolean deleted = Estate.delete(id);
        if(deleted) {
            System.out.println("Estate erfolgreich gelöscht");
        } else {
            System.out.println("Estate exisitert nicht");
        }
    }

    private static void showContractMenu() {
        //Menüoptionen
        final int NEW_PERSON = 0;
        final int BACK = 1;
        final int SELL_HOUSE = 2;
        final int RENT_APARTMENT = 3;
        final int OVERVIEW = 4;

        //Maklerverwaltungsmenü
        Menu maklerMenu = new Menu("Vertrags-Verwaltung");
        maklerMenu.addEntry("Neuer Kunde", NEW_PERSON);
        maklerMenu.addEntry("Haus verkaufen", SELL_HOUSE);
        maklerMenu.addEntry("Apartment vermieten", RENT_APARTMENT);
        maklerMenu.addEntry("Vertags-Übersicht", OVERVIEW);
        maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while (true) {
            int response = maklerMenu.show();
            switch (response) {
                case NEW_PERSON:
                    newPerson();
                    break;
                case SELL_HOUSE:
                    sellHouse();
                    break;
                case RENT_APARTMENT:
                    rentApartment();
                    break;
                case OVERVIEW:
                    contractOverview();
                    break;
                case BACK:
                    return;
            }
        }
    }

    private static void newPerson() {
        Person person = new Person();
        person.setFirstname(FormUtil.readString("Vorname"));
        person.setName(FormUtil.readString("Nachname"));
        person.setAddress(FormUtil.readString("Adresse"));
        person.save();
        System.out.println("Person gespeichert");

    }

    private static void sellHouse() {
        System.out.println("Welche Person will ein Haus kaufen?");
        Person.getAll().forEach(it -> System.out.println(it.toString()));
        int personID = FormUtil.readInt("ID des Käufers");
        Person person = Person.load(personID);
        if(person != null) {
            System.out.println("Welches Haus soll gekauft werden?");
            House.getAll().forEach(it -> System.out.println(it.toString()));
            int houseID = FormUtil.readInt("ID des Hauses");
            House house = House.load(houseID);
            if(house != null) {
                PurchaseContract contract = new PurchaseContract();
                contract.setPlace(FormUtil.readString("Ort der Unterschrift"));
                contract.setcDate(new Date(System.currentTimeMillis()));
                contract.setRate(FormUtil.readInt("Rate"));
                contract.setNumberOfInstallments(FormUtil.readInt("Anzahl Zahlungen"));
                contract.save();

                Sell sell = new Sell();
                sell.setHouseID(houseID);
                sell.setPersonID(personID);
                sell.setPurchaseContractNo(contract.getNo());
                sell.save();
                System.out.println("Vertrag erfolgreich gespeichert");
            } else {
                System.out.println("Haus existiert nicht");
            }
        } else {
            System.out.println("Person existiert nicht");
        }
    }

    private static void rentApartment() {
        System.out.println("Welche Person will ein Apartment mieten?");
        Person.getAll().forEach(it -> System.out.println(it.toString()));
        int personID = FormUtil.readInt("ID des Mieters");
        Person person = Person.load(personID);
        if(person != null) {
            System.out.println("Welches Apartment soll gemietet werden?");
            Apartment.getAll().forEach(it -> System.out.println(it.toString()));
            int apartmentID = FormUtil.readInt("ID des Apartments");
            Apartment apartment = Apartment.load(apartmentID);
            if(apartment != null) {
                TenancyContract contract = new TenancyContract();
                contract.setPlace(FormUtil.readString("Ort der Unterschrift"));
                contract.setcDate(new Date(System.currentTimeMillis()));
                try {
                    contract.setStartDate(new Date(new SimpleDateFormat("dd.MM.YYYY").parse(FormUtil.readString("Startdatum")).getTime()));
                } catch (ParseException e) {
                    // e.printStackTrace();
                    System.out.println("Invalid date, set date to today");
                    contract.setStartDate(new Date(System.currentTimeMillis()));
                }
                contract.setDuration(FormUtil.readInt("Dauer in Tagen"));
                contract.setAdditionalCosts(FormUtil.readInt("Zusatzkosten"));
                contract.save();

                Rent rent = new Rent();
                rent.setApartmentID(apartmentID);
                rent.setPersonID(personID);
                rent.setTenancyContractNo(contract.getNo());
                rent.save();
                System.out.println("Vertrag erfolgreich gespeichert");
            } else {
                System.out.println("Aparment existiert nicht");
            }
        } else {
            System.out.println("Person existiert nicht");
        }
    }

    private static void contractOverview() {
        List<Contract> contractList = new ArrayList<>();
        contractList.addAll(TenancyContract.getAll());
        contractList.addAll(PurchaseContract.getAll());
        contractList.forEach( it ->
                System.out.println(it.toString())
        );

    }
}
