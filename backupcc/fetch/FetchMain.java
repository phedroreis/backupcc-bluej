package backupcc.fetch;

import static backupcc.file.Util.readTextFile;
import backupcc.pages.Header;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe encarregada de baixar a página principal do forum, localizar (nesta
 * pagina) o endereco das paginas dos headers e disponibilizar estes 
 * enderecos atraves de um objeto TreeSet.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (23 de agosto de 2022)
 * @version 1.0
 */
final class FetchMain {
    
    private final backupcc.pages.Main main;
    
    private static final int COLOR = backupcc.tui.Tui.MAGENTA;;
    
    private static final Pattern HEADER_FINDER = 
        backupcc.pages.Header.getFinder();
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param main Um objeto Main com os dados da pagina principal do forum.
     */
    public FetchMain(final backupcc.pages.Main main) {
        
        this.main = main;
               
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/    
    /**
     * Baixa a pagina principal do forum para o diretorio onde todas as paginas
     * brutas (arquivos HTML nao editados pelo programa) serao gravadas.
     * 
     */
    private void downloadMainPage() {
              
        backupcc.net.Util.downloadUrlToPathname(
            main.getAbsoluteURL(),
            backupcc.file.Util.RAW_PAGES + '/' + main.getFilename(),
            main.getName(),
            COLOR
        );
          
    }//downloadMainPage()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Este metodo coleta dados (na pagina principal do forum) sobre as paginas
     * de HEADER do forum. Para cada HEADER apontado na pagina principal, este 
     * metodo ira criar um objeto backupcc.pages.Header com os dados deste 
     * header. No final, um TreeSet contendo todos estes objetos serah retornado
     * pelo metodo.
     * 
     * Uma pagina de HEADER eh aquela que aponta para paginas de secao do forum.
     *  
     * @return Um TreeSet (ordenado por ID do Header) com todos os Headers
     * apontados na pagina principal.
     * 
     */
    public TreeSet<Header> getHeaders() {
        
        downloadMainPage();
        
        String mainPage = null;
        try {
            
            mainPage = readTextFile(
                backupcc.file.Util.RAW_PAGES + '/' + main.getFilename()
            );
            
        } 
        catch (IOException e) {
            
            String[] msgs = {e.getMessage() + '\n'};
               
            backupcc.tui.OptionBox.abortBox(msgs);
            
        }//try-catch
        
        TreeSet <Header> headers; headers = new TreeSet<>();
        
        Matcher matcher = HEADER_FINDER.matcher(mainPage);
        
        backupcc.tui.Tui.decoratedPrintlnc(
            "Coletando dados de ", 
            "cabe\u00E7alhos",
            " na " + main.getName() + " ...", 
            COLOR
        );
        
        while (matcher.find()) {
            
            Header header = new Header(matcher.group());
            headers.add(header);
            
        }//while
                
        return headers;
        
    }//getHeaders()    
   
}//classe FetchMain
