package computer.obscure.piku.mod.common.ui

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult


object TextInterpolator {
    private val mc get() = Minecraft.getInstance()

    private val providers = mutableMapOf<String, () -> String>()

    init {
        register("CLIENT_PING") {
            mc.player?.connection!!.getPlayerInfo(mc.player!!.uuid)?.latency?.toString() ?: "N/A"
        }
        register("CLIENT_FPS") {
            "${mc.fps}"
        }
        register("PLAYER_NAME") {
            mc.player?.name?.string ?: "Unknown"
        }

        register("CLIENT_DIMENSION") { mc.level?.dimension()?.location()?.path ?: "Unknown" }
        register("CLIENT_MEMORY") {
            val rt = Runtime.getRuntime()
            val used = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024
            val max = rt.maxMemory() / 1024 / 1024
            "${used}MB/${max}MB"
        }

        register("RANDOM_INT") { (0..9999).random().toString() }
        register("PLAYER_PING_COLORED") {
            val ping = mc.player?.connection?.getPlayerInfo(mc.player!!.uuid)?.latency ?: -1
            "${getPlayerPingColor()}${ping}"
        }
        register("PLAYER_PING_COLOR") {
            getPlayerPingColor()
        }
        register("PLAYER_POS_X") {
            mc.player?.x?.toString() ?: "0"
        }
        register("PLAYER_POS_Y") {
            mc.player?.y?.toString() ?: "0"
        }
        register("PLAYER_POS_Z") {
            mc.player?.z?.toString() ?: "0"
        }
        register("PLAYER_POS_YAW") {
            mc.player?.yRot?.toString() ?: "0"
        }
        register("PLAYER_POS_PITCH") {
            mc.player?.xRot?.toString() ?: "0"
        }
        register("PLAYER_WORLD") {
            mc.level?.dimension()?.location()?.path ?: "Unknown"
        }
        register("PLAYER_SPEED") {
            mc.player?.deltaMovement?.horizontalDistance()?.toString() ?: "0"
        }

        register("PLAYER_DIRECTION") {
            mc.player?.direction?.name ?: "Unknown"
        }

        register("PLAYER_BIOME") {
            val pos = mc.player?.blockPosition() ?: return@register "Unknown"
            mc.level?.getBiome(pos)?.unwrapKey()?.get()?.location()?.path ?: "Unknown"
        }

        register("WORLD_TIME_TICKS") {
            mc.level?.gameTime?.toString() ?: "0"
        }

        register("WORLD_IS_RAINING") {
            if (mc.level?.isRaining == true) "true" else "false"
        }
        register("PLAYER_HEALTH") {
            (mc.player?.health?.toDouble() ?: 0.0).toString()
        }

        register("PLAYER_MAX_HEALTH") {
            (mc.player?.maxHealth?.toDouble() ?: 0.0).toString()
        }

        register("PLAYER_HEARTS") {
            val player = mc.player ?: return@register "?"
            val hearts = (player.health / 2).toInt().coerceAtLeast(0)
            "❤".repeat(hearts)
        }

        register("PLAYER_ABSORPTION") {
            mc.player?.absorptionAmount?.toString() ?: "0"
        }

        register("PLAYER_HUNGER") {
            mc.player?.foodData?.foodLevel?.toString() ?: "0"
        }

        register("PLAYER_SATURATION") {
            mc.player?.foodData?.saturationLevel?.toString() ?: "0"
        }

        register("PLAYER_ARMOR") {
            mc.player?.armorValue?.toString() ?: "0"
        }

        register("PLAYER_AIR") {
            mc.player?.airSupply?.toString() ?: "0"
        }

        register("PLAYER_ON_GROUND") {
            mc.player?.onGround()?.toString() ?: "false"
        }

        register("PLAYER_SNEAKING") {
            mc.player?.isCrouching?.toString() ?: "false"
        }

        register("PLAYER_SPRINTING") {
            mc.player?.isSprinting?.toString() ?: "false"
        }

        register("PLAYER_FLYING") {
            (mc.player?.abilities?.flying ?: false).toString()
        }

        register("PLAYER_GAMEMODE") {
            mc.gameMode?.playerMode?.name ?: "Unknown"
        }

        register("PLAYER_ON_FIRE") {
            mc.player?.isOnFire?.toString() ?: "false"
        }

        register("PLAYER_IN_LAVA") {
            mc.player?.isInLava?.toString() ?: "false"
        }

        register("PLAYER_IN_WATER") {
            mc.player?.isInWater?.toString() ?: "false"
        }

        register("TARGET_NAME") {
            val hit = mc.hitResult ?: return@register "None"
            when (hit.type) {
                HitResult.Type.ENTITY -> (hit as EntityHitResult).entity.name.string
                HitResult.Type.BLOCK -> "Block"
                else -> "None"
            }
        }

        register("TARGET_HEALTH") {
            val hit = mc.hitResult ?: return@register "0"
            if (hit.type == HitResult.Type.ENTITY) {
                val e = (hit as EntityHitResult).entity
                if (e is LivingEntity) e.health.toString() else "0"
            } else "0"
        }

        register("TARGET_TYPE") {
            val hit = mc.hitResult ?: return@register "None"
            when (hit.type) {
                HitResult.Type.ENTITY -> "Entity"
                HitResult.Type.BLOCK -> "Block"
                else -> "None"
            }
        }

        register("PLAYER_HELD_ITEM") {
            mc.player?.mainHandItem?.hoverName?.string ?: "Empty"
        }

        register("PLAYER_HELD_ITEM_DURABILITY") {
            val item = mc.player?.mainHandItem ?: return@register "0"
            if (item.isDamageableItem) (item.maxDamage - item.damageValue).toString() else "0"
        }

        register("PLAYER_HELD_ITEM_MAX_DURABILITY") {
            mc.player?.mainHandItem?.maxDamage?.toString() ?: "0"
        }

        register("PLAYER_OFFHAND_ITEM") {
            mc.player?.offhandItem?.hoverName?.string ?: "Empty"
        }

        register("PLAYER_OFFHAND_ITEM_DURABILITY") {
            val item = mc.player?.offhandItem ?: return@register "0"
            if (item.isDamageableItem) (item.maxDamage - item.damageValue).toString() else "0"
        }

        register("PLAYER_OFFHAND_ITEM_MAX_DURABILITY") {
            mc.player?.offhandItem?.maxDamage?.toString() ?: "0"
        }

        register("PLAYER_YAW_CARDINAL_8") {
            val yaw = mc.player?.yRot ?: return@register "?"
            val dir = (((yaw % 360) + 360) % 360)
            when {
                dir < 22.5 -> "S"
                dir < 67.5 -> "SW"
                dir < 112.5 -> "W"
                dir < 157.5 -> "NW"
                dir < 202.5 -> "N"
                dir < 247.5 -> "NE"
                dir < 292.5 -> "E"
                dir < 337.5 -> "SE"
                else -> "S"
            }
        }

    }

    fun getPlayerPingColor(): String {
        val ping = mc.player?.connection?.getPlayerInfo(mc.player!!.uuid)?.latency ?: -1
        return when {
            ping < 0 -> "N/A"
            ping < 50 -> "§a"
            ping < 150 -> "§e"
            else -> "§c"
        }
    }

    fun register(name: String, provider: () -> String) {
        providers[name.uppercase()] = provider
    }

    fun interpolate(input: String): String {
        var output = input

        // {NAME} or {NAME:R2} (R = round)
        val regex = Regex("\\{([A-Z_]+)(?::R(\\d+))?}")

        output = regex.replace(output) { match ->
            val key = match.groupValues[1]
            val rounding = match.groupValues[2]

            val provider = providers[key] ?: return@replace match.value

            val value = provider()

            // no rounding requested, return normal value
            if (rounding.isEmpty()) return@replace value

            val digits = rounding.toIntOrNull() ?: return@replace value
            val number = value.toDoubleOrNull() ?: return@replace value

            "%.${digits}f".format(number)
        }

        return output
    }



    fun interpolate(component: Component): Component {
        val regex = Regex("\\{([A-Z_]+)(?::R(\\d+))?}")

        return component.replaceText(
            TextReplacementConfig.builder()
                .match(regex.toPattern())
                .replacement { match, _ ->
                    val key = match.group(1)
                    val rounding = match.group(2)

                    val provider = providers[key] ?: return@replacement Component.text(match.group())

                    val value = provider()

                    val finalValue =
                        if (rounding != null) {
                            val digits = rounding
                            val number = value.toDoubleOrNull()
                            if (number != null)
                                "%.${digits}f".format(number)
                            else value
                        } else value

                    Component.text(finalValue)
                }
                .build()
        )
    }
}