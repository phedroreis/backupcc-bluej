package backupcc.pages;

/**
 * Um objeto desta subclasse de Page deverah ser construido com os dados da
 * pagina principal do forum. Para que, a partir do codigo fonte obtido com o
 * download desta pagina, o programa possa obter acesso as paginas de headers.
 * E por meio dos headers, acesso as sections. E das sections obter os topics.
 * 
 * @author "Pedro Reis"
 * @since 25 de agosto de 2022
 * @version 1.0
 */
public final class Main extends Page {
    /**
     * Um objeto desta subclasse de Page deverah ser construido com os dados da
     * pagina principal do forum. Para que, a partir do codigo fonte obtido com
     * o download desta pagina, o programa possa obter acesso as paginas de 
     * headers.
     * E por meio dos headers, acesso as sections. E das sections obter os
     * topics. 
     * 
     * @param url Deve ser a URL de acesso ao forum.
     * @param filename O nome com o qual a pagina principal serah gravada no 
     * disco local.
     */
    public Main(final String url, final String filename) {
        id = 0;
        name = "P\u00e1gina Principal";
        absoluteURL = url;
        this.filename = filename;
    }//construtor
    
}//classe Main
