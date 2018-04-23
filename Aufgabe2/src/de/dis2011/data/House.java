package de.dis2011.data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class House extends Estate {

    private int floors;
	private int price;
	private boolean garden;

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

    public boolean hasGarden() {
        return garden;
    }

    public void setGarden(boolean garden) {
        this.garden = garden;
    }

    public static List<House> getAll() {
        try {
            // Hole Verbindung
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM house INNER JOIN estate on house.estate = estate.id";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            List<House> houses = new ArrayList<House>();
            while (rs.next()) {
                House a = new House();
                a.setId(rs.getInt("id"));
                a.setAddress(rs.getString("address"));
                a.setArea(rs.getInt("area"));
                a.setFloors(rs.getInt("floors"));
                a.setGarden(rs.getBoolean("garden"));
                a.setPrice(rs.getInt("price"));
                houses.add(a);
            }
            rs.close();
            pstmt.close();
            return houses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<House>();
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

                insertSQL = "INSERT INTO house(estate, floors, price, garden) VALUES (?, ?, ?, ?)";

                pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setInt(1, getId());
                pstmt.setInt(2, getFloors());
                pstmt.setInt(3, getPrice());
                pstmt.setBoolean(4, hasGarden());
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

                updateSQL = "UPDATE house SET floors = ?, price = ?, garden = ? WHERE estate = ?";
                pstmt = con.prepareStatement(updateSQL);
                pstmt.setInt(1, getFloors());
                pstmt.setInt(2, getPrice());
                pstmt.setBoolean(3, hasGarden());
                pstmt.setInt(4, getId());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static House load(int id) {
        try {
            // Hole Verbindung
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM house INNER JOIN estate on house.estate = estate.id WHERE estate.id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                House a = new House();
                a.setId(rs.getInt("id"));
                a.setAddress(rs.getString("address"));
                a.setArea(rs.getInt("area"));
                a.setFloors(rs.getInt("floors"));
                a.setPrice(rs.getInt("price"));
                a.setGarden(rs.getBoolean("garden"));
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
        return "Haus Nr. " + getId() + ", Adresse: " + getAddress();
    }
}
