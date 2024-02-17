package backupcc.tui;

import static backupcc.strings.Util.pad;
import static backupcc.strings.Util.repeatChar;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Um objeto desta classe permite exibir no terminal um box com um titulo 
 * configuravel, varias linhas de texto e uma lista de opcoes para o usuario
 * escolher. As opcoes devem ser selecionadas com uma unica tecla de letra 
 * (pode ser maiuscula ou minuscula) e em seguida ENTER.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (20 de agosto de 2022)
 * @version 1.0
 */
public final class OptionBox {
    
    /*
    Caracteres especiais para desenhar a moldura do box
    */
    private static final char FULL_BLOCK = '\u2588';
    private static final char UPPER_HALF_BLOCK = '\u2580';
    private static final char CENTER_HALF_BLOCK = '\u25A0';
    private static final char LOWER_HALF_BLOCK = '\u2584';
    
    /*
    Uma lista de mensagens que serao exibidas cada uma em sua linha e 
    centralizadas no box. Se um item deste array terminar com newline, uma
    linha em branco sera criada abaixo dele.
    */
    private final String[] msgs;
    
    /*
    Uma lista com as opcoes que podem ser selecionadas.
    */
    private final String[] options;
    
    /*
    Uma regexp que soh aceita os caracteres que selecionam alguma das opcoes
    */
    private final String shortcuts;
    
    /*
    A cor da moldura do box
    */
    private final int frameColor;
    
    /*
    A cor de texto das mensagens
    */
    private final int msgsColor;
    
    /*
    Um objeto para ler entradas do teclado
    */
    private final Scanner scanner;
    
    /*
    A largura - em caracteres - do box, sem contar com a moldura
    */
    private final int innerBoxWidth;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param msgs Uma lista de linhas de texto a serem exibidas no box. Quando
     * uma destas linhas terminar em newline, uma linha em branco abaixo dela 
     * sera criada no box. Se passada como null uma excecao eh lancada.
     * 
     * @param options Uma lista de opcoes a serem escolhidas pelo usuario. 
     * Opcoes do tipo Aborta, Repete, Termina, Sim, Nao ... etc... Cada opcao
     * devera ser selecionada com uma letra do teclado. Tanto faz se teclando
     * esta letra em maiuscula ou minuscula. Se passada como null uma excecao eh
     * lancada.
     * 
     * @param shortcuts Uma string contendo as letras que selecionam, 
     * respectivamente as opcoes passadas no parametro options, seguindo a
     * mesma ordem destes paramentros no array options. Exemplo: se options =
     * {"Aborta", "Repete"}, entao shortcuts = "ar" faria com que A ou a 
     * selecionasse Aborta e R ou r selecionasse repete. Na verdade o metodo
     * showBox() desta classe retornaria 'a' caso fosse teclado A ou a para o
     * metodo que o chamou. E este metodo eh que se encarregaria de tomar a acao
     * correspondente a Abortar. showBox() apenas informa qual opcao escolhida 
     * pelo usuario.
     * 
     * @param frameColor A cor da moldura do box. Pode ser entre -1 (nenhuma)
     * e 7.
     * 
     * @param msgsColor A cor do texto das mensagens.Pode ser entre -1 (nenhuma)
     * e 7.
     * 
     * @throws InvalidParameterException Se um parametro invalido for passado a
     * este construtor.
     */
    public OptionBox(
        final String[] msgs,
        final String[] options,
        final String shortcuts,
        final int frameColor,
        final int msgsColor             
    ) throws InvalidParameterException {
        
        /*
        Testa se os parametros sao validos
        */
        if (msgs == null) 
            throw new InvalidParameterException("Messages array is null");
        
        if (options == null) 
            throw new InvalidParameterException("Options array is null");
         
        if (options.length != shortcuts.length()) 
            throw new IllegalArgumentException(
                "Shortcuts do not match options"
            );
              
        if (
            frameColor < Tui.NONE || frameColor > Tui.WHITE 
            || msgsColor < Tui.NONE || msgsColor > Tui.WHITE
        )
            throw new InvalidParameterException("Color out of range");
      
        this.msgs = Arrays.copyOf(msgs, msgs.length);
        this.msgs[0] = ' ' + msgs[0] + ' ';
        
        String lc = shortcuts.toLowerCase();
        String uc = shortcuts.toUpperCase();
        
        this.options = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            this.options[i] = 
                " [" + uc.charAt(i) + "/" + lc.charAt(i) + "] - " + options[i];
        }
        
        String temp = uc.charAt(0) + "|" + lc.charAt(0);
        for (int i = 1; i < shortcuts.length(); i++)
            temp += "|" + uc.charAt(i) + "|" + lc.charAt(i);
        this.shortcuts = temp;
             
        this.frameColor = frameColor;
        this.msgsColor = msgsColor;
        
        scanner = new Scanner(System.in);
        
        /*
        A largura interna do box serah o numero de caracteres da linha com 
        maior numero de caracteres mais 6.
        */
        int maxWidth = this.msgs[0].length();
        
        for (int i = 1; i < msgs.length; i++)
            if (msgs[i].length() > maxWidth) maxWidth = msgs[i].length();
        for (String opt: this.options)
            if (opt.length() > maxWidth) maxWidth = opt.length();
        
        innerBoxWidth = maxWidth + 6;
          
    }//construtor
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /*
    Este metodo funciona como um auxiliar pra o metodo showBox. Formata e
    imprime as linhas de texto dentro de um metodo showBox(). Quando o
    parametro s eh passado terminando com um newline, abaixo de s serah 
    impressa uma linha em branco no OptionBox
    */
    private void writeln(String s, final int color) {
        
        String vblock = "" + FULL_BLOCK;
        int sl = s.length();
        boolean newline = (s.charAt(sl -1) == '\n');
        
        if (newline) s = s.substring(0, sl -1);
        
        Tui.printc(vblock, frameColor);
        Tui.printc(pad(' ', s, innerBoxWidth), color);
        Tui.printlnc(vblock, frameColor);
        
        if (!newline) return;
        
        Tui.printc(vblock, frameColor);
        Tui.printc(
            pad(' ', pad(' ', "", innerBoxWidth), innerBoxWidth), color
        );
        Tui.printlnc(vblock, frameColor);
        
    }//writeln()
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Exibe o box no terminal e solicita que o usuario tecle a opcao escolhida.
     * 
     * @return O caractere (em MINUSCULA) que foi teclado para selecionar uma
     * das opcoes.
     */
    public char showBox() {
        
        Tui.println(" ");
        
        writeln(pad(UPPER_HALF_BLOCK, msgs[0], innerBoxWidth), frameColor);
          
        for (int i = 1; i < msgs.length; i++) writeln(msgs[i], msgsColor);
        
        writeln(repeatChar(CENTER_HALF_BLOCK, innerBoxWidth), frameColor);
        
        for (String option: options)
            writeln(
                option + repeatChar(' ', innerBoxWidth - option.length()),
                frameColor
            );
        
         
        writeln(repeatChar(LOWER_HALF_BLOCK, innerBoxWidth), frameColor);
          
        char c = 0; boolean inputMismatch;
        
        Tui.println(" ");
        
        do {
            
            inputMismatch = false;
            
            Tui.print("> ");

            try {
    
                c = scanner.next(shortcuts).toLowerCase().charAt(0);
                
            }
            catch (InputMismatchException e) {
                
                Tui.printlnc("Op\u00E7\u00E3o inv\u00E1lida!", Tui.RED);
                
                inputMismatch = true;
                
                scanner.next();//limpa o buffer de teclado

            }
            
        }
        while (inputMismatch);
        
        return c;
        
    }//showBox()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Um OptionBox para abortar o programa em caso de erro fatal.
     * 
     * @param msgs As mensagens do box.
     * 
     */
    public static void abortBox(final String[] msgs) {
        
        String[] aux = new String[msgs.length + 1]; aux[0] = "Erro Fatal!";
        
        System.arraycopy(msgs, 0, aux, 1, msgs.length);
         
        String[] options = {
            "Qualquer tecla e <ENTER> para encerrar."
        };

        OptionBox o = new OptionBox(aux, options, ".", Tui.RED, Tui.WHITE);
        
        o.showBox();
        
        System.exit(0);

    }//abortBox()
      
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Um OptionBox para mensagens de aviso.
     * 
     * @param msgs As mensagens do box.
     * 
     */
    public static void warningBox(final String[] msgs) {
        
        String[] aux = new String[msgs.length + 1]; aux[0] = "Aviso!";
        
        System.arraycopy(msgs, 0, aux, 1, msgs.length);
          
        String[] options = {
            "Prosseguir mesmo assim",
            "Abortar"
        };

        OptionBox o = new OptionBox(aux, options, "pa", Tui.YELLOW, Tui.WHITE);
        
        if (o.showBox() == 'a') System.exit(0);

    }//warningBox()
    
    /*[04]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static boolean retryBox(final String[] msgs) {
        
        String[] aux = new String[msgs.length + 1]; aux[0] = "Falha!";
        
        System.arraycopy(msgs, 0, aux, 1, msgs.length);
          
        String[] options = {
            "Pular",
            "Tentar novamente"
        };

        OptionBox o = new OptionBox(aux, options, "pt", Tui.YELLOW, Tui.WHITE);
        
        return (o.showBox() == 't'); 
        
    }//retryBox()
      
    /*[05]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Um OptionBox para aviso de termino do programa.
     */
    public static void theEndBox() {
        
        String[] msgs = {
            "THE END",
            "Deu tudo certo desta vez!\n",
            backupcc.datetime.Util.getElapsedTimeTo(
                backupcc.datetime.Util.BACKUP_START_TIME
            ) + "\n",
            "Pressione qualquer tecla e <ENTER> para encerrar"
        };

          
        String[] options = {
            "Qualquer tecla e <ENTER>"
        };

        OptionBox o = new OptionBox(msgs, options, ".", Tui.GREEN, Tui.WHITE);
        
        if (o.showBox() == 'a') System.exit(0);

    }//theEndBox()
         
}//classe OptionBox
