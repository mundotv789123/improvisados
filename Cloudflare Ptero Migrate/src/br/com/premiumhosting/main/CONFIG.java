package br.com.premiumhosting.main;

import java.util.ArrayList;
import java.util.List;

public class CONFIG {
    //banco de dados
    public static String SQL_IP = ""; //ip de acesso do banco de dados
    public static String SQL_USERNAME = ""; //nome de usuario do banco de dados
    public static String SQL_PASSWORD = ""; //senha do banco de dados
    public static String SQL_DATABASE = ""; //nome do banco de dados do painel
    
    //cloudflare api
    public static String CF_EMAIL = ""; //email do cloudflare
    public static String CF_KEY = ""; //api key do cloudflare
    
    public static String[] ZONES_IDS = {""}; //Aqui você deve colocar o id de todos os dominios usados no cloudflare
    public static List<String> CNAMES_TARGETS = new ArrayList<>();
    //aqui você deve colocar todos os cnames usados nos subdominios
    public static void load() {
        //verifique antes se os allocations estão com os ip_alias definidos
        //CNAMES_TARGETS.add("node1.minha_hosting.com.br");
        //CNAMES_TARGETS.add("node2.minha_hosting.com.br");
        //....
    }
}
