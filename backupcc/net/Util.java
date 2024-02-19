package backupcc.net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.io.File;

/**
 * Metodos e campos estaticos relacionados com operacoes de conexao de internet.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (21 de agosto de 2022)
 * @version 1.2
 */
public final class Util {
     
    /**
     * O protocolo utilizado na conexao com o servidor do forum.
     */
    public static final String FORUM_PROTOCOL = "https://";
    
    /**
     * O nome de dominio do forum.
     */
    public static final String FORUM_DOMAIN = "clubeceticismo.com.br";
    
    /**
     * A URL do forum
     */
    public static final String FORUM_URL = FORUM_PROTOCOL + FORUM_DOMAIN;   
    
    private static void downloadUrlToPathname(
         final String url,
         final String pathname
    ) throws IOException {
        
        URL theFile = null;
        try {
            
            theFile = new URL(url);
            
        } 
        catch (MalformedURLException e) {}
        
        try (

            FileOutputStream fos = new FileOutputStream(pathname);

            @SuppressWarnings("null")
            ReadableByteChannel rbc = 
                Channels.newChannel(theFile.openStream());

        ) {

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        catch (IOException e) {
            
            try {
                new File(pathname).delete();
            }
            catch(SecurityException es) {}
            
            throw new IOException(e.getMessage());
              
         }//try-cache       
        
    }//downloadUrlToPathname()    
     
    /*[01]---------------------------------------------------------------------

    -------------------------------------------------------------------------*/
    /**
     * Baixa um arquivo estatico no servidor do forum apontado por uma URL 
     * para um diretorio especificado que
     * jah deve indicar tambem o nome do arquivo que serah gravado.
     * 
     * @param url A url do arquivo a ser baixado.
     * 
     * @param pathname Caminho absoluto ou relativo para onde gravar o arquivo,
     * incluindo tambem o nome que serah dado ao arquivo baixado. Os diretorios
     * neste caminho devem existir ou uma excecao serah lancada. Se um arquivo 
     * com o nome indicado em pathname nao existir, serah criado um arquivo com
     * esse nome.
     * 
     * @param name O nome (sem o caminho) do arquivo.
     * 
     * @param color A cor do texto com a qual o método vai escrever a mensagem
     * no terminal.
     * 
     */
    public static void downloadStaticFileToPathname(
        final String url, 
        final String pathname,
        final String name,
        final int color
    ) {
        
        if (name != null)
            backupcc.tui.Tui.printlnc("Obtendo " + name + " ...", color);
        
        boolean retry;
        
        do {
            retry = false;
            try {

                downloadUrlToPathname(url, pathname);
            }
            catch (IOException e) {
                
                String[] msgs = {
                    e.getMessage() + "\n",
                    "Erro ao obter: " + pathname + "\n",
                    "Voc\u00EA pode desistir ou tentar novamente\n"
                };
                
                retry = backupcc.tui.OptionBox.retryBox(msgs);
                                
                if (!retry) {
 
                    String[] warnMsgs = {
                        "Falha ao obter: " + pathname + "\n",
                        "O backup ainda pode prosseguir, mas a",
                        "aus\u00EAncia deste arquivo pode prejudicar a",
                        "visualiza\u00E7\u00E3o de algumas p\u00E1ginas\n",
                        "Aborte se o arquivo for JS ou CSS e tente mais tarde",
                        "A MENOS QUE SAIBA O QUE EST\u00C1 FAZENDO!"
                    };

                    backupcc.tui.OptionBox.warningBox(warnMsgs); 
                    
                }//if !retry    
                
            }//try-cache
            
        }while (retry);
       
    }//downloadStaticFileToPathname() 
    
    /*[02]---------------------------------------------------------------------

    -------------------------------------------------------------------------*/
    /**
     * Baixa um arquivo de Smyle no servidor do forum apontado por uma URL 
     * para um diretorio especificado que jah deve indicar tambem o nome do
     * arquivo que serah gravado. Estas URLs sao coletadas em postagens de
     * foristas.
     * 
     * @param url A url do arquivo a ser baixado.
     * 
     * @param pathname Caminho absoluto ou relativo para onde gravar o arquivo,
     * incluindo tambem o nome que serah dado ao arquivo baixado. Os diretorios
     * neste caminho devem existir ou uma excecao serah lancada. Se um arquivo 
     * com o nome indicado em pathname nao existir, serah criado um arquivo com
     * esse nome.
     * 
     * @param name O nome (sem o caminho) do arquivo.
     * 
     * @param color A cor do texto com a qual o método vai escrever a mensagem
     * no terminal.
     * 
     */    
    public static void downloadSmileToPathname(
        final String url, 
        final String pathname,
        final String name,
        final int color
    ) {
        
        if (name != null)
            backupcc.tui.Tui.printc("Obtendo " + name + " ...", color);
 
        try {

            downloadUrlToPathname(url, pathname);
        }
        catch (IOException e) {
            
            backupcc.tui.Tui.printc(" ERRO!", backupcc.tui.Tui.RED);
                
        }
        
        backupcc.tui.Tui.println("");
            
        
    }//downloadSmileToPathname()  
    
    /*[03]---------------------------------------------------------------------

    -------------------------------------------------------------------------*/
    /**
     * Baixa uma pagina do forum apontada por uma URL 
     * para um diretorio especificado que
     * jah deve indicar tambem o nome do arquivo que serah gravado.
     * 
     * @param url A url do arquivo a ser baixado.
     * 
     * @param pathname Caminho absoluto ou relativo para onde gravar o arquivo,
     * incluindo tambem o nome que serah dado ao arquivo baixado. Os diretorios
     * neste caminho devem existir ou uma excecao serah lancada. Se um arquivo 
     * com o nome indicado em pathname nao existir, serah criado um arquivo com
     * esse nome.
     * 
     * @param name O nome (sem o caminho) do arquivo.
     * 
     * @param color A cor do texto com a qual o método vai escrever a mensagem
     * no terminal.
     * 
     */    
    public static void downloadPageToPathname(
        final String url, 
        final String pathname,
        final String name,
        final int color
    ) {
        
        if (name != null)
            backupcc.tui.Tui.printlnc("Obtendo " + name + " ...", color);
            
        boolean retry;
        
        do {
            
            retry = false;
            
            try {

                downloadUrlToPathname(url, pathname);
            }
            catch (IOException e) {
                
                retry = true;
                
                String[] msgs = {
                    e.getMessage() + "\n",
                    "Erro ao obter: " + pathname + "\n",
                    "O backup n\u00E3o pode prosseguir sem este arquivo",
                    "Voc\u00EA pode abortar o backup ou tentar novamente\n"
                };
                
                backupcc.tui.OptionBox.retryOrAbortBox(msgs);

            }//try-cache
            
        }while (retry);
       
    }//downloadPageToPathname()     
         
}//classe Util