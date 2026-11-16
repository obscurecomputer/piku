package computer.obscure.piku.core.scripting.platform

abstract class Platform {
    abstract fun entity(dto: CoreEntityDto): CoreEntity?
    abstract fun player(dto: CorePlayerDto): CorePlayer?
    abstract fun block(dto: CoreBlockDto): CoreBlock?

    companion object {
        lateinit var current: Platform
    }
}