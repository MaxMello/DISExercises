package de.dis2011.data;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TenancyContract extends Contract {

    private Date startDate;
    private int duration;
    private int additionalCosts;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAdditionalCosts() {
        return additionalCosts;
    }

    public void setAdditionalCosts(int additionalCosts) {
        this.additionalCosts = additionalCosts;
    }

    public static List<TenancyContract> getAll() {
        try {
            // Hole Verbindung
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM tenancycontract INNER JOIN contract on tenancycontract.contract = contract.no";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            List<TenancyContract> contracts = new ArrayList<TenancyContract>();
            while (rs.next()) {
                TenancyContract contract = new TenancyContract();
                contract.setNo(rs.getInt("no"));
                contract.setAdditionalCosts(rs.getInt("addcosts"));
                contract.setDuration(rs.getInt("duration"));
                contract.setStartDate(rs.getDate("startdate"));
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
        return new ArrayList<TenancyContract>();
    }

    public void save() {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        try {
            // FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
            if (getNo() == -1) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                String insertSQL = "INSERT INTO contract(cdate, place) VALUES (?, ?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setDate(1, getcDate());
                pstmt.setString(2, getPlace());
                pstmt.executeUpdate();

                // Hole die Id des engefügten Datensatzes
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setNo(rs.getInt(1));
                }
                System.out.println("TEST::: Generated contract");

                insertSQL = "INSERT INTO tenancycontract(contract, startdate, duration, addcosts) VALUES (?, ?, ?, ?)";

                pstmt = con.prepareStatement(insertSQL);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setInt(1, getNo());
                pstmt.setDate(2, getStartDate());
                pstmt.setInt(3, getDuration());
                pstmt.setInt(4, getAdditionalCosts());
                pstmt.executeUpdate();

                rs.close();
                pstmt.close();
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "UPDATE contract SET cdate = ?, place = ? WHERE no = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setDate(1, getcDate());
                pstmt.setString(2, getPlace());
                pstmt.setInt(3, getNo());
                pstmt.executeUpdate();

                updateSQL = "UPDATE tenancycontract SET startdate = ?, duration = ?, addcosts = ? WHERE contract = ?";
                pstmt = con.prepareStatement(updateSQL);
                pstmt.setDate(1, getStartDate());
                pstmt.setInt(2, getDuration());
                pstmt.setInt(3, getAdditionalCosts());
                pstmt.setInt(4, getNo());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TenancyContract load(int id) {
        try {
            // Hole Verbindung
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM tenancycontract INNER JOIN contract on tenancycontract.contract = contract.no WHERE contract.no = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                TenancyContract contract = new TenancyContract();
                contract.setNo(id);
                contract.setAdditionalCosts(rs.getInt("addcosts"));
                contract.setDuration(rs.getInt("duration"));
                contract.setStartDate(rs.getDate("startdate"));
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
        return new StringBuilder("Mietvertrag Nr. ")
                .append(no)
                .append(", Unterschriftdatum: ")
                .append(cDate)
                .append(", Unterschriftort: ")
                .append(place)
                .append(", Startdatum: ")
                .append(df.format(getStartDate()))
                .append(", Dauer: ")
                .append(duration)
                .append(" Tage")
                .append(", Zusätzliche Kosten: ")
                .append(additionalCosts)
                .toString();
    }
}
