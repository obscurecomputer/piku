package computer.obscure.piku.mod.fabric.controlify

import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.PikuClient
import dev.isxander.controlify.Controlify
import dev.isxander.controlify.api.bind.InputBindingSupplier
import dev.isxander.controlify.bindings.ControlifyBindings
import dev.isxander.controlify.controller.ControllerEntity

object ControlifyIntegration : PikuService {
    fun tick() {
        val controller = Controlify.instance().currentController.orElse(null)
            ?: return

        addBinding(controller, ControlifyBindings.WALK_LEFT, "move_left")
        addBinding(controller, ControlifyBindings.WALK_RIGHT, "move_right")
        addBinding(controller, ControlifyBindings.WALK_FORWARD, "move_forward")
        addBinding(controller, ControlifyBindings.WALK_BACKWARD, "move_backward")

        fireAxis(controller)
    }

    fun addBinding(
        controller: ControllerEntity,
        binding: InputBindingSupplier,
        nameOverride: String? = null,
    ) {
        val name = nameOverride ?: binding.bindId().path
        val bind = binding.on(controller)

        if (bind.justPressed()) fireButton(name, "press")
        if (bind.justReleased()) fireButton(name, "release")
        if (bind.justTapped()) fireButton(name, "tapped")
    }

    private fun fireButton(name: String, action: String) {
        val eventData = mapOf(
            "button" to name,
            "action" to action
        )
        PikuClient.engine!!.events.fire("client.controller.button", eventData)
    }

    private fun fireAxis(controller: ControllerEntity) {
        val moveX = ControlifyBindings.WALK_RIGHT.on(controller).analogueNow() -
                ControlifyBindings.WALK_LEFT.on(controller).analogueNow()
        val moveY = ControlifyBindings.WALK_FORWARD.on(controller).analogueNow() -
                ControlifyBindings.WALK_BACKWARD.on(controller).analogueNow()

        PikuClient.engine!!.events.fire(
            "client.controller.move",
            mapOf(
                "x" to moveX,
                "y" to moveY,
                "vector" to LuaVec2Instance(
                    moveX.toDouble(), moveY.toDouble()
                )
            )
        )

        val lookX = ControlifyBindings.LOOK_RIGHT.on(controller).analogueNow() -
                ControlifyBindings.LOOK_LEFT.on(controller).analogueNow()
        val lookY = ControlifyBindings.LOOK_DOWN.on(controller).analogueNow() -
                ControlifyBindings.LOOK_UP.on(controller).analogueNow()

        PikuClient.engine!!.events.fire(
            "client.controller.look",
            mapOf(
                "x" to lookX,
                "y" to lookY,
                "vector" to LuaVec2Instance(
                    lookX.toDouble(), lookY.toDouble()
                )
            )
        )
    }
}