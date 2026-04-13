package computer.obscure.piku.mod.fabric.ui

import com.mojang.blaze3d.platform.NativeImage
import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.core.ui.Anchor
import computer.obscure.piku.core.ui.UIEventQueue
import computer.obscure.piku.core.ui.UIWindow
import computer.obscure.piku.core.ui.classes.DirtyFlag
import computer.obscure.piku.core.ui.classes.FlowDirection
import computer.obscure.piku.core.ui.classes.RelativePosition
import computer.obscure.piku.core.ui.classes.Spacing
import computer.obscure.piku.core.ui.components.*
import computer.obscure.piku.core.ui.events.*
import computer.obscure.piku.mod.fabric.animation.AnimationUtil
import computer.obscure.piku.mod.fabric.animation.AnimationUtil.lerp
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaEasingInstance
import computer.obscure.piku.mod.fabric.ui.components.*
import computer.obscure.piku.mod.fabric.ui.events.*
import computer.obscure.twine.LuaCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier
import org.joml.Matrix3x2f

object UIRenderer : PikuService {
    val currentWindow: UIWindow = UIWindow("main")

    var activeAnimations = mutableListOf<PropertyAnimation<*, *>>()
    val registeredEasings = mutableMapOf<String, LuaCallback>()

    var debugEnabled: Boolean = false

    val eventDispatcher = UIEventDispatcher(
        handlers = mapOf(
            MoveEvent::class.java to MoveEventHandler(),
            OpacityEvent::class.java to OpacityEventHandler(),
            DestroyEvent::class.java to DestroyEventHandler(),
            RotateEvent::class.java to RotateEventHandler(),
            PaddingEvent::class.java to PaddingEventHandler(),
            ProgressEvent::class.java to ProgressEventHandler(),
            SizeEvent::class.java to SizeEventHandler(),
            ScaleEvent::class.java to ScaleEventHandler(),
            LineToEvent::class.java to LineToEventHandler(),
            LineFromEvent::class.java to LineFromEventHandler()
        ),
        context = UIEventContext(
            currentWindow = { currentWindow },
            enqueueAnimation = { anim -> enqueueAnimation(anim) }
        )
    )

    val componentRenderer = UIComponentRenderer(
        handlers = mapOf(
            Text::class.java to TextRenderer(),
            Sprite::class.java to SpriteRenderer(),
            Group::class.java to GroupRenderer(),
            Line::class.java to LineRenderer(),
            Gradient::class.java to GradientRenderer(),
            Box::class.java to BoxRenderer(),
            ProgressBar::class.java to ProgressBarRenderer(),
            FlowContainer::class.java to FlowContainerRenderer()
        )
    )

    override fun shutdown() {
        debugEnabled = false
        activeAnimations.clear()
        registeredEasings.clear()
        currentWindow.components.clear()
    }

    fun cancelAnimations() {
        activeAnimations.clear()
    }

    fun animations(): List<PropertyAnimation<*, *>> {
        return activeAnimations
    }

    fun registerEasing(easing: LuaEasingInstance) {
        registeredEasings[easing.id] = easing.function
    }

//    fun setWindow(window: UIWindow?) {
//        currentWindow = window
//        activeAnimations = mutableListOf()
//    }

    fun onWindowResized() {
        // not the most efficient method, but it works and fixes
        // ui components not re-laying out when the window is resized
        layout(currentWindow)
    }

    fun layoutIfNeeded(window: UIWindow) {
        window.components.values.forEach { component ->
            layoutComponentIfDirty(component, window)
        }
    }

    private fun layoutComponentIfDirty(
        component: Component,
        window: UIWindow,
        parentScale: Vec2 = Vec2(1.0, 1.0)
    ) {
        val props = component.props

        if (!props.isDirty(DirtyFlag.LAYOUT)) {
            if (component is Group) {
                component.props.components.forEach {
                    layoutComponentIfDirty(it, window, component.computedScale)
                }
            }
            return
        }

        layoutComponent(component, window, parentScale)

        props.clear(DirtyFlag.LAYOUT)
    }

    fun markDependentsDirty(providerId: String) {
        currentWindow.components.values.forEach { component ->
            if (component.relativeTo == providerId) {
                component.props.mark(DirtyFlag.LAYOUT)
                // recursively check if those components have dependents too
                markDependentsDirty(component.internalId)
            }
            component.props.components.forEach { child ->
                if (child.relativeTo == providerId) {
                    child.props.mark(DirtyFlag.LAYOUT)
                    // recursively check if those components have dependents too
                    markDependentsDirty(child.internalId)
                }
            }

        }
    }

    fun drawComponent(context: GuiGraphics, component: Component) {
        if (!component.props.visible) return
        component.draw(context)
    }

    /**
     * Handles the first render or a re-render of a UI.
     */
//    fun handleFreshRender(window: UIWindow) {
//        setWindow(window)
//    }

    /**
     * Handles UI updates for an already rendered UI.
     * Handles animations.
     */
    fun handleUpdateRender() {
        UIEventQueue.tick().forEach { event ->
            eventDispatcher.applyEvent(event)
        }
    }

    fun <C : Component, T> enqueueAnimation(event: PropertyAnimation<C, T>) {
        val comp = currentWindow.getComponentByIdDeep(event.targetId) ?: return
        event.target = comp as C

        if (event.from == null) {
            event.from = (event.getter as (Component) -> T)(comp)
        }

        activeAnimations += event
    }

    fun tickAnimations(deltaSeconds: Double) {
        val iterator = activeAnimations.iterator()
        while (iterator.hasNext()) {
            val anim = iterator.next()

            val comp = anim.target ?: continue
            anim.elapsed += deltaSeconds

            val t = (anim.elapsed / anim.durationSeconds).coerceIn(0.0, 1.0)

            val easedT = AnimationUtil.resolveEasing(anim.easing, t, registeredEasings)

            @Suppress("UNCHECKED_CAST")
            val typedGetter = anim.getter as (Component) -> Any?
            @Suppress("UNCHECKED_CAST")
            val typedSetter = anim.setter as (Component, Any?) -> Unit

            val start = anim.from ?: typedGetter(comp)
            val end = anim.to

            // Get the result no matter what data type it may be
            val result = when (start) {
                is Float -> lerp(start, end as Float, easedT)
                is Int -> lerp(start.toFloat(), (end as Int).toFloat(), easedT).toInt()
                is Vec2 -> Vec2(
                    lerp(start.x, (end as Vec2).x, easedT),
                    lerp(start.y, end.y, easedT)
                )
                is Spacing -> {
                    val target = anim.to as Spacing
                    Spacing(
                        left = lerp(start.left, target.left, easedT),
                        top = lerp(start.top, target.top, easedT),
                        right = lerp(start.right, target.right, easedT),
                        bottom = lerp(start.bottom, target.bottom, easedT)
                    )
                }

                else -> end
            }

            typedSetter(comp, result)
            if (t >= 1.0) iterator.remove()
        }
    }

    /**
     * Performs a layout pass, resolving every component's position.
     */
    fun layout(window: UIWindow) {
        val snapshot = window.components.values.toList()
        snapshot.forEach { component ->
            layoutComponent(component, window)
        }
    }

    /**
     * Layout a component and all its children.
     */
    private fun layoutComponent(
        component: Component,
        window: UIWindow,
        parentScale: Vec2 = Vec2(1.0, 1.0),
    ) {
        val effectiveScale = Vec2(
            component.props.scale.x * parentScale.x,
            component.props.scale.y * parentScale.y
        )
        component.computedScale = effectiveScale

        when (component) {
            is Group -> {
                var maxW = 0.0
                var maxH = 0.0
                component.props.components.forEach { child ->
                    child.screenX = component.screenX + child.props.pos.x.toInt()
                    child.screenY = component.screenY + child.props.pos.y.toInt()

                    layoutComponent(child, window, component.computedScale)

                    maxW = maxOf(maxW, child.props.pos.x + child.width())
                    maxH = maxOf(maxH, child.props.pos.y + child.height())
                }
                component.computedSize = Vec2(maxW, maxH)
                return
            }
            is Text -> {
                // handle text early so string variables can be extracted and the new size can be calculated
                // and the position
                // additionally, handle multiline strings
                val renderer = Minecraft.getInstance().font

                val interpolated = TextInterpolator.interpolate(component.props.rawText)

                if (component.cachedText != interpolated) {
                    component.cachedText = interpolated

                    val lines = interpolated.split("\n")
                    val widestLine = lines.maxOfOrNull { renderer.width(it) }?.toFloat() ?: 0f

                    component.cachedTextSize = Vec2(
                        widestLine.toDouble(),
                        (renderer.lineHeight * lines.size).toDouble()
                    )
                }
                val size = component.cachedTextSize!!
                component.computedSize = Vec2(
                    size.x + component.props.padding.left + component.props.padding.right,
                    size.y + component.props.padding.top + component.props.padding.bottom
                )
            }
            is Sprite -> {
                // handle textures early so width and height are accurate
                // (since the server does not know clientside textures)
                component.resolveTextureOnce()
            }
            is FlowContainer -> {
                val props = component.props
                val isVertical = props.direction == FlowDirection.VERTICAL
                val isReverse = props.reversed

                var offsetX = if (isReverse && !isVertical)
                    props.size.x - props.padding.right
                else props.padding.left

                var offsetY = if (isReverse && isVertical)
                    props.size.y - props.padding.bottom
                else props.padding.top

                component.props.components.forEach { child ->
                    val margin = child.props.margin

                    if (isVertical && isReverse) {
                        offsetY -= (child.height() + margin.bottom)
                    }
                    if (!isVertical && isReverse) {
                        offsetX -= (child.width() + margin.right)
                    }

                    child.computedPos = Vec2(offsetX, offsetY)
                    child.screenX = (component.screenX + child.computedPos!!.x).toInt()
                    child.screenY = (component.screenY + child.computedPos!!.y).toInt()

                    layoutComponent(child, window, component.computedScale)

                    if (isVertical && !isReverse) {
                        offsetY += child.height() + margin.bottom + props.gap
                    } else if (!isVertical && !isReverse) {
                        offsetX += child.width() + margin.right + props.gap
                    } else if (isVertical) {
                        offsetY -= (margin.top + props.gap)
                    } else {
                        offsetX -= (margin.left + props.gap)
                    }
                }

                component.computedSize = Vec2(
                    if (props.direction == FlowDirection.HORIZONTAL)
                        offsetX + props.padding.right else component.width(),
                    if (props.direction == FlowDirection.VERTICAL)
                        offsetY + props.padding.bottom else component.height()
                )
            }

            else -> {}
        }

        if (component.computedPos == null) {
            resolvePosition(component, window)
        }
    }


    /**
     * Manipulate the matrices of the component to apply scale and rotation.
     */
    fun applyComponentMatrices(context: GuiGraphics, component: Component) {
        context.pose().pushMatrix()
        val props = component.props
        val scale = props.scale
        val rotation = props.rotation

        val width = component.width()
        val height = component.height()
        val pivotX = width / 2f
        val pivotY = height / 2f

        context.pose().translate(component.screenX.toFloat(), component.screenY.toFloat())
        context.pose().translate(pivotX.toFloat(), pivotY.toFloat())
        context.pose().scale(scale.x.toFloat(), scale.y.toFloat())
        context.pose().mul(Matrix3x2f().rotation(rotation.toFloat()))
        context.pose().translate(-pivotX.toFloat(), -pivotY.toFloat())
    }

    /**
     * Resolve absolute screen position for a component.
     */
    private fun resolvePosition(component: Component, window: UIWindow) {
        // TODO: cleanup
        val screenWidth = Minecraft.getInstance().window.guiScaledWidth
        val screenHeight = Minecraft.getInstance().window.guiScaledHeight

        val width = component.width().takeIf { it > 0 } ?: component.props.size.x
        val height = component.height().takeIf { it > 0 } ?: component.props.size.y

        // default relative positioning: the screen
        var baseX = component.computedPos?.x ?: component.props.pos.x
        var baseY = component.computedPos?.y ?: component.props.pos.y
        var baseWidth = screenWidth.toDouble()
        var baseHeight = screenHeight.toDouble()

        // if the component is relative to another, use that component's bounds as the base
        component.relativeTo?.let { relId ->
            val relativeTo = window.getComponentByIdDeep(relId)
            if (relativeTo != null) {
                baseX = relativeTo.screenX.toDouble()
                baseY = relativeTo.screenY.toDouble()
                baseWidth = relativeTo.width()
                baseHeight = relativeTo.height()
            }
        }

        // base anchor logic (works for screen or relative component)
        var resolvedX = when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT ->
                baseX + component.props.pos.x
            Anchor.TOP_CENTER, Anchor.CENTER_CENTER, Anchor.BOTTOM_CENTER ->
                baseX + baseWidth / 2f - width / 2f + component.props.pos.x
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT ->
                baseX + baseWidth - width + component.props.pos.x
        }

        var resolvedY = when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT ->
                baseY + component.props.pos.y
            Anchor.CENTER_LEFT, Anchor.CENTER_CENTER, Anchor.CENTER_RIGHT ->
                baseY + baseHeight / 2f - height / 2f + component.props.pos.y
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT ->
                baseY + baseHeight - height + component.props.pos.y
        }

        // apply relative positioning (overrides anchor)
        component.relativeTo?.let { relId ->
            val relativeTo = window.getComponentByIdDeep(relId) ?: return@let
            when (component.relativePosition) {
                RelativePosition.RIGHT_OF -> {
                    resolvedX = relativeTo.screenX + relativeTo.width() + relativeTo.props.margin.right
                    resolvedY = relativeTo.screenY + relativeTo.props.margin.top
                }
                RelativePosition.LEFT_OF -> {
                    resolvedX = relativeTo.screenX - width - relativeTo.props.margin.left
                    resolvedY = relativeTo.screenY + relativeTo.props.margin.top
                }
                RelativePosition.BELOW -> {
                    resolvedX = relativeTo.screenX + relativeTo.props.margin.left
                    resolvedY = relativeTo.screenY + relativeTo.height() + relativeTo.props.margin.bottom
                }
                RelativePosition.ABOVE -> {
                    resolvedX = relativeTo.screenX + relativeTo.props.margin.left
                    resolvedY = relativeTo.screenY - height - relativeTo.props.margin.top
                }

                else -> {}
            }

            resolvedX += component.props.pos.x
            resolvedY += component.props.pos.y
        }


        // apply margins (inward or outward depending on anchor)
        val margin = component.props.margin

        when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT ->
                resolvedX += margin.left
            Anchor.TOP_CENTER, Anchor.CENTER_CENTER, Anchor.BOTTOM_CENTER ->
                resolvedX += (margin.left + margin.right) / 2f
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT ->
                resolvedX -= margin.right
        }

        when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT ->
                resolvedY += margin.top
            Anchor.CENTER_LEFT, Anchor.CENTER_CENTER, Anchor.CENTER_RIGHT ->
                resolvedY += (margin.top + margin.bottom) / 2f
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT ->
                resolvedY -= margin.bottom
        }

        component.screenX = resolvedX.toInt()
        component.screenY = resolvedY.toInt()
    }

    fun getTexture(path: String): DynamicTexture? {
        // FIXME: gpu memory leaking? DynamicTexture implements Dumpable
        val id = getIdentifier(path) ?: return null
        return try {
            val resourceManager = Minecraft.getInstance().resourceManager
            val resourceOptional = resourceManager.getResource(id).orElse(null) ?: return null
            val image = NativeImage.read(resourceOptional.open())

            DynamicTexture({ "piku_texture_$path" }, image)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getIdentifier(path: String): Identifier? {
        return try {
            if (':' in path) {
                Identifier.parse(path)
            } else {
                Identifier.fromNamespaceAndPath("minecraft", path)
            }
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * An optimisation to only resolve the texture if it has not been resolved before
 * for this component.
 */
fun Sprite.resolveTextureOnce() {
    if (textureResolved) return
    textureResolved = true

    if (props.texturePath.isNotEmpty()) {
        val texture = UIRenderer.getTexture(props.texturePath)
        texture?.let { dynamicTexture ->
            val img = dynamicTexture.pixels
            if (img != null) {
                computedSize = Vec2(
                    img.width.toDouble(),
                    img.height.toDouble()
                )
            }
        }
    }
}

fun Component.draw(context: GuiGraphics) {
    UIRenderer.componentRenderer.draw(
        this,
        context,
        Minecraft.getInstance()
    )
}
