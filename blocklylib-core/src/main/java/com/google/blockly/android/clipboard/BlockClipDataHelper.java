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

package com.google.blockly.android.clipboard;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.DragEvent;

import com.google.blockly.android.ui.BlockGroup;
import com.google.blockly.android.ui.PendingDrag;
import com.google.blockly.model.Block;

import java.io.IOException;


/**
 * {@code ClipDataTransformer} is an interface to help transform {@link Block} data and view objects
 * into ClipData, and back. This is used for drag-and-drop operations and copy/paste actions.
 * 是一个接口，用于帮助将数据和视图对象转换为ClipData并返回。这用于拖放操作和复制/粘贴操作。
 * <p/>
 * Every application needs one implementation. Most applications will be content with每个应用程序都需要一个实现。大多数应用程序都会满足
 * {@link SingleMimeTypeClipDataHelper}.
 */
public interface BlockClipDataHelper {
    /**
     * Constructs a new populated {@link ClipData} using the information from a {@link PendingDrag}.
     *使用{@link PendingDrag}中的信息构建新的填充的{@link ClipData}。
     * @param pendingDrag The source of clip
     * @return A new {@link ClipData} representing the dragged or copied {@link BlockGroup}.
     */
    ClipData buildDragClipData(PendingDrag pendingDrag) throws IOException;

    /**
     * This determines whether an incoming {@link ClipData} is a representation of Blockly
     * {@link Block}s that can be handled by this {@link BlockClipDataHelper}.
     *
     * @param clipDescrip A description of the incoming clipboard data.
     * @return True if the MIME type is found.
     */
    boolean isBlockData(ClipDescription clipDescrip);

    /**
     * Extracts a PendingDrag from a {@link DragEvent}. Assumes that {@link #isBlockData} has been
     * called and returned {@link true}.
     *
     * @param event A block drag event.
     * @return The PendingDrag for {@code event}
     */
    PendingDrag getPendingDrag(DragEvent event);
}
