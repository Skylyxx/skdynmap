package fr.skylyxx.skdynmap.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import fr.skylyxx.skdynmap.utils.AreaBuilder;
import fr.skylyxx.skdynmap.utils.AreaStyle;
import fr.skylyxx.skdynmap.utils.DynmapArea;

public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(DynmapArea.class, "dynmaparea")
                .user("(dynmap )?area((-)?marker)?")
                .name("Dynmap Area")
                .description("Represents a SkDynmap Area.")
                .since("1.0-beta02")
                .parser(new Parser<DynmapArea>() {

                    @Override
                    public String toString(DynmapArea dynmapArea, int flags) {
                        return dynmapArea.toString();
                    }

                    @Override
                    public String toVariableNameString(DynmapArea dynmapArea) {
                        return dynmapArea.toString();
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
                .name("Dynmap Area Style")
                .description("Represents the SkDynmap AreaStyle object.")
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

        Classes.registerClass(new ClassInfo<>(AreaBuilder.class, "areabuilder")
                .user("(dynmap )?area(-)?builder?")
                .name("Dynmap Area Builder")
                .description("Represents the SkDynmap AreaBuilder object.")
                .since("1.0.1")
                .parser(new Parser<AreaBuilder>() {
                    @Override
                    public String toString(AreaBuilder areaBuilder, int flags) {
                        return areaBuilder.toString();
                    }

                    @Override
                    public String toVariableNameString(AreaBuilder areaBuilder) {
                        return areaBuilder.toString();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return ".+";
                    }

                    @Override
                    public AreaBuilder parse(String s, ParseContext context) {
                        return null;
                    }
                })
        );
    }

}
