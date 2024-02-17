/**
 * <p>Contém a classe que faz o parse da linha de comando.</p>
 * 
 * <p>Com argumentos de linha de comando, o usuário pode determinar como será a
 * saída no terminal (padrão Windows ou Unix) e em que páginas os links para 
 * posts serão redirecionados para páginas de tópicos da versão estática.</p>
 * 
 * <p>Esta opção está presente porque na falta do arquivo que cataloga os 
 * tópicos onde cada post foi publicado, o sistema precisa acessar o servidor 
 * para recuperar este tipo de dado. Um acesso para cada link para post que for
 * localizado, tornado o processo muito lento.</p>
 * 
 * <p>No modo default todos os links para posts são redirecionados e o terminal
 * segue o padrão do sistema operacional onde a aplicação estiver rodando.</p>
 * 
 * <p>Também fornece o método getPostLinksRedirectLevel(), que retorna em quais
 * tipos de páginas os links para posts devem ser redirecionados para a cópia 
 * estática.</p>
 */
package backupcc.command;
