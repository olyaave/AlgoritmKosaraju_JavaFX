package sample;

import java.awt.*;
import java.util.ArrayList;

public class LogString{

    private ArrayList<String> list = new ArrayList<String>();



    public void addString(String str)
    {
        list.add(str);
    }
    public void clearLog(){
        list.clear();
    }

    public void updateOnlyLog(String log){
//        Controller controller = new Controller();
//        controller.TextView = TextView;
//        controller.updateLog(log);
    }

    public String updateog(String log){
        addString(log);
        log = "";
        for (int i = 0; i < list.size(); i++){
            log+= list.get(i)+" ";
        }
        return log;
    }

}
