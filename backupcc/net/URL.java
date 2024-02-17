package backupcc.net;

import java.net.MalformedURLException;

/**
 * Esta classe deve recuperar informacoes de uma URL que aponte para pagina ou 
 * arquivo no dominio do forum ClubeCeticismo.
 * As informacoes devem ser obtidas da propria String da URL, sem necessidade de
 * acesso ao servidor do forum.
 * 
 * @since 14 de agosto de 2022
 * @version 1.0
 * @author "Pedro Reis"
 */
public final class URL {
    
    private final java.net.URL absoluteUrl;
  
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor que inicializa a classe com a URL que poderah ser processada
     * pelos metodos da classe para retornar informacoes uteis.
     * 
     * @param url Um objeto String com a Url.
     */
    public URL(final String url) {
        
        java.net.URL urlAux = null;
        
        try {
            
            urlAux = new java.net.URL(new java.net.URL(Util.FORUM_URL), url);
            
        }
        catch (MalformedURLException e) {
            
            String[] msgs = {
                e.getMessage()
            };
            
            backupcc.tui.OptionBox.abortBox(msgs);
            
        }
        finally {
            
            absoluteUrl = urlAux;
        }
          
    }//construtor
        
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public String getRef() {
        
        String r = absoluteUrl.getRef();
        
        if (r == null) return "";
        
        return '#' + r;
        
    }//getRef()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public String getQuery() {
        
        String q = absoluteUrl.getQuery();
        
        if (q == null) return "";
        
        return '?' + q;
        
    }//getQuery()
        
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a url como endereço absoluto. Se já for um endereco absoluto, a 
     * url eh retornada sem alteracao.
     * 
     * @return A url no formato de endereco absoluto.
     */
    public String getAbsoluteUrl() {
               
        if (absoluteUrl.getPath().contains("./download/file.php"))
            
            return absoluteUrl.toString();  
        
        else
            
            return absoluteUrl.toString().replace(getQuery(), "").
                replace(getRef(), "");
       
    }//getAbsoluteUrl()
    
    /*[04]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public String getServerPathname() {
        
        return absoluteUrl.getPath();
        
    }//getServerPathname()
    
    /*[05]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public String getServerPath() {
        
        String serverPathname = getServerPathname();
        
        int separatorLastIndex = serverPathname.lastIndexOf('/');
        
        if (separatorLastIndex == 0) return "/";
        
        return serverPathname.substring(0, separatorLastIndex);
        
    }//getServerPath()
    
    /*[06]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public String getServerFilename() {
        
        String serverPathname = getServerPathname();
        
        return serverPathname.substring(serverPathname.lastIndexOf('/') + 1).
            replace(getQuery(), "").replace(getRef(), "");
        
    }//getServerFilename()
    
    /*[07]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public String getLocalPathname() {
        
        String pathname = absoluteUrl.getPath();
            
        if (pathname.contains("./download/file.php")) 
            return backupcc.file.Util.FORUM_HOME + "/./download/" +
                absoluteUrl.getQuery();
        
        return backupcc.file.Util.FORUM_HOME + pathname;
                
    }//getLocalPathname()
    
    /*[08]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public String getLocalPath() {
        
        String pathname = getLocalPathname();
        
        return pathname.substring(0, pathname.lastIndexOf('/'));
        
    }//getLocalPath()
    
    /*[09]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public String getLocalFilename() {
        
        String pathname = getLocalPathname();
        
        return pathname.substring(pathname.lastIndexOf('/') + 1);
        
    }//getLocalFilename()
        
}//classe URL
