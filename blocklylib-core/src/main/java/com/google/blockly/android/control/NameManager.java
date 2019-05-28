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

package com.google.blockly.android.control;

import android.annotation.SuppressLint;
import android.database.DataSetObservable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility functions for handling variable and procedure names.用于处理变量和过程名称的实用程序函数
 */
public abstract class NameManager extends DataSetObservable {
    // Regular expression with two groups.  The first lazily looks for any sequence of characters正则表达式有两组。 第一个懒惰地寻找任何字符序列
    // and the second looks for one or more numbers.  So foo2 -> (foo, 2).  f222 -> (f, 222).第二个查找一个或多个数字。 所以foo2 - >（foo，2）。 f222 - >（f，222）。
    private static final Pattern mRegEx = Pattern.compile("^(.*?)(\\d+)$");
    protected final SortedSet<String> mDisplayNamesSorted = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    protected final ArrayMap<String, String> mCanonicalMap = new ArrayMap<>();

    /**
     * Generates a name that is unique within the scope of the current NameManager, based on the
     * input name.  If the base name was unique, returns it directly.根据输入名称生成在当前NameManager范围内唯一的名称。 如果基本名称是唯一的，则直接返回。
     *
     * @param name The name upon which to base the unique name.基于唯一名称的名称。
     * @param addName Whether to add the generated name to the used names list.是否将生成的名称添加到使用的名称列表中
     *
     * @return A unique name.一个独特的名字
     */
    public String generateUniqueName(String name, boolean addName) {
        while (mCanonicalMap.containsKey(makeCanonical(name))) {
            Matcher matcher = mRegEx.matcher(name);
            if (matcher.matches()) {
                // Increment digits suffix, preserving leading zeros. Ex., "var001" => "var002"增加数字后缀，保留前导零。 例如，“var001”=>“var002”
                String digits = matcher.group(2);
                int newValue = Integer.parseInt(digits) + 1;
                String newDigits = String.format("%0" + digits.length() +  "d", newValue);

                name = matcher.group(1) + newDigits;
            } else {
                name = name + "2";
            }
        }
        if (addName) {
            addName(name);
        }
        return name;
    }

    /**
     * @param name The string to look up.要查找的字符串。
     *
     * @return True if name's lowercase equivalent is in the list.如果名称的小写等效项在列表中，则为True
     */
    public boolean contains(String name) {
        return mCanonicalMap.containsKey(makeCanonical(name));
    }

    /**
     * @return The number of names that have been used.已使用的名称数
     */
    public int size() {
        return mDisplayNamesSorted.size();
    }

    /**
     * Convert a Blockly entity name to a legal exportable entity name.将Blockly实体名称转换为合法的可导出实体名称。
     * Ensure that this is a new name not overlapping any previously defined name.确保这是一个与以前定义的名称不重叠的新名称。
     * Also check against list of reserved words for the current language and
     * ensure name doesn't collide.还要检查当前语言的保留字列表，并确保名称不会发生冲突
     * The new name will conform to the [_A-Za-z][_A-Za-z0-9]* format that most languages consider
     * legal for variables.新名称将符合大多数语言认为对变量合法的[_A-Za-z] [_ A-Za-z0-9] *格式
     *
     * @param reservedWords Reserved words in the target language.目标语言中的保留字
     * @param baseName The name to convert.要转换的名称
     *
     * @return A legal variable or procedure name in the target language.目标语言中的合法变量或过程名称
     */
    public abstract String generateExternalName(Set<String> reservedWords, String baseName);

    /**
     * Adds the name to the list of used names.  Does not check if the name is already there.
     *将名称添加到使用的名称列表中。 不检查名称是否已存在
     * @param name The name to add.
     * @throws IllegalArgumentException If the name is not valid.如果名称无效
     */
    public void addName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Invalid name \"" + name + "\".");
        }
        if (mCanonicalMap.put(makeCanonical(name), name) == null) {
            mDisplayNamesSorted.add(name);
            notifyChanged();
        }
    }

    /**
     * @return An alphabetically sorted list of all of the names that have already been used.按字母顺序排列的所有已使用名称的列表
     *         This list is not modifiable, but is backed by the real list and will stay updated.此列表不可修改，但由真实列表支持，并将保持更新
     */
    public SortedSet<String> getUsedNames() {
        return Collections.unmodifiableSortedSet(mDisplayNamesSorted);
    }

    /**
     * Clear the list of used names.清除已使用名称的列表
     */
    public void clear() {
        if (mDisplayNamesSorted.size() != 0) {
            mDisplayNamesSorted.clear();
            mCanonicalMap.clear();
            notifyChanged();
        }
    }

    /**
     * Remove a single name from the list of used names.从使用的名称列表中删除单个名称
     *
     * @param toRemove The name to remove.
     */
    public boolean remove(String toRemove) {
        String canonical = makeCanonical(toRemove);
        if (mCanonicalMap.remove(canonical) != null) {
            mDisplayNamesSorted.remove(toRemove);
            notifyChanged();
            return true;
        }
        return false;
    }

    public boolean isValidName(@NonNull String name) {
        if (name.isEmpty() || name.trim().length() != name.length()) {
            return false;
        }
        return true;
    }

    public String makeValidName(@NonNull String name, @Nullable String fallbackName) {
        name = name.trim();
        if (name.isEmpty()) {
            return fallbackName;
        } else {
            return name;
        }
    }

    /**
     * Returns the existing name, if the {@code name} will map to the same canonical form.如果{@code name}将映射到相同的规范形式，则返回现有名称
     * Otherwise, return the proposedName.
     * @param name The proposed name.建议的名称
     * @return A previous added name that shares the same canonical form. Otherwise return null.以前添加的名称，它共享相同的规范形式。 否则返回null
     */
    @Nullable
    public String getExisting(String name) {
        return mCanonicalMap.get(makeCanonical(name));
    }

    /**
     * @param name The proposed name
     * @return The canonical string for the provided name, as used by {@link #mCanonicalMap}.{@link #mCanonicalMap}使用的提供名称的规范字符串
     */
    @SuppressLint("DefaultLocale")
    protected String makeCanonical(@NonNull String name) {
        return name.toLowerCase();
    }

    /**
     * The NameManager for procedure names.NameManager用于过程名称
     */
    // TODO(602): Move to com.google.blockly.android.codegen.LanguageDefinition
    public static final class ProcedureNameManager extends NameManager {
        @Override
        public String generateExternalName(Set<String> reservedWords, String baseName) {
            // TODO: Implement.
            return baseName;
        }
    }

    /**
     * The NameManager for variable names.NameManager用于变量名称
     */
    public static final class VariableNameManager extends NameManager {
        private static final String LETTERS = "ijkmnopqrstuvwxyzabcdefgh"; // no 'l', start at i.
        private String mVariablePrefix;

        // TODO(602): Move to com.google.blockly.android.codegen.LanguageDefinition
        @Override
        public String generateExternalName(Set<String> reservedWords, String baseName) {
            // TODO: Implement.
            return mVariablePrefix + baseName;
        }

        /**
         * Sets the prefix that will be attached to external names during generation.设置在生成期间将附加到外部名称的前缀。
         * Some languages need a '$' or a namespace before all variable names.在所有变量名称之前，某些语言需要'$'或命名空间
         *
         * @param variablePrefix The prefix to attach.要附加的前缀
         */
        // TODO: Move to com.google.blockly.android.codegen.LanguageDefinition
        public void setVariablePrefix(String variablePrefix) {
            mVariablePrefix = variablePrefix;
        }

        /**
         * Return a new variable name that is not yet being used. This will try to
         * generate single letter variable names in the range 'i' to 'z' to start with.
         * 返回尚未使用的新变量名称。 这将尝试生成“i”到“z”范围内的单字母变量名称
         * If no unique name is located it will try 'i' to 'z', 'a' to 'h',
         * then 'i2' to 'z2' etc.  Skip 'l'.如果找不到唯一的名称，它将尝试'i'到'z'，'a'到'h'，然后'i2'到'z2'等。跳过'l'
         *
         * @param addName Whether to add the new name to the list of variables.是否将新名称添加到变量列表中
         *
         * @return New variable name.
         */
        public String generateVariableName(boolean addName) {
            String newName;
            int suffix = 1;
            while (true) {
                for (int i = 0; i < LETTERS.length(); i++) {
                    newName = Character.toString(LETTERS.charAt(i));
                    if (suffix > 1) {
                        newName += suffix;
                    }
                    String canonical = makeCanonical(newName);  // In case override by subclass.
                    if (!mCanonicalMap.containsKey(canonical)) {
                        if (addName) {
                            mCanonicalMap.put(canonical, newName);
                            mDisplayNamesSorted.add(newName);
                            notifyChanged();
                        }
                        return newName;
                    }
                }
                suffix++;
            }
        }
    }
}
