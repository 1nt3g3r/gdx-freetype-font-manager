# Description

Utility class for Gdx Freetype Extension which allows you easily manage multiple .ttf fonts.

LibGDX allows you to use .ttf fonts using gdx-freetype-extension. This extension can generate needed bitmap font from almost any .ttf font in runtime. It's very nice feature and it's simple to use it when you're using only one font. But when you're using multiple .ttf fonts with different size, it's little hard to manage all them. This library allows you avoid this trouble.

# How to use it in your project?

This library is available via JitPack. See more - https://jitpack.io

Edit your top build.gradle file:

**Add JitPack repository**

    allprojects {
        ...
        repositories {
            ...
	          maven { url "https://jitpack.io" }
        }
    }
    
**Add Android dependency:**

    project(":android") {
        ...
        dependencies {
           ...
           compile 'com.github.1nt3g3r:gdx-freetype-font-manager:v1.0'
        }
    }

**Add Core dependency:**

    project(":core") {
        ...
        dependencies {
              ...
    	        compile 'com.github.1nt3g3r:gdx-freetype-font-manager:v1.0'
        }
    }
    
  You see we are using **v1.0** version here. But you can change it. Just look **Releases** section of this repository if you want to use any other version.
  
# How to init and destroy library?
Just create new instance of FreetypeFontManager class and keep it. You will use this instance to generate fonts. Also don't forget to dispose library when you will exit from your game to avoid memory leaks. Example code:
  
      using ua.com.integer.gdx.freetype.font.manager.FreetypeFontManager;
      
      class YourGame extends Game {
         private FreetypeFontManager fontManager;
         
         public void create() {
            fontManager = new FreetypeFontManager(); //Init library
         }
        
         public void dispose() {
            fontManager.dispose(); //Don't forget to dispose library
         }
      }
      
# What's place for .ttf files?
By default you should put all your .ttf files to **android/assets/ttf-fonts** folder. But you can change it:

    ...
    fontManager.setFontsFolder("path/to/my/fonts");
    ...
    
Currently only internal files are supported.

# How to load and use fonts?

There are few ways to load fonts. Simplest way is next:

    ...
    fontManager.loadFont("your-font-name", 16); //16 - font size
    BitmapFont loadedFont = fontManager.getFont("your-font-name", 16); //use just loaded font
    ...
    
FreetypeFontManager supports lazy loading. It mean when you will try access to not loaded font, it will try to load it and return it you. So you can just call **fontManager.getFont("font-name", fontSize)**. But if you call **fontManager.getFont("font-name", fontSize)**, it will load font (if not loaded) with default params. If you want to get custom font, you should call **fontManager.loadFont(...)** with some additional params (described below).

All fonts are cached. It means when you call **getFont** with the same parameters second time, cached instance of font will be returned.

### Characters for generated font

You can get generated characters by call **fontManager.getDefaultChars()** method. By default it included Russian and English lowcase and uppercase letters, digits, and some other symbols (like dot, comma, etc). You can change this characted calling **fontManager.setDefaultChars("YOURCHARS")**. So all future calls **fontManager.loadFont("font-name", fontSize)** will use provided characters.

Also you can pass needed chars directly when you're creating new font. Just call **fontManager.loadFont("font-name", fontSize, "YOURCHARS")**. After it calling **fontManager.getFont("font-name", fontSize)** will return you font with specified characters.
  
### Customizing font

**gdx-freetype-extension** allows you to generate fonts with custom effect - like border, shadow, etc. You should provide FreetypeFontParameter for it. You can read more about font effects here - https://github.com/libgdx/libgdx/wiki/Gdx-freetype

Example how to load custom font with some black border effect:

    ...
    FreetypeFontParameter param = new FreetypeFontParameter();
    param.borderColor = Color.BLACK; //Generated font will have black border 3px width
    param.borderWidth = 3;
    
    fontManager.loadFont("font-name", fontSize, param); //load font using just created parameter
    ... or
    fontManager.loadFont("font-name", fontSize, "CUSTOM_CHARS, param); //load font using just created parameter and custom characters for this font
    
### Using some font as default.

If you're using only one font, you can set it as default by calling **fontManager.setDefaultFontName("default-font")**. After it you can load and get your font without passing font name.

    ...
    fontManager.setDefaultFontName("my-font");
    fontManager.loadFont(16); //it's equal loadFont("my-font", 16), but shorter
    fontManager.getFont(16); //it's equal getFont("my-font", 16), but shorter
    
### Unloading fong generator after load bitmap font

By default generators for all fonts are cached. It mean when you want to create two fonts with the same name, but with different size, only one instance of font generator will be used. But when if you don't need font generator for some font, you can dispose it. It will release some memory:

    ...
    fontGenerator.loadFont("my-font", 16); //load font. We will not load "my-font" fonts more
    fontGenerator.unloadGenerator("my-font");
    ... or you can unload all generators
    fontGenerator.unloadAllGenerators();
    
# If you read all it
Don't hesitate send me any proposals to improve code or documentation. I'm open to discuss.
