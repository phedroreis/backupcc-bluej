package backupcc.fetch;

import static backupcc.file.Util.readTextFile;
import backupcc.incremental.Incremental;
import backupcc.pages.Section;
import backupcc.pages.Topic;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe encarregada de localizar (nas paginas dos headers) o endereco das 
 * paginas das secoes e disponibilizar estes enderecos atraves de um objeto 
 * TreeSet.
 *
 * @author "Pedro Reis"
 * @since 1.0 (25 de agosto de 2022)
 * @version 1.1
 */
final class FetchSections {
    /*
    Objeto com os dados da pagina principal do forum.
    */  
    private final backupcc.pages.Main main;
    
    private static final int COLOR = backupcc.tui.Tui.BLUE;
    
    private static final Pattern TOPIC_FINDER = 
        backupcc.pages.Topic.getFinder();
      
    private TreeSet<Section> sections;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param main Objeto que deve conter os dados da pagina principal do forum.
     */
    public FetchSections(final backupcc.pages.Main main) {
        
        this.main = main;
          
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /*
     * Baixa as páginas de seçâo.
     * 
     */
    private void downloadSectionsPages() {
        
        FetchHeaders headersPages = new FetchHeaders(main);
        
        sections = headersPages.getSections();
        
        for (Section section: sections) {
               
            for (int i = 0; i < section.getNumberOfPages(); i++) {
         
                backupcc.net.Util.downloadUrlToPathname(
                    section.getAbsoluteURL(i), 
                    backupcc.file.Util.RAW_PAGES + '/' + section.getFilename(i),
                    section.getName() + " [" + (i + 1) + "]",
                    COLOR
                );
                
            }//for i
            
        }//for section
        
    }//downloadSectionsPages()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Processa os arquivos com as páginas de seção para obter os dados que
     * permitam baixar todas as páginas de tópicos.
     * 
     * @return Um TreeSet com registros para todos os tópicos do fórum.
     */
    public TreeSet<Topic> getTopics() {
        
        downloadSectionsPages();
        
        TreeSet<Topic> topics = new TreeSet<>();
                  
        for (Section section: sections) {
            
            String sectionPage = null;
    
            for (int i = 0; i < section.getNumberOfPages(); i++) {

                try {
                    
                    sectionPage = readTextFile(
                        backupcc.file.Util.RAW_PAGES + '/' +
                        section.getFilename(i)
                    );
                }
                catch (IOException e) {
                    
                    String[] msgs = {e.getMessage() + '\n'};
               
                    backupcc.tui.OptionBox.abortBox(msgs);
                    
                }//try-catch

                Matcher matcher = TOPIC_FINDER.matcher(sectionPage);
                
                backupcc.tui.Tui.decoratedPrintlnc(
                    "Coletando dados de ", 
                    "t\u00F3picos",
                    " em " + section.getName() + "  [" + (i+1) + "] ...", 
                    COLOR
                );                
                
                while (matcher.find()) {
   
                    Topic topic = new Topic(matcher.group());
                    
                    Incremental.updateLastPostNumberList(
                        topic.getId(), 
                        topic.getNumberOfPosts()
                    );
                    
                    topics.add(topic);

                }//while
    
            }//for i
        
        }//for section
               
        return topics;
        
    }//getTopics()

}//classe FetchSections
