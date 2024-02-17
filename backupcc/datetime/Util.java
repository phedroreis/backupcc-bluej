package backupcc.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Métodos utilitários estáticos para data e hora.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (1 de setembro de 2022)
 * @version 1.0
 */
public class Util {
    
    /**
     * Localiza string com o padrão de apresentação compacto de data e hora 
     * usado por esta classe.
     */
    private final static Pattern DATE_TIME = 
        Pattern.compile(
            "(\\d{2})-(\\d{2})-(\\d{4})\\(((\\d{2})h(\\d{2})m(\\d{2})s)"
        );
    
    private final static String[] MESES = {
        
        "janeiro", "fevereiro", "mar\u00E7o", "abril", "maio", "junho",
        "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"
    };
    
    /**
     * Data e hora em que o processo de backup foi iniciado. 
     */
    private static LocalDateTime backupStartTime;
    
    /**
     * Armazena um instante qualquer no tempo, tipicamente no início de 
     * algum processo, para quando se quer determinar quanto tempo levou
     * tal processo.
     */
    private static LocalDateTime milestoneTime;
    
    /**
     * Constantes para serem usadas nas chamadas do método
     * {@link #getElapsedTimeTo(int) }, determinando se o intervalo de tempo
     * retornado é desde o início do processo de backup ou desde o último
     * milestoneTime definido.
     */
    public static final int BACKUP_START_TIME = 0;
    
    public static final int MILESTONE = 1;
    
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a data e hora atuais formatada como dd-MM-yyyy(HH'h'mm'm'ss's').
     * 
     * @return Data e hora formatadas compactamente.
     */
    public static String now() {
          LocalDateTime localDateTime = LocalDateTime.now();
  
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern("dd-MM-yyyy(HH.mm;ss,)");
        
        return 
            localDateTime.format(formatter).replace('.','h').replace(';','m').
                replace(',','s');

    }//now()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Converte do formato compacto para o formato por extenso.
     * 
     * @param datetime Data e hora no formato compacto.
     * 
     * @return Data e hora por extenso.
     */
    public static String dateTime(final String datetime) {
        
        Matcher m = DATE_TIME.matcher(datetime);
                     
        m.find();
        
        return String.format(
            "%s de %s de %s \u00E0s %s", 
            m.group(1),
            MESES[Integer.valueOf(m.group(2)) - 1],
            m.group(3),
            m.group(4)                    
        );             
                   
    }//dateTime()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Executado no método {@link backupcc.incremental.Incremental#init() init }
     * marca o instante do início do processo de backup.
     */
    public static void setBackupStartTime() {
        
        backupStartTime = LocalDateTime.now();
        
    }//setBackupStartTime()
    
    /*[04]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Guarda o instante de tempo em que foi chamado. Para que o método
     * {@link #getElapsedTimeTo(int) }, quando executado, determine o intervalo
     * de tempo entre as chamadas destes dois métodos.
     */
    public static void setMilestoneTime() {
        
        milestoneTime = LocalDateTime.now();
        
    }//setMilestoneTime()
    
    /*[05]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna o intervalo de tempo desde o início do processo de backup, ou
     * da definição do último milestoneTime setado pela chamada do método
     * {@link #setMilestoneTime() }, de acordo com o argumento passado ao
     * parãmetro start.
     * 
     * @param start Se o intervalo é referente ao início do processo de backup
     * ou ao último milestoneTime definido.
     * 
     * @return O intervalo de tempo formatado. 
     */
    public static String getElapsedTimeTo(final int start) {
        
        LocalDateTime now = LocalDateTime.now();
        
        long elapsed = 0;
            
        switch (start) {
            
            case BACKUP_START_TIME :  
                
                elapsed = backupStartTime.until(now, ChronoUnit.SECONDS);
                break;
            
            case MILESTONE :
                
                elapsed = milestoneTime.until(now, ChronoUnit.SECONDS);
           
        }//switch
          
        long h = elapsed / 3600;
        long m = (elapsed % 3600) / 60;
        long s = ((elapsed % 3600) % 60);
        
        return String.format("Tempo decorrido: %02dh%02dm%02ds", h, m, s);
        
    }//getElapsedTimeTo()
            
}//classe Util