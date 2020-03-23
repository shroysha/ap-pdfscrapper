package dev.shroysha.ap.pdfscrapper.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PdfScrapper {

    private final URL link;

    public PdfScrapper(URL link) {
        this.link = link;
    }

    public String[] scrapePDFs() throws IOException {
        URLConnection connection = link.openConnection();
        InputStream is = connection.getInputStream();
        Scanner reader = new Scanner(is);

        String content = getContent(reader);
        String[] justBeforeLinks = justBeforeLinks(content);
        String[] hrefs = getHREF(justBeforeLinks);
        String[] links = getLink(hrefs);
        String[] pdfs = new String[links.length];
        for (int i = 0; i < pdfs.length; i++) {
            pdfs[i] = getPDF(links[i]);
        }

        return pdfs;
    }

    private String getContent(Scanner reader) {

        StringBuilder content = new StringBuilder();

        while (reader.hasNextLine()) {
            content.append(reader.nextLine());
        }

        return content.toString();
    }

    private String[] justBeforeLinks(String content) {
        String[] splits = content.split("<!-- TRANSIT - HYPERLINK --><");
        String[] justBeforeLinks = new String[splits.length - 1];

        for (int i = 0; i < justBeforeLinks.length; i++) {
            justBeforeLinks[i] = splits[i + 1].substring(5);
        }

        return justBeforeLinks;
    }

    private String[] getHREF(String[] justBeforeLinks) {

        String[] hrefs = new String[justBeforeLinks.length];
        //Remove first >
        for (int i = 0; i < justBeforeLinks.length; i++) {
            justBeforeLinks[i] = justBeforeLinks[i].substring(justBeforeLinks[i].indexOf(">") + 1);

            try {
                int start = justBeforeLinks[i].indexOf("<") + 1;
                int stop = justBeforeLinks[i].indexOf(">");
                //System.out.println(justBeforeLinks[i]);
                //System.out.println(start + " , " + stop);
                hrefs[i] = justBeforeLinks[i].substring(start, stop);
                //System.out.println(hrefs[i]);
            } catch (Exception e) {
                hrefs[i] = null;
            }
        }

        return hrefs;

    }

    private String[] getLink(String[] hrefs) {
        String[] links = new String[hrefs.length];
        for (int i = 0; i < links.length; i++) {
            if (hrefs[i] != null) {
                int firstQuote = hrefs[i].indexOf("\"");

                boolean done = false;

                if (firstQuote != -1)
                    for (int j = firstQuote + 1; j < hrefs[i].length() && !done; j++) {
                        if (hrefs[i].charAt(j) == '\"') {
                            links[i] = hrefs[i].substring(firstQuote + 1, j);
                            done = true;
                        }
                    }

                if (!done) {
                    System.err.println("Couldnt parse link");
                } else {
                    System.err.println("Link parsed successfully! : " + links[i]);
                }
            }
        }
        return links;
    }

    public String getPDF(String linky) {
        try {
            if (linky == null)
                throw new Exception("null");
            System.out.println(linky);
            URL url = new URL(linky.replaceAll(":80", ""));
            URLConnection connection = url.openConnection();
            Scanner reader = new Scanner(connection.getInputStream());
            String content = getContent(reader);

            return parsePDFLink(content);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PdfScrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ignored) {
        }

        return null;
    }

    private String parsePDFLink(String content) {
        String LOOKINGFOR = "<a href=";
        int firstQuote = content.indexOf(LOOKINGFOR) + LOOKINGFOR.length();
        StringBuilder build = new StringBuilder(content);

        boolean done = false;
        for (int i = firstQuote; i < content.length(); i++) {
            if (content.charAt(i) == '\"') {
                String linky = content.substring(firstQuote + 1, i);
                System.out.println(linky);
                return linky;
            }
        }

        return null;
    }


}
