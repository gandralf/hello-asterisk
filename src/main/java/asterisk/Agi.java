package asterisk;

import org.asteriskjava.Cli;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

public class Agi extends BaseAgiScript {
    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        answer();
        exec("Playback", "tt-monkeys");
        hangup();
    }


    public static void main(String[] args) throws Exception {
        Cli.main(new String[] { Agi.class.getCanonicalName() } );
    }
}
