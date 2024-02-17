package backupcc.edit;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Um objeto desta classe é responsável por editar os links que apontam para a
 * página principal do fórum.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (6 de setembro de 2022)
 * @version 1.0
 */
final class IndexPhp extends EditableLink {
    /**
     * Localiza links para a página principal do fórum.
     */
    private static final Pattern INDEX_REGEX =
    Pattern.compile(
        "href=\"(" +
        FORUM_URL_REGEX_STYLE + 
        "|[.]/index\\.php.*?)\""                  
    );
    
    /**
     * Link que aponta para o arquivo na cópia estática que exibe a página
     * principal.
     */
    private static final String INDEX_LINK = 
        "./" + backupcc.file.Util.INDEX_HTML + "\"";
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Associa um link para a página principal com o link que irá substitui-lo
     * na cópia estática apontando para o arquivo no disco com uma cópia 
     * estática da página principal.
     * 
     * @param matcher Objeto Matcher passado logo após o método find() ter 
     * localizado um link para a página principal.
     * 
     * @param hashMap HashMap para associar o link original ao link que deverá
     * substitui-lo nas cópias estáticas.
     */
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
                        
        hashMap.put(matcher.group(1), INDEX_LINK);

    }//map()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a regex que localiza, nos arquivos originais, links para a
     * página principal.
     * 
     * @return regex que localiza, nos arquivos originais, links para a
     * página principal.
     */
    @Override
    public Pattern getPattern() {
        
        return INDEX_REGEX;
        
    }//getPattern()   
      
}//classe IndexPhp
