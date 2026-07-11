package computer.obscure.piku.mod.fabric.ui

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.controlify.ActionEvent
import computer.obscure.piku.mod.fabric.controlify.BindingEvent
import computer.obscure.piku.mod.fabric.ui.classes.ControllerActivateMode
import computer.obscure.piku.mod.fabric.ui.classes.ControllerScrollAxis
import computer.obscure.piku.mod.fabric.ui.classes.ControllerScrollDirection
import computer.obscure.piku.mod.fabric.ui.components.FlowNode
import computer.obscure.piku.mod.fabric.ui.components.UINode
import dev.isxander.controlify.api.bind.InputBindingSupplier
import dev.isxander.controlify.controller.ControllerEntity
import kotlin.math.abs

object ControlifyUI : PikuService {
    val tickingScrollers = mutableListOf<FlowNode>()

    override fun shutdown() {
        tickingScrollers.clear()
    }

    fun fireButton(
        controller: ControllerEntity,
        binding: InputBindingSupplier,
        name: String,
        action: BindingEvent
    ) {
        println("BUTTON FIRE CALLED FROM CONTROLIFYUI: $name $action")

        tickingScrollers.toList().forEach { node ->
            val options = node.controllerOptions
            if (!options.activateBindings.contains(binding)) return@forEach
            if (options.activateBindingEvent != action) return@forEach

            val mode = options.activateMode
            val selectedIndex = node.controllerData.currentSelectionIndex
            val eligible = eligibleChildren(node)
            eligible.forEachIndexed { index, child ->
                when (mode) {
                    ControllerActivateMode.MULTI -> {
                        if (index != selectedIndex) return@forEachIndexed

                        if (child.activated)
                            child.onDeactivate?.invoke()
                        else
                            child.onActivate?.invoke()
                        child.activated = !child.activated
                    }
                    ControllerActivateMode.TOGGLE -> {
                        if (index != selectedIndex) {
                            if (!child.activated) return@forEachIndexed
                            child.onDeactivate?.invoke()
                            child.activated = false
                            return@forEachIndexed
                        }
                        if (child.activated) return@forEachIndexed
                        child.onActivate?.invoke()
                        child.activated = true
                    }
                    ControllerActivateMode.SINGLE_TOGGLE -> {
                        if (index != selectedIndex) {
                            if (!child.activated) return@forEachIndexed
                            child.onDeactivate?.invoke()
                            child.activated = false
                            return@forEachIndexed
                        }

                        if (child.activated) {
                            child.onDeactivate?.invoke()
                            child.activated = false
                            return@forEachIndexed
                        }
                        child.onActivate?.invoke()
                        child.activated = true
                    }
                    ControllerActivateMode.NONE -> {}
                }
            }
        }
    }

    fun fireAxis(
        controller: ControllerEntity,
        action: ActionEvent,
        vector: Vec2
    ) {
        val flowNodes = UIRenderer.findAllOfType<FlowNode>()
        val eligible = flowNodes.filter {
            it.controllerOptions.active &&
                    it.controllerOptions.actions.contains(action)
        }

        eligible.forEach { node ->
            detectAxis(node, vector)
        }

        tickingScrollers += eligible.filter {
            !tickingScrollers.contains(it)
        }
    }

    fun tick(delta: Int = 1) {
        tickingScrollers.toList().forEach { node ->
            tickNode(node, delta)
        }
    }

    fun tickNode(node: FlowNode, delta: Int = 1) {
        val data = node.controllerData
        val options = node.controllerOptions

        if (!data.repeating) {
            // Initial movement from the first input
            if (data.ticksScrolled == 0) {
                findNext(node)
            }

            // Begin repeat phase once the initial delay is reached
            if (data.ticksScrolled >= options.initialDelay) {
                data.repeating = true
                data.ticksScrolled = 0
            }
        } else {
            // Repeat movement
            if (data.ticksScrolled >= options.repeatDelay) {
                data.ticksScrolled = 0
                findNext(node)
            }
        }

        data.ticksScrolled += delta
    }

    fun detectAxis(node: FlowNode, vector: Vec2) {
        val data = node.controllerData
        val options = node.controllerOptions

        val axisValue = when (options.scrollAxis) {
            ControllerScrollAxis.HORIZONTAL -> vector.x
            ControllerScrollAxis.VERTICAL -> -vector.y
        }

        // No direction is currently held
        // Require the engage threshold before accepting input
        if (data.heldDirection == ControllerScrollDirection.NO_INPUT) {
            if (axisValue > options.engageThreshold) {
                data.direction = ControllerScrollDirection.FORWARD
                data.holding = true
            } else if (axisValue < -options.engageThreshold) {
                data.direction = ControllerScrollDirection.BACKWARD
                data.holding = true
            }
        } else {
            // A direction is already active, don't immediately
            // change from small movements.

            // Allow deliberate direction change through the engage threshold
            if (data.heldDirection == ControllerScrollDirection.FORWARD
                && axisValue < -options.engageThreshold) {
                data.direction = ControllerScrollDirection.BACKWARD
            } else if (data.heldDirection == ControllerScrollDirection.BACKWARD
                && axisValue > options.engageThreshold) {
                data.direction = ControllerScrollDirection.FORWARD
            } else if (abs(axisValue) < options.releaseThreshold) {
                // Release the input only once the axisValue is within the release threshold
                data.direction = ControllerScrollDirection.NO_INPUT
                data.holding = false
                data.repeating = false
                data.ticksScrolled = 0
            }
        }

        // Update the currently locked direction, preventing small
        // movements from causing accidental direction changes
        if (data.direction != ControllerScrollDirection.NO_INPUT
            && data.heldDirection != data.direction) {
            data.heldDirection = data.direction
        }

        // Reset when the stick stops receiving input
        if (data.direction == ControllerScrollDirection.NO_INPUT) {
            data.heldDirection = ControllerScrollDirection.NO_INPUT
        }
    }

    fun findNext(node: FlowNode) {
        val data = node.controllerData
        val direction = data.direction

        data.previousSelectionIndex = data.currentSelectionIndex
        data.previousSelection = data.currentSelection
        data.currentSelectionIndex += direction.direction

        val eligible = eligibleChildren(node)
        if (data.currentSelectionIndex < 0) {
            data.currentSelectionIndex = eligible.size - 1
        } else if (data.currentSelectionIndex > eligible.size - 1) {
            data.currentSelectionIndex = 0
        }

        updateSelection(node)
    }

    fun updateSelection(node: FlowNode) {
        val data = node.controllerData
        val eligible = eligibleChildren(node)

        eligible.forEachIndexed { index, child ->
            if (index == data.currentSelectionIndex) {
                data.currentSelection = child
                child.onSelect?.invoke()
                child.selected = true
            } else {
                child.onDeselect?.invoke()
                child.selected = false
            }
        }
    }

    fun eligibleChildren(node: FlowNode): List<UINode> {
        return node.children.filter { it !is FlowNode && it.selectable }
    }
}