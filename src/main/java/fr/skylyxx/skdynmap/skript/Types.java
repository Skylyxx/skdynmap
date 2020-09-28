package fr.skylyxx.skdynmap.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.DynmapArea;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(DynmapArea.class, "dynmaparea")
                .user("(dynmap )?area((-)?marker)?")
                .name("Dynmap Area")
                .description("Represents a Dynmap Area.")
                .since("1.0-beta02")
                .parser(new Parser<DynmapArea>() {

                    @Override
                    public String toString(DynmapArea dynmapArea, int flags) {
                        return "dynmap area named " + dynmapArea.getName();
                    }

                    @Override
                    public String toVariableNameString(DynmapArea dynmapArea) {
                        return dynmapArea.getName();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }

                    @Override
                    public DynmapArea parse(String s, ParseContext context) {
                        return null;
                    }
                })
        );

        Classes.registerClass(new ClassInfo<>(AreaStyle.class, "areastyle")
                .user("(dynmap )?area((-)?style)?")
                .name("Dynmap Style")
                .description("Represents the Dynmap Style object.")
                .since("1.0-beta02")
                .parser(new Parser<AreaStyle>() {

                    @Override
                    public String toString(AreaStyle style, int flags) {
                        return "style " + style.toString();
                    }

                    @Override
                    public String toVariableNameString(AreaStyle style) {
                        return style.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }

                    @Override
                    public AreaStyle parse(String s, ParseContext context) {
                        return null;
                    }
                })
        );
    }

}
