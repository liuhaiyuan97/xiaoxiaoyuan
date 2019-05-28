package com.google.blockly.android.codegen;

/**
 * Defines the core language file to be used in code generation. To be used by the generator定义要在代码生成中使用的核心语言文件。供生成器使用
 * Blockly needs to know the path to the file and the object that has the generator.Blockly需要知道文件的路径和具有生成器的对象。
 * functions. For example: {"javascript_compressed.js", "Blockly.JavaScript"}.
 */
// TODO (#378): Add the list of reserved keywords to the language definition.
public class LanguageDefinition {
    /**
     * Standard definition for the JavaScript language generator.JavaScript语言生成器的标准定义。
     */
    public final static LanguageDefinition JAVASCRIPT_LANGUAGE_DEFINITION
            = new LanguageDefinition("javascript_compressed.js", "Blockly.JavaScript");

    /**
     * The path to the language generation file relative to相对于语言生成文件的路径
     * file:///android_assets/background_compiler.html.
     */
    public final String mLanguageFilename;
    /**
     * The Generator object that is defined by the file and should be called to perform the code
     * generation, such as "Blockly.JavaScript".Generator对象，它由文件定义，应该被调用来执行代码生成，例如“Blockly.JavaScript”
     */
    public final String mGeneratorRef;

    /**
     * Create a language definition with the given filename and generator object.用给定的文件名和生成器对象创建语言定义。
     *
     * @param filename The path to the language generator file relative to有关语言生成器文件的路径
     *                 file:///android_assets/background_compiler.html.
     * @param generatorObject The generator object provided by the file, such as文件提供的生成器对象，如
     *                  "Blockly.JavaScript"
     */
    public LanguageDefinition(String filename, String generatorObject) {
        mLanguageFilename = filename;
        mGeneratorRef = generatorObject;
    }
}
