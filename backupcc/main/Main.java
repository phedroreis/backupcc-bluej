package backupcc.main;

import java.io.IOException;

/**
 * Ponto de entrada para execucao da aplicacao.
 * 
 * @since 1.0 (14 de agosto de 2022)
 * @author "Pedro Reis"
 * @version 1.1
 */
public final class Main {
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) {
        
        /* Processa os parametros de linha de comando */
        backupcc.command.CommandLine commandLine = 
            new backupcc.command.CommandLine(args);
        
        backupcc.tui.Tui.println(commandLine.toString() + '\n');
        
        /* Inicializa o sistema para um backup incremental */
        backupcc.incremental.Incremental.init();
        
        backupcc.tui.Tui.println(
            backupcc.incremental.Incremental.lastBackupDatetime() + '\n'
        );
       
        /*
        Marca o instante em que se inicia o processo de backup
        */
        backupcc.datetime.Util.setBackupStartTime();
        
        /*
        Baixa as páginas do fórum
        */
        backupcc.fetch.FetchPages.downloadPages();
        
        /*
        Baixa arquivos estáticos do servidor do fórum
        */
        backupcc.fetch.FetchStaticFiles.fetchStaticFiles();
         
        /*
        Edita as páginas baixadas, produzindo cópias estáticas destas páginas
        */
        backupcc.edit.EditableLink.editFiles();
          
        /* Grava arquivos de finalizacao */
        try {
            
            backupcc.incremental.Incremental.finish();
            
        }
        catch (IOException e) {
            
            String[] msgs = {
                e.getMessage() + '\n',
                "Falha ao gravar arquivos de dados do backup\n",
                "Este backup n\u00E3o deve ser utilizado\n",
                "Apague a pasta " + backupcc.file.Util.RAW_PAGES + " e",
                "renomeie a pasta de algum backup anterior para " + 
                backupcc.file.Util.RAW_PAGES + '\n',
                "Em seguida execute novo bakcup incremental"
            };
            
            backupcc.tui.OptionBox.abortBox(msgs);
  
        }//try-catch
        
        backupcc.tui.OptionBox.theEndBox();
        
    }//main()
    
}//classe Main
