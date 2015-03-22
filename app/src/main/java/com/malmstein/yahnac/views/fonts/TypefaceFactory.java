package com.malmstein.yahnac.views.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.malmstein.yahnac.R;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

 public class TypefaceFactory {

    private static final Map<FontType, SoftReference<Typeface>> FONT_CACHE = new HashMap<FontType, SoftReference<Typeface>>();
    private static final int INVALID_FONT_ID = -1;
    private static final Typeface DEFAULT_TYPEFACE = null;

    public Typeface createFrom(Context context, FontType fontType) {
        if (fontType == null) {
            return DEFAULT_TYPEFACE;
        }
        return getTypeface(context, fontType);
    }

    public Typeface createFrom(Context context, AttributeSet attrs) {
        int fontId = getFontId(context, attrs);
        if (!isValidId(fontId)) {
            return DEFAULT_TYPEFACE;
        }
        FontType fontType = getFontType(fontId);
        return getTypeface(context, fontType);
    }

    private int getFontId(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextAppearance);
        if (typedArray == null) {
            return INVALID_FONT_ID;
        }

        try {
            return typedArray.getInt(R.styleable.CustomTextAppearance_fontAsset, INVALID_FONT_ID);
        } finally {
            typedArray.recycle();
        }
    }

    private boolean isValidId(int fontId) {
        return fontId > INVALID_FONT_ID && fontId < FontType.values().length;
    }

    private FontType getFontType(int fontId) {
        return FontType.values()[fontId];
    }

    private Typeface getTypeface(Context context, FontType fontType) {
        synchronized (FONT_CACHE) {
            if (fontExistsInCache(fontType)) {
                return getCachedTypeface(fontType);
            }

            Typeface typeface = createTypeface(context, fontType);
            saveFontToCache(fontType, typeface);

            return typeface;
        }
    }

    private boolean fontExistsInCache(FontType fontType) {
        return FONT_CACHE.get(fontType) != null && getCachedTypeface(fontType) != null;
    }

    private Typeface getCachedTypeface(FontType fontType) {
        return FONT_CACHE.get(fontType).get();
    }

    protected Typeface createTypeface(Context context, FontType fontType) {
        return Typeface.createFromAsset(context.getAssets(), fontType.assetUrl);
    }

    private void saveFontToCache(FontType fontType, Typeface typeface) {
        FONT_CACHE.put(fontType, new SoftReference<Typeface>(typeface));
    }

}
