package backupcc.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Um filtro para o metodo listFiles() da classe File. Retorna apenas arquivos
 * que sejam paginas do forum gravadas em disco.
 * 
 * @author "Pedro Reis"
 * @since 3 de setembro de 2022
 * @version 1.0
 */
public final class ForumPageFilter implements FilenameFilter {
    
    private static final String MAIN_PAGE = 
        backupcc.net.Util.FORUM_DOMAIN.replace(".", "\\.");
    
    private static final Pattern ACCEPT = 
        Pattern.compile(
            "^(("+ MAIN_PAGE + ")|((f|t)=\\d+(&start=(\\d+))?))\\.html"
        );
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Verifica se eh um arquivo cujo nome corresponde a uma pagina do forum
     * gravada no disco.
     * 
     * @param dir nao usado.
     * 
     * @param filename O nome do arquivo.
     * 
     * @return true se eh um arquivo cujo nome corresponde a uma pagina do forum
     * gravada no disco.
     */
    @Override
    public boolean accept(File dir, String filename) {
        
        Matcher matcher = ACCEPT.matcher(filename);
        
        if (matcher.find()) {
            
           if (matcher.group(6) == null) return true;
           
           int startDigits = Integer.valueOf(matcher.group(6));
            
           return ( 
                (startDigits != 0) && 
                ((startDigits % backupcc.pages.Page.MAX_ROWS_PER_PAGE) == 0) 
            );
            
        }
        
        return false;
        
    }//accept()
           
}//classe ForumPageFilter
