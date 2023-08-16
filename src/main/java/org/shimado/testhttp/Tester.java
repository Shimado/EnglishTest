package org.shimado.testhttp;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.*;

public class Tester {

    final static String url = "https://britlex.ru/5000words1.php";
    private static int firstPage = 1;
    private static int pages = 1;
    private static String lang = "en";

    private static Map<String, String> MAP = new HashMap<>();

    public static void main(String[] args) {
        System.out.print("Введи число страниц: ");
        Scanner scannerPages = new Scanner(System.in);
        try{
            pages = scannerPages.nextInt();
        }catch (Exception e){
            scannerPages = new Scanner(System.in);
            return;
        }

        System.out.print("С какой страницы начать: ");
        Scanner scannerFirstPages = new Scanner(System.in);
        try{
            firstPage = scannerPages.nextInt();
        }catch (Exception e){
            scannerFirstPages = new Scanner(System.in);
            return;
        }

        System.out.print("Введи на каком языке будет показываться (en, ru): ");
        Scanner scannerLang = new Scanner(System.in);
        try{
            lang = scannerPages.next();
            if(!(lang.equals("ru") || lang.equals("en"))){
                lang = "en";
            }
        }catch (Exception e){
            scannerLang = new Scanner(System.in);
            return;
        }

        loadWords();
        showAllWords();
    }

    private static void loadWords(){
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        try {
            HtmlPage page = webClient.getPage(url);
            DomElement table = page.getElementById("wordlist").getFirstElementChild();
            table.getChildElements().forEach(el -> {
                List<String> elements = new ArrayList<>();
                for(DomElement a : el.getChildElements()){
                    elements.add(a.getVisibleText());
                }
                if(elements.size() == 4){
                    int key = Integer.parseInt(elements.get(3));
                    if(key <= pages && key >= firstPage){
                        String value = MAP.getOrDefault(elements.get(1), "");
                        if(value.length() > 0){
                            value = value + ", " + elements.get(2);
                        }else{
                            value = elements.get(2);
                        }
                        MAP.put(elements.get(1), value);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //SHUFFLE
        List<String> keys = new ArrayList<>(MAP.keySet());
        Collections.shuffle(keys);
        Map<String, String> shuffledMap = new LinkedHashMap<>();
        keys.forEach(k -> shuffledMap.put(k, MAP.get(k)));
        MAP = shuffledMap;
    }

    private static void showAllWords(){
        int count = 0;
        for(Map.Entry<String, String> a : MAP.entrySet()){
            count++;
            System.out.print((lang.equals("en") ? a.getKey() : a.getValue()) + " (" + count + "/" + MAP.entrySet().size() + ")");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            System.out.println(!lang.equals("en") ? a.getKey() : a.getValue());
            System.out.println("");
        }

        System.out.println("COMPLETED!");
        count = 0;
    }


}
