package backupcc.tui;

import static backupcc.strings.Util.repeatChar;

/**
 * Implementa uma barra de status de progresso no terminal. 
 * 
 * @since 16 de agosto de 2022
 * @version 1.0
 * @author "Pedro Reis"
 */
public final class ProgressBar {
    /**
    * Largura padrao da barra de progresso para ser utilizada por todas as 
    * ProgressBar do programa.
    */
    public static final int LENGTH = 50;
    
    /*
    Total de itens que serao processados
    */
    private final int total;
    
    /*
    Comprimento da barra em caracteres
    */
    private final int barLength;
    
    /*
    Quantos caracteres ja foram impressos na barra de progresso
    */
    private int filled;
    
    /*
    Percentual do processo jah realizado
    */
    private int percent;
    
    private final int foregroundColor;
    
    private final int backgroundColor;
    
    /*
    Se true a barra estah sendo exibida no terminal
    */
    private boolean hidden;
    
    private int d = 9;
    
    private final static String DIGITS = 
        "012345678901234567890123456789012345678901234567890123456789";
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * O construtor da classe.
     * 
     * @param total Total de itens a serem processados.
     * 
     * @param barLength Largura da barra em caracateres.
     * 
     * @param fg A cor de texto para a barra de progresso.
     * 
     * @param bg A cor de fundo para a barra de progresso.
     */
    public ProgressBar(
        final int total,
        final int barLength,
        final int fg,
        final int bg
    ) {
        
        this.total = total;
        this.barLength = barLength;
        foregroundColor = fg;
        backgroundColor = bg;
        filled = 0;
        percent = 0;
        hidden = true;
         
    }//construtor
            
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * O construtor da classe.
     * 
     * @param total Total de itens a serem processados.
     * 
     * @param barLength Largura da barra em caracateres.
     * 
     * @param fg A cor de texto para a barra de progresso.
     * 
     */
    public ProgressBar(
        final int total,
        final int barLength,
        final int fg
        
    ) {
        
        this(total, barLength, fg, Tui.NONE);
        backupcc.datetime.Util.setMilestoneTime();
         
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Exibe ou reexibe a barra de progresso no terminal.
     */
    public void show() {
        
        hidden = false;
        
        Tui.println(" ");
        
        if (Tui.isWindowsOS()) {
              
            System.out.println("0%|" + repeatChar(' ', barLength - 2) + "|100%");
            System.out.println("  v" + repeatChar(' ', barLength - 2) + "v");
            System.out.print("  " + DIGITS.substring(0, filled));
            
        }
        else {
            
            Tui.setColor(foregroundColor);
            if (backgroundColor != Tui.NONE) Tui.setBgColor(backgroundColor);

            Tui.hideCursor();
            System.out.print(
                "(" + percent +"%)  " +
                "0%[" + 
                repeatChar('#', filled) +
                repeatChar(' ', barLength - filled) + 
                "]100%"
            );
            Tui.resetCursorPosition();
        }
        
        Tui.cursorMoved = false;
        
    }//show()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Atualiza a barra de progresso.
     * 
     * @param done Quantos itens ja foram processados.
     */
    public void update(final int done) {
        
        if (hidden) return;
        
        if (Tui.cursorMoved) show();
        
        int f = done * barLength / total;
        int p = done * 100 / total;
        
        if (Tui.isWindowsOS())
            if (f <= filled) return;
        else 
            if (p <= percent) return;
          
        filled = f;
        percent = p;
        
       if (Tui.isWindowsOS()) {
           
            d = ++d % 10;              
              
            System.out.print(d);
     
        }
        else { 
           
            Tui.resetCursorPosition();
          
            System.out.print(
                "(" + percent +"%)  " +
                "0%[" + 
                repeatChar('#', filled) +
                repeatChar(' ', barLength - filled) + 
                "]100%"
            );

        }//if-else
        
        if (done == total) {
            
            System.out.println(
                "  " + backupcc.datetime.Util.getElapsedTimeTo(
                    backupcc.datetime.Util.MILESTONE
                )
            );
            Tui.restoreTerminalDefaults();

        }
             
    }//update()
    
    
}//classe ProgressBar
