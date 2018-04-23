package de.dis2011.data;

import java.sql.*;

public class Rent {

    private int tenancyContractNo;
    private int apartmentID;
    private int personID;

    public int getTenancyContractNo() {
        return tenancyContractNo;
    }

    public void setTenancyContractNo(int tenancyContractNo) {
        this.tenancyContractNo = tenancyContractNo;
    }

    public int getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(int apartmentID) {
        this.apartmentID = apartmentID;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public void save() {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // Achtung, hier wird noch ein Parameter mitgegeben,
            // damit spC$ter generierte IDs zurC<ckgeliefert werden!
            String insertSQL = "INSERT INTO rents(tenancycontract, apartment, person) VALUES (?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(insertSQL);

            // Setze Anfrageparameter und fC<hre Anfrage aus
            pstmt.setInt(1, getTenancyContractNo());
            pstmt.setInt(2, getApartmentID());
            pstmt.setInt(3, getPersonID());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
