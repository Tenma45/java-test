package com.example;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchData {

    private static int validateInput(String input) throws Exception {
        int id;
        try {
            id = Integer.parseInt(input);
        }
        catch (Exception e) {throw e;};
        return id;
    }

    private static Photo fetch(String sURL) throws IOException {
        URL url = new URL(sURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        Gson g = new Gson();
        Photo result= g.fromJson(String.valueOf(content),Photo.class);
        in.close();
        return result;
    }

    public static void main(String[] args) throws Exception {
        final int THREAD = 1; // todo
        final String CSV_SEPARATOR = ",";
        int id = validateInput(args[0]);

        if(args.length != 1){throw new Error();}

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data.csv"), "UTF-8"));
        StringBuffer header = new StringBuffer();
        header.append("EmployeeId, ThreadID, AlbumID, Id, Title, Url, ThumbnailUrl");
        bw.write(header.toString());
        bw.newLine();

        for(int i=1;i<=250;i++){
            Photo photo = fetch("https://jsonplaceholder.typicode.com/photos/"+i);
            System.out.println("Fetched: "+photo.getId());
            try
            {
                    StringBuffer oneLine = new StringBuffer();
                    oneLine.append(id);
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(THREAD);
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(photo.getAlbumId());
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(photo.getId());
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(photo.getTitle());
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(photo.getUrl());
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(photo.getThumbnailUrl());
                    bw.write(oneLine.toString());
                    bw.newLine();
            }
            catch (UnsupportedEncodingException e) {}
            catch (FileNotFoundException e){}
            catch (IOException e){}
        }
        bw.flush();
        bw.close();
    }

}
