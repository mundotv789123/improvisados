package br.com.premiumhosting;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

public class CloudFlareAPI {

    private String api_key;
    private String email;

    public CloudFlareAPI(String api_key, String email) {
        this.api_key = api_key;
        this.email = email;
    }

    public boolean setTargetSRV(String zid, String dns_id, String target) throws IOException {
        String str = sendRequest("GET", "/client/v4/zones/" + zid + "/dns_records/" + dns_id, null);
        JSONObject json = new JSONObject(str);
        if (json.getBoolean("success")) {
            JSONObject jsonr = json.getJSONObject("result");
            jsonr.getJSONObject("data").put("target", target);
            JSONObject rjson = new JSONObject(sendRequest("PUT", "/client/v4/zones/" + zid + "/dns_records/" + dns_id, jsonr.toString()));
            if (rjson.getBoolean("success")) {
                return true;
            } else {
                System.out.println(rjson.toString());
                return false;
            }
        }
        return false;
    }

    public boolean delDns(String zid, String dns_id) {
        try {
            sendRequest("DELETE", "/client/v4/zones/"+zid+"/dns_records/"+dns_id, null);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public JSONObject getDNSs(String zid, int page) throws MalformedURLException, IOException {
        return new JSONObject(sendRequest("GET", "/client/v4/zones/" + zid + "/dns_records?page=" + page + "&per_page=25", null));
    }

    private String sendRequest(String meth, String url_path, String datas) throws MalformedURLException, IOException {
        URL url = new URL("https://api.cloudflare.com" + url_path);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod(meth);
        huc.setRequestProperty("X-Auth-Email", email);
        huc.setRequestProperty("X-Auth-Key", api_key);
        if (datas != null) {
            huc.setDoOutput(true);
            huc.getOutputStream().write(datas.getBytes());
        }
        huc.connect();
        InputStream in = huc.getInputStream();
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            sb.append((char) c);
        }
        return sb.toString();
    }
}
