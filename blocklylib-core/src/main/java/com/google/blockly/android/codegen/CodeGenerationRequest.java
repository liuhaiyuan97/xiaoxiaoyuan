/*
 *  Copyright 2015 Google Inc. All Rights Reserved.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.blockly.android.codegen;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

/**
 * Container for the information needed to generate code through the {@link CodeGeneratorService}.用于生成代码所需的信息的容器
 */
public class CodeGenerationRequest {
    private final CodeGeneratorCallback mCallback;
    private final String mBlocklyXml;
    private final List<String> mBlockDefinitionsFilenames;
    private final List<String> mBlockGeneratorsFilenames;
    private final LanguageDefinition mGeneratorLanguage;

    /**
     * Constructor for a code generation request.代码生成请求的构造函数。
     * @param xml The xml of a full workspace for which code should be generated.应该生成代码的完整工作区的XML
     * @param callback A callback specifying what to do with the generated code.指定如何处理生成代码的回调
     * @param generatorsLanguage The {@link LanguageDefinition} for the core language being
 *                                   used to generate code.用于生成代码的核心语言。
     * @param blockDefinitionsFilenames The paths of the js files containing block definitions,
*                                  relative to file:///android_assets/background_compiler.html.包含文件定义的JS文件的路径
     * @param blockGeneratorsFilenames The path of the js file containing block generators, relative
*                                  to file:///android_assets/background_compiler.html.包含文件生成器的JS文件相对于文件的路径
     */
    public CodeGenerationRequest(@NonNull String xml, CodeGeneratorCallback callback,
            @NonNull LanguageDefinition generatorsLanguage, List<String> blockDefinitionsFilenames,
            List<String> blockGeneratorsFilenames) {
        if (xml == null || xml.isEmpty()) {
            throw new IllegalArgumentException("The blockly workspace string must not be empty " +
                    "or null.");
        }
        if (generatorsLanguage == null || TextUtils.isEmpty(generatorsLanguage.mLanguageFilename)
                || TextUtils.isEmpty(generatorsLanguage.mGeneratorRef)) {
            throw new IllegalArgumentException("The generator language must be defined.");
        }
        mCallback = callback;
        mBlocklyXml = xml;
        mBlockDefinitionsFilenames = blockDefinitionsFilenames;
        mBlockGeneratorsFilenames = blockGeneratorsFilenames;
        mGeneratorLanguage = generatorsLanguage;
    }

    public CodeGeneratorCallback getCallback() {
        return mCallback;
    }

    public String getXml() {
        return mBlocklyXml;
    }

    public List<String> getBlockDefinitionsFilenames() {
        return mBlockDefinitionsFilenames;
    }

    public List<String> getBlockGeneratorsFilenames() {
        return mBlockGeneratorsFilenames;
    }

    public LanguageDefinition getGeneratorLanguageDefinition() {
        return mGeneratorLanguage;
    }

    public interface CodeGeneratorCallback {
        /**
         * Called when finished generating code.完成生成代码时调用
         *
         * @param generatedCode The string containing all of the generated code.包含所有生成代码的字符串
         */
        void onFinishCodeGeneration(String generatedCode);
    }

}
