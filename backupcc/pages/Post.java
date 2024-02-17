package backupcc.pages;

import java.util.regex.Pattern;

/**
 *
 * @author "Pedro Reis"
 * @since 1.0 (18 de setembro de 2022)
 * @version 1.0
 */
public final class Post extends Page {
    
    /**
     * Regexp para localizar os dados de um Topic na pagina de Section a qual
     * este topic pertence.
     */
    private static final Pattern FINDER_REGEXP = 
        Pattern.compile("<div id=\"post_content(\\d+)\">"); 
         
    public static Pattern getFinder() {
        
        return FINDER_REGEXP;
        
    }
    
}//classe Post
