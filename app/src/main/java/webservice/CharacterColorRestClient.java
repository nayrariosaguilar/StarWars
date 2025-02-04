package webservice;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CharacterColorRestClient {

    //URL base de la API de Marvel. Totes les peticions parteixen d'aquí.
    final private String baseUrl = "https://bibils.net/dam2/listcolor.php?num=20";
    public ArrayList<CharacterColor> listCharacters() throws IOException {

        ArrayList<CharacterColor> characters = new ArrayList<>();
        //Generem l'objecte URL que fa servir HttpURLConnection
        URL url = new URL(baseUrl);
        //L'objecte HttpUrlConnection ens permet manipular una connexió HTTP.
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //GET, POST, PUT, DELETE, OPTIONS, HEAD
        con.setRequestMethod("GET");
        con.connect();
        int responseCode = con.getResponseCode();
        System.out.println("Response status: " + con.getResponseCode() + " " + con.getResponseMessage());
        System.out.println("Response Body : ");
        if(responseCode != 200){
            return new ArrayList<>();
        }
        String response = getResponseBody(con);
      //  JsonArray jsonObject = JsonParser.parseString(response).getAsJsonArray();
        try{
            JsonArray jsonObject = JsonParser.parseString(response).getAsJsonArray();
            for (JsonElement element : jsonObject) {
                CharacterColor character = new Gson().fromJson(element, CharacterColor.class);
                characters.add(character);
            }
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        //Data d = new Gson().fromJson(jsonObject.getAsJsonArray(), Data.class);
        return characters;
    }
    private String getResponseBody(HttpURLConnection con) throws IOException {
        BufferedReader br;

        if (con.getResponseCode() >= 400) {
            //Si el codi de resposta és superior a 400 s'ha produit un error i cal llegir
            //el missatge d'ErrorStream().
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        } else {
            //Si el codi és inferior a 400 llavors obtenim una resposta correcte del servidor.
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }
}
