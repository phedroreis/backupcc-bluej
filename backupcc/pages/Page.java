package backupcc.pages;

/**
 * Superclasse para as classes que analisam, coletam, armazenam e fornecem 
 * (atraves dos metodos desta superclasse) dados da PAGINA PRINCIPAL, das 
 * paginas de HEADERS, SECOES E TOPICOS do forum.
 * 
 * Uma subclasse que estenda esta (exceto a subclasse Main), deve 
 * obrigatoriamente fornecer um metodo estatico getFinder(), que deverah
 * retornar um objeto Pattern com o padrao de regexp a ser aplicado para
 * localizar o bloco HTML que contem os dados da pagina de 
 * HEADER, SECAO OU TOPICO. Bloco HTML este que deve ser enviado ao 
 * construtor da respectiva subclasse, para que dele o construtor extraia os
 * seguintes dados:
 * 
 * - id (um numero inteiro) do HEADER, SECAO ou TOPICO. Para Main serah 0.
 * - filename com o qual o arquivo de HEADER, SECAO ou TOPICO sera gravado no 
 * disco local. Para a subclasse Main este nome de arquivo serah enviado em
 * um parametro no seu construtor.
 * 
 * - name do HEADER, SECAO ou TOPICO. Para Main o name sera "Principal"
 * 
 * - absoluteURL A URL para baixar o arquivo referente ao HEADER, SECAO ou
 * TOPICO. Para a subclasse Main (que eh referente a pagina inicial do forum) 
 * esta URL serah a URL de acesso ao proprio forum.
 * 
 * @author "Pedro Reis"
 * @since 23 de agosto de 2022
 * @version 1.0
 */
public abstract class Page implements Comparable {
    
    /**
     * id da pagina principal (0), de pagina de header, secao ou topico.
     */
    protected int id;
    
    /**
     * O nome do header, secao ou topico. Para um objeto da subclasse Main este
     * campo armazenarah "Principal"
     */
    protected String name;
    
    /**
     * O nome do arquivo onde a pagina do header, secao ou topico sera gravada.
     * Nesta versao, o nome do arquivo para gravar a pagina principal eh
     * clubeceticismo.com.br.html
     */
    protected String filename;
    
    /**
     * A URL para baixar a 1a pagina de determinado header, secao ou topico.
     * Na da pagina principal este campo armazenarah a URL de acesso ao proprio
     * forum.
     */
    protected String absoluteURL;
    
    public static final int MAX_ROWS_PER_PAGE = 50;
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/   
    /**
     * Retorna a id de um header, secao ou topico. 0 se for a pagina principal.
     * 
     * @return A id.
     */
    public int getId() {
        
        return id;
        
    }//getId()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna o nome de um header, secao ou topico. "Principal" para um objeto
     * da subclasse Main.
     * 
     * @return O nome.
     */
    public String getName() {
        
        return name;
        
    }//getName()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * O nome do arquivo de um header ou da pagina principal.
     * Nesta versao, para a pagina principal (dados coletados por um objeto da
     * subclasse Main), o nome do arquivo eh clubeceticismo.com.br.html
     * 
     * @return O nome do arquivo.
     */
    public String getFilename() {
        
        return filename;  
        
    }//getFilename()
    
    /*[04]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * O nome do arquivo de secao ou topico. 
     * 
     * @param pageIndex O indice da pagina. A primeira tem indice 0.
     * 
     * @return O nome do arquivo.
     */
    public String getFilename(final int pageIndex) {
        
        if (pageIndex == 0)
            return filename.replace("&start=", "") + ".html";
        
        return filename + (pageIndex * MAX_ROWS_PER_PAGE) + ".html";
        
    }//getFilename()
      
    /*[05]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * O endereco absoluto da pagina com o header, secao ou topico.
     * 
     * @return A url absoluta. O endereco atual da pagina principal eh
     * https://clubeceticismo.com.br
     */
    public String getAbsoluteURL() {
        
        return absoluteURL;
        
    }//getAbsoluteURL()
    
    /*[06]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * O endereco absoluto da pagina de secao ou topico.
     * 
     * @param pageIndex O indice da pagina. A primeira tem indice 0.
     * 
     * @return A url absoluta. 
     */
    public String getAbsoluteURL(final int pageIndex) {
        
        if (pageIndex == 0) return absoluteURL;
        
        return absoluteURL + "&start=" + (pageIndex * MAX_ROWS_PER_PAGE);
        
    }//getAbsoluteURL()
    
    /*[07]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Implementa a interface Comparable usando como criterio de ordenacao a
     * id das paginas. Na estrutura TreeSet que ira armazenar objetos de 
     * subclasses de Page, estes objetos estarao portanto ordenados pela id.
     * 
     * @param page Um objeto Main, Header, Section, Topic, ou Post.
     * 
     * @return Um numero positivo se a Id deste item for maior que a do item
     * comparado. 0 se for igual e negativo se for menor.
     */
    @Override
    public int compareTo(Object page) {
        
        return ( getId() - ((Page)page).getId() );
        
    }//compareTo()
    
}//classe abstrata Page