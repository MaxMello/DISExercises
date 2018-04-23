package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Sell {

    private int purchaseContractNo;
    private int houseID;
    private int personID;

    public int getPurchaseContractNo() {
        return purchaseContractNo;
    }

    public void setPurchaseContractNo(int purchaseContractNo) {
        this.purchaseContractNo = purchaseContractNo;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
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
            String insertSQL = "INSERT INTO sells(contract, house, person) VALUES (?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(insertSQL);

            // Setze Anfrageparameter und fC<hre Anfrage aus
            pstmt.setInt(1, getPurchaseContractNo());
            pstmt.setInt(2, getHouseID());
            pstmt.setInt(3, getPersonID());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
