package de.bsautermeister.snegg.screen.transition;

import com.badlogic.gdx.math.Interpolation;

import de.bsautermeister.snegg.model.Direction;

public final class ScreenTransitions {
    public static final ScreenTransition FADE = new FadeScreenTransition(0.75f, Interpolation.fade);
    public static final ScreenTransition SCLAE = new ScaleScreenTransition(0.75f, Interpolation.elastic, true);
    public static final ScreenTransition SLIDE = new SlideScreenTransition(0.75f, Interpolation.circleIn, true, Direction.LEFT);

    private ScreenTransitions() {
    }
}
