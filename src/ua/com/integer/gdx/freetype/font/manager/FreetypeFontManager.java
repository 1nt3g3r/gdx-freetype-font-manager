/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package ua.com.integer.gdx.freetype.font.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Helper class to manage multiple FreeType (.ttf) fonts.
 * 
 * By default you should put all your .ttf files into android/assets/ttf-fonts folder.
 * 
 * Don't forget to call dispose() when you don't need instance of this class more.
 *
 * @author 1nt3g3r
 */
public class FreetypeFontManager implements Disposable {
    private String defaultChars = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯяabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>’";
    private String fontsFolder = "ttf-fonts/";
    private StringBuilder tmpStringBuilder = new StringBuilder();
    private String defaultFontName = "Roboto-Condensed";
    
    private ObjectMap<String, FreeTypeFontGenerator> generators = new ObjectMap<String, FreeTypeFontGenerator>();
    private ObjectMap<String, BitmapFont> fonts = new ObjectMap<String, BitmapFont>();

    /**
     * Set folder where this class will look .ttf files
     */
    public void setFontsFolder(String fontsFolder) {
        this.fontsFolder = fontsFolder;
    }
    
    /**
     * Return folder where this class looks .ttf files
     */
    public String getFontsFolder() {
		return fontsFolder;
	}
    
    /**
     * @param defaultChars characters that will be used for font generation
     */
    public void setDefaultChars(String defaultChars) {
		this.defaultChars = defaultChars;
	}
    
    /**
     * @return default characters that will be in generated fonts
     */
    public String getDefaultChars() {
		return defaultChars;
	}
    
    /**
     * @param defaultFontName if you call loadFont(size) or getFont(size), this defaultFontName will be used.
     * 
     * It can be useful when you're using only one font.
     */
    public void setDefaultFontName(String defaultFontName) {
		this.defaultFontName = defaultFontName;
	}
    
    /**
     * See setDefaultFontName
     */
    public String getDefaultFontName() {
		return defaultFontName;
	}
    
    /**
     * Load font using defaultFontName. Equal to: loadFont(getDefaultFontName(), size)
     */
    public void loadFont(int size) {
    	loadFont(defaultFontName, size);
    }
    
    /**
     * Load font with given font name and size
     * @param name name of .ttf file (without .ttf extension)
     */
    public void loadFont(String name, int size) {
        loadFont(name, size, new FreeTypeFontGenerator.FreeTypeFontParameter());
    }

    /**
     * Load font with given font name, size, and additional parameters
     * @param name name of .ttf file (without .ttf extension)
     * @param parameter specific font effects (shadow, border, etc). Look {@link FreeTypeFontParameter} to get more
     */
    public void loadFont(String name, int size, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        loadFont(name, size, defaultChars, parameter);
    }
    
    /**
     * Load font with given name, size and specific chars
     */
    public void loadFont(String name, int size, String chars) {
    	loadFont(name, size, chars, new FreeTypeFontGenerator.FreeTypeFontParameter());
    }

    /**
     * Load font with given name, size, chars and specific font effects
     */
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
    
    /**
     * Returns default font with given size. Equal to: getFont(getDefaultFontName(), size)
     */
    public BitmapFont getFont(int fontSize) {
    	return getFont(defaultFontName, fontSize);
    }

    /**
     * Return font by given font name and font size. If font isn't loaded, it will be loaded by calling 
     * loadFont(fontName, size)
     */
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

    /**
     * Call this method to avoid memory leaks. Typically you should do it in your dispose() method of your game.
     */
    @Override
    public void dispose() {
        for(BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();

        unloadAllGenerators();
    }
    
    /**
     * Print debug info. How many generators and fonts are loaded, and their names. Can be useful for debugging.
     */
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
    
    /**
     * Unloads generator with given name. It can be useful if you don't need this generator more (you will not 
     * create more fonts using this generator).
     */
    public void unloadGenerator(String generatorName) {
    	if (generators.containsKey(generatorName)) {
    		generators.get(generatorName).dispose();
    		generators.remove(generatorName);
    	}
    }
    
    /**
     * Unload all generators. See unloadGenerator(String generatorName);
     */
    public void unloadAllGenerators() {
    	for(FreeTypeFontGenerator generator : generators.values()) {
    		generator.dispose();
    	}
    	generators.clear();
    }
    
    /**
     * Unloads font with given name. It can be useful if you don't need this font.
     */
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
