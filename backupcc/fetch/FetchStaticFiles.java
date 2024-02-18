package backupcc.fetch;

import backupcc.file.ForumPageFilter;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe encarregada de baixar os arquivos estáticos hospedados no servidor
 * do fórum.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (3 de setembro 2022)
 * @version 1.1
 */
public final class FetchStaticFiles {
    
    private static final int COLOR = backupcc.tui.Tui.CYAN;
    
    /*
     * Procura por urls de arqs. estaticos em arquivos HTML
     */
    private static final Pattern HTML_SEARCH = 
        Pattern.compile("(href|src|data-src-hd)=\"(.+?)\"");
    
    /*
     * Procura por urls de arqs. estaticos em arquivos CSS
     */    
    private static final Pattern CSS_SEARCH =
        Pattern.compile("url\\(['\"](.+?)['\"]");
    
    /*
     * Nao procura por urls de arquivos estaticos no corpo de posts 
     * dos foristas
     */    
    private static final String POST_CONTENT_REGEX = 
        "<div class=\"content\">[\\s\\S]*?<span class=\"sr-only\">Voltar ao topo</span>";
    
    private static int total;
       
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static void searchInFile(
        final File file,
        final Pattern pattern,
        final int group,
        final String serverPath
    ) {
        
        String forumUrlPrefix = backupcc.net.Util.FORUM_URL + '/';
        
        String contentFile = null;
        
        try {
            
            contentFile = backupcc.file.Util.readTextFile(file).replaceAll(POST_CONTENT_REGEX, "");
            
        }
        catch (IOException e) {
               
            String[] msgs = {e.getMessage() + '\n'};
               
            backupcc.tui.OptionBox.abortBox(msgs);
            
        }//try-catch
        

        Matcher matcher = pattern.matcher(contentFile);
        
        boolean flag = true;
        
        while (matcher.find()) {

            String url =  matcher.group(group);

            if (
                (url.matches(".+?\\.php.*")) &&
                (!url.matches(".+?file\\.php[/?]avatar=.*"))
            ) 
                continue;

            if (url.startsWith(".") || url.startsWith(forumUrlPrefix)) {
                
                backupcc.net.URL urlInfo =
                    new backupcc.net.URL(serverPath + url);
                
                String localPathname = urlInfo.getLocalPathname();
                String localPath = urlInfo.getLocalPath();
                String localFilename = urlInfo.getLocalFilename(); 
                
                File f = new File(localPathname);

                if (!f.exists()) {
                    
                    backupcc.file.Util.mkDirs(localPath);

                    if (flag) {
                        
                        backupcc.tui.Tui.println("\n");
                        flag = false;
                        
                    }
                    
                    backupcc.net.Util.downloadUrlToPathname(
                        urlInfo.getAbsoluteUrl(), 
                        localPathname, 
                        localFilename, 
                        COLOR
                    );
           
   
                    if (localFilename.endsWith(".css")) {
                        
                        searchInFile(
                            new File(localPathname),
                            CSS_SEARCH,
                            1,
                            urlInfo.getServerPath() + '/'
                        );
                        
                    }
                    
                }//if

            }//if

        }//while

    }//searchInFile()
       
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/    
    public static void fetchStaticFiles() {
        
        File dir = new File(backupcc.file.Util.RAW_PAGES);
        
        File[] listFiles = dir.listFiles(new ForumPageFilter());
        
        backupcc.tui.Tui.println(" ");
        backupcc.tui.Tui.printlnc("Obtendo os arquivos do servidor ...", COLOR);
          
        total = listFiles.length; int countFiles = 0;
        
        int barLength = (total <= backupcc.tui.ProgressBar.LENGTH) ? 
            total : backupcc.tui.ProgressBar.LENGTH;
        
        backupcc.tui.ProgressBar bar = 
            new backupcc.tui.ProgressBar(total, barLength, COLOR);
        
        bar.show();
                       
        for (File file: listFiles) {
            
            bar.update(++countFiles);
            
            searchInFile(file, HTML_SEARCH, 2, ""); 
            
        }//for file
        
    }//fetchStaticFiles()
    
    
}//classe FetchStaticFiles
