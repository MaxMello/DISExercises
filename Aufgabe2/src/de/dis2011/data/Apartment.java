package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import de.dis2011.data.DB2ConnectionManager;


public class Apartment extends Estate {

	private int floor;
	private int rent;
	private int rooms;
	private boolean balcony;
	private boolean kitchen;

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

    public boolean hasBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    public boolean hasKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public static List<Apartment> getAll() {
        try {
            // Hole Verbindung
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM apartment INNER JOIN estate on apartment.estate = estate.id";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            List<Apartment> apartments = new ArrayList<Apartment>();
            while (rs.next()) {
                Apartment a = new Apartment();
                a.setId(rs.getInt("id"));
                a.setAddress(rs.getString("address"));
                a.setArea(rs.getInt("area"));
                a.setFloor(rs.getInt("floor"));
                a.setBalcony(rs.getBoolean("balcony"));
                a.setKitchen(rs.getBoolean("kitchen"));
                a.setRent(rs.getInt("rent"));
                a.setRooms(rs.getInt("rooms"));
                apartments.add(a);
            }
            rs.close();
            pstmt.close();
            return apartments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Apartment>();
    }

    public void save() {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
            if (getId() == -1) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                String insertSQL = "INSERT INTO estate(address, area) VALUES (?, ?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setString(1, getAddress());
                pstmt.setInt(2, getArea());
                pstmt.executeUpdate();

                // Hole die Id des engefC<gten Datensatzes
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt(1));
                }

                insertSQL = "INSERT INTO apartment(estate, floor, rent, rooms, balcony, kitchen) VALUES (?, ?, ?, ?, ?, ?)";

                pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setInt(1, getId());
                pstmt.setInt(2, getFloor());
                pstmt.setInt(3, getRent());
                pstmt.setInt(4, getRooms());
                pstmt.setBoolean(5, hasBalcony());
                pstmt.setBoolean(6, hasKitchen());
                pstmt.executeUpdate();

                rs.close();
                pstmt.close();
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "UPDATE estate SET address = ?, area = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setString(1, getAddress());
                pstmt.setInt(2, getArea());
                pstmt.setInt(3, getId());
                pstmt.executeUpdate();

                updateSQL = "UPDATE apartment SET floor = ?, rent = ?, rooms = ?, balcony = ?, kitchen = ? WHERE estate = ?";
                pstmt = con.prepareStatement(updateSQL);
                pstmt.setInt(1, getFloor());
                pstmt.setInt(2, getRent());
                pstmt.setInt(3, getRooms());
                pstmt.setBoolean(4, hasBalcony());
                pstmt.setBoolean(5, hasKitchen());
                pstmt.setInt(6, getId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Apartment load(int id) {
        try {
            // Hole Verbindung
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM apartment INNER JOIN estate on apartment.estate = estate.id WHERE estate.id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Apartment a = new Apartment();
                a.setId(rs.getInt("id"));
                a.setAddress(rs.getString("address"));
                a.setArea(rs.getInt("area"));
                a.setFloor(rs.getInt("floor"));
                a.setBalcony(rs.getBoolean("balcony"));
                a.setKitchen(rs.getBoolean("kitchen"));
                a.setRent(rs.getInt("rent"));
                a.setRooms(rs.getInt("rooms"));
                return a;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Apartment Nr. " + getId() + ", Adresse: " + getAddress();
    }
}
