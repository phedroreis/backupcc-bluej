package backupcc.strings;

/**
 * <p>Esta classe se destina a fornecer um objeto mais eficiente para edição de 
 * String.</p>
 * 
 * <p>Como é sabido, um objeto String é imutável e alterações em Strings são 
 * computacionalmente custosas em Java. Um objeto StringEditor fornece o 
 * método {@link #replace(java.lang.String, java.lang.String) replace} que
 * substitui substrings em um objeto String usando internamente um objeto 
 * StringBuilder.</p>
 * 
 * @author "Pedro Reis"
 * @since 1.0 (12 de setembro de 2022)
 * @version 1.0
 */
public final class StringEditor {
    
    private StringBuilder sb;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Contrutor da classe
     * 
     * @param str Uma string a ser editada com o médoto {@link #replace(java.lang.String, java.lang.String) replace}
     */
    public StringEditor(final String str) {
        
        sb = new StringBuilder(str);
        
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Substitui substrings na String original.
     * 
     * @param target Substring a ser substituída.
     * 
     * @param replacement String de substituição.
     */
    public void replace(final String target, final String replacement) {
        
        int lengthTarget = target.length();
        int lengthReplacement = replacement.length();
        int indexOf = sb.indexOf(target);
        
        while (indexOf != -1) {
            
            sb = sb.replace(indexOf, indexOf + lengthTarget, replacement);
            
            indexOf = sb.indexOf(target, indexOf + lengthReplacement);
        }
        
    }//replace()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a String original com as edições feitas pelo método replace.
     * 
     * @return A String original editada pelo método replace.
     */
    @Override
    public String toString() {
        
        return sb.toString();
        
    }//toString()
        
}//classe StringEditor
