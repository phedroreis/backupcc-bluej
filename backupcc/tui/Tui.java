package backupcc.tui;

/**
 * Esta classe fornece metodos estaticos basicos para a construcao da Text
 * User Interface (TUI) do programa. O objetivo eh fornecer metodos basicos para 
 * controlar cores e cursor em terminais compativeis com o padrao POSIX de 
 * sistemas UNIX LIKE, como o Linux e MacOS. 
 * 
 * Quando a interface executar em um terminal em sistema Windows os comandos 
 * ANSI nao serao enviados ao terminal.
 * 
 * @since 16 de agosto de 2022
 * @version 1.0
 * @author "Pedro Reis"
 */
public final class Tui {
    /*
    Armazena se o SO eh Windows ou Unix like
    */
    private static String osType;
    
    /*
    Control Sequence Introducer de comandos ANSI para terminais
    */
    private static final String CSI = "\u001B[";
    
    /*
    Constante que se enviada a um metodo reseta cores e cursor no terminal
    */
    public static final int NONE = -1;
    
    /*
    Codigos para as cores de texto no terminal
    */
    public static final int BLACK = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int YELLOW = 3;
    public static final int BLUE = 4;
    public static final int MAGENTA = 5;
    public static final int CYAN = 6;
    public static final int WHITE = 7;
    
    /*
    Codigos para efeitos no texto do terminal. Estes valores podem ser somados
    para que os efeitos sejam sobrepostos. Exemplo: BOLD + UNDERLINE = 5 e terah
    como efeito caracteres em negrito E sublinhados
    */
    public static final int BOLD = 1;
    public static final int UNDERLINE = 4;
    public static final int REVERSE = 7;      
    
    /*
    Este campo serah utilizado pela barra de progresso da TUI do programa. Para 
    saber se algum texto foi despejado no terminal enquanto a barra de progresso
    estava ativa.
    */
    public static boolean cursorMoved = false;
     
    /*
    Este bloco de codigo executa quando a classe eh carregada pela 1a vez. 
    Significa que serah executado apenas uma vez durante o programa. Serve
    para detectar o tipo de sistema onde a aplicacao esta rodando.
    */
    static {
        
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            
            osType = "windows";
            
        }
        else {
            
            osType = "unix";
            
        }
        
    }
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Habilita envio de codigos de controle ansi mesmo se o sistema for 
     * Windows.
     */
    public static void ansiCodesEnable() {
        
        osType = "unix";
        
    }//HansiCodesEnable()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Desabilita envio de codigos de controle ansi mesmo se o sisetma for 
     * "unix like".
     */
    public static void ansiCodesDisable() {
        
        osType = "windows";
        
    }//ansiCodesDisable()
    
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna se o sistema eh um Windows.
     * 
     * @return true se for um sistema da Microsoft. false se nao.
     */
    public static boolean isWindowsOS() {
        
        return osType.equals("windows");
        
    }//isWindowsOS() 
    
    /*[04]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Envia um ansi escape code para o terminal caso o sistema nao seja 
     * Windows.
     * 
     * @param command O comando ANSI.
     */
    private static void ansiCode(final String command) {
        
        if (isWindowsOS()) return;
        
        System.out.print(CSI + command);
        
    }//ansiCode()
    
    /*[05]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Envia um comando ANSI para o terminal compativel com padrao UNIX, caso
     * o comando seja um valor numerico. 
     * 
     * @param command Quando o comando eh um valor numerico.
     */
    private static void ansiCode(final int command) {
        
        if (isWindowsOS()) return;
        
        System.out.print(CSI + command + "m");
        
    }//ansiCode
    
    /*[06]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Comando para esconder o cursor no terminal.
     */
    public static void hideCursor() {
        
        ansiCode("?25l");
        
    }//hideCursor()
    
    /*[07]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Comando para voltar a exibir o cursor no terminal.
     */
    public static void showCursor() {
        
        ansiCode("?25h");
        
    }//showCursor()
    
    /*[08]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna o cursor para o inicio da linha onde estiver posicionado.
     */
    public static void resetCursorPosition() {
        
        ansiCode("1000D");
        
    }//resetCursorPosition()
    
    /*[09]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Imprime uma string no terminal e mantem o cursor na mesma linha.
     * 
     * @param s A String a ser impressa no terminal.
     */
    public static void print(final String s) {
        
        System.out.print(s);
        
        /*
        "Avisa" para um objeto ProgressBar que o cursor nao estah mais onde
        ele deixou quando atualizou a barra de progresso pela ultima vez. Isto
        obriga o objeto ProgressBar a redesenhar a barra de progresso na linha
        onde o cursor estiver agora.
        */
        cursorMoved = true;
        
    }//print()
    
    /*[10]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Imprime uma string no terminal e salta para a proxima linha.
     * 
     * @param s A string a ser impressa no terminal.
     */
    public static void println(final String s) {
        
        System.out.println(s);
        
        /*
        "Avisa" para um objeto ProgressBar que o cursor nao estah mais onde
        ele deixou quando atualizou a barra de progresso pela ultima vez. Isto
        obriga o objeto ProgressBar a redesenhar a barra de progresso na linha
        onde o cursor estiver agora.
        */
        cursorMoved = true;
        
    }//println()
    
    /*[11]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Depois da execucao deste metodo, qualquer customizacao de cor ou de 
     * efeitos no texto serah desligada.
     */
    public static void resetColorsAndDecorations() {
        
       ansiCode(0);
         
    }//resetColorsAndDecorations()
    
    /*[12]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Restaura o estado original do terminal. Cores e reexibe o cursor caso
     * este tenha sido ocultado.
     */
    public static void restoreTerminalDefaults() {
        
        resetColorsAndDecorations();
        showCursor();
        
    }//restoreTerminalDefaults()
    
    /*[13]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Ajusta a cor do texto no terminal. Desde que nao seja terminal Windows. A
     * cor configurada aqui ira perdurar ate que outra seja definida ou o metodo
     * resetColorsAndDecorations() executado.
     * 
     * @param color Uma cor que deve ser um valor entre 0 e 7, -1 reseta as 
     * customizacoes de cores e efeitos de texto.
     */
    public static void setColor(final int color) {
        
        if (color == NONE) resetColorsAndDecorations();
        
        ansiCode(30 + color);
        
    }//setColor()
    
    /*[14]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Como o metodo setcolor(), soh que para as cores de fundo do terminal.
     * 
     * @param color A cor do fundo do terminal.
     */
    public static void setBgColor(final int color) {
        
        if (color == NONE) resetColorsAndDecorations();
        
        ansiCode(40 + color);
        
    }//setBgColor()
    
    /*[15]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Imprime uma string (sem avancar a linha) no terminal com uma cor 
     * especificada. Apos a execucao deste metodo as customizacoes de cor e 
     * decoracao de texto sao resetadas.
     * 
     * @param s A string.
     * 
     * @param color A cor da string.
     */
    public static void printc(final String s, final int color) {
        
        setColor(color);
        print(s);
        resetColorsAndDecorations();
        
    }//printc()
    
    /*[16]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Como o metodo printc, mas avanca o cursor uma linha apos imprimir a 
     * string no terminal.
     * 
     * @param s A string.
     * 
     * @param color A cor da string.
     */
    public static void printlnc(final String s, final int color) {
                  
        setColor(color);
        println(s);
        resetColorsAndDecorations();
        
    }//printlnc()
    
    /*[17]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Ajusta efeitos do texto no terminal.
     * 
     * @param decoration 1 = negrito 4 = sublinhado 7 = invertido Estes valores
     * podem ser somados para sobrepor os efeitos.
     */
    public static void setDecoration(final int decoration) {
           
        switch (decoration) {
            
            case NONE:
                resetColorsAndDecorations();
                break;
                
            case (REVERSE + UNDERLINE + BOLD):
                ansiCode(REVERSE);
                
            case (UNDERLINE + BOLD):
                ansiCode(UNDERLINE);
                
            case BOLD:
                ansiCode(BOLD);
                break;
                
            case (REVERSE + UNDERLINE):
                ansiCode(REVERSE);
                
            case UNDERLINE:
                ansiCode(UNDERLINE); 
                break;
                
            case REVERSE: 
                ansiCode(REVERSE);
                break;
                
        }//switch        
        
    }//setDecoration()   
    
   /*[18]-----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Para destacar uma substring em uma string. Imprime s1, s2 e s3 decorando 
     * s2 em negrito, sublinhado e cor padrão do terminal. 
     * 
     * @param s1 Parte da String que será impressa em cor color.
     * @param s2 Parte da String decorada.
     * @param s3 Parte da String que será impressa em cor color.
     * 
     * @param color Cor do texto.
     */
    public static void decoratedPrintlnc(
        final String s1, final String s2, final String s3, final int color
    ) {
        printc(s1, color);
        setDecoration(BOLD + UNDERLINE);
        print(s2);
        resetColorsAndDecorations();
        printlnc(s3, color);
    }//decoratedPrintlnc()
           
}//classe Tui