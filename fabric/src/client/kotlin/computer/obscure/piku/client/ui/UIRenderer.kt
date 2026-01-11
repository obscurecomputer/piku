package computer.obscure.piku.client.ui

import computer.obscure.piku.client.PikuClient
import computer.obscure.piku.client.scripting.api.ui.LuaEasingInstance
import computer.obscure.piku.client.ui.components.BoxRenderer
import computer.obscure.piku.client.ui.components.FlowContainerRenderer
import computer.obscure.piku.client.ui.components.GradientRenderer
import computer.obscure.piku.client.ui.components.GroupRenderer
import computer.obscure.piku.client.ui.components.LineRenderer
import computer.obscure.piku.client.ui.components.ProgressBarRenderer
import computer.obscure.piku.client.ui.components.SpriteRenderer
import computer.obscure.piku.client.ui.components.TextRenderer
import computer.obscure.piku.client.ui.events.DestroyEventHandler
import computer.obscure.piku.client.ui.events.MoveEventHandler
import computer.obscure.piku.client.ui.events.OpacityEventHandler
import computer.obscure.piku.client.ui.events.PaddingEventHandler
import computer.obscure.piku.client.ui.events.ProgressEventHandler
import computer.obscure.piku.client.ui.events.RotateEventHandler
import computer.obscure.piku.client.ui.events.ScaleEventHandler
import computer.obscure.piku.client.ui.events.SizeEventHandler
import computer.obscure.piku.client.ui.events.UIEventContext
import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.*
import computer.obscure.piku.common.ui.classes.Easing
import computer.obscure.piku.common.ui.classes.FlowDirection
import computer.obscure.piku.common.ui.classes.RelativePosition
import computer.obscure.piku.common.ui.classes.Spacing
import computer.obscure.piku.common.ui.components.Box
import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.components.FlowContainer
import computer.obscure.piku.common.ui.components.Gradient
import computer.obscure.piku.common.ui.components.Group
import computer.obscure.piku.common.ui.components.Line
import computer.obscure.piku.common.ui.components.ProgressBar
import computer.obscure.piku.common.ui.components.Sprite
import computer.obscure.piku.common.ui.components.Text
import computer.obscure.piku.common.ui.events.DestroyEvent
import computer.obscure.piku.common.ui.events.MoveEvent
import computer.obscure.piku.common.ui.events.OpacityEvent
import computer.obscure.piku.common.ui.events.PaddingEvent
import computer.obscure.piku.common.ui.events.ProgressEvent
import computer.obscure.piku.common.ui.events.PropertyAnimation
import computer.obscure.piku.common.ui.events.RotateEvent
import computer.obscure.piku.common.ui.events.ScaleEvent
import computer.obscure.piku.common.ui.events.SizeEvent
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.resource.Resource
import net.minecraft.util.Identifier
import org.joml.Matrix3x2f
import java.io.IOException
import kotlin.to

object UIRenderer {
    val currentWindow: UIWindow = UIWindow("main")
    private var activeAnimations = mutableListOf<PropertyAnimation<*, *>>()
    private val registeredEasings = mutableMapOf<String, (time: Double) -> Double>()

    val eventDispatcher = UIEventDispatcher(
        handlers = mapOf(
            MoveEvent::class.java to MoveEventHandler(),
            OpacityEvent::class.java to OpacityEventHandler(),
            DestroyEvent::class.java to DestroyEventHandler(),
            RotateEvent::class.java to RotateEventHandler(),
            PaddingEvent::class.java to PaddingEventHandler(),
            ProgressEvent::class.java to ProgressEventHandler(),
            SizeEvent::class.java to SizeEventHandler(),
            ScaleEvent::class.java to ScaleEventHandler()
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

    fun registerEasing(easing: LuaEasingInstance) {
        registeredEasings[easing.id] = easing.function
    }

//    fun setWindow(window: UIWindow?) {
//        currentWindow = window
//        activeAnimations = mutableListOf()
//    }

    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register {
            handleUpdateRender()
        }

        var lastTimeNano = System.nanoTime()
        HudRenderCallback.EVENT.register { context, _ ->
            val currentTime = System.nanoTime()
            val deltaSeconds = (currentTime - lastTimeNano) / 1_000_000_000.0
            lastTimeNano = currentTime

            tickAnimations(deltaSeconds)

            currentWindow.let { window ->
                layout(window)
                window.components.values
                    .toList()
                    .sortedBy { it.props.zIndex }
                    .forEach { component ->
                        component.draw(context)
                    }
            }
        }
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
        val comp = currentWindow.getComponentByIdDeep(event.targetId)
        if (comp != null && event.from == null) {
            @Suppress("UNCHECKED_CAST")
            event.from = (event.getter as (Component) -> T)(comp)
        }
        @Suppress("UNCHECKED_CAST")
        activeAnimations += event as PropertyAnimation<*, *>
    }

    fun tickAnimations(deltaSeconds: Double) {
        val iterator = activeAnimations.iterator()
        while (iterator.hasNext()) {
            val anim = iterator.next()

            val comp = currentWindow.getComponentByIdDeep(anim.targetId) ?: continue
            anim.elapsed += deltaSeconds

            val t = (anim.elapsed / anim.durationSeconds).coerceIn(0.0, 1.0)

            val easedT: Double = try {
                ((registeredEasings[anim.easing]?.invoke(t)
                    ?: Easing.valueOf(anim.easing.uppercase()).getValue(t)))
                    .coerceIn(0.0, 1.0)
            } catch (_: Exception) {
                PikuClient.warn("Unknown easing \"${anim.easing}\". Falling back to LINEAR.")
                Easing.LINEAR.getValue(t)
            }

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

    fun lerp(start: Float, end: Float, t: Double): Float {
        return start + ((end - start) * t).toFloat()
    }

    /**
     * Performs a layout pass, resolving every component's position.
     */
    private fun layout(window: UIWindow) {
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
        parentScale: Vec2 = Vec2(1f, 1f)
    ) {
        val effectiveScale = Vec2(
            component.props.scale.x * parentScale.x,
            component.props.scale.y * parentScale.y
        )
        component.computedScale = effectiveScale

        when (component) {
            is Group -> {
                component.props.components.forEach { child ->
                    val childBasePos = child.props.pos
                    val groupOffset = component.props.pos
                    val effectivePos = Vec2(
                        childBasePos.x + groupOffset.x,
                        childBasePos.y + groupOffset.y
                    )

                    child.computedPos = effectivePos

                    layoutComponent(child, window, component.computedScale)
                }
                return
            }
            is Text -> {
                // handle text early so string variables can be extracted and the new size can be calculated
                // and the position
                // additionally, handle multiline strings
                val renderer = MinecraftClient.getInstance().textRenderer

                val text = TextInterpolator.interpolate(component.props.text)
                val lines = text.split("\n")

                // measure widest line and total height
                val widestLine = lines.maxOfOrNull { renderer.getWidth(it) }?.toFloat() ?: 0f
                val totalHeight = renderer.fontHeight.toFloat() * lines.size

                // apply text scale
                val scaledWidth = widestLine * component.props.textScale.x
                val scaledHeight = totalHeight * component.props.textScale.y

                // add padding
                val widthWithPadding =
                    scaledWidth + component.props.padding.left + component.props.padding.right
                val heightWithPadding =
                    scaledHeight + component.props.padding.top + component.props.padding.bottom

                component.computedSize = Vec2(widthWithPadding, heightWithPadding)
            }
            is Sprite -> {
                // handle textures early so width and height are accurate
                // (since the server does not know clientside textures)
                component.resolveTexture()
            }
            is FlowContainer -> {
                val props = component.props
                var cursorX = props.padding.left
                var cursorY = props.padding.top

                // apply inherited scale (very important for nested layouts)
                val effectiveScale = Vec2(
                    props.scale.x * parentScale.x,
                    props.scale.y * parentScale.y
                )
                component.computedScale = effectiveScale

                props.components.forEach { child ->
                    val margin = child.props.margin
                    val x = component.screenX.toFloat()
                    val y = component.screenY.toFloat()

                    when (props.direction) {
                        FlowDirection.HORIZONTAL -> {
                            val childX = x + cursorX + margin.left
                            val childY = y + props.padding.top + margin.top

                            cursorX += child.width() + margin.left + margin.right + props.gap
                            child.screenX = childX.toInt()
                            child.screenY = childY.toInt()
                        }

                        FlowDirection.VERTICAL -> {
                            val childX = x + props.padding.left + margin.left
                            val childY = y + cursorY + margin.top

                            cursorY += child.height() + margin.top + margin.bottom + props.gap
                            child.screenX = childX.toInt()
                            child.screenY = childY.toInt()
                        }
                    }

                    layoutComponent(child, window, effectiveScale)
                }
                return
            }

            else -> {}
        }

        resolvePosition(component, window)
    }


    /**
     * Manipulate the matrices of the component to apply scale and rotation.
     */
    fun applyComponentMatrices(context: DrawContext, component: Component) {
        context.matrices.pushMatrix()
        val props = component.props
        val scale = props.scale
        val rotation = props.rotation

        val width = component.width()
        val height = component.height()
        val pivotX = width / 2f
        val pivotY = height / 2f

        context.matrices.translate(component.screenX.toFloat(), component.screenY.toFloat())
        context.matrices.translate(pivotX, pivotY)
        context.matrices.scale(scale.x, scale.y)
        context.matrices.mul(Matrix3x2f().rotation(rotation.toFloat()))
        context.matrices.translate(-pivotX, -pivotY)
    }

    /**
     * Resolve absolute screen position for a component.
     */
    private fun resolvePosition(component: Component, window: UIWindow) {
        // TODO: cleanup
        val screenWidth = MinecraftClient.getInstance().window.scaledWidth
        val screenHeight = MinecraftClient.getInstance().window.scaledHeight

        val width = component.width().takeIf { it > 0 } ?: component.props.size.x
        val height = component.height().takeIf { it > 0 } ?: component.props.size.y

        // default relative positioning: the screen
        var baseX = component.computedPos?.x ?: component.props.pos.x
        var baseY = component.computedPos?.y ?: component.props.pos.y
        var baseWidth = screenWidth.toFloat()
        var baseHeight = screenHeight.toFloat()

        // if the component is relative to another, use that component's bounds as the base
        component.relativeTo?.let { relId ->
            val relativeTo = window.getComponentByIdDeep(relId)
            if (relativeTo != null) {
                baseX = relativeTo.screenX.toFloat()
                baseY = relativeTo.screenY.toFloat()
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

    fun getTexture(path: String): NativeImageBackedTexture? {
        val correctedPath = if (path.endsWith(".png")) path else "$path.png"
        val id = if (':' in correctedPath) {
            Identifier.of(correctedPath)
        } else {
            Identifier.of("minecraft", correctedPath)
        }

        return try {
            val resourceManager = MinecraftClient.getInstance().resourceManager
            val resourceOptional = resourceManager.getResource(id)

            if (resourceOptional.isEmpty) return null

            val resource: Resource = resourceOptional.get()
            val image = NativeImage.read(resource.inputStream)
            NativeImageBackedTexture({ "texture_$correctedPath" }, image)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getIdentifier(path: String): Identifier {
        return if (':' in path) {
            Identifier.of(path)
        } else {
            Identifier.of("minecraft", path)
        }
    }
}

/**
 * Resolve the sprite's texture before we run any logic.
 * This resolves relative positioning issues, since the server does
 * not know what the height and width of the image is, since we do not
 * know what texture pack the player is using.
 */
fun Sprite.resolveTexture() {
    if (props.texturePath != "") {
        val texture = UIRenderer.getTexture(props.texturePath)
        texture?.let {
            computedSize = Vec2(
                it.image?.width?.toFloat() ?: 0f,
                it.image?.height?.toFloat() ?: 0f
            )
        }
    }
}

fun Component.draw(context: DrawContext) {
    UIRenderer.componentRenderer.draw(
        this,
        context,
        MinecraftClient.getInstance()
    )
}
