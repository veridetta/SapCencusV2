package com.example.sapcencuskotlin.graph

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.google.mlkit.vision.text.Text

// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
class TextGraphic internal constructor(
    overlay: GraphicOverlay,
    private val element: Text.Element?
) :
    GraphicOverlay.Graphic(overlay) {
    private val rectPaint: Paint
    private val textPaint: Paint

    init {
        rectPaint = Paint()
        rectPaint.color = TEXT_COLOR
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = STROKE_WIDTH
        textPaint = Paint()
        textPaint.color = TEXT_COLOR
        textPaint.textSize = TEXT_SIZE
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }
    companion object {
        private const val TAG = "TextGraphic"
        private const val TEXT_COLOR = Color.RED
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f
    }

    override fun draw(canvas: Canvas?) {
        Log.d(TAG, "on draw text graphic")
        checkNotNull(element) { "Attempting to draw a null text." }

        // Draws the bounding box around the TextBlock.

        // Draws the bounding box around the TextBlock.
        val rect = RectF(element.boundingBox)
        canvas!!.drawRect(rect, rectPaint)

        // Renders the text at the bottom of the box.

        // Renders the text at the bottom of the box.
        canvas.drawText(element.text, rect.left, rect.bottom, textPaint)
    }
}