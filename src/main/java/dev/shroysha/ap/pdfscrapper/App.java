package dev.shroysha.ap.pdfscrapper;

import dev.shroysha.ap.pdfscrapper.model.PdfScrapper;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {


    public static void main(String[] args) {
        try {
            final URL LINK = new URL("http://portal.acs.org/portal/acs/corg/content?_nfpb=true&_pageLabel=PP_SUPERARTICLE&node_id=1508&use_sec=false&sec_url_var=region1&__uuid=ee5ea882-84ba-4b62-86a2-66e5e85b1136");

            PdfScrapper scrapper = new PdfScrapper(LINK);
            String[] links = scrapper.scrapePDFs();


            URL[] pdfLinks = new URL[links.length];
            for (int i = 0; i < pdfLinks.length; i++) {
                if (links[i] != null) {
                    System.out.print(i + ": ");
                    pdfLinks[i] = new URL(scrapper.getPDF(links[i]));
                    System.out.println(pdfLinks[i]);
                } else {
                    System.out.println("Null Link");
                }
            }

        }
        /*
         * http://portal.acs.org/portal/acs/corg/content?_nfpb=true&_pageLabel=PP_SUPERARTICLE&node_id=1508&use_sec=false&sec_url_var=region1&__uuid=ee5ea882-84ba-4b62-86a2-66e5e85b1136
         * http://portal.acs.org/portal/acs/corg/content?_nfpb=true&_pageLabel=PP_SUPERARTICLE&node_id=1508&use_sec=false&sec_url_var=region1&__uuid=ee5ea882-84ba-4b62-86a2-66e5e85b1136
         */ catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
