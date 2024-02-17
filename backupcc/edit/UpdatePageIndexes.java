package backupcc.edit;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Como o processo de backup deste sistema é incremental, páginas de tópicos
 * que já "fecharam" com 50 posts não seriam mais atualizadas. Porém isto 
 * deixaria os links com índices numéricos para navegação nas páginas do tópico,
 * inconsistentes quando novas páginas fossem acrescentadas ao tópico.</p:
 * 
 * <p>
 * O método updatePageIndexes() desta classe insere, quando necessário, um 
 * botão com um link que aponta para a última página do tópico. O que informa
 * a quem esteja visualizando esta página, quantas páginas tinha realmente o
 * tópico quando do último backup incremental.</p>
 * 
 * @author "Pedro Reis"
 * @since 1.0 (27 de setembro de 2022)
 * @version 1.0
 */
public final class UpdatePageIndexes {
    
    /**
     * Uma lista com os tópicos que possuem páginas cujos índices numéricos de
     * navegação irão ficar desatualizados no corrente backup incremental.
     */
    private static final List<int[]> TOPICS_LIST = new LinkedList<>();
    
    /**
     * Cor de texto para saídas no terminal tipo Unix.
     */
    private static final int COLOR = backupcc.tui.Tui.YELLOW;
    
    /**
     * Este comentário é gravado ao final do arquivo HTML para o programa saber
     * que já existe um botão de link atualizando a última do tópico,
     * acrescentado a lista de índices numéricos. Porque na próxima vez que os
     * índices numéricos desta página precisarem ser atualizados, o botão não 
     * deve ser inserido novamente, mas apenas ter seu rótulo editado.
     */
    private static final String INDEXES_UPDATED = "<!--updated-->";
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p>
     * Quando, no backup incremental, um tópico é atualizado porque foram feitas 
     * novas postagens desde o último backup, pode acontecer que estas novas 
     * postagens criem novas páginas acrescentadas ao tópico. Nesse caso, os
     * índices de navegação nas páginas que já fecharam (não serão mais 
     * atualizadas porque já tem 50 posts), iriam ficar inconsistentes com o
     * número real de páginas do tópico. Quando isto aocntecer, o sistema insere
     * um botão nestas páginas (um link), que vai apontar para a última página
     * acrescentada ao tópico. A última página encontrada quando do último 
     * backup incremental.</p>
     * 
     * <p>
     * O objeto da classe FetchTopics identifica quando um tópico vai ficar com
     * seus índices de navegação inconsistentes e em quais páginas do tópico
     * isto irá acontecer, e então insere este tópico em uma lista chamando 
     * este método.</p>
     * 
     * @param topicId A id do tópico.
     * 
     * @param lastPageToUpdate Até que pág. deste tópico precisa que o botão com
     * o link para a última página seja inserido.
     * 
     * @param numberOfPagesOnThisTopic Quantas páginas tem o tópico ao todo,
     * no momento do backup incremental que executa este método.
     */
    public static void add(
        final int topicId, 
        final int lastPageToUpdate,
        final int numberOfPagesOnThisTopic
    ) {
        
        int[] topic = new int[3];
        
        topic[0] = topicId;
        topic[1] = lastPageToUpdate;
        topic[2] = numberOfPagesOnThisTopic;
        
        TOPICS_LIST.add(topic);
        
    }//add()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna o nome do arquivo no disco que tem o HTML fonte do tópico.
     * 
     * @param topicId A id do tópico.
     * @param page A página do tópico.
     * @return O nome do arquivo do tópico de ID = topicId e página = page.
     */
    private static String getFilename(final int topicId, final int page) {
          
        return (
            "/t=" + 
            topicId + 
            ((page == 1) ? 
                "" :
                "&start=" + ((page - 1) * backupcc.pages.Page.MAX_ROWS_PER_PAGE)) +
            ".html"
        );
    }//getFilename()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Insere um botão com um link para a última página do tópico em todas as 
     * páginas de tópicos que teriam seus índices de navegação numéricos 
     * inconsistentes neste backup incremental.
     */
    public static void updatePageIndexes() {
        
        if (TOPICS_LIST.isEmpty()) return;
        
        backupcc.tui.Tui.printlnc(
            "\nT\u00F3picos com \u00EDndices a atualizar ...\n",
            COLOR
        );
        
        for (int[] toUpdate: TOPICS_LIST) {
                    
            backupcc.tui.Tui.printlnc(
                "T\u00F3pico: " + toUpdate[0] +
                " -> Atualizando \u00EDndices da p\u00E1g.1 at\u00E9 p\u00E1g." +
                toUpdate[1] + 
                " - Nova \u00FAltima p\u00E1g.: " + 
                toUpdate[2],
                COLOR
            );
            
            for (int i = 1; i <= toUpdate[1]; i++) {
                
                String pathname = 
                    backupcc.file.Util.FORUM_HOME + getFilename(toUpdate[0], i);
                
                String contentFile;
                              
                try {
                    
                    contentFile = backupcc.file.Util.readTextFile(pathname);
                } 
                catch (IOException e) {
                    
                    String[] msgs = {
                        e.getMessage() + '\n',
                        "Erro ao ler: " + pathname + '\n',
                        "Os \u00EDndices de p\u00E1ginas podem se tornar inconsistentes neste arquivo!",
                        "Mesmo se o programa for abortado agora\n",
                        "Um novo 'full backup' \u00E9 recomend\u00E1vel"
                    };
                    
                    backupcc.tui.OptionBox.warningBox(msgs);
                    
                    continue;//Tenta atualizar indices da prox. pag. do topico
                }
                
                if (contentFile.endsWith(INDEXES_UPDATED)) {
                
                    contentFile = 
                        contentFile.replaceAll(
                            "href=\"\\./t=\\d+.+?\u00DAltima p\u00E1g. : \\d+",
                            "href=\"." + 
                            getFilename(toUpdate[0], toUpdate[2]) + 
                            "\">\u00DAltima p\u00E1g. : " + 
                            toUpdate[2]
                        );
                    
                }
                else {
                    
                    contentFile =
                        contentFile.replace(
                            "<div class=\"pagination\">",
                            "<div class=\"pagination\"><a style=\"color: green; \"class=\"button\" href=\"." +
                            getFilename(toUpdate[0], toUpdate[2]) +
                            "\">\u00DAltima p\u00E1g. : " + 
                            toUpdate[2] + 
                            "</a>"
                                    
                        ) + INDEXES_UPDATED;
                    
                    contentFile =
                        contentFile.replaceAll(
                            "&bull;.+?<strong>1</strong> de <strong>\\d+</strong>",
                            "&bull; P\u00E1gina: <strong>1</strong> de <strong>" +
                            toUpdate[2] + 
                            "</strong>"
                        );
                    
                }//if-else
                
                try {
                    
                    backupcc.file.Util.writeTextFile(pathname, contentFile);
                } 
                catch (IOException e) {
                    
                    String[] msgs = {
                        e.getMessage() + '\n',
                        "Erro ao gravar: " + pathname + '\n', 
                        "Os \u00EDndices de p\u00E1ginas podem se tornar inconsistentes neste arquivo!",
                        "Mesmo se o programa for abortado agora\n",
                        "Um novo 'full backup' \u00E9 recomend\u00E1vel"
                    };
                    
                    backupcc.tui.OptionBox.warningBox(msgs);
                }
                 
            }//for i
            
        }//for toUpdate
        
        backupcc.tui.Tui.println(" ");
        
    }//updatePageIndexes()
    
}//classe UpdatePageIndexes
