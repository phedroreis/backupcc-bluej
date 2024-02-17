package backupcc.fetch;

/**
 * Classe pública do pacote que fornece o método {@link #downloadPages() }
 * encarregado de baixar todas as páginas do fórum.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (25 de agosto de 2022)
 * @version 1.1
 */
public final class FetchPages {
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Baixa todas as páginas do fórum. 
     * 
     */
    public static void downloadPages() {
        
        backupcc.pages.Main main = 
            new backupcc.pages.Main(
                backupcc.net.Util.FORUM_URL, 
                backupcc.file.Util.INDEX_HTML
            );
                         
        FetchTopics topicsPages = new FetchTopics(main);
        
        topicsPages.getPosts();
               
    }//downloadPages()
    
}//classe FetchPages
