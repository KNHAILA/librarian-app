package com.sorbonne.library.utils;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BuildBooksDatabase {
    private static String absolutePathForDownload = Paths.get("").toAbsolutePath()+
            "/src/main/java/com/sorbonne/library/books/";
    private static String gutendexLink = "http://gutendex.com/books/";
    private static String gutenbergLink = "http://www.gutenberg.org/files/";


    public static void main(String[] args) throws IOException, JSONException {
        BuildBooksDatabase b =new BuildBooksDatabase();
        System.out.println(b.buildDatabase(2).size());
    }

    public ArrayList<Integer> buildDatabase(int nbBooks) throws IOException, JSONException {
        ArrayList<Integer> booksIds= new ArrayList<Integer>();
        int i = 1;
        while (booksIds.size()<nbBooks){
            booksIds.addAll(auxBuildBooksDatabase(i, nbBooks));
            i++;
        }
        return booksIds;
    }

    public String getHtmlPageWithPageId(String website, int page){
        log.info("GetHtml ("+website+"?page=)"+page+ " is starting");
        try{
            HttpResponse<JsonNode> response= Unirest.get(website)
                    .header("Accept", "application/json")
                    .queryString("page", page)
                    .asJson();

            if(response.isSuccess() && !response.getBody().getObject().isEmpty())
                return response.getBody().getObject().toString();
        }catch (UnirestException e){
            log.error("Error in getHtml ("+website+"?page="+page+"):"+e.getMessage());}
        return null;
    }

    public String getHtmlPage(String website){
        log.info("Get Book Content ("+website+")"+" is starting");
        try{
            String response= Unirest.get(website)
                    .asString()
                    .getBody();
            return response;
        }catch (UnirestException e){
            log.error("Error in getHtml ("+website+"?page=):"+e.getMessage());}
        return null;
    }



    private ArrayList<Integer> auxBuildBooksDatabase(int page,int nbbooks) throws IOException, JSONException {
        ArrayList<Integer> booksIds= new ArrayList<Integer>();
        String content = getHtmlPageWithPageId(gutendexLink,page);
        if(content != null) {
            JSONObject contentJson = new JSONObject(content);
            JSONArray jsonArray = contentJson.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                AtomicInteger id = new AtomicInteger(jsonArray.getJSONObject(i).getInt("id"));
                if (booksIds.size() >= nbbooks)
                    return booksIds;
                String url = gutenbergLink+id+"/"+id+"-0.txt";
                String contentBook = getHtmlPage(url);
                if (countWordsInBook(contentBook)) {
                    booksIds.add(id.intValue());
                    downloadBook( contentBook, id.intValue());
                }
            }
        }
        return booksIds;
    }

    private boolean countWordsInBook(String content){
        int length = 0;
        log.info("content"+content);
        if (content != null) {
            String[] words = content.split("\\s+");
            length = words.length;
        }
        return (length>=10000);
    }

    public static void downloadBook(String str,int id) throws IOException {
        String dir= absolutePathForDownload+id+".txt";
        FileUtils.writeStringToFile(new File(dir), str);
    }
}

