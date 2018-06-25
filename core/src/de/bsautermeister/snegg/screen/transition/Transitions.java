package de.bsautermeister.snegg.screen.transition;

import com.badlogic.gdx.math.Interpolation;

public final class Transitions {
    public static final ScreenTransition FADE = new FadeScreenTransition(0.75f);
    public static final ScreenTransition SCLAE = new ScaleScreenTransition(0.75f, true, Interpolation.elastic);

    private Transitions() {
    }
}
