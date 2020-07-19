package br.com.premiumhosting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;

public class MySQL {
    private final String db_name, db_password, db_host, db_username;
    private Connection conn;
    public MySQL(String db_name, String db_password, String db_host, String db_username) throws SQLException {
        this.db_name = db_name;
        this.db_password = db_password;
        this.db_host = db_host;
        this.db_username = db_username;
        conn = openConn();
    }
    
    private Connection openConn() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:mysql://"+db_host+"/"+db_name, db_username, db_password);
        }
        return conn;
    }
    
    
    public void insertDomain(int domid, int svid, String dubdom, int port) throws SQLException {
        JSONObject json = new JSONObject();
        json.put("table", "subdomain_manager_subdomains");
        JSONObject datas = new JSONObject();
        datas.put("server_id", svid);
        datas.put("domain_id", domid);
        datas.put("subdomain", dubdom);
        datas.put("port", port);
        datas.put("record_type", "SRV");
        json.put("datas", datas);
        PreparedStatement ps = openConn().prepareStatement("INSERT INTO subdomain_manager_subdomains (`server_id`, `domain_id`, `subdomain`, `port`, `record_type`)"
                + "values (?, ?, ?, ?, 'SRV')");
        ps.setInt(1, svid);
        ps.setInt(2, domid);
        ps.setString(3, dubdom);
        ps.setInt(4, port);
        ps.execute();
    }
    
    public int getServerIdByPort(int port, String ip_alias) throws SQLException, NotFoundException {
        PreparedStatement ps = openConn().prepareStatement("SELECT server_id FROM allocations WHERE port=? and ip_alias=? LIMIT 1");
        ps.setInt(1, port);
        ps.setString(2, ip_alias);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("server_id");
        }
        throw new NotFoundException("Allocation not found");
    }
    public int getDomaindID(String domain) throws SQLException, NotFoundException {
        PreparedStatement ps = openConn().prepareStatement("SELECT id FROM subdomain_manager_domains WHERE domain=? LIMIT 1");
        ps.setString(1, domain);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
        throw new NotFoundException("Domain not found");
    }
}
