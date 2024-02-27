package backupcc.command;

/**
 * Esta classe é responsável por fazer o parse da linha de comando que pode
 * selecionar o tipo de terminal. 
 * 
 * @author "Pedro Reis"
 * @since 1.0 (10 de setembro de 2022)
 * @version 1.0
 */
public final class CommandLine {
    
    public static boolean listTopics;
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p>Faz o parse dos argumentos passados na linha de comando para a 
     * aplicação.</p>
     * 
     * <p> Sem argumentos na linha de comando, o sistema irá usar o padrão de 
     * terminal de acordo com o sistema operacional onde estiver rodando.</p>
     * 
     * <ul>
     * <li>
     * -w : força que as saídas para o terminal sejam no padrão de um terminal
     * do sistema Windows
     * </li>
     * <li>
     * -u : força que as saídas para o terminal sejam no padrão de sistemas
     * Unix, reconhecendo comandos ANSI
     * </li>
     * <li>
     * -t : gera um arquivo com uma lista de todos os tópicos
     * </li>
     * </ul>
     * 
     * @param commands Linha de comando digitada ao executar o programa.
     */
    public CommandLine(final String[] commands) {
        
        listTopics = false;
        
        for (String command: commands) {

            switch (command) {
                case "-w" : 
                    backupcc.tui.Tui.ansiCodesDisable();
                    break;
                
                case "-u" :
                    backupcc.tui.Tui.ansiCodesEnable();
                    break;
                    
                case "-t" :
                    listTopics = true;
                    break;
                    
                default : 
                    String[] msgs = {
                        commands[0] + " <- flag n\u00e3o reconhecido!\n",
                        "Uso : [terminal] [listar t\u00f3picos]\n",
                        "terminal: -w , -u\n",
                        "-w: terminal modo Windows",
                        "-u: terminal modo Unix",
                        "-t: gera arquivo com a lista de todos os t\u00f3picos"
                    };
                    backupcc.tui.OptionBox.abortBox(msgs);
                
            }//switch
            
        }//for
              
    }//CommandLine()
        
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Informação textual sobre a classe.
     * 
     * @return Informação textual sobre a classe.
     */
    @Override
    public String toString() {
        
        String toString = "Interface de terminal = modo ";
        
        toString += backupcc.tui.Tui.isWindowsOS() ? "Windows" : "Unix";
        
        if (listTopics) 
            toString += "\nSer\u00e1 gerado arquivo com lista de t\u00f3picos";
                
        return toString;
        
    }//toString()
   
}//classe CommandLine
