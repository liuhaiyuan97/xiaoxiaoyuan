/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.blockly.android.ui;

import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Rotation constants in 90&ordm; increments for {@link View} rotation.旋转常数为90＆ordm; {@link View}旋转的增量
 *
 * @see RotatedViewGroup
 */
public final class Rotation {

    /** Child is not rotated.孩子不会轮换 */
    public static final int NONE = 0;

    /** Child is always rotated clockwise, so its top is on the right.孩子总是顺时针旋转，所以它的顶部在右边。 */
    public static final int CLOCKWISE = 1;

    /** Child is always rotated counter-clockwise, so its top is on the left.孩子总是逆时针旋转，所以它的顶部在左边 */
    public static final int COUNTER_CLOCKWISE = 2;

    /** Rotation flag bit mask for clockwise or counter-clockwise directions.顺时针或逆时针方向的旋转标志位掩码 */
    public static final int ROTATION_DIRECTION_MASK = CLOCKWISE | COUNTER_CLOCKWISE;

    /** Rotation flag bit field to mark LTR/RTL aware rotations.旋转标志位字段用于标记LTR / RTL感知旋转 */
    public static final int RTL_AWARE_BIT = 0x10;

    /** Child is rotated clockwise in LTR, and counter-clockwise in RTL. 子项在LTR中顺时针旋转，在RTL中逆时针旋转。*/
    public static final int ADAPTIVE_CLOCKWISE = RTL_AWARE_BIT | CLOCKWISE;

    /** Child is rotated counter-clockwise in LTR, and clockwise in RTL.子项在LTR中逆时针旋转，在RTL中顺时针旋转 */
    public static final int ADAPTIVE_COUNTER_CLOCKWISE = RTL_AWARE_BIT | COUNTER_CLOCKWISE;

    @IntDef(flag=true, value={
            NONE,
            CLOCKWISE,
            COUNTER_CLOCKWISE,
            ADAPTIVE_CLOCKWISE,
            ADAPTIVE_COUNTER_CLOCKWISE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Enum {}

    /** @return True if {@code rotation} represents rotation in either direction. @return如果{@code rotation}表示任一方向的旋转，则为True。*/
    public static boolean isRotated(@Enum int rotation) {
        return (rotation & ROTATION_DIRECTION_MASK) != 0;
    }

    public static boolean isRtlAware(@Rotation.Enum int rotation) {
        return (rotation & RTL_AWARE_BIT) == RTL_AWARE_BIT;
    }

    /** @return The absolute rotation direction in the context of {@code view}.@return {@code view}上下文中的绝对旋转方向 */
    @Rotation.Enum
    public static int normalize(@Rotation.Enum int rotation, View view) {
        return normalize(rotation, ViewCompat.getLayoutDirection(view));
    }

    /** @return The absolute rotation direction, given the provided {@code layoutDir}.@return绝对旋转方向，给定{@code layoutDir} */
    @Rotation.Enum
    public static int normalize(@Rotation.Enum int rotation, int layoutDir) {
        return normalize(rotation, layoutDir == ViewCompat.LAYOUT_DIRECTION_RTL);
    }

    /**
     * @return The absolute rotation direction, accounting for right-to-left if {@code isRtl} is
     *         true.@return绝对旋转方向，如果{@code isRtl}为真，则从右到左。
     */
    @Rotation.Enum
    public static int normalize(@Rotation.Enum int rotation, boolean isRtl) {
        if (!isRtlAware(rotation)) {
            return rotation;
        }
        int ltrRotation = (rotation & ROTATION_DIRECTION_MASK);
        if (!isRtl || ltrRotation == NONE) {
            return ltrRotation;
        }
        // Reverse direction.反方向
        return ltrRotation == CLOCKWISE ? COUNTER_CLOCKWISE : CLOCKWISE;
    }

    // Do not instantiate.不要实例化。
    private Rotation() {}
}
