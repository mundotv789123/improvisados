package br.com.premiumhosting.main;

import br.com.premiumhosting.CloudFlareAPI;
import br.com.premiumhosting.MySQL;
import br.com.premiumhosting.NotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws SQLException {
        //abra o arquivo CONFIG.java para colocar todas as informações nela
        CloudFlareAPI cf_api = new CloudFlareAPI(CONFIG.CF_KEY, CONFIG.CF_EMAIL);
        MySQL mysql = new MySQL(CONFIG.SQL_DATABASE, CONFIG.SQL_PASSWORD, CONFIG.SQL_IP, CONFIG.SQL_USERNAME);
        for (String zone_id : CONFIG.ZONES_IDS) {
            int page = 1, totp = 0;
            do {
                try {
                    JSONObject json = cf_api.getDNSs(zone_id, page);
                    JSONObject jinfo = json.getJSONObject("result_info");
                    totp = jinfo.getInt("total_pages");
                    JSONArray result = (JSONArray) json.get("result");
                    for (Object o : result) {
                        if (o instanceof JSONObject) {
                            JSONObject json_result = (JSONObject) o;
                            if (json_result.getString("type").equals("SRV")) { //verificando se o tipo é SRV
                                JSONObject json_data = json_result.getJSONObject("data");
                                String targe = json_data.getString("target");
                                if (CONFIG.CNAMES_TARGETS.contains(targe)) { //verificando cnames
                                    try { //caso der erro ele considera que ou servidor não existe ou que o allocation com ip_alias n existe
                                        int svid = mysql.getServerIdByPort(json_data.getInt("port"), targe);
                                        int domain_id = mysql.getDomaindID(json_result.getString("zone_name"));
                                        if (svid != 0) {
                                            String domain = json_data.getString("name").replace("." + json_result.getString("zone_name"), "");
                                            mysql.insertDomain(domain_id, svid, domain, json_data.getInt("port"));
                                        } else {
                                            //cf_api.delDns(zone_id, json_result.getString("id")); //deletar subdominio automatico
                                            System.out.println(json_data.getString("name") + " não tem servidor correspondido");
                                        }
                                    } catch (NotFoundException ex) {
                                        //cf_api.delDns(zone_id, json_result.getString("id")); //deletar subdominio automatico
                                        System.out.println(json_data.getString("name") + " " + ex.getMessage());
                                    }
                                }
                            } else {
                                //cf_api.delDns(zone_id, json_result.getString("id")); //deletar subdominio automatico
                                System.out.println(json_result.getString("name") + " não esta na lista dos CNAMES_TARGETS!");
                            }
                        }
                    }
                    page++;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } while (page < totp);
        }
    }
}
