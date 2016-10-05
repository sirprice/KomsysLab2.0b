import sun.misc.Signal;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cj on 2016-10-05.
 */
public class CommunicationHub {

    private static final String DELIMITERS = " ";
    private ConcurrentHashMap<String, SignalInvoker> signalList = new ConcurrentHashMap<String, SignalInvoker>();

    private void registrateAllCommands() {
//        signalList.put("quit", new CmdQuit(this));
//        signalList.put("who", new CmdWho(this));
//        signalList.put("nick", new CmdChangeNick(this));
//        signalList.put("help", new CmdHelp(this));
    }

    public void evaluateCommand(String msg) {
        StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITERS);
        String cmd = null;
        String body = null;

        if (tokenizer.hasMoreTokens()) {
            cmd = tokenizer.nextToken();
            System.out.println("EvaluateCommand: Command = "+cmd);
        }
        while (tokenizer.hasMoreTokens()){
            body += tokenizer.nextToken() +" ";
        }
        System.out.println("EvaluateCommand: Body = "+body);

    }
}
