package de.dis2011.data;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PurchaseContract extends Contract {


    private int numberOfInstallments;
    private int rate;


    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


    public static List<PurchaseContract> getAll() {
        try {
            // Hole Verbindung
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM purchasecontract INNER JOIN contract on purchasecontract.contract = contract.no";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            List<PurchaseContract> contracts = new ArrayList<PurchaseContract>();
            while (rs.next()) {
                PurchaseContract contract = new PurchaseContract();
                contract.setNo(rs.getInt("no"));
                contract.setNumberOfInstallments(rs.getInt("nrinstallments"));
                contract.setRate(rs.getInt("rate"));
                contract.setcDate(rs.getDate("cdate"));
                contract.setPlace(rs.getString("place"));
                contracts.add(contract);
            }
            rs.close();
            pstmt.close();
            return contracts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<PurchaseContract>();
    }

    public void save() {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
            if (getNo() == -1) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                String insertSQL = "INSERT INTO contract(CDATE, PLACE) VALUES (?, ?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setDate(1, getcDate());
                pstmt.setString(2, getPlace());
                pstmt.executeUpdate();

                // Hole die Id des engefC<gten Datensatzes
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setNo(rs.getInt(1));
                }

                insertSQL = "INSERT INTO purchasecontract(contract, nrinstallments, rate) VALUES (?, ?, ?)";

                pstmt = con.prepareStatement(insertSQL);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setInt(1, getNo());
                pstmt.setInt(2, getNumberOfInstallments());
                pstmt.setInt(3, getRate());
                pstmt.executeUpdate();

                rs.close();
                pstmt.close();
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "UPDATE contract SET cDate = ?, place = ? WHERE no = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setDate(1, getcDate());
                pstmt.setString(2, getPlace());
                pstmt.setInt(3, getNo());
                pstmt.executeUpdate();

                updateSQL = "UPDATE purchasecontract SET nrinstallments = ?, rate = ? WHERE contract = ?";
                pstmt = con.prepareStatement(updateSQL);
                pstmt.setInt(1, getNumberOfInstallments());
                pstmt.setInt(2, getRate());
                pstmt.setInt(3, getNo());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PurchaseContract load(int id) {
        try {
            // Hole Verbindung
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM purchasecontract INNER JOIN contract on purchasecontract.contract = contract.no WHERE contract.no = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PurchaseContract contract = new PurchaseContract();
                contract.setNo(id);
                contract.setNumberOfInstallments(rs.getInt("nrinstallments"));
                contract.setRate(rs.getInt("rate"));
                contract.setcDate(rs.getDate("cdate"));
                contract.setPlace(rs.getString("place"));
                return contract;
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
        DateFormat df = new SimpleDateFormat("dd.MM.YYYY");
        return new StringBuilder("Kaufvertrag Nr. ")
                .append(no)
                .append(", Unterschriftdatum: ")
                .append(df.format(cDate))
                .append(", Unterschriftort: ")
                .append(place)
                .append(", Raten: ")
                .append(numberOfInstallments)
                .append(", Preis pro Rate: ")
                .append(rate)
                .toString();
    }

}
