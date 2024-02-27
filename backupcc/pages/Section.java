package backupcc.pages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Um objeto desta classe coleta e retorna informacoes sobre uma pagina de 
 * secao do forum. Estes dados sao obtidos das paginas de Header.
 * 
 * Um trecho de codigo HTML, contendo dados de uma secao, deve ser enviado como
 * argumento ao construtor da classe. E deste bloco de codigo o construtor ira
 * obter a ID, NAME, FILENAME e URL ABSOLUTE desta section.
 * 
 * Portanto um metodo deverah varrer o arquivo fonte de cada pagina de header em 
 * busca destes blocos de codigo, criando objetos desta classe. Um para cada
 * secao apontada na pagina de header que encontrar.
 *
 * @author "Pedro Reis"
 * @since 1.0 (24 de agosto de 2022)
 * @version 1.1
 */
public final class Section extends Page {
    
    private final int numberOfTopics;
    
    /*
     * Regexp para localizar os dados de uma Section na pagina do Header ao qual
     * essa Section pertence. Estes dados estao sempre inseridos no escopo de
     * uma tag a localizavel por este Pattern.
     */
    private static final Pattern FINDER_REGEXP = 
        Pattern.compile(
            "<a href=\"[.]/viewforum[.]php[?]f=.+?" +
            "class=\"forumtitle\" data-id=\"(\\d+)\">(.+)</a>" +
            "[\\s\\S]+?T.picos</span>: <span class=\"value\">(\\d+)"
        ); 
     
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe extrai os dados de uma Section a partir de um bloco
     * de codigo contido no codigo fonte de uma pagina de Header do forum.
     * 
     * @param htmlBlock O bloco de codigo HTML da pagina Header do forum de
     * onde se pode extrair dados de uma Section.
     */
    public Section(final String htmlBlock) {
         
        /*
        Localiza a id e o nome da Section no htmlBlock, que por sua vez foi 
        obtido de uma pagina de Header do forum com a regexp FINDER_REGEXP.
        */
        Matcher matcher = FINDER_REGEXP.matcher(htmlBlock);
        
        boolean find = matcher.find();
        
        assert(find == true) : "Can't parse SECTION data for\n\n" + htmlBlock;
        
        if (find) {
            
            /* Estes campos sao declarados na super classe Page */
            id = Integer.valueOf(matcher.group(1));
            filename = "f=" + id + "&start=";
            name = matcher.group(2);
            absoluteURL = 
                backupcc.net.Util.FORUM_URL + "/viewforum.php?f=" + id;
            numberOfTopics = Integer.valueOf(matcher.group(3));
           
        }
        /*
        Se os dados do bloco if nao puderam ser localizados, ha um bug no
        programa ou o padrao do HTML das paginas do forum foi alterado desde que
        este codigo foi escrito.
        */
        else {
            numberOfTopics = 0;//final tem que ser inicializada
            
            String[] msgs = {                
                "Dados de se\u00e7\u00e3o n\u00e3o localizados\n",
                "Houve um erro ao fazer o 'parse' de um trecho HTML",
                "O backup ser\u00e1 abortado\n",
                "Execute novamente o programa com java -ea 'nomeDoPrograma'",
                "E contacte o desenvolvedor"
            };
            
            backupcc.tui.OptionBox.abortBox(msgs);
        }
   
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna um objeto Pattern com a regexp que permite localizar blocos de
     * codigo HTML (em uma pagina de Header) de onde os dados de uma 
     * Section podem ser extraidos.
     * 
     * @return A regexp para localizar blocos de codigo HTML na pagina principal
     * do forum que contem os dados das secoes do forum.
     */
    public static Pattern getFinder() {
        
        return FINDER_REGEXP;
        
    }//getFinder()
    
        
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna o numero de topicos que tem esta secao.
     * 
     * @return O numero de topicos da secao.
     */
    public int getNumberOfTopics() {
        
        return numberOfTopics;
        
    }//getNumberOfTopics()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna o numero de paginas que tem esta secao.
     * 
     * @return O numero de paginas da secao.
     */
    public int getNumberOfPages() {
        
        return ((numberOfTopics - 1) / MAX_ROWS_PER_PAGE) + 1;
        
    }//getNumberOfPages()
    
}//classe Section
