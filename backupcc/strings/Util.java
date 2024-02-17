package backupcc.strings;

/**
 * Esta classe disponibiliza metodos estaticos uteis para manipulacao de
 * Strings. 
 * 
 * @since 16 de agosto de 2022
 * @version 1.0
 * @author "Pedro Reis"
 */
public final class Util {
        
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna um objeto String com o caractere c repetindo count vezes. Se 
     * count = 0 serah retornada uma string vazia.
     * 
     * @param c O caractere a ser repetido na string.
     * @param count O num. de repeticoes do caractere. Sera tambem o comprimento
     * da string retornada.
     * @return Um objeto String com o char c repetido count vezes.
     */
    public static String repeatChar(final char c, final int count) {
        
        StringBuilder sb = new StringBuilder(256);
        
        for (int i = 1; i <= count; i++) sb.append(c);
        
        return new String(sb);
    }//repeatChar()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * "Acolchoa" uma string com o caractere especificado.
     * 
     * @param c O caractere para acolchoamento.
     * 
     * @param s A string.
     * 
     * @param length Qual o comprimento da string acolchoada que deve ser 
     * retornada.
     * 
     * @return A String s acolchoada com o caractere c ate atingir o comprimento
     * length. A String s eh retornada centralizada no acolchoamento.
     */
    public static String pad(final char c, final String s, final int length) {
        
        if (length < s.length()) return s;
               
        int dif = length - s.length();
        
        int padLength = dif / 2;
        
        String pad = repeatChar(c, padLength);
        
        String r = pad + s + pad;
        
        if (dif % 2 == 1) r = r + c;  
        
        return r;        
    }//pad()
           
    
}//classe Util
