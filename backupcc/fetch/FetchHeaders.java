package backupcc.fetch;

import backupcc.pages.Header;
import backupcc.pages.Section;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe encarregada de localizar (na pagina principal) o endereco das paginas
 * dos headers e disponibilizar estes enderecos atraves de um objeto TreeSet.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (23 de agosto de 2022)
 * @version 1.1
 */
final class FetchHeaders {
    
    private final backupcc.pages.Main main;
    
    private static final int COLOR = backupcc.tui.Tui.CYAN;;
    
    private static final Pattern SECTION_FINDER = 
        backupcc.pages.Section.getFinder();
    
    private TreeSet<Header> headers;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param main Objeto com os dados da pagina principal do forum.
     */
    public FetchHeaders(final backupcc.pages.Main main) {
        
        this.main = main;
          
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Obtem da pagina  principal a lista de todos os headers do forum e baixa
     * as paginas de HEADER.
     * 
     */
    private void downloadHeadersPages() {
        
        FetchMain mainPage = new FetchMain(main);
       
        headers = mainPage.getHeaders();
               
        for (Header header: headers) {   
            
            backupcc.net.Util.downloadPageToPathname(
                header.getAbsoluteURL(), 
                backupcc.file.Util.RAW_PAGES + '/' + header.getFilename(),
                header.getName(),
                COLOR
            );
        }
        
    }//downloadHeadersPages()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Obtem os dados das paginas de secao a partir da lista de todas as paginas
     * de headers.
     * 
     * @return Um TreeSet com objetos Section com os dados de todas as paginas
     * de secao do forum.
     * 
     */
    public TreeSet<Section> getSections() {
        
        downloadHeadersPages();
        
        TreeSet<Section> sections = new TreeSet<>();
        
        String headerPage = null;
        
        for (Header header: headers) {
        
            try {
                headerPage = 
                    backupcc.file.Util.readTextFile(
                        backupcc.file.Util.RAW_PAGES + '/' +
                        header.getFilename()
                    );
            }
            catch (IOException e) {
                
                String[] msgs = {e.getMessage() + '\n'};
               
                backupcc.tui.OptionBox.abortBox(msgs);
                
            }//try-catch

            Matcher matcher = SECTION_FINDER.matcher(headerPage);
            
            backupcc.tui.Tui.decoratedPrintlnc(
                "Coletando dados de ", 
                "se\u00e7\u00f5es",
                " em " + header.getName() + " ...", 
                COLOR
            );
  
            while (matcher.find()) {
                
                Section section = new Section(matcher.group());
                sections.add(section);

            }//while
        
        }
               
        return sections;
        
    }//getSections()
    
    
}//classe FetchHeaders
