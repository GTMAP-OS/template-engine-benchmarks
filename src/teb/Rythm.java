/*
 * $Copyright: copyright(c) 2007-2011 kuwata-lab.com all rights reserved. $
 * $License: Creative Commons Attribution (CC BY) $
 */
package teb;

import java.io.*;
import java.util.*;

import com.greenlaw110.rythm.*;
import teb.model.Stock;


public class Rythm extends _BenchBase {

    RythmEngine engine;
    private String template = "templates/stocks.rythm.html";
    
    public Rythm() {
        Properties p = new Properties();
        p.put("rythm.cache.enabled", false);
        p.put("rythm.logger.disabled", true);
        //p.put("rythm.tmpDir", "c:\\tmp");
        //p.put("rythm.mode", "dev");
        engine = new RythmEngine(p);
    }
    
    protected void shutdown() {
        engine.shutdown();
    }

    @Override
    public void execute(Writer w0, Writer w1, int ntimes, List<Stock> items) throws Exception {
        String tmpl = template;
        boolean newAPI = Boolean.parseBoolean(System.getProperty("rythm.new", "false"));
        if (newAPI) {
            while (--ntimes >= 0) {
                if (ntimes == 0) {
                    engine.render(w1, tmpl, items);
                } else {
                    engine.render(w0, tmpl, items);
                }
            }
        } else {
            String output;
            while (--ntimes >= 0) {
                output = engine.render(tmpl, items);
                if (ntimes == 0) {
                    w1.write(output);
                } else {
                    w0.write(output);
                }
            }
        }
    }

    @Override
    public void execute(OutputStream o0, OutputStream o1, int ntimes, List<Stock> items) throws Exception {
        String tmpl = template;
        boolean newAPI = Boolean.parseBoolean(System.getProperty("rythm.new", "false"));
        if (newAPI) {
            while (--ntimes >= 0) {
                if (ntimes == 0) {
                    engine.render(o1, tmpl, items);
                } else {
                    engine.render(o0, tmpl, items);
                }
            }
        } else {
            String output;
            Writer w0 = new OutputStreamWriter(o0);
            Writer w1 = new OutputStreamWriter(o1);
            if (_BenchBase.bufferMode.get()) {
                w0 = new BufferedWriter(w0);
                w1 = new BufferedWriter(w1);
            }
            while (--ntimes >= 0) {
                output = engine.render(tmpl, items);
                if (ntimes == 0) {
                    w1.write(output);
                } else {
                    w0.write(output);
                }
            }
        }
    }

    @Override
    protected String execute(int ntimes, List<Stock> items) throws Exception {
        String tmpl = template;
        String output = null;
        while (--ntimes >= 0) {
            output = engine.render(tmpl, items);
        }
        return output;
    }


    public static void main(String[] args) {
        new Rythm().run();
        //String s = com.greenlaw110.rythm.Rythm.render("hool @abc", "sd");
        //System.out.println(s);
    }

}
