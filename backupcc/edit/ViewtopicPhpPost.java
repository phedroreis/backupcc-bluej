package backupcc.edit;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author "Pedro Reis"
 * @since 1.0 (9 de setembro de 2022)
 * @version 1.0
 */
final class ViewtopicPhpPost extends EditableLink {
    
    private static final Pattern VIEWTOPIC_REGEX = 
        Pattern.compile("href=\"\\S*?(/viewtopic\\.php\\?\\S*?#p(\\d+))\"");
         
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static String getFilename(final int post) {
        
        return backupcc.incremental.Incremental.getFilenameOnMap(post);
        
    }//getFilename()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        String original = matcher.group(1);
                
        int post = Integer.valueOf(matcher.group(2));
        
        String filename = getFilename(post);

        assert(filename != null) : "Can't find the file of this post";

        hashMap.put(original, filename);
        
    }//map()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public Pattern getPattern() {
        
        return VIEWTOPIC_REGEX;
        
    }//getPattern()    
        
}//classe ViewtopicPhpPost

