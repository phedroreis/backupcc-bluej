package backupcc.edit;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>O objetivo desta classe eh construir o link que deve substituir qualquer 
 * link ,nos HTMLs baixados do forum, que aponte para algum script php.</p>
 * 
 * <p>Exceto os scripts viewforum.php, viewtopic.php e file.php. Portanto quando 
 * os metodos desta classe forem executados, os metodos (codificados em outras 
 * subclasses de EditableLink), já devem ter sido executados sobre estes arquivos,
 * para que os links destes scripts já tenham sido editados e substituidos.</p>
 * 
 * <p>Todos os links remanescentes para scripts php serao editados para apontar 
 * para um arquivo informando que se trata de uma copia estatica e a 
 * funcionalidade clicada nao stah disponivel nessa versao off line.</p>
 * 
 * @author "Pedro Reis"
 * @since 1.0 (6 de setembro de 2022)
 * @version 1.0
 */
final class AnyPhp extends EditableLink {
    
    private static final Pattern PHP = 
        Pattern.compile("href=\"(\\S*?\\.php.*?\")");
    
    private static final String EDITED = 
        backupcc.file.Util.WARNING_FILENAME + '"';
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Quando, no fonte HTML, eh localizado um link para script php (que nao 
     * seja viewforum.php, viewtopic.php ou file.php) esse metodo eh chamado 
     * para inserir no hashMap o "value" associado a este link
     * ("key" no hashMap) que sera usado para substitui-lo quando da edicao do
     * arquivo original.
     * Porque numa primeira etapa eh montado este hashMap associando todos os
     * links para scripts php encontrados, com os links que irao substitui-los
     * na copia estatica. E na etapa seguinte o hashMap eh utilizado para se
     * fazer esta substituicao.
     * 
     * @param matcher Um objeto Matcher que ja exectou seu metodo find() e 
     * encontrou o padrao a ser substituido, que pode ser recuperado pelo
     * metodo group() da classe Matcher.
     * 
     * @param hashMap Um hashMap&lt;String, String&gt; que jah foi inicializado na
     * superclasse e serve para armazenar a lista de links originais 
     * associados a string que serah utilizada para editar este link.
     */
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        /*
        O link para o script php sera redirecionado para uma pagina que
        apenas informa tratar-se de uma copia estatica e a funcionalidade que
        era provida por esse script no forum real nao esta disponivel na copia
        estatica.
        */
        hashMap.put(matcher.group(1), EDITED);
    
    }//map()
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a expressao regular que serah utilizada para localizar links
     * para scripts php (exceto viewforum.php, viewtopic.php e file.php) no
     * metodo editFile() da superclasse.
     * 
     * @return Um objeto Pattern com a regex que sera utilizada.
     */
    @Override
    public Pattern getPattern() {
        
        return PHP;
    
    }//getPattern()
        
}//classe AnyPhp
