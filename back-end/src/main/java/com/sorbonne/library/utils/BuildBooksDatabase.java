package com.sorbonne.library.utils;

import com.google.gson.Gson;
import com.sorbonne.library.model.Book;
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
import static com.sorbonne.library.config.Constants.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BuildBooksDatabase {

    public static void main(String[] args) throws IOException, JSONException {
        BuildBooksDatabase b =new BuildBooksDatabase();
        System.out.println(b.buildDatabase(10).size());
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
        List<Book> books = new ArrayList<>();
        String content = getHtmlPageWithPageId(GUTENDEX_LINK,page);
        if(content != null) {
            JSONObject contentJson = new JSONObject(content);
            JSONArray jsonArray = contentJson.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                int id = jsonArray.getJSONObject(i).getInt("id");
                String title = jsonArray.getJSONObject(i).getString("title");
                String authors = jsonArray.getJSONObject(i).getJSONArray("authors").getJSONObject(0).getString("name");
                System.out.println(id+"::::"+title+":::"+authors);
                if (booksIds.size() >= nbbooks) {
                    saveBooksInfo(books);
                    System.out.println(books.toString());
                    return booksIds;
                }
                String url = GUTENBERG_LINK+id+"/"+id+"-0.txt";
                System.out.println(url);
                String contentBook = getHtmlPage(url);
                if (countWordsInBook(contentBook)) {
                    books.add(new Book(id,title,authors, "https://www.gutenberg.org/cache/epub/"+id+"/pg"+id+".cover.medium.jpg"));
                    booksIds.add(id);
                    downloadBook( contentBook, id);
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
        String dir= ABSOLUTE_PATH+BOOKS+id+TXT_EXTENSION;
        FileUtils.writeStringToFile(new File(dir), str);
    }

    public static void saveBooksInfo(List<Book> books)
    {
        try {
            FileWriter file = new FileWriter(ABSOLUTE_PATH+"config/info.json");
            file.write(new Gson().toJson(books));
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

