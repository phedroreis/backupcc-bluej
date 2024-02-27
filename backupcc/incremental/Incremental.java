package backupcc.incremental;

import static backupcc.file.Util.RAW_PAGES;
import static backupcc.file.Util.mkDirs;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Um arquivo com o numero de posts de cada topico eh gravado ao fim de cada 
 * backup. No proximo backup, estes dados serao comparados com o numero de 
 * posts de cada  topico indicado nas paginas de Section do forum. Se entre um
 * backup e outro, um topico tiver recebido novos posts, as paginas destes 
 * novos posts (e apenas estas) serao baixadas.
 * 
 * Esta classe eh responsavel por ler e gravar o arquivo com o numero de posts
 * de cada topico alem de fornecer os metodos estaticos necessarios ao 
 * gerenciamento do backup incremental.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (27 de agosto de 2022)
 * @version 1.0
 */
public final class Incremental {
    
    private static final String DELIMITER = ",";
    
    private static final char SEPARATOR = ' ';
    
    /**
     * O nome do arquivo com a lista de todos os posts com os respectivos
     * arquivos ao qual pertencem.
     */
    private static final String ALLPOSTS_LIST_FILENAME = "_all-posts.dat";
    
    /**
     * O pathname do arquivo com a lista de todos os posts com os respectivos
     * arquivos ao qual pertencem.
     */
    private static final String ALLPOSTS_LIST_PATHNAME = 
        RAW_PAGES + '/' + ALLPOSTS_LIST_FILENAME;
    
    private static final String ALLPOSTS_LIST_SHA256 = 
        ALLPOSTS_LIST_PATHNAME + ".sha";
   
    /*
    Nome do arquivo com a lista de numero de posts por topico.
    */
    private static final String LASTPOSTS_LIST_FILENAME = "_last-posts.dat";
        
    /*
    O pathname do arquivo onde eh gravado o numero de posts de cada topico.
    */
    private static final String LASTPOSTS_LIST_PATHNAME =
        RAW_PAGES + '/' + LASTPOSTS_LIST_FILENAME;
    
    /*
    Path do arquivo com o hash de verificacao do arquivo de dados.
    */
    private static final String LASTPOSTS_LIST_SHA256 = 
        LASTPOSTS_LIST_PATHNAME + ".sha";
    
    /*
    
    */
    private static final File RAW_PAGES_DIR = new File(RAW_PAGES);
    
    /*
    Uma lista com o numero de posts de cada topico ao fim do backup anterior.
    */    
    private static HashMap<Integer, Integer> previousLastPostsPerTopic;
    
    /*
    A lista sendo atualizada pelo backup que estiver executando no momento. 
    */
    private static StringBuilder updatedLastPostsList;
    
   /*
    Indica se o backup eh incremental ou full.
    */
    private static boolean backupIncremental;
    
    private static String lastBackupDatetime = null;
    
    private static final HashMap<Integer, int[]> POST_MAP = new HashMap<>();
             
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Inicializa o processo incremental verificando se um backup previo jah
     * foi realizado. Se sim, le o arquivo e carrega os dados do ultimo backup
     * com o numero de posts em cada topico quando do backup anterior. Em caso 
     * contrario, se nao houve backup anterior, um full backup serah iniciado.
     * 
     */
    public static void init() {
        
        String PreviousAllPostsList = null;
        String PreviousLastPostsList = null;
        String sha256File;
        String sha256;
        
        try {
            
            PreviousLastPostsList = 
                backupcc.file.Util.readTextFile(LASTPOSTS_LIST_PATHNAME);
            
            sha256 = backupcc.security.Util.sha256(PreviousLastPostsList);
            
            sha256File = backupcc.file.Util.readTextFile(LASTPOSTS_LIST_SHA256);
            
            backupIncremental = sha256.equals(sha256File);
            
            if (backupIncremental) {
                      
                PreviousAllPostsList =
                    backupcc.file.Util.readTextFile(ALLPOSTS_LIST_PATHNAME);
                
                sha256 = backupcc.security.Util.sha256(PreviousAllPostsList);
                
                sha256File = 
                    backupcc.file.Util.readTextFile(ALLPOSTS_LIST_SHA256);
            
                backupIncremental = sha256.equals(sha256File);
                               
            }//if
            
        }        
        catch (IOException e) {
            
            backupIncremental = false;
           
        }//try-catch
            
        if (backupIncremental) {
                     
            Scanner scanner = new Scanner(PreviousAllPostsList);

            while (scanner.hasNext()) putPostTopicPageOnMap(scanner.next());
             
            previousLastPostsPerTopic = new HashMap<>();

            Pattern delimiters = Pattern.compile(DELIMITER);

            String[] split = delimiters.split(PreviousLastPostsList);

            for (String keyValue: split) {

                int separatorPosition = keyValue.indexOf(SEPARATOR);
                
                if (separatorPosition == -1) {//data do backup
                    
                    RAW_PAGES_DIR.renameTo(new File(RAW_PAGES + '-' + keyValue));
                    
                    lastBackupDatetime = keyValue;

                    mkDirs(RAW_PAGES);
                          
                }
                else {//ultimo post de um topico

                    String key = keyValue.substring(0, separatorPosition);

                    String value = keyValue.substring(
                        separatorPosition + 1, keyValue.length()
                    );

                    previousLastPostsPerTopic.put(
                        Integer.valueOf(key),
                        Integer.valueOf(value)
                    );
                    
                }//if-else

            }//for
        }
        else {//backup full
                       
            String[] msgs = {
                "Dados do backup anterior n\u00e3o existem ou est\u00e3o corrompidos",
                "Se continuar ser\u00e1 iniciado um \"full backup\"\n",
                "Ou pode abortar e restaurar o backup destes arquivos"
            };

            backupcc.tui.OptionBox.warningBox(msgs);
                         
            previousLastPostsPerTopic = null;//backup full
            
            backupcc.file.Util.mkDirs(backupcc.file.Util.RAW_PAGES);
                  
            /* Cria o _warning.html */
            backupcc.file.Util.createWarningFile();
           
        }//if-else
                
        updatedLastPostsList = new StringBuilder(16384);        
               
    }//init()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Recebe a Id de um topico e retorna quantos posts havia neste topico 
     * quando foi realizado o ultimo backup.
     * 
     * @param topicId A id do topico.
     * 
     * @return Quantos posts havia neste topico no ultimo backup.
     */
    public static int lastPostOnPreviousBackup(final int topicId) {
        /*
        Nao existe backup anterior a este.
        */
        if (previousLastPostsPerTopic == null) return 0;
        
        Integer lastPost = previousLastPostsPerTopic.get(topicId);
        
        /*
        O topico ainda nao havia sido criado quando do backup anterior.
        */
        if (lastPost == null) return 0;
        
        return lastPost;
                
    }//lastPostOnPreviousBakcup()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Insere na lista com o numero de posts de cada topico, o numero de posts
     * (postNumber) no topico de id = topicId.
     * 
     * @param topicId A Id do topico que tera seu numero de posts atualizado.
     * 
     * @param postNumber A indice do ultimo post neste topico ou o numero de
     * posts no topico, o que eh a mesma coisa.   
     */
    public static void updateLastPostNumberList(
        final int topicId, 
        final int postNumber
    ) {
        
        updatedLastPostsList.append(topicId).append(SEPARATOR).
            append(postNumber).append(DELIMITER);
        
    }//updateLastPostNumberList()
    
        
    /**
     * Um Pattern para localizar Id de post, Id de tópico e índice da página 
     * nesta tópico onde o post está publicado.
     */
    private static final Pattern POST_TOPIC_PAGE_PATTERN = 
        Pattern.compile("(\\d+):(\\d+):(\\d+)");
    
    /*[04]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Insere o postId em um hashMap que o associa ao arquivo de tópico onde
     * está publicado.
     * 
     * @param postTopicPage Post ID, Topic ID e Page Index no formato 
     * postId:topicId:PageIndex
     * 
     */
    public static void putPostTopicPageOnMap(final String postTopicPage) {
        
        int postId;
        
        int[] topicPage = new int[2]; 
        
        Matcher matcher = POST_TOPIC_PAGE_PATTERN.matcher(postTopicPage);
        
        if (matcher.find()) {    
            
            postId = Integer.valueOf(matcher.group(1));
            topicPage[0] = Integer.valueOf(matcher.group(2));
            topicPage[1] = Integer.valueOf(matcher.group(3));
            
            POST_MAP.put(postId, topicPage);
         
        }
          
    }//putPostTopicPageOnMap()
    
    /*[05]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p>
     * Obtém (dada uma ID de post) o postID, o topicID onde o post está 
     * publicado e a página desse tópico (com a primeira tendo índice 0).</p>
     * 
     * <p>Formatado como postID:topicID:PageIndex</p>
     * 
     * @param postId A id de um post.
     * 
     * @return Uma String com postID:topicID:PageIndex.
     */
    private static String getPostTopicPageOnMap(final int postId) {
        
        int[] topicIdAndPageIndex = POST_MAP.get(postId);
        
        return String.format(
            "%d:%d:%d", postId, topicIdAndPageIndex[0], topicIdAndPageIndex[1]
        );
        
    }//getPostTopicPageOnMap()
    
    /*[06]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Obtém o nome do arquivo no disco com a página de tópico onde o post de 
     * ID = postId está publicado. Com uma referência para o post no nome do 
     * arquivo.
     * 
     * @param postId A ID do post sem o prefixo p.
     * 
     * @return Um nome como /t=123&start=50.html#p5652 ou /t=32.html#p54
     */
    public static String getFilenameOnMap(final int postId) {
        
        int[] topicIdAndPageIndex = POST_MAP.get(postId);
        
        if (topicIdAndPageIndex == null) 
            return '/' + backupcc.file.Util.WARNING_FILENAME;
        
        int topicId = topicIdAndPageIndex[0];
        int page = topicIdAndPageIndex[1];
        
        String start = (page == 0) ? 
            "" : "&start=" + (page * backupcc.pages.Page.MAX_ROWS_PER_PAGE);
        
        return String.format("/t=%d%s.html#p%d",topicId, start, postId);
        
    }//getFilenameOnMap()
    
    /*[07]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Acrescenta no arquivo com a lista de todos os posts (relacionados com o
     * arquivo em que estão publicados), os novos posts encontrados em um backup
     * full ou incremental.
     */
    private static void saveAllPostsList() throws IOException {
        
        //Set<Integer> keySet = POST_MAP.keySet();
        
        Iterator<Integer> iterator = POST_MAP.keySet().iterator();

        StringBuilder sb = new StringBuilder(1000000);
        
        while (iterator.hasNext()) {
            
            int postId = iterator.next();
            
            sb.append(getPostTopicPageOnMap(postId)).append('\n');
            
            iterator.remove();
        }
/*
        for (Integer postId: keySet) {
            sb.append(getPostTopicPageOnMap(postId));
            sb.append('\n');
        }*/
              
        backupcc.file.Util.writeTextFile(ALLPOSTS_LIST_PATHNAME, sb.toString());
          
        backupcc.file.Util.writeTextFile(ALLPOSTS_LIST_SHA256, 
            backupcc.security.Util.sha256(sb.toString())
        );

    }//saveAllPostsList()
    
    /*[08]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p>Grava em disco a lista com o numero de posts em cada topico obtida no
     * backup corrente.</p>
     * 
     * <p>Também atualiza a lista de nomes de arquivos que
     * associa cada post ao arquivo em disco onde este post pode ser 
     * visualizado na cópia estática. Uma lista completa com estes nomes
     * de arquivos é necessária para agilizar o processo de edição de links
     * para posts. No entanto, obviamente, novos posts publicados desde o 
     * último backup não estão ainda presentes na lista, que portanto é
     * atualizada ao final do processo de backup</p>
     * 
     * <p>Este método deve ser chamado ao término do processo de backup, depois
     * que foi finalizada a edição de páginas que gera a cópia estática</p>
     *  
     * @throws IOException Em caso de erro de IO ao tentar gravar o arquivo.
     */
    public static void finish() throws IOException {
        
        saveAllPostsList();
             
        updatedLastPostsList.append(backupcc.datetime.Util.now());
        
        String lastPostsList = updatedLastPostsList.toString();
        
        backupcc.file.Util.writeTextFile(LASTPOSTS_LIST_PATHNAME, lastPostsList);
         
        backupcc.file.Util.writeTextFile(LASTPOSTS_LIST_SHA256, 
            backupcc.security.Util.sha256(lastPostsList)
        );
         
    }//finish()  
    
    /*[09]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna se o backup sendo realizado eh incremental ou full.
     * 
     * @return true se for incremental ou false se nao.
     */
    public static boolean isIncremental() {
        
        return backupIncremental;
        
    }//isIncremental()
    
    /*[10]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a data hora do último backup ou se é full.
     * 
     * @return Data e hora do último backup.
     */
    public static String lastBackupDatetime() {
        
        if (lastBackupDatetime == null) return "Full backup";
        
        return 
            "\u00daltimo backup realizado em " +
            backupcc.datetime.Util.dateTime(lastBackupDatetime);
        
    }//lastBackupDatetime()

         
}//classe Incremental
