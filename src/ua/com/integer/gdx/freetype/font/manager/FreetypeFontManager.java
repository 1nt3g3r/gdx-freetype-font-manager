package ua.com.integer.gdx.freetype.font.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class FreetypeFontManager implements Disposable {
    private String defaultChars = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯяabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>’";
    private String fontsFolder = "ttf-fonts/";
    private StringBuilder tmpStringBuilder = new StringBuilder();
    
    private ObjectMap<String, FreeTypeFontGenerator> generators = new ObjectMap<String, FreeTypeFontGenerator>();
    private ObjectMap<String, BitmapFont> fonts = new ObjectMap<String, BitmapFont>();

    public void setFontsFolder(String fontsFolder) {
        this.fontsFolder = fontsFolder;
    }
    
    public void setDefaultChars(String defaultChars) {
		this.defaultChars = defaultChars;
	}

    public void loadFont(String name, int size) {
        loadFont(name, size, new FreeTypeFontGenerator.FreeTypeFontParameter());
    }

    public void loadFont(String name, int size, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        loadFont(name, size, defaultChars, parameter);
    }

    public void loadFont(String fontName, int fontSize, String chars, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        String fullFontName = getFullFontName(fontName, fontSize);

        if (!fonts.containsKey(fullFontName)) {
            FreeTypeFontGenerator generator = getGenerator(fontName, chars);

            parameter.size = fontSize;
            parameter.characters = chars;
            BitmapFont font = generator.generateFont(parameter);
            fonts.put(fullFontName, font);
        }
    }

    public BitmapFont getFont(String fontName, int fontSize) {
        String fullFontName = getFullFontName(fontName, fontSize);
        if (!fonts.containsKey(fullFontName)) {
            loadFont(fontName, fontSize);
        }

        return fonts.get(fullFontName);
    }

    private FreeTypeFontGenerator getGenerator(String name, String chars) {
        if (!generators.containsKey(name)) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontsFolder + name + ".ttf"));
            generators.put(name, generator);
        }

        return generators.get(name);
    }

    @Override
    public void dispose() {
        for(BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();

        unloadAllGenerators();
    }
    
    public void printInfo() {
    	System.out.println("Loaded generator count: " + generators.size + ". Loaded font generator names:");
    	for(String generatorName : generators.keys()) {
    		System.out.println(generatorName);
    	}
    	
    	System.out.println("Loaded font count: " + fonts.size + ". Loaded font names:");
    	for(String fontName : fonts.keys()) {
    		System.out.println(fontName);
    	}
    }
    
    public void unloadGenerator(String generatorName) {
    	if (generators.containsKey(generatorName)) {
    		generators.get(generatorName).dispose();
    		generators.remove(generatorName);
    	}
    }
    
    public void unloadAllGenerators() {
    	for(FreeTypeFontGenerator generator : generators.values()) {
    		generator.dispose();
    	}
    	generators.clear();
    }
    
    public void unloadFont(String fontName, int fontSize) {
    	String fullFontName = getFullFontName(fontName, fontSize);

    	if (fonts.containsKey(fullFontName)) {
    		fonts.get(fullFontName).dispose();
    		fonts.remove(fullFontName);
    	}
    }
    
    private String getFullFontName(String fontName, int fontSize) {
    	tmpStringBuilder.setLength(0);
    	return tmpStringBuilder.append(fontName).append(fontSize).toString();
    }
}
