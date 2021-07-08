package com.benrostudios.enchante.ui.ar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.benrostudios.enchante.R
import com.benrostudios.enchante.ui.onboarding.OnboardingActivity
import com.benrostudios.enchante.utils.SharedPrefManager
import com.benrostudios.enchante.utils.SnackbarHelper
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import org.koin.android.ext.android.inject

class CloudARActivity : AppCompatActivity() {
    lateinit var arFragment: CloudAnchorFragment
    var cloudAnchor: Anchor? = null
    private val sharedPrefManager: SharedPrefManager by inject()
    private var isScanner = false


    enum class AppAnchorState {
        NONE,
        HOSTING,
        HOSTED,
        RESOLVING,
        RESOLVED
    }

    var appAnchorState = AppAnchorState.NONE
    var snackbarHelper = SnackbarHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_aractivity)

        isScanner = intent.getBooleanExtra("scanner", false)
        arFragment =
            supportFragmentManager.findFragmentById(R.id.cloud_fragment) as CloudAnchorFragment
        arFragment.arSceneView.scene.addOnUpdateListener(this::onUpdateFrame)
        arFragment.planeDiscoveryController.hide()
        arFragment.planeDiscoveryController.setInstructionView(null)
        if (isScanner) {
            arFragment.setOnTapArPlaneListener { hitResult, plane, _ ->
                if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING || appAnchorState != AppAnchorState.NONE) {
                    return@setOnTapArPlaneListener
                }
                val anchor =
                    arFragment.arSceneView.session?.hostCloudAnchor(hitResult.createAnchor())
                cloudAnchor(anchor)
                appAnchorState = AppAnchorState.HOSTING
                snackbarHelper.showMessage(this, "Hosting anchor")
                placeObject(arFragment, cloudAnchor!!)
            }
        } else {
            arFragment.setOnTapArPlaneListener { _, _, _ ->
                val arNodes = sharedPrefManager.arId
                val nodes = arNodes.split(";")
                nodes.forEach {
                    if (it.isNotEmpty()) {
                        onResolveOkPressed(it)
                    }
                }

            }
        }
    }

    private fun onResolveOkPressed(anchorId: String) {
        val resolvedAnchor = arFragment.arSceneView.session?.resolveCloudAnchor(anchorId)
        cloudAnchor(resolvedAnchor)
        placeObject(arFragment, cloudAnchor!!)
        snackbarHelper.showMessage(this, "Fetching all shops nearby")
        appAnchorState = AppAnchorState.RESOLVING
    }

    private fun onUpdateFrame(frameTime: FrameTime) {
        checkUpdatedAnchor()
    }

    @Synchronized
    private fun checkUpdatedAnchor() {
        if (appAnchorState != AppAnchorState.HOSTING && appAnchorState != AppAnchorState.RESOLVING)
            return
        val cloudState: Anchor.CloudAnchorState = cloudAnchor?.cloudAnchorState!!
        if (appAnchorState == AppAnchorState.HOSTING) {
            if (cloudState.isError) {
                snackbarHelper.showMessageWithDismiss(this, "Error hosting anchor...")
                appAnchorState = AppAnchorState.NONE
            } else if (cloudState == Anchor.CloudAnchorState.SUCCESS) {
                snackbarHelper.showMessageWithDismiss(this, "${cloudAnchor?.cloudAnchorId}")
                d("CloudAnchor", "${cloudAnchor?.cloudAnchorId}")
                sharedPrefManager.arId += ";" + cloudAnchor?.cloudAnchorId.toString()
                appAnchorState = AppAnchorState.HOSTED
            }
        } else if (appAnchorState == AppAnchorState.RESOLVING) {
            if (cloudState.isError) {
                snackbarHelper.showMessageWithDismiss(this, "Error resolving anchor...")
                appAnchorState = AppAnchorState.NONE
            } else if (cloudState == Anchor.CloudAnchorState.SUCCESS) {
                snackbarHelper.showMessageWithDismiss(this, "Shops Fetched!")
                appAnchorState = AppAnchorState.RESOLVED
            }
        }
    }

    private fun cloudAnchor(newAnchor: Anchor?) {
        cloudAnchor?.detach()
        cloudAnchor = newAnchor
        appAnchorState = AppAnchorState.NONE
        snackbarHelper.hide(this)
    }

    private fun placeObject(fragment: ArFragment, anchor: Anchor) {
        val shopView = ViewRenderable.builder()
            .setView(this, R.layout.place_view)
            .build()
            .thenAccept { renderable ->
                addNodeToScene(fragment, anchor, renderable)

            }
    }

    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: ViewRenderable) {
        val node = AnchorNode(anchor)
        node.localPosition.z = 10f
        val transformableNode = TransformableNode(fragment.transformationSystem)
        transformableNode.renderable = renderable
        transformableNode.setParent(node)
        node.setOnTapListener { _, _ ->
            startActivity(Intent(this, OnboardingActivity::class.java))
            this.finish()
        }
        fragment.arSceneView.scene.addChild(node)
        transformableNode.select()
    }
}