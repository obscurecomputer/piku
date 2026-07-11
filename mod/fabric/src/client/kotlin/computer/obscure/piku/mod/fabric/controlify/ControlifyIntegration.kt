package computer.obscure.piku.mod.fabric.controlify

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.ui.ControlifyUI
import dev.isxander.controlify.Controlify
import dev.isxander.controlify.api.bind.InputBindingSupplier
import dev.isxander.controlify.bindings.ControlifyBindings
import dev.isxander.controlify.controller.ControllerEntity
import net.minecraft.network.chat.Component

enum class BindingEvent {
    PRESS,
    RELEASE,
    TAP,
}

enum class ActionEvent {
    LOOK,
    MOVE
}

object ControlifyIntegration : PikuService {
    val bindings = mutableMapOf<String, InputBindingSupplier>()
    private val prevTapped = mutableMapOf<String, Boolean>()
    val prevState = mutableMapOf<ActionEvent, Vec2>()

    fun tick() {
        val controller = Controlify.instance().currentController.orElse(null)
            ?: return

//        addBinding(controller, ControlifyBindings.WALK_LEFT, "move_left")
//        addBinding(controller, ControlifyBindings.WALK_RIGHT, "move_right")
//        addBinding(controller, ControlifyBindings.WALK_FORWARD, "move_forward")
//        addBinding(controller, ControlifyBindings.WALK_BACKWARD, "move_backward")
//        addBinding(controller, ControlifyBindings.LOOK_LEFT, "look_left")
//        addBinding(controller, ControlifyBindings.LOOK_RIGHT, "look_right")
//        addBinding(controller, ControlifyBindings.LOOK_UP, "look_up")
//        addBinding(controller, ControlifyBindings.LOOK_DOWN, "look_down")
        addBinding(controller, ControlifyBindings.JUMP, "jump")

        fireAxis(controller)
    }

    fun getGlyph(binding: String): Component? {
        val bind = bindings[binding] ?: return null
        return bind.inputGlyph()
    }

    fun addBinding(
        controller: ControllerEntity,
        binding: InputBindingSupplier,
        nameOverride: String? = null,
    ) {
        val name = nameOverride ?: binding.bindId().path
        val bind = binding.on(controller)

        bindings[name] = binding

        var fireEvent: BindingEvent? = null
        if (bind.justPressed()) fireEvent = BindingEvent.PRESS
        if (bind.justReleased()) fireEvent = BindingEvent.RELEASE

        val tappedNow = bind.justTapped()
        val tappedBefore = prevTapped[name] ?: false
        if (tappedNow && !tappedBefore) fireEvent = BindingEvent.TAP
        prevTapped[name] = tappedNow

        if (fireEvent != null)
            fireButton(controller, binding, name, fireEvent)
    }

    private fun fireButton(
        controller: ControllerEntity,
        binding: InputBindingSupplier,
        name: String,
        action: BindingEvent
    ) {
        val eventData = mapOf(
            "button" to name,
            "action" to action.name,
        )
        PikuClient.engine!!.events.fire("client.controller.button", eventData)
        ControlifyUI.fireButton(controller, binding, name, action)
    }

    private fun fireAxis(controller: ControllerEntity) {
        val moveX = ControlifyBindings.WALK_RIGHT.on(controller).analogueNow() -
                ControlifyBindings.WALK_LEFT.on(controller).analogueNow()
        val moveY = ControlifyBindings.WALK_FORWARD.on(controller).analogueNow() -
                ControlifyBindings.WALK_BACKWARD.on(controller).analogueNow()

        val lookX = ControlifyBindings.LOOK_RIGHT.on(controller).analogueNow() -
                ControlifyBindings.LOOK_LEFT.on(controller).analogueNow()
        val lookY = ControlifyBindings.LOOK_UP.on(controller).analogueNow() -
                ControlifyBindings.LOOK_DOWN.on(controller).analogueNow()

        val moveVec = Vec2(moveX, moveY)
        val lookVec = Vec2(lookX, lookY)
        val prevMoveVec = prevState[ActionEvent.MOVE]
        val prevLookVec = prevState[ActionEvent.LOOK]

        if (moveVec != Vec2.ZERO || prevMoveVec != Vec2.ZERO) {
            PikuClient.engine!!.events.fire(
                "client.controller.move",
                mapOf(
                    "vector" to moveVec.toLuaInstance()
                )
            )
            ControlifyUI.fireAxis(controller, ActionEvent.MOVE, moveVec)
        }

        if (lookVec != Vec2.ZERO || prevLookVec != Vec2.ZERO) {
            PikuClient.engine!!.events.fire(
                "client.controller.look",
                mapOf(
                    "vector" to lookVec.toLuaInstance()
                )
            )
            ControlifyUI.fireAxis(controller, ActionEvent.LOOK, lookVec)
        }

        prevState[ActionEvent.MOVE] = moveVec
        prevState[ActionEvent.LOOK] = lookVec
    }
}