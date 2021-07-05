package com.benrostudios.enchante.ui.ar

import android.content.Context
import com.benrostudios.enchante.R
import com.benrostudios.enchante.data.models.ar.Place
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable

class PlaceNode(
    val context: Context,
) : Node() {

    private var placeRenderable: ViewRenderable? = null

    override fun onActivate() {
        super.onActivate()
        if (scene == null) {
            return
        }
        if (placeRenderable != null) {
            return
        }
        ViewRenderable.builder()
            .setView(context, R.layout.place_view)
            .build()
            .thenAccept { renderable ->
                setRenderable(renderable)
                placeRenderable = renderable
            }
    }
}