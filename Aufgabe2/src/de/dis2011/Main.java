package de.dis2011;

import de.dis2011.data.Apartment;
import de.dis2011.data.Estate;
import de.dis2011.data.House;
import de.dis2011.data.Makler;

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
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int QUIT = 1;
		final int MENU_ESTATE = 2;
		final int MENU_CONTRACT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
        mainMenu.addEntry("Estate-Verwaltung", MENU_ESTATE);
        mainMenu.addEntry("Vertrags-Verwaltung", MENU_ESTATE);
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
	public static void showMaklerMenu() {
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
	public static void newMakler() {
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
    public static void changeMakler() {
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
    public static void deleteMakler() {
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

    public static void showEstateMenu() {
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

    public static void newEstate() {
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
    public static void changeEstate() {
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
    public static void deleteEstate() {
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

    public static void showContractMenu() {
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
}
