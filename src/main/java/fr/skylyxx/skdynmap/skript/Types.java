package fr.skylyxx.skdynmap.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.registrations.Classes;
import fr.skylyxx.skdynmap.utils.types.AreaBuilder;
import fr.skylyxx.skdynmap.utils.types.AreaStyle;
import fr.skylyxx.skdynmap.utils.types.DynmapArea;

import javax.annotation.Nullable;

public class Types {
    static {
        Classes.registerClass(new ClassInfo<>(DynmapArea.class, "dynmaparea")
                .user("(dynmap )?area")
                .name("SkDynmap DynmapArea")
                .description("SkDynmap's DynmapArea object")
                .since("1.0-beta02")
                .parser(new Parser<DynmapArea>() {
                    @Override
                    public String toString(DynmapArea o, int flags) {
                        return o.toString();
                    }

                    @Override
                    public String toVariableNameString(DynmapArea o) {
                        return o.toString();
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
                .user("(dynmap )?area(-| )?style")
                .name("SkDynmap AreaStyle")
                .description("SkDynmap's AreaStyle object")
                .since("1.0-beta02")
                .parser(new Parser<AreaStyle>() {
                    @Override
                    public String toString(AreaStyle o, int flags) {
                        return o.toString();
                    }

                    @Override
                    public String toVariableNameString(AreaStyle o) {
                        return o.toString();
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
                .user("(dynmap )?area(-| )?builder")
                .name("SkDynmap AreaBuilder")
                .description("SkDynmap's AreaBuilder object")
                .since("1.0.1")
                .parser(new Parser<AreaBuilder>() {
                    @Override
                    public String toString(AreaBuilder o, int flags) {
                        return o.toString();
                    }

                    @Override
                    public String toVariableNameString(AreaBuilder o) {
                        return o.toString();
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
