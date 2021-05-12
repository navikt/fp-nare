package no.nav.fpsak.nare.doc.doclet;

import java.util.ArrayList;
import java.util.List;

import io.github.swagger2markup.markup.builder.MarkupDocBuilder;

class RegelflytModell implements MarkupOutput {

    private final List<Entry> entries = new ArrayList<>();

    static record Entry(String name, String simpleName) {
        Entry(String name) {
             this(name, name.replaceAll("\\.",  "_").toLowerCase());
        }
    }

    @Override
    public void apply(int sectionLevel, MarkupDocBuilder doc) {
        entries.forEach(entry -> {
            doc.textLine("++++");

            doc.textLine(
                "<script>\n" +
                    "function func_" + entry.simpleName + "() {" +
                    "   var regelVindu = window.open('', 'regelVindu');" +
                    "   regelVindu.document.write(\"<h1>" + entry.name + "</h1>\");" +
                    "   regelVindu.document.write(\"<script type='text/javascript' src='resources/jquery.js' ><\\/script>\");" +
                    "   regelVindu.document.write(\"<script type='text/javascript' src='resources/vis.js' ><\\/script>\");" +
                    "   regelVindu.document.write(\"<script type='text/javascript' src='resources/fpsysdok.js'><\\/script>\");" +
                    "   regelVindu.document.write(\"<link href='resources/fpsysdok.css' rel='stylesheet' type='text/css' />\");" +
                    "   regelVindu.document.write(\"<link href='resources/qtip.css' rel='stylesheet' type='text/css' />\");" +
                    "   regelVindu.document.write(\"<link href='resources/vis.css' rel='stylesheet' type='text/css' />\");" +
                    "   regelVindu.document.write(\"<div id='regelgraf' style='width:100vw;height:100vh'></div>\");" +
                    "   regelVindu.document.write(\"<script type='text/javascript'>\");" +
                    "        regelVindu.document.write(\"var medlemskap = document.getElementById('regelgraf');\");" +
                    "        regelVindu.document.write(\"loadJSON('../" + entry.name + ".json', regelgraf);\");" +
                    "   regelVindu.document.write(\"<\\/script>\");" +
                    "   }" +
                    "  </script>" +
                    "<button onclick='func_" + entry.simpleName + "()'>Regelgraf</button>");

            doc.textLine("++++");

        });
    }

    public void leggTil(String name) {
        this.entries.add(new Entry(name));
    }
}
